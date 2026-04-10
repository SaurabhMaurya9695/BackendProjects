# synchronized vs ReentrantLock

## The Core Question

Both `synchronized` and `ReentrantLock` achieve mutual exclusion —
only one thread can execute the critical section at a time.
So why does `ReentrantLock` exist?

Because `synchronized` is simple but inflexible.
`ReentrantLock` gives you control that `synchronized` cannot.

---

## synchronized — The Basics

Built into the Java language. Every object has an intrinsic lock (monitor).

```java
public class Counter {
    private int count = 0;

    public synchronized void increment() {
        count++;  // only one thread at a time
    }
}
```

Or using a block (preferred — locks only what needs locking):

```java
public void increment() {
    synchronized (this) {
        count++;
    }
}
```

### How it works internally
- Thread enters the `synchronized` block → acquires the monitor lock
- Other threads trying to enter → **blocked indefinitely** (no timeout, no escape)
- Thread exits (normally or via exception) → lock released automatically

---

## ReentrantLock — The Basics

Explicit lock from `java.util.concurrent.locks`.
You lock and unlock manually.

```java
import java.util.concurrent.locks.ReentrantLock;

public class Counter {
    private int count = 0;
    private final ReentrantLock lock = new ReentrantLock();

    public void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();  // ALWAYS unlock in finally
        }
    }
}
```

The `finally` block is mandatory — if you forget `unlock()`, the lock is held forever.

---

## Why "Reentrant"?

Both are reentrant — a thread that already holds the lock can acquire it again
without deadlocking itself.

```java
public synchronized void methodA() {
    methodB();  // same thread re-acquires the lock — works fine
}

public synchronized void methodB() {
    // ...
}
```

Same with ReentrantLock — the same thread can call `lock()` multiple times.
It keeps a hold count. Each `lock()` must be matched by an `unlock()`.

---

## Head-to-Head Comparison

| Feature | synchronized | ReentrantLock |
|---------|-------------|---------------|
| **Syntax** | Built-in keyword | Explicit API |
| **Lock release** | Automatic (JVM handles it) | Manual (`unlock()` in finally) |
| **Trylock (non-blocking)** | No | Yes — `tryLock()` |
| **Timeout** | No | Yes — `tryLock(time, unit)` |
| **Interruptible wait** | No | Yes — `lockInterruptibly()` |
| **Fairness** | No (random thread wakes up) | Yes — `new ReentrantLock(true)` |
| **Multiple conditions** | One wait-set per lock | Multiple `Condition` objects |
| **Read/Write split** | No | Yes (via `ReentrantReadWriteLock`) |
| **Performance (modern JVM)** | Same (JVM optimizes heavily) | Same |
| **Visibility in debugger** | JVM stack shows lock owner | `lock.getOwner()` programmatically |

---

## The Key Differences Explained

### 1. tryLock() — Non-blocking acquire

`synchronized` blocks forever if the lock is taken.
`ReentrantLock.tryLock()` lets you give up immediately or after a timeout.

```java
// synchronized — no escape
synchronized (lock) { ... }  // waits forever ❌

// ReentrantLock — try and move on
if (lock.tryLock()) {
    try {
        // got the lock
    } finally {
        lock.unlock();
    }
} else {
    // lock not available — do something else ✅
}

// With timeout
if (lock.tryLock(500, TimeUnit.MILLISECONDS)) {
    // got it within 500ms
} else {
    // timed out — handle gracefully
}
```

**When useful:** Resource pools, retry logic, avoiding deadlocks.

---

### 2. lockInterruptibly() — Cancellable wait

With `synchronized`, a blocked thread cannot be interrupted.
With `lockInterruptibly()`, another thread can cancel the wait.

```java
// Thread can be interrupted while waiting for the lock
try {
    lock.lockInterruptibly();
    try {
        // critical section
    } finally {
        lock.unlock();
    }
} catch (InterruptedException e) {
    // gracefully handle cancellation ✅
}
```

**When useful:** Tasks with deadlines, user-cancellable operations.

---

### 3. Fairness — Who gets the lock next?

