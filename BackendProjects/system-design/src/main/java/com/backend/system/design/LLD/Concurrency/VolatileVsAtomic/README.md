# volatile vs Atomic (AtomicInteger, AtomicLong)

## Start With the Problem — CPU Caches

Modern CPUs don't read directly from RAM on every operation — that would be too slow.
Instead, each CPU core has its own **local cache (L1/L2)** where it stores copies of variables.

```
         Core-1                    Core-2
     ┌──────────┐              ┌──────────┐
     │  Cache   │              │  Cache   │
     │ count=0  │              │ count=0  │  ← each core has its own copy
     └──────────┘              └──────────┘
          │                         │
          └──────────┬──────────────┘
                     │
              ┌──────────────┐
              │     RAM      │
              │   count=0    │  ← actual value in memory
              └──────────────┘
```

**The problem:**
- Thread-1 (on Core-1) increments `count` → updates its local cache → `count=1` in cache
- Thread-2 (on Core-2) reads `count` → reads from its own cache → still sees `count=0`
- Thread-2 is working with **stale data**

This is called a **visibility problem** — one thread's changes are invisible to other threads.

---

## Part 1 — volatile

### What it does

`volatile` tells the JVM:
> "Do NOT cache this variable in CPU registers or local cache.
> Every read must come from main memory.
> Every write must go directly to main memory."

```java
private volatile int count = 0;
```

```
         Core-1                    Core-2
     ┌──────────┐              ┌──────────┐
     │  Cache   │              │  Cache   │
     │  (skip)  │              │  (skip)  │  ← volatile bypasses local cache
     └──────────┘              └──────────┘
          │                         │
          └──────────┬──────────────┘
                     │
              ┌──────────────┐
              │     RAM      │
              │   count=1    │  ← all threads read/write here directly
              └──────────────┘
```

Now when Thread-1 writes `count=1`, Thread-2 immediately sees `count=1`.

---

### What volatile solves — Visibility

```java
class RunningFlag {
    private volatile boolean running = true;  // without volatile, Thread-2 may never see the update

    public void stop() {
        running = false;  // Thread-1 writes
    }

    public void run() {
        while (running) {  // Thread-2 reads — with volatile, sees false immediately ✅
            doWork();
        }
    }
}
```

Without `volatile`, the JVM may optimize `while (running)` into `while (true)` because
it thinks no other thread changes `running`. The loop never stops. This is a real bug.

---

### What volatile does NOT solve — Atomicity

Atomicity means: an operation completes in one step with no interruption possible.

`volatile` only guarantees visibility. It does NOT make compound operations atomic.

```java
private volatile int count = 0;

count++;  // looks like 1 step — it is actually 3 steps:
          // Step 1: READ  count from memory  (count = 0)
          // Step 2: ADD   1                  (count = 1)
          // Step 3: WRITE count to memory    (count = 1)
```

Thread-1 and Thread-2 can interleave these 3 steps:

```
Thread-1: READ  count = 0
Thread-2: READ  count = 0   ← reads before Thread-1 writes
Thread-1: ADD 1, WRITE count = 1
Thread-2: ADD 1, WRITE count = 1   ← overwrites Thread-1's result!

Expected: count = 2
Actual:   count = 1  ❌
```

`volatile` cannot fix this. You need `Atomic` classes or `synchronized` for this.

---

### When to use volatile

Use `volatile` when:
- One thread **writes**, other threads only **read**
- The operation is a **single read or write** (not read-modify-write like `++`)
- You need a **flag** or **state variable** that signals other threads

```java
// Perfect use cases for volatile
private volatile boolean shutdownRequested = false;
private volatile String currentConfig = "default";
private volatile long lastHeartbeatTime = 0L;
```

---

## Part 2 — Atomic Classes

### What they are

`java.util.concurrent.atomic` package provides classes that perform
**read-modify-write operations atomically** — as a single unbreakable step.

Common ones:
| Class | Wraps |
|-------|-------|
| `AtomicInteger` | `int` |
| `AtomicLong` | `long` |
| `AtomicBoolean` | `boolean` |
| `AtomicReference<T>` | any object |

---

### How they work internally — CAS (Compare-And-Swap)

Atomic classes use a **hardware-level CPU instruction** called **CAS (Compare-And-Swap)**.

CAS does this in one unbreakable hardware step:
> "If the current value is **X**, swap it to **Y**. Otherwise, do nothing and tell me it failed."

```java
// CAS pseudocode
boolean compareAndSwap(int expected, int newValue) {
    if (currentValue == expected) {  // these 3 lines happen atomically at hardware level
        currentValue = newValue;
        return true;
    }
    return false;
}
```

### How AtomicInteger.incrementAndGet() uses CAS

```java
// What happens inside AtomicInteger.incrementAndGet()
int current;
do {
    current = get();               // read current value
} while (!compareAndSwap(current, current + 1));  // retry if someone else changed it first
return current + 1;
```

