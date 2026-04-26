# ConcurrentHashMap - Visual Diagrams & Illustrations

## 1. Lock Contention Comparison

### Hashtable (Single Lock - BAD)
```
Time →

Thread1: [Lock acquired    work work work    Release]
Thread2:                    [Waiting....    Lock  work Release]
Thread3:                                    [Waiting....   Lock work Release]

Only ONE thread works at a time ❌
Total time = T1 + T2 + T3
```

### ConcurrentHashMap (Bucket Locks - GOOD)
```
Time →

Thread1: [Lock B0 work Release] [Idle]                  [Idle]
Thread2:                   [Lock B1 work Release]       [Idle]
Thread3:                                  [Lock B2 work Release]

Multiple threads work SIMULTANEOUSLY ✅
Total time ≈ max(T1, T2, T3) (much better!)
```

---

## 2. Internal Structure Evolution

### Java 7 (Segment-based)
```
ConcurrentHashMap
    ├── segments: Segment[16]
    │   ├── Segment[0]
    │   │   ├── count: 5
    │   │   ├── lock: ReentrantLock
    │   │   └── table: HashEntry[16]
    │   │       ├── [0]: null
    │   │       ├── [1]: HashEntry → HashEntry → HashEntry
    │   │       ├── [2]: HashEntry
    │   │       └── ...
    │   ├── Segment[1]
    │   │   └── ...
    │   └── ...
```

**16 Segments = 16 independent hash tables with own locks**

### Java 8+ (Node-based)
```
ConcurrentHashMap
    ├── table: Node[] (volatile, size = capacity)
    │   ├── [0]: null
    │   ├── [1]: Node(hash, key, val) → Node → Node (linked list)
    │   │         [LOCK ON THIS NODE]
    │   ├── [2]: TreeNode (Red-Black Tree for collisions)
    │   │         ├── TreeNode
    │   │         ├── TreeNode
    │   │         └── TreeNode
    │   └── [3]: null
    ├── sizeCtl: long (resize control)
    ├── baseCount: long (base size counter)
    └── counterCells: CounterCell[]
```

**No pre-allocated segments, dynamic locking**

---

## 3. PUT Operation Flow Diagram

```
put("key", "value")
    ↓
[1] Hash key → hash = 0x12345678
    ↓
[2] Find bucket index → index = (table.length - 1) & hash = 7
    ↓
[3] Check table[7]
    ├─→ If null
    │   ├─→ CAS insert (atomic, NO LOCK)
    │   └─→ Success? Return
    │
    ├─→ If MOVED (resizing)
    │   ├─→ Help transfer
    │   └─→ Retry
    │
    └─→ If Node exists
        ├─→ synchronized(first_node)  ← LOCK acquired
        │   ├─→ Search linked list
        │   ├─→ Found? Update value
        │   ├─→ Not found? Add new Node
        │   └─→ Check binCount >= 8? Convert to tree
        └─→ Release lock
    ↓
[4] Update size counter (distributed)
    ├─→ CAS baseCount
    └─→ If contention, use counterCells[random]
    ↓
[5] Check if size > threshold? Trigger resize
    ↓
Return old value (or null)
```

---

## 4. GET Operation (Lock-Free)

```
get("key")
    ↓
[1] Hash key → hash = 0x12345678
    ↓
[2] Find bucket → index = 7
    ↓
[3] Read table[7] (volatile read)
    │
    ├─→ If null → return null
    │
    ├─→ If node.hash == hash
    │   ├─→ Return node.value (volatile read)
    │
    └─→ Else traverse linked list / search tree
        └─→ Continue until found or end
    ↓
NO LOCKS ACQUIRED ✅
Other threads can WRITE to different buckets simultaneously!
```

---

## 5. Resizing Process

### Initial State
```
Table size = 16, Load factor = 0.75
Resize trigger = 12

After 12 PUTs:
┌─────────────────────────────┐
│ Old Table (size=16)         │
├─────────────────────────────┤
│[0][1][2][3][4][5][6][7]    │
│[8][9][10][11][12][13][14]  │
│[15]                         │
└─────────────────────────────┘
size > 12, RESIZE TRIGGERED!
```

### During Resize
```
┌──────────────────────┐          ┌──────────────────────┐
│ Old Table (16)       │          │ New Table (32) EMPTY │
├──────────────────────┤          ├──────────────────────┤
│[0] → data            │    ①    │[0] null              │
│[1] → data            │    →    │[1] null              │
│[2] → MOVED           │  Copy   │[2] → data (from [2]) │
│[3] → data            │    →    │[3] null              │
│[4] → MOVED           │         │[4] → data (from [4]) │
│...                   │         │...                   │
│[15] → data           │         │[31] null             │
└──────────────────────┘         └──────────────────────┘
  ↑
Threads see MOVED:
Help with transfer or
redirect to new table
```

### After Resize
```
┌──────────────────────┐
│ New Table (32) FULL  │
├──────────────────────┤
│[0][1][2]...[31]     │
│ All data migrated    │
└──────────────────────┘

sizeCtl = new_threshold = 24 (32 * 0.75)
Ready for next 12 puts before another resize
```

---

## 6. Size Counting Illustration

### Single Counter Problem
```
Global Counter = 0

Thread1: Counter++  → Lock → Counter = 1 → Unlock
Thread2: Counter++  → Wait → Lock → Counter = 2 → Unlock
Thread3: Counter++  → Wait → Lock → Counter = 3 → Unlock

[CONTENTION - Threads block each other]
```