`synchronized` gives no guarantee — any waiting thread can get the lock next
(often the most recently unblocked one = **barging**). This can starve some threads.

```java
// Fair lock — threads get the lock in the order they asked (FIFO)
ReentrantLock fairLock = new ReentrantLock(true);
```

**Trade-off:** Fair locks have lower throughput because the JVM can't do
opportunistic scheduling. Use only when starvation is a real concern.

---

### 4. Multiple Conditions

`synchronized` has one wait-set per object (`wait()` / `notify()` / `notifyAll()`).
You cannot wake up a specific group of waiting threads.

`ReentrantLock` lets you create multiple `Condition` objects — separate wait-sets.

```java
ReentrantLock lock = new ReentrantLock();
Condition notFull  = lock.newCondition();  // producers wait here
Condition notEmpty = lock.newCondition();  // consumers wait here

// Producer
lock.lock();
try {
    while (queue.isFull()) notFull.await();   // only producers sleep here
    queue.add(item);
    notEmpty.signal();  // wake only a consumer ✅
} finally {
    lock.unlock();
}

// Consumer
lock.lock();
try {
    while (queue.isEmpty()) notEmpty.await(); // only consumers sleep here
    queue.poll();
    notFull.signal();  // wake only a producer ✅
} finally {
    lock.unlock();
}
```

With `synchronized` + `notifyAll()`, you'd wake ALL waiting threads (producers + consumers)
even if only one consumer needs to wake — wasteful.

---

### 5. ReentrantReadWriteLock — Read/Write Split

Multiple threads can read simultaneously as long as no one is writing.
`synchronized` serializes everything, including concurrent reads.

```java
ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
Lock readLock  = rwLock.readLock();
Lock writeLock = rwLock.writeLock();

// Many threads can hold readLock simultaneously
public String getData() {
    readLock.lock();
    try {
        return data;  // concurrent reads ✅
    } finally {
        readLock.unlock();
    }
}

// Only one thread can hold writeLock, and only when no readers
public void setData(String value) {
    writeLock.lock();
    try {
        data = value;
    } finally {
        writeLock.unlock();
    }
}
```

**When useful:** Read-heavy caches, configuration stores, shared lookup tables.

---

## The Trade-offs

### Use `synchronized` when:
- The critical section is simple and short
- You don't need timeout, interruptibility, or fairness
- You want clean, less error-prone code (no risk of forgetting `unlock()`)
- Performance is not a bottleneck (JVM optimizes `synchronized` aggressively via biased locking, lock elision)

### Use `ReentrantLock` when:
- You need `tryLock()` — non-blocking acquire or timeout
- You need `lockInterruptibly()` — cancellable waits
- You need fairness — prevent thread starvation
- You need multiple conditions — e.g., producer-consumer with separate wait sets
- You need read/write splitting — `ReentrantReadWriteLock` for read-heavy workloads
- You need to inspect lock state programmatically (`isLocked()`, `getQueueLength()`, `getOwner()`)

---

## Common Mistake — Forgetting unlock()

```java
// WRONG — if exception thrown, lock is never released → deadlock
lock.lock();
doSomethingThatMightThrow();  // exception here
lock.unlock();  // never reached ❌

// CORRECT — always in finally
lock.lock();
try {
    doSomethingThatMightThrow();
} finally {
    lock.unlock();  // always runs ✅
}
```

---

## Performance — Is ReentrantLock faster?

In early Java (pre-1.6), `ReentrantLock` was faster.
Since Java 6+, the JVM applies heavy optimizations to `synchronized`:
- **Biased locking** — if only one thread uses the lock, the lock is free
- **Lock elision** — JVM removes locks it proves are unnecessary
- **Adaptive spinning** — brief spin before blocking

Today, performance is essentially the same under most workloads.
Choose based on **features needed**, not performance.

---

## Summary

```
synchronized    → simple, safe, less control
ReentrantLock   → verbose, powerful, full control

The trade-off:
  Simplicity  ←————————————→  Control
  synchronized              ReentrantLock
```

See `SynchronizedVsReentrantLockDemo.java` for runnable examples of each feature.
