package com.backend.system.design.LLD.Cache.policy;

/**
 * Interface representing a cache eviction policy (e.g., LRU, FIFO, LFU).
 * The policy dictates which key should be removed when the cache is full.
 * 
 * SOLID Principles:
 * - Open/Closed: Open for extension (new policies), closed for modification
 * - Single Responsibility: Only responsible for tracking and deciding eviction
 * - Liskov Substitution: All implementations can be used interchangeably
 * 
 * @param <Key> The type of keys tracked by this policy
 */
public interface EvictionPolicy<Key> {

    /**
     * Called when a key is accessed (read).
     * This updates the policy's internal state (e.g., move an LRU key to the front).
     *
     * @param key The key that was accessed
     */
    void keyAccessed(Key key);

    /**
     * Called when a new key is added to the cache.
     * This tracks the new key's position or history within the policy.
     *
     * @param key The key that was added
     */
    void keyAdded(Key key);

    /**
     * Called when a key is explicitly removed from the cache.
     * This removes the key from the policy's internal tracking data structures.
     *
     * @param key The key that was removed
     */
    void keyRemoved(Key key);

    /**
     * Returns the key that should be evicted from the cache based on the policy's rules.
     * This is called when the cache is full and a new entry needs to be inserted.
     *
     * @return The key to be evicted, or null if no key can be evicted
     */
    Key evictKey();
    
    /**
     * Returns the name/type of this eviction policy.
     * Useful for logging and debugging.
     * 
     * @return The policy name (e.g., "LRU", "FIFO", "LFU")
     */
    String getPolicyName();
}