### Distributed Counter Solution
```
baseCount = 5
counterCells = [CounterCell, CounterCell, CounterCell, ...]

Thread1: counterCells[0]++  (no lock, CAS)  → counterCells[0] = 10
Thread2: counterCells[1]++  (no lock, CAS)  → counterCells[1] = 8
Thread3: counterCells[2]++  (no lock, CAS)  → counterCells[2] = 7

Total Size = 5 + (10 + 8 + 7 + ...) = 30+

[NO CONTENTION - All threads proceed]
```

---

## 7. Collision Handling

### Before Java 8
```
Bucket[5]:
Node(hash: 0x5) → Node(hash: 0x5) → Node(hash: 0x5) → ...
                                    [O(n) traversal for GET]
```

### Java 8+ (Above threshold=8)
```
Bucket[5]:
TreeNode (Red-Black Tree)
    ├── TreeNode
    │   ├── TreeNode
    │   └── TreeNode
    └── TreeNode
        ├── TreeNode
        └── TreeNode

[O(log n) search for GET, Self-balancing]
```

---

## 8. Concurrent Scenario Example

```
Initial: ConcurrentHashMap with 16 buckets
         │[0]│[1]│[2]│[3]│[4]│[5]│[6]│[7]│
         │[8]│[9]│[10][11][12][13][14][15]

Timeline:

T0: Thread1: put("Alice", "data_A")     → bucket[h1]
T1: Thread2: put("Bob", "data_B")       → bucket[h2]  (different bucket!)
T2: Thread3: get("Alice")               → bucket[h1]  (no lock needed!)
T3: Thread1: Release lock on bucket[h1]
T4: Thread2: Release lock on bucket[h2]
T5: Thread3: Return "data_A"

All 4 operations in parallel! ✅
If this were Hashtable, only one could proceed at a time.
```

---

## 9. CAS (Compare-And-Swap) Operation

```
Atomic Update without Lock:

Initial: table[i] = null

Thread1:
BEFORE: table[i] = null
CAS(table[i], null, newNode)
        ↓
        if (table[i] == null)  ✓
            table[i] = newNode
AFTER:  table[i] = newNode

SUCCESS! No lock, atomic operation.

If another thread already did:
table[i] = someNode
Then CAS fails and thread retries with locking.
```

---

## 10. Memory Visibility (Volatile)

```
┌─────────────────┐              ┌─────────────────┐
│   Thread1       │              │   Thread2       │
│  (CPU1 Cache)   │              │  (CPU2 Cache)   │
│ value = "old"   │              │ value = ???     │
└────────┬────────┘              └────────┬────────┘
         │                                │
         │ volatile write                 │
         ├──────────────────────────────→ Flush to memory
         │                    value = "new"
         │                                │
         │                         volatile read
         │ ← ← ← ← ← ← ← ← ← ← ← ← Read from memory
         │                        value = "new"
         │                                │
     ✅ Visibility guaranteed!

Without volatile:
value might be stuck in Thread2's cache = WRONG!
```

---

## 11. Thread-Safe Compound Operations

### Problem
```
❌ DON'T DO THIS:
if (!map.containsKey("key")) {
    map.put("key", "value");
}

Race condition:
Thread1: if (!map.containsKey("key"))  → true
Thread2:                                  if (!map.containsKey("key")) → true
Thread1:     map.put("key", "value1")
Thread2:     map.put("key", "value2")    ← Overwrites!
```

### Solution
```
✅ USE ATOMIC OPERATIONS:
map.putIfAbsent("key", "value");

// Or
map.computeIfAbsent("key", k -> computeValue());

// Or
V prev = map.putIfAbsent("key", "value");
if (prev == null) {
    // Successfully inserted
}
```

---

## 12. MOVED Marker During Resize

```
Before:
table[0] = Node(data)
table[1] = Node(data)
...

During transfer:
table[0] = Node(hash=MOVED)  ← Signal: moved to new table
table[1] = Node(hash=MOVED)  ← Signal: moved to new table

New thread arrives:
1. Reads table[0].hash = MOVED
2. Recognizes: bucket is being transferred
3. Calls helpTransfer() to assist
4. Redirects to new table

After transfer:
table = newTable (capacity doubled)
```

---

## 13. Interview Whiteboard Sketch

```
┌─────────────────────────────────────────────────┐
│     ConcurrentHashMap vs Alternatives           │
├─────────────────────────────────────────────────┤
│                                                 │
│  HashMap: Single-threaded (fastest if sync)     │
│  ┌────┬────┬────┐                               │
│  │    │    │    │ No synchronization            │
│  └────┴────┴────┘                               │
│                                                 │
│  Hashtable: Full lock (slowest)                 │
│  [LOCK ENTIRE TABLE - ONE THREAD]               │
│  ├────┬────┬────┐                               │
│  │    │    │    │ synchronized(table) {}        │
│  └────┴────┴────┘                               │
│                                                 │
│  ConcurrentHashMap: Bucket locks (best balance) │
│  ├────┬────┬────┐                               │
│  │Lock│    │Lock│ synchronized(bucket) {}       │
│  └────┴────┴────┘                               │
│   ↑         ↑ Multiple locks, high concurrency  │
│                                                 │
│  Performance ranking:                           │
│  Single-thread: HashMap > CHM > Hashtable       │
│  Multi-thread:  CHM >> Hashtable > HashMap      │
│                                                 │
└─────────────────────────────────────────────────┘
```

---

*Created: 2026-04-26*
*Use these diagrams when explaining to interviewers!*
