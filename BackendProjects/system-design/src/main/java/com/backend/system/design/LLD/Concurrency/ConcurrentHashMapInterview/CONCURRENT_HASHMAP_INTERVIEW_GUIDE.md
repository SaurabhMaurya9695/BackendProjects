# ConcurrentHashMap Internal Working - Java 8 Interview Guide

## Table of Contents
1. [Overview](#overview)
2. [Core Concepts](#core-concepts)
3. [Internal Architecture](#internal-architecture)
4. [Thread-Safety Mechanisms](#thread-safety-mechanisms)
5. [Operations Deep-Dive](#operations-deep-dive)
6. [Interview Q&A](#interview-qa)
7. [Performance Tips](#performance-tips)

---

## Overview

**What is ConcurrentHashMap?**
- Thread-safe hash table that allows concurrent reads and writes
- Alternative to Hashtable (which locks entire map)
- Allows multiple threads to read/write different buckets simultaneously
- No ConcurrentModificationException on iteration

**Why ConcurrentHashMap instead of HashMap + synchronization?**
```
HashMap + synchronized:
Thread1: Lock entire map → Update bucket 1 → Unlock
Thread2: Wait for lock... → Update bucket 2 (blocked!)

ConcurrentHashMap:
Thread1: Lock bucket 1 → Update → Unlock bucket 1
Thread2: Lock bucket 2 → Update → Unlock bucket 2 (parallel!)
```

**Key Advantage:** Fine-grained locking = better concurrency

---

## Core Concepts

### 1. Segmentation vs Full Locking

| Aspect | HashMap | Hashtable | ConcurrentHashMap |
|--------|---------|-----------|-------------------|
| Thread-Safe | ❌ No | ✅ Yes | ✅ Yes |
| Lock Strategy | None | Full map lock | Bucket-level lock |
| Concurrent Readers | N/A | 1 | Many |
| Concurrent Writers | N/A | 1 | Many (different buckets) |
| Performance | Best | Worst | Best of both |

### 2. Lock Granularity

```
Full Lock (Hashtable):
┌─────────────────────────┐
│  LOCKED ENTIRE MAP      │ ← Only one thread can access
│ [Bucket0][Bucket1][...] │
└─────────────────────────┘

Fine-Grained Lock (ConcurrentHashMap):
┌────────┬────────┬────────┐
│Lock B0 │Lock B1 │Lock B2 │ ← Multiple threads can lock different buckets
│Bucket0 │Bucket1 │Bucket2 │
└────────┴────────┴────────┘
Thread1 → Lock B0     Thread2 → Lock B1
```

### 3. Concurrency Level (Java 7)
```java
ConcurrentHashMap(int initialCapacity, float loadFactor, int concurrencyLevel)
// concurrencyLevel = expected number of concurrent threads
// Internally creates 'concurrencyLevel' segments for optimal locking
// Default in Java 7: 16 segments
// Java 8+: Dynamic, no explicit concurrencyLevel parameter
```

---

## Internal Architecture

### Java 7 Architecture (Segment-based)

```
ConcurrentHashMap
├── segments: Segment<K, V>[]  (array of segments, size = concurrencyLevel)
│   └── Segment[0..15]
│       ├── table: HashEntry[]  (bucket array within segment)
│       ├── count: int           (element count)
│       └── lock: ReentrantLock  (synchronization object)
```

**Segment = Mini HashMap**
- Each segment maintains its own hash table and lock
- Thread acquires lock on segment before modifying
- Multiple threads can lock different segments

### Java 8+ Architecture (Node-based)

```
ConcurrentHashMap
├── table: Node<K, V>[]        (main bucket array, volatile)
├── sizeCtl: int               (capacity control, volatile)
├── baseCount: long            (base size counter)
└── counterCells: CounterCell[] (distributed size counters)

Node structure:
├── hash: int
├── key: K
├── value: V (volatile)
├── next: Node (for linked list)
└── (Can be TreeNode for Red-Black tree)
```

**Key Differences from Java 7:**
- No segments, no pre-allocated locks
- Each bucket head is synchronized (first Node acts as lock)
- Uses CAS (Compare-And-Swap) for atomic operations
- TreeNode conversion for hash collisions (like HashMap 8+)

### Volatile Fields (Critical for Visibility)

```java
volatile Node<K, V>[] table;      // Array itself is volatile
volatile int sizeCtl;              // Resize control signal
volatile long baseCount;           // Size counter

class Node<K, V> {
    volatile V value;              // Value can change, must see latest
    volatile Node<K, V> next;      // Next pointer must be visible
}
```

**Why Volatile?** Ensures memory visibility across threads without explicit locking

---

## Thread-Safety Mechanisms

### 1. Bucket-Level Synchronization (Java 8+)

```java
// Simplified PUT operation
final V putVal(K key, V value, boolean onlyIfAbsent) {
    int hash = spread(key.hashCode());
    int binCount = 0;
    for (Node<K, V>[] tab = table; ; ) {
        Node<K, V> f;
        int n, i;
        
        if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
            // Bucket is empty, use CAS to insert atomically (NO LOCK)
            if (casTabAt(tab, i, null, new Node<K, V>(hash, key, value, null)))
                break;
        } else {
            // Bucket not empty, synchronize on first Node (bucket-level lock)
            synchronized (f) {  // ← Lock only this bucket, not entire map
                // Search and update/insert in bucket's linked list/tree
            }
        }
    }
    addCount(1L, binCount);
    return null;
}

// Helper methods using Unsafe CAS
private static boolean casTabAt(Node<K,V>[] tab, int i, Node<K,V> c, Node<K,V> v) {
    return U.compareAndSwapObject(tab, ((long)i << ASHIFT) + ABASE, c, v);
}
```

**Key Points:**
- First insertion into empty bucket: CAS (no lock)
- Existing bucket: synchronized on first Node (bucket lock)
- Only one bucket locked at a time
- Multiple buckets can be modified simultaneously

### 2. CAS Operations (Atomic, Lock-free)

```java
// Compare-And-Swap: Atomically update if current value matches expected
boolean compareAndSet(volatile Object location, Object expected, Object newValue) {
    if (location == expected) {
        location = newValue;
        return true;
    }
    return false;
}

// Used in ConcurrentHashMap for:
1. Initial insertion into empty bucket (no lock)
2. Resizing table transitions
3. Size updates in distributed counters
```

**Benefits:**
- No lock → faster than synchronized
- Atomic operation → thread-safe
- Hardware-level support (x86 CMPXCHG instruction)

### 3. Distributed Size Counting

```java
class CounterCell {
    volatile long value;
}

// Size = baseCount + sum of all counterCell.value
// When multiple threads update size:
// - Instead of locking one counter
// - Different threads update different cells
// - Reduces contention on size updates

private final void addCount(long x, int check) {
    CounterCell[] as;
    long b, s;
    
    if ((as = counterCells) != null || !casBaseCount(b = baseCount, s = b + x)) {
        long v;
        int m;
        boolean uncontended = true;
        
        // Try to update a counterCell instead of baseCount
        if (as != null && (m = as.length - 1) >= 0 &&
            (a = as[ThreadLocalRandom.getProbe() & m]) != null &&
            !(uncontended = a.cas(v = a.value, v + x))) {
            // If contention, expand counterCells array
            fullAddCount(x, uncontended);
        }
    }
}
```

---

## Operations Deep-Dive

### GET Operation (Lock-Free, Most Frequent)

```java
public V get(Object key) {
    Node<K, V>[] tab;
    Node<K, V> e, p;
    int n, eh;
    K ek;
    
    int h = spread(key.hashCode());  // Step 1: Hash the key
    
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (e = tabAt(tab, (n - 1) & h)) != null) {  // Step 2: Get bucket
        
        do {
            if ((eh = e.hash) == h &&  // Step 3: Traverse linked list
                ((ek = e.key) == key || (ek != null && key.equals(ek))))
                return e.val;  // Step 4: Return if found
        } while ((e = e.next) != null);
    }
    
    return null;
}

// NO LOCKS INVOLVED!
// - Read table (volatile, so sees latest)
// - No synchronization needed
// - Multiple GET threads can read simultaneously while PUT modifies different bucket
```

**Performance:** O(1) average, O(log n) with tree conversion
**Concurrency:** Multiple reads + one write to different buckets = ALL CONCURRENT

### PUT Operation (Bucket-Level Lock)

```java
public V put(K key, V value) {
    return putVal(key, value, false);
}

private final V putVal(K key, V value, boolean onlyIfAbsent) {
    // PHASE 1: Find bucket
    int hash = spread(key.hashCode());
    int binCount = 0;
    
    for (Node<K, V>[] tab = table; ; ) {
        Node<K, V> f;
        int n, i;
        
        // PHASE 2: Initialize table if null
        if (tab == null || (n = tab.length) == 0)
            tab = initTable();
        
        // PHASE 3: Try atomic insertion if bucket empty
        else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
            if (casTabAt(tab, i, null, new Node<K, V>(hash, key, value, null)))
                break;  // NO LOCK needed, CAS succeeded
        }
        
        // PHASE 4: Check if resizing
        else if ((fh = f.hash) == MOVED)
            tab = helpTransfer(tab, f);
        
        // PHASE 5: Bucket exists, acquire lock on bucket
        else {
            V oldVal = null;
            synchronized (f) {  // ← LOCK acquired on first Node of bucket
                if (tabAt(tab, i) == f) {
                    if (fh >= 0) {  // Linked list
                        binCount = 1;
                        for (Node<K, V> e = f; ; ++binCount) {
                            K ek;
                            if ((ek = e.key) == key || (ek != null && key.equals(ek))) {
                                oldVal = e.val;
                                if (!onlyIfAbsent)
                                    e.val = value;  // Update existing
                                break;
                            }
                            Node<K, V> pred = e;
                            if ((e = e.next) == null) {
                                pred.next = new Node<K, V>(hash, key, value, null);  // Add new
                                break;
                            }
                        }
                    } else if (f instanceof TreeBin) {  // Tree
                        // Insert into Red-Black tree
                    }
                }
            }  // ← LOCK released
            
            // PHASE 6: Check if need to convert to tree
            if (binCount != 0) {
                if (binCount >= TREEIFY_THRESHOLD)
                    treeifyBin(tab, i);  // Convert linked list to tree
                if (oldVal != null)
                    return oldVal;
                break;
            }
        }
    }
    
    // PHASE 7: Update size and check for resize
    addCount(1L, binCount);
    return null;
}
```

**Lock Duration:** Very short, only while modifying linked list/tree in bucket

### REMOVE Operation

```java
public V remove(Object key) {
    return replaceNode(key, null, null);
}

private final V replaceNode(Object key, V value, Object cv) {
    int hash = spread(key.hashCode());
    for (Node<K, V>[] tab = table; ; ) {
        Node<K, V> f;
        int n, i, fh;
        
        if (tab == null || (n = tab.length) == 0 ||
            (f = tabAt(tab, i = (n - 1) & hash)) == null)
            break;
        
        if ((fh = f.hash) == MOVED)
            tab = helpTransfer(tab, f);
        else {
            V oldVal = null;
            boolean validated = false;
            
            synchronized (f) {  // ← Lock bucket
                if (tabAt(tab, i) == f) {
                    // Remove from linked list or tree
                    // Similar to PUT, traverse and remove node
                }
            }  // ← Unlock bucket
            
            if (validated)
                addCount(-1L, -1);
        }
    }
    return null;
}
```

---

## Interview Q&A

### Q1: How is ConcurrentHashMap different from Hashtable?

**Answer:**
```
                  HashMap      Hashtable        ConcurrentHashMap
Thread-Safe       ❌           ✅               ✅
Lock Strategy     None         Full map         Bucket-level
Reads             N/A          1 at a time      Many concurrent
Writes            N/A          1 at a time      Many (diff buckets)
Performance       Best         Slowest          Best

Hashtable Example:
Thread1: synchronized(hashtable) { read/write } → Everyone waits!
Thread2: synchronized(hashtable) { read/write } → Blocked!

ConcurrentHashMap:
Thread1: synchronized(bucket1) { } → Can do bucket2 in parallel!
Thread2: synchronized(bucket2) { } → Both run simultaneously!
```

---

### Q2: What is the role of sizeCtl in ConcurrentHashMap?

**Answer:**
```
sizeCtl is a volatile int that encodes multiple states:

sizeCtl = -1        → Table initialization in progress
sizeCtl = -N        → N-1 threads are performing resizing (concurrent resize)
sizeCtl = 0         → Table not initialized, capacity defaults to 16
sizeCtl > 0         → Threshold for next resize, or table capacity if > 0

Example:
- Initially: sizeCtl = 0, table capacity = 16
- Add elements, when size > 16 * 0.75 = 12:
  - sizeCtl = new capacity = 32
  - Start resizing process
  - Set sizeCtl = -(1 + num_resizing_threads)
  - Multiple threads help with resize
  - When done, sizeCtl = new threshold

This allows concurrent resizing by multiple threads!
```

---

### Q3: Why is GET operation lock-free? Why doesn't it need synchronization?

**Answer:**
```
GET doesn't modify map, only reads:
1. Reading from volatile field ensures latest value
2. If value is already inserted, GET won't miss it
3. If value is being inserted, one of these happens:
   - GET sees old value (acceptable, happens before insert)
   - GET sees new value (fine, it's there now)
   - GET sees neither (neither exists in atomicity window)

Volatile guarantees:
- Reads of volatile fields establish a happens-before relationship
- All writes before the put are visible to subsequent gets
- No synchronization needed for reading

Example:
Thread1: PUT("key", "value")
Thread2: GET("key")

Timeline:
T0: Thread1 acquires lock on bucket
T1: Thread1 writes value to Node
T2: Thread1 releases lock
T3: Thread2 reads (lock not needed, volatile ensures visibility)
```

---

### Q4: How does ConcurrentHashMap handle resizing with concurrent modifications?

**Answer:**
```
Resizing challenges:
- HashMap: Simple, single-threaded
- ConcurrentHashMap: Must not lose data while threads are writing

Solution: Cooperative Multi-threaded Resizing

1. One thread detects need for resize (when size > threshold)
2. Sets sizeCtl = -(1 + num_threads_helping)
3. Starts transferring buckets from old table to new table
4. Other threads see MOVED marker, help with transfer
5. Transfer moves entire bucket chain atomically

Example:
Old table: [Bucket0][Bucket1][Bucket2]
           size=12, need resize to 32

Resize in progress:
- Move Bucket0 → new table
- Move Bucket1 → new table
- Meanwhile, new PUTs:
  - Check table, see MOVED marker
  - Redirect to helping transfer
  - Then perform PUT on new table
  - No data loss, no duplication!

This is why hash field = MOVED:
It's a signal: "This bucket is being transferred, help or wait"
```

---

### Q5: What's the difference between capacity and size?

**Answer:**
```
Size:     Number of key-value pairs currently in map
Capacity: Total number of buckets in internal table

Example:
ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>(16);
               ↑
           Initial capacity = 16 buckets

map.put("a", "1");
map.size()          → 1 (one entry)
Internal capacity   → Still 16 buckets

Load Factor = 0.75:
Resize triggers when: size > capacity * 0.75
So with capacity = 16:
Resize triggers when size > 12
New capacity becomes 32
```

---

### Q6: Why can't ConcurrentHashMap be null for keys/values?

**Answer:**
```
public V get(Object key) {
    // ...
    return e.val;  // Returns null
}

Problem: Can't distinguish between:
1. Key not found → returns null
2. Key found with value = null

// This is ambiguous:
if (map.get("key") == null) {
    // Is "key" not in map?
    // Or is "key" in map with null value?
    // Don't know!
}

HashMap: Allows null (single-threaded, can use containsKey())
ConcurrentHashMap: Doesn't allow null (would break concurrent semantics)

Solution for checking:
V v = map.get("key");
if (v == null && !map.containsKey("key")) {
    // Key not found
}

But this is racy in concurrent environment!
That's why null isn't allowed.
```

---

### Q7: Explain the TREEIFY_THRESHOLD = 8 in Java 8

**Answer:**
```
When a bucket has too many collisions (linked list grows long):
GET: O(1) → O(n) if list has n elements
Solution: Convert linked list to Red-Black tree

Threshold = 8:
- If binCount >= 8, convert to TreeNode
- Reduces get() from O(n) to O(log n)
- Red-Black tree is self-balancing

Example:
Bucket: Node → Node → Node → Node → Node → Node → Node → Node (8 nodes)
                                              ↓ Convert to Tree
Bucket: Red-Black Tree (much faster)

Reverse threshold = 6:
- If tree shrinks back to 6 nodes, convert back to linked list
- Saves memory for sparse maps
```

---

### Q8: What is the happens-before relationship in ConcurrentHashMap?

**Answer:**
```
Java Memory Model guarantees for ConcurrentHashMap:

1. Actions before put() returns:
   - happens-before
   - Actions after the returned put() in another thread

2. PUT Happens-Before GET:
   Thread1:                    Thread2:
   map.put("key", "val")
       ↓
   (happens-before)
       ↓
                              V val = map.get("key")
                              // Guaranteed to see "val"

3. All modifications before put() complete:
   - happens-before
   - Subsequent get() in any thread

This is guaranteed by:
- Volatile fields (table, value, next)
- synchronized blocks
- CAS operations
```

---

### Q9: How does distributed counting (baseCount + counterCells) improve performance?

**Answer:**
```
Problem with single size counter:
Thread1: map.size()++  → Lock counter → Update → Unlock
Thread2: map.size()++  → Wait for lock → Lock → Update → Unlock
         [Contention!]

Solution - Distributed Counting:
baseCount: long (initially 0)
counterCells: CounterCell[] (array of counters)

When updating size:
- Try to update baseCount (CAS, no lock)
- If contention detected, use counterCells
- Different threads update different cells
- Final size = baseCount + sum(counterCells[])

Example with 4 threads:
baseCount = 5
counterCells[0] = 10  (Thread1 incremented this)
counterCells[1] = 8   (Thread2 incremented this)
counterCells[2] = 12  (Thread3 incremented this)
counterCells[3] = 6   (Thread4 incremented this)

Total size = 5 + 10 + 8 + 12 + 6 = 41

Benefits:
- No lock contention on size updates
- Multiple threads can update simultaneously
- size() call is O(1) but not always exact (eventually consistent)
```

---

### Q10: What is MOVED marker in resizing?

**Answer:**
```
During concurrent resizing:
- Old table and new table exist simultaneously
- Some buckets transferred, some not
- Threads must know where to look/insert

Solution: MOVED marker
- Old bucket that's been transferred contains: Node with hash = MOVED
- Signals: "This bucket is moved, go to new table"

Example:
Old Table (being resized):
  [0]: Node(MOVED)     ← Transferred to new table
  [1]: Node(data)      ← Not yet transferred
  [2]: Node(MOVED)     ← Transferred
  [3]: Node(data)      ← Not yet transferred

When thread finds MOVED:
1. Identify bucket is being resized
2. Help with transfer process
3. Then perform operation on new table

Code:
if ((fh = f.hash) == MOVED)  // Check for MOVED marker
    tab = helpTransfer(tab, f);  // Help with resize
```

---

## Performance Tips

### ✅ DO:
```java
// 1. Use appropriate initial capacity
ConcurrentHashMap<String, Value> map = new ConcurrentHashMap<>(1000);
// Reduces resizing overhead

// 2. Use iterator for concurrent reads
for (Entry<String, Value> entry : map.entrySet()) {
    // Safe snapshot-like iteration
}

// 3. Use computeIfAbsent for atomic get-or-create
map.computeIfAbsent("key", k -> expensiveComputation());

// 4. Use forEach for parallel processing
map.forEach(1000, (k, v) -> System.out.println(k + "=" + v));
// Parallel processing using parallelism threshold
```

### ❌ DON'T:
```java
// 1. Don't use null
map.put("key", null);  // throws NPE

// 2. Don't rely on exact size() in highly concurrent scenarios
long size = map.size();
// Might be stale immediately after

// 3. Don't synchronize on map for compound operations
synchronized (map) {  // ← Defeats the purpose!
    if (!map.containsKey("key"))
        map.put("key", "value");
}
// Use putIfAbsent instead: map.putIfAbsent("key", "value");

// 4. Don't iterate and modify in single thread:
Iterator<Entry> it = map.entrySet().iterator();
it.next();
map.put("new", "value");  // OK (won't throw exception, but semantics unclear)
```

---

## Summary Comparison Table

| Operation | Lock | Thread-Safe | O(n) Worst Case |
|-----------|------|-------------|-----------------|
| GET | No | Yes | Hash collision chain |
| PUT | Bucket | Yes | Resize or tree conversion |
| REMOVE | Bucket | Yes | Hash collision chain |
| SIZE | Distributed | Yes (eventual) | — |
| REPLACE | Bucket | Yes | Hash collision chain |
| CONTAINS | No | Yes | Hash collision chain |

---

## Key Takeaways for Interviews

1. **Bucket-level locking** allows multiple threads to write to different buckets simultaneously
2. **GET is lock-free** - reads volatile fields, no synchronization needed
3. **CAS operations** used for atomic operations without full locks
4. **Distributed counting** reduces size update contention
5. **Java 8 removed segments** in favor of Node-based structure with CAS + synchronized
6. **Resizing is concurrent** - multiple threads help transfer buckets
7. **MOVED marker** signals bucket transfer during resize
8. **Tree conversion** at TREEIFY_THRESHOLD = 8 for collision chains
9. **No null keys/values** allowed
10. **Snapshot iteration** - iterator doesn't fail on concurrent modifications

---

## Practice Questions

1. What happens if two threads try to PUT into the same bucket simultaneously?
2. Why doesn't GET operation need to acquire a lock?
3. How would you implement an LRU cache that's thread-safe and efficient?
4. Can you design a distributed counter similar to baseCount + counterCells?
5. Why is CAS preferred over synchronized in certain scenarios?
6. How would you handle the case where you need atomic operations like "put-if-absent"?

---

*Last Updated: 2026-04-26*
*For execution examples, see: ConcurrentHashMapInternal.java*