If two threads try to increment simultaneously:
```
Thread-1: reads count=0, tries CAS(0 → 1) → succeeds ✅, count=1
Thread-2: reads count=0, tries CAS(0 → 1) → fails (count is already 1)
          retries: reads count=1, tries CAS(1 → 2) → succeeds ✅, count=2

Final: count = 2 ✅  (correct, no lock needed)
```

This is called **lock-free** — no thread ever blocks. They just retry.

---

### AtomicInteger — Basic Usage

```java
AtomicInteger count = new AtomicInteger(0);

// Increment and get new value
int newVal = count.incrementAndGet();   // count=1, returns 1

// Get and increment (returns old value)
int oldVal = count.getAndIncrement();   // returns 1, count=2

// Add a specific amount
count.addAndGet(5);                     // count=7

// Compare and set — only sets if current value matches expected
boolean updated = count.compareAndSet(7, 10);  // if count==7, set to 10 → true ✅
boolean failed  = count.compareAndSet(7, 10);  // count is now 10, not 7 → false ❌

// Just read the value
int val = count.get();                  // 10
```

---

### AtomicLong — Same API, for long values

```java
AtomicLong requestCount = new AtomicLong(0);
requestCount.incrementAndGet();
requestCount.addAndGet(100);
long total = requestCount.get();
```

Use `AtomicLong` over `AtomicInteger` when your counter can exceed `Integer.MAX_VALUE` (2.1 billion)
— e.g., total API requests, bytes transferred, event counts.

---

### Note on AtomicFloat / AtomicDouble

Java does NOT have `AtomicFloat` or `AtomicDouble` in the standard library.

The workaround is `AtomicReference<Double>`:

```java
AtomicReference<Double> price = new AtomicReference<>(0.0);

// Atomic update using CAS loop
double current, updated;
do {
    current = price.get();
    updated = current + 9.99;
} while (!price.compareAndSet(current, updated));
```

Or use `DoubleAdder` (Java 8+) — better for high-contention counters (like metrics):

```java
DoubleAdder totalRevenue = new DoubleAdder();
totalRevenue.add(9.99);
double total = totalRevenue.sum();
```

---

## Head-to-Head Comparison

| | `volatile` | `Atomic` classes |
|---|------------|-----------------|
| **What it fixes** | Visibility only | Visibility + Atomicity |
| **How** | Forces main memory read/write | CAS hardware instruction |
| **Compound ops** (`++`, `+=`) | NOT safe ❌ | Safe ✅ |
| **Simple read/write** | Safe ✅ | Safe ✅ |
| **Blocking** | No | No (lock-free) |
| **Performance** | Slightly faster (no CAS retry) | Fast, but CAS retry under contention |
| **Use case** | Flag / single-write variables | Counters, accumulators, CAS logic |

---

## Compared to synchronized

```java
// Option 1: synchronized
private int count = 0;
public synchronized void increment() { count++; }

// Option 2: volatile — WRONG for increment
private volatile int count = 0;
public void increment() { count++; }  // ❌ not atomic

// Option 3: AtomicInteger — correct AND lock-free
private AtomicInteger count = new AtomicInteger(0);
public void increment() { count.incrementAndGet(); }  // ✅
```

| | `synchronized` | `volatile` | `AtomicInteger` |
|---|---|---|---|
| Visibility | ✅ | ✅ | ✅ |
| Atomicity | ✅ | ❌ | ✅ |
| Blocking | Yes (threads queue up) | No | No (lock-free) |
| Best for | Complex critical sections | Simple flags | Single variable counters |

---

## Real-World Examples

### volatile — shutdown flag
```java
public class DataProcessor {
    private volatile boolean running = true;

    public void process() {
        while (running) {       // Thread-2 reads
            processNextBatch();
        }
    }

    public void shutdown() {
        running = false;        // Thread-1 writes — volatile ensures Thread-2 sees it
    }
}
```

### AtomicInteger — request counter
```java
public class ApiMetrics {
    private final AtomicInteger totalRequests = new AtomicInteger(0);
    private final AtomicInteger failedRequests = new AtomicInteger(0);

    public void recordRequest(boolean success) {
        totalRequests.incrementAndGet();
        if (!success) failedRequests.incrementAndGet();
    }

    public double getFailureRate() {
        return (double) failedRequests.get() / totalRequests.get();
    }
}
```

### AtomicLong — unique ID generator
```java
public class IdGenerator {
    private final AtomicLong sequence = new AtomicLong(0);

    public long nextId() {
        return sequence.incrementAndGet();  // guaranteed unique, no locks ✅
    }
}
```

---

## Summary

```
CPU caches cause visibility problems — threads see stale data.

volatile  → fixes visibility only
           "always read/write from main memory"
           safe for: flags, single-write variables
           NOT safe for: count++, check-then-act

Atomic    → fixes visibility + atomicity
           uses CAS hardware instruction — no locks, no blocking
           safe for: counters, accumulators, compare-and-swap logic

Rule of thumb:
  Need a flag?          → volatile
  Need a counter?       → AtomicInteger / AtomicLong
  Need complex logic?   → synchronized or ReentrantLock
```
