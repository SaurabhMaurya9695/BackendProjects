package com.backend.system.design.LLD.Cache;

import java.util.Optional;

/**
 * Demonstration class showcasing the Cache System design.
 * <p>
 * This demo illustrates:
 * 1. Creating caches with different eviction policies (LRU, FIFO, LFU)
 * 2. Basic cache operations (put, get, remove)
 * 3. Eviction behavior under different policies
 * 4. Cache statistics tracking
 * 5. Extensibility of the design
 */
public class CacheDemo {

    public static void main(String[] args) {
        System.out.println("=================================");
        System.out.println("CACHE SYSTEM DESIGN DEMONSTRATION");
        System.out.println("Following SOLID Principles and Low-Level Design Best Practices");
        System.out.println("=================================");
        System.out.println();

        // Demo 1: LRU Cache
        demonstrateLRUCache();

        System.out.println();

        // Demo 2: FIFO Cache
        demonstrateFIFOCache();

        System.out.println();

        // Demo 3: LFU Cache
        demonstrateLFUCache();

        System.out.println();

        // Demo 4: Cache Statistics
        demonstrateCacheStats();
    }

    /**
     * Demonstrates LRU (Least Recently Used) Cache behavior.
     */
    private static void demonstrateLRUCache() {
        System.out.println("DEMO 1: LRU (Least Recently Used) Cache");
        System.out.println("=================================");

        // Create an LRU cache with capacity 3
        Cache<String, Integer> cache = CacheFactory.createLRUCache(3);
        System.out.println("Created LRU Cache with capacity: " + cache.capacity());
        System.out.println();

        // Add some entries
        System.out.println("Adding entries to cache...");
        cache.put("A", 1);
        System.out.println("  put('A', 1) -> Cache: [A]");

        cache.put("B", 2);
        System.out.println("  put('B', 2) -> Cache: [B, A]");

        cache.put("C", 3);
        System.out.println("  put('C', 3) -> Cache: [C, B, A]");
        System.out.println("  Cache is now full (size: " + cache.size() + "/" + cache.capacity() + ")");
        System.out.println();

        // Access an entry
        System.out.println("Accessing entry 'A'...");
        Optional<Integer> value = cache.get("A");
        System.out.println("  get('A') = " + value.orElse(null) + " -> Cache: [A, C, B]");
        System.out.println();

        // Add new entry - should evict 'B' (least recently used)
        System.out.println("Adding new entry 'D' to full cache...");
        cache.put("D", 4);
        System.out.println("  put('D', 4) -> Evicted 'B' (LRU) -> Cache: [D, A, C]");
        System.out.println();

        // Verify eviction
        System.out.println("Verifying eviction...");
        System.out.println("  get('B') = " + cache.get("B").orElse(null) + " (Cache Miss - 'B' was evicted)");
        System.out.println("  get('A') = " + cache.get("A").orElse(null) + " (Cache Hit)");
        System.out.println("  get('C') = " + cache.get("C").orElse(null) + " (Cache Hit)");
        System.out.println("  get('D') = " + cache.get("D").orElse(null) + " (Cache Hit)");
    }

    /**
     * Demonstrates FIFO (First In First Out) Cache behavior.
     */
    private static void demonstrateFIFOCache() {
        System.out.println("DEMO 2: FIFO (First In First Out) Cache");
        System.out.println("=================================");

        // Create a FIFO cache with capacity 3
        Cache<String, Integer> cache = CacheFactory.createFIFOCache(3);
        System.out.println("Created FIFO Cache with capacity: " + cache.capacity());
        System.out.println();

        // Add some entries
        System.out.println("Adding entries to cache...");
        cache.put("A", 1);
        System.out.println("  put('A', 1) -> Queue: [A]");

        cache.put("B", 2);
        System.out.println("  put('B', 2) -> Queue: [A, B]");

        cache.put("C", 3);
        System.out.println("  put('C', 3) -> Queue: [A, B, C]");
        System.out.println("  Cache is now full (size: " + cache.size() + "/" + cache.capacity() + ")");
        System.out.println();

        // Access an entry (doesn't affect FIFO order)
        System.out.println("Accessing entry 'A' (doesn't affect FIFO order)...");
        cache.get("A");
        System.out.println("  get('A') = 1 -> Queue still: [A, B, C]");
        System.out.println();

        // Add new entry - should evict 'A' (first in)
        System.out.println("Adding new entry 'D' to full cache...");
        cache.put("D", 4);
        System.out.println("  put('D', 4) -> Evicted 'A' (first in) -> Queue: [B, C, D]");
        System.out.println();

        // Verify eviction
        System.out.println("Verifying eviction...");
        System.out.println("  get('A') = " + cache.get("A").orElse(null) + " (Cache Miss - 'A' was evicted)");
        System.out.println("  get('B') = " + cache.get("B").orElse(null) + " (Cache Hit)");
    }

