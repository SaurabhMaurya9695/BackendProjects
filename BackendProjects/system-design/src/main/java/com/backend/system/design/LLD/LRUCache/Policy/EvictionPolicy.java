package com.backend.system.design.LLD.LRUCache.Policy;

/**
 * Interface representing a cache eviction policy (e.g., LRU, FIFO, LFU).
 * The policy dictates which key-value pair should be removed when the cache is full.
 */
public interface EvictionPolicy<Key> {

    /**
     * Called when a key is accessed (read or written).
     * This is used to update the policy's internal state (e.g., move an LRU key to the front).
     *
     * @param key The key that was just accessed.
     */
    void keyAccessed(Key key);

    /**
     * Called when a new key is added to the cache.
     * This is used to track the new key's position or history within the policy.
     *
     * @param key The key that was just added.
     */
    void keyAdded(Key key);

    /**
     * Called when a key is explicitly removed from the cache (e.g., due to an 'invalidate' operation).
     * This removes the key from the policy's internal tracking data structures.
     *
     * @param key The key that was just removed.
     */
    void keyRemoved(Key key);

    /**
     * Returns the key that should be evicted from the cache based on the policy's rules.
     * This is called when the cache is full and a new entry needs to be inserted.
     *
     * @return The key to be evicted.
     */
    Key evictKey();
}