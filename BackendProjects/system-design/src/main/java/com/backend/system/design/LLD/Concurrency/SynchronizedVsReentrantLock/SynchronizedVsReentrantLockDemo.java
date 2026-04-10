package com.backend.system.design.LLD.Concurrency.SynchronizedVsReentrantLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * synchronized vs ReentrantLock — Demo
 * <p>
 * Demonstrates 5 scenarios where ReentrantLock gives control
 * that synchronized cannot provide.
 */
public class SynchronizedVsReentrantLockDemo {

    // ─────────────────────────────────────────────────────────────
    // DEMO 1: Basic synchronized vs ReentrantLock
    // ─────────────────────────────────────────────────────────────

    static class SynchronizedCounter {

        private int count = 0;

        public synchronized void increment() {
            count++;
        }

        public synchronized int get() {
            return count;
        }
    }

    static class ReentrantCounter {

        private int count = 0;
        private final ReentrantLock lock = new ReentrantLock();

        public void increment() {
            lock.lock();
            try {
                count++;
            } finally {
                lock.unlock(); // must always be in finally
            }
        }

        public int get() {
            lock.lock();
            try {
                return count;
            } finally {
                lock.unlock();
            }
        }
    }

    static void demoBasic() throws InterruptedException {
        System.out.println("\n--- DEMO 1: Basic Counter (synchronized vs ReentrantLock) ---");

        SynchronizedCounter sc = new SynchronizedCounter();
        ReentrantCounter rc = new ReentrantCounter();

        // Run 3 threads incrementing 1000 times each
        Thread[] threads = new Thread[3];
        for (int i = 0; i < 3; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    sc.increment();
                    rc.increment();
                }
            });
            threads[i].start();
        }
        for (Thread t : threads) {
            t.join(); // main thread will wait to other threads to finish first
        }

        System.out.println("synchronized counter: " + sc.get() + " (expected 3000)");
        System.out.println("ReentrantLock counter: " + rc.get() + " (expected 3000)");
    }

    // ─────────────────────────────────────────────────────────────
    // DEMO 2: tryLock() — Non-blocking acquire
    // ─────────────────────────────────────────────────────────────

    static void demoTryLock() throws InterruptedException {
        System.out.println("\n--- DEMO 2: tryLock() — Non-blocking acquire ---");

        ReentrantLock lock = new ReentrantLock();

        // Thread 1 holds the lock for 300ms
        Thread holder = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("  Thread-1: acquired lock, holding for 300ms");
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
                System.out.println("  Thread-1: released lock");
            }
        });

        holder.start();
        Thread.sleep(50); // ensure Thread-1 holds the lock first

        // Thread 2 tries with timeout — gives up after 100ms
        boolean acquired = lock.tryLock(100, TimeUnit.MILLISECONDS);
        if (acquired) {
            try {
                System.out.println("  Thread-2: got the lock ✅");
            } finally {
                lock.unlock();
            }
        } else {
            System.out.println("  Thread-2: could not get lock in 100ms — moving on ✅");
            // with synchronized, Thread-2 would be stuck here waiting indefinitely
        }

        holder.join();
    }

    // ─────────────────────────────────────────────────────────────
    // DEMO 3: lockInterruptibly() — Cancellable wait
    // ─────────────────────────────────────────────────────────────

    static void demoInterruptible() throws InterruptedException {
        System.out.println("\n--- DEMO 3: lockInterruptibly() — Cancellable wait ---");

        ReentrantLock lock = new ReentrantLock();
        lock.lock(); // main thread holds the lock

        Thread waiter = new Thread(() -> {
            try {
                System.out.println("  Waiter: waiting for lock (interruptibly)...");
                lock.lockInterruptibly(); // can be interrupted while waiting
                try {
                    System.out.println("  Waiter: got the lock");
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                System.out.println("  Waiter: interrupted while waiting — cancelled gracefully ✅");
                // with synchronized, this is impossible — thread would wait forever
            }
        });

        waiter.start();
        Thread.sleep(100);

        waiter.interrupt(); // cancel the waiting thread
        waiter.join();
        lock.unlock();

        /*
            Real life analogy first
            You go to a bank. The counter is busy. You take a token and wait.
            lock.lock() = you're chained to the seat. Even if you want to leave, you can't. You MUST wait until the counter is free. No escape.
            lockInterruptibly() = you're waiting, but the security guard can tap your shoulder and say "Sir, the bank is closing, please leave." You get up and walk out gracefully.
        * */
    }

    // ─────────────────────────────────────────────────────────────
    // DEMO 4: Fairness — FIFO ordering
    // ─────────────────────────────────────────────────────────────

    static void demoFairness() throws InterruptedException {
        System.out.println("\n--- DEMO 4: Fair Lock — FIFO thread ordering ---");

        ReentrantLock fairLock = new ReentrantLock(true); // fair = true

        Runnable task = () -> {
            fairLock.lock();
            try {
                System.out.println("  " + Thread.currentThread().getName() + " acquired lock");
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                fairLock.unlock();
            }
        };

        Thread t1 = new Thread(task, "Thread-A");
        Thread t2 = new Thread(task, "Thread-B");
        Thread t3 = new Thread(task, "Thread-C");

        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();
        // With fair=true, threads get lock roughly in order A → B → C
        // With synchronized or fair=false, order is unpredictable
    }

    // ─────────────────────────────────────────────────────────────
    // DEMO 5: Multiple Conditions — Producer / Consumer
    // ─────────────────────────────────────────────────────────────

    static class BoundedBuffer {

        private final int[] buffer;
        private int count, putIndex, takeIndex;

        private final ReentrantLock lock = new ReentrantLock();
        private final Condition producerWaitQueue = lock.newCondition(); // producer waits here when buffer is FULL
        private final Condition consumerWaitQueue = lock.newCondition(); // consumer waits here when buffer is EMPTY

        BoundedBuffer(int capacity) {
            buffer = new int[capacity];
        }

        void put(int value) throws InterruptedException {

            lock.lock();
            try {
                while (count == buffer.length) {
                    System.out.println("  Producer: buffer full, waiting...");
                    producerWaitQueue.await(); // only producers sleep here
                }
                buffer[putIndex] = value;
                putIndex = (putIndex + 1) % buffer.length;
                count++;
                System.out.println("  Producer: put " + value + " (count=" + count + ")");
                consumerWaitQueue.signal(); // wake only one consumer ✅
            } finally {
                lock.unlock();
            }
        }

        int take() throws InterruptedException {
            lock.lock();
            try {
                while (count == 0) {
                    System.out.println("  Consumer: buffer empty, waiting...");
                    consumerWaitQueue.await(); // only consumers sleep here
                }
                int value = buffer[takeIndex];
                takeIndex = (takeIndex + 1) % buffer.length;
                count--;
                System.out.println("  Consumer: took " + value + " (count=" + count + ")");
                producerWaitQueue.signal(); // wake only one producer ✅
                return value;
            } finally {
                lock.unlock();
            }
        }
    }

    static void demoMultipleConditions() throws InterruptedException {
        System.out.println("\n--- DEMO 5: Multiple Conditions — Producer/Consumer ---");

        BoundedBuffer buffer = new BoundedBuffer(2); // capacity 2

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 4; i++) {
                    buffer.put(i);
                    Thread.sleep(50);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Producer");

        Thread consumer = new Thread(() -> {
            try {
                Thread.sleep(150); // start consuming after producer fills buffer
                for (int i = 0; i < 4; i++) {
                    buffer.take();
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Consumer");

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
    }

    // ─────────────────────────────────────────────────────────────
    // DEMO 6: ReadWriteLock — Concurrent reads
    // ─────────────────────────────────────────────────────────────

    static class SharedCache {

        private String data = "initial";
        private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

        String read(String threadName) {
            rwLock.readLock().lock(); // multiple threads can hold this simultaneously
            try {
                System.out.println("  " + threadName + ": reading → " + data);
                Thread.sleep(100); // simulate slow read
                return data;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            } finally {
                rwLock.readLock().unlock();
            }
        }

        void write(String value) {
            rwLock.writeLock().lock(); // exclusive — no readers or writers
            try {
                System.out.println("  Writer: updating to '" + value + "'");
                data = value;
            } finally {
                rwLock.writeLock().unlock();
            }
        }
    }

    static void demoReadWriteLock() throws InterruptedException {
        System.out.println("\n--- DEMO 6: ReadWriteLock — Concurrent reads ---");

        SharedCache cache = new SharedCache();

        // 3 readers run concurrently (all hold readLock at same time)
        Thread r1 = new Thread(() -> cache.read("Reader-1"));
        Thread r2 = new Thread(() -> cache.read("Reader-2"));
        Thread r3 = new Thread(() -> cache.read("Reader-3"));
        Thread writer = new Thread(() -> cache.write("updated"));

        r1.start();
        r2.start();
        r3.start(); // all 3 read concurrently ✅
        r1.join();
        r2.join();
        r3.join();

        writer.start(); // writer gets exclusive access
        writer.join();

        System.out.println("  Final value: " + cache.read("Main"));
    }

    // ─────────────────────────────────────────────────────────────
    // Main
    // ─────────────────────────────────────────────────────────────

    public static void main(String[] args) throws InterruptedException {
        System.out.println("==========================================");
        System.out.println("  synchronized vs ReentrantLock — Demo");
        System.out.println("==========================================");

//        demoBasic();
//        demoTryLock();
//        demoInterruptible();
//        demoFairness();
//        demoMultipleConditions();
        demoReadWriteLock();

        System.out.println("\n==========================================");
        System.out.println("SUMMARY");
        System.out.println("==========================================");
//        System.out.println("Demo 1 — Basic mutual exclusion     → both work the same");
//        System.out.println("Demo 2 — tryLock with timeout       → ReentrantLock only");
//        System.out.println("Demo 3 — Interruptible wait         → ReentrantLock only");
//        System.out.println("Demo 4 — Fair FIFO ordering         → ReentrantLock only");
//        System.out.println("Demo 5 — Multiple conditions        → ReentrantLock only");
        System.out.println("Demo 6 — Concurrent reads           → ReentrantReadWriteLock only");
    }
}