    /**
     * Demonstrates LFU (Least Frequently Used) Cache behavior.
     */
    private static void demonstrateLFUCache() {
        System.out.println("DEMO 3: LFU (Least Frequently Used) Cache");
        System.out.println("=================================");

        // Create an LFU cache with capacity 3
        Cache<String, Integer> cache = CacheFactory.createLFUCache(3);
        System.out.println("Created LFU Cache with capacity: " + cache.capacity());
        System.out.println();

        // Add some entries
        System.out.println("Adding entries to cache...");
        cache.put("A", 1);
        System.out.println("  put('A', 1) -> Frequency: A=1");

        cache.put("B", 2);
        System.out.println("  put('B', 2) -> Frequency: A=1, B=1");

        cache.put("C", 3);
        System.out.println("  put('C', 3) -> Frequency: A=1, B=1, C=1");
        System.out.println("  Cache is now full (size: " + cache.size() + "/" + cache.capacity() + ")");
        System.out.println();

        // Access entries to change frequencies
        System.out.println("Accessing entries to change frequencies...");
        cache.get("A");
        System.out.println("  get('A') -> Frequency: A=2, B=1, C=1");

        cache.get("A");
        System.out.println("  get('A') -> Frequency: A=3, B=1, C=1");

        cache.get("B");
        System.out.println("  get('B') -> Frequency: A=3, B=2, C=1");
        System.out.println();

        // Add new entry - should evict 'C' (least frequently used)
        System.out.println("Adding new entry 'D' to full cache...");
        cache.put("D", 4);
        System.out.println("  put('D', 4) -> Evicted 'C' (LFU) -> Frequency: A=3, B=2, D=1");
        System.out.println();

        // Verify eviction
        System.out.println("Verifying eviction...");
        System.out.println("  get('C') = " + cache.get("C").orElse(null) + " (Cache Miss - 'C' was evicted)");
        System.out.println("  get('A') = " + cache.get("A").orElse(null) + " (Cache Hit)");
        System.out.println("  get('B') = " + cache.get("B").orElse(null) + " (Cache Hit)");
    }

    /**
     * Demonstrates cache statistics tracking.
     */
    private static void demonstrateCacheStats() {
        System.out.println("DEMO 4: Cache Statistics");
        System.out.println("=================================");

        Cache<Integer, String> cache = CacheFactory.createLRUCache(5);
        System.out.println("Created LRU Cache with capacity: " + cache.capacity());
        System.out.println();

        // Perform various operations
        System.out.println("Performing cache operations...");
        cache.put(1, "One");
        cache.put(2, "Two");
        cache.put(3, "Three");
        cache.put(4, "Four");
        cache.put(5, "Five");

        // Simulate cache hits
        cache.get(1);  // Hit
        cache.get(2);  // Hit
        cache.get(1);  // Hit

        // Simulate cache misses
        cache.get(10); // Miss
        cache.get(20); // Miss

        // Trigger evictions
        cache.put(6, "Six");   // Evicts 3
        cache.put(7, "Seven"); // Evicts 4

        System.out.println("Operations completed.");
        System.out.println();

        // Display statistics
        CacheStats stats = cache.getStats();
        System.out.println("Cache Statistics:");
        System.out.println("  " + stats);
        System.out.println();
        System.out.println("Detailed Metrics:");
        System.out.println("  Total Requests: " + stats.getTotalRequests());
        System.out.println("  Hits: " + stats.getHits());
        System.out.println("  Misses: " + stats.getMisses());
        System.out.println("  Evictions: " + stats.getEvictions());
        System.out.println("  Hit Rate: " + String.format("%.2f%%", stats.getHitRate() * 100));
        System.out.println("  Miss Rate: " + String.format("%.2f%%", stats.getMissRate() * 100));
        System.out.println("  Current Size: " + stats.getCurrentSize() + "/" + stats.getCapacity());
        System.out.println("  Utilization: " + String.format("%.2f%%", stats.getUtilization() * 100));

        System.out.println();
        System.out.println("=================================");
        System.out.println("DEMONSTRATION COMPLETE");
        System.out.println("=================================");
    }
}



