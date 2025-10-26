package com.backend.system.design.LLD.Cache.policy;

import java.util.LinkedList;
import java.util.Queue;

/**
 * FIFO (First In First Out) Eviction Policy.
 * 
 * Implementation Details:
 * - Uses a Queue to maintain insertion order
 * - First added items are evicted first, regardless of access patterns
 * - Simple and predictable behavior
 * 
 * Time Complexity:
 * - keyAccessed: O(1) - no-op for FIFO
 * - keyAdded: O(1)
 * - keyRemoved: O(n) - need to search and remove from queue
 * - evictKey: O(1)
 * 
 * Space Complexity: O(n) where n is the cache capacity
 * 
 * Use Case:
 * - When you want predictable eviction based solely on insertion time
 * - Simpler than LRU but doesn't consider access patterns
 * 
 * @param <Key> The type of keys tracked by this policy
 */
public class FIFOEvictionPolicy<Key> implements EvictionPolicy<Key> {

    // Queue to maintain FIFO order
    private final Queue<Key> queue;

    /**
     * Creates a new FIFO eviction policy with the specified capacity.
     * 
     * @param capacity The maximum number of keys this policy can track
     */
    public FIFOEvictionPolicy(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.queue = new LinkedList<>();
    }

    /**
     * FIFO doesn't care about access patterns, so this is a no-op.
     * 
     * @param key The key that was accessed
     */
    @Override
    public void keyAccessed(Key key) {
        // No-op for FIFO - access doesn't affect eviction order
    }

    /**
     * When a key is added, add it to the end of the queue.
     * 
     * @param key The key that was added
     */
    @Override
    public void keyAdded(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        queue.offer(key);
    }

    /**
     * When a key is removed, remove it from the queue.
     * Note: This is O(n) operation in worst case.
     * 
     * @param key The key that was removed
     */
    @Override
    public void keyRemoved(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        queue.remove(key);
    }

    /**
     * Returns the oldest key (first in) for eviction.
     * 
     * @return The FIFO key, or null if no keys are tracked
     */
    @Override
    public Key evictKey() {
        return queue.poll(); // Remove and return the head of the queue
    }

    @Override
    public String getPolicyName() {
        return "FIFO (First In First Out)";
    }
}

