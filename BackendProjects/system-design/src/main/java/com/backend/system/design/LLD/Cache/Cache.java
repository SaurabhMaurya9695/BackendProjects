package com.backend.system.design.LLD.Cache;

import java.util.Optional;

/**
 * Cache interface defining core cache operations.
 * 
 * This interface follows the Single Responsibility and Interface Segregation principles,
 * providing a clean, focused API for cache operations.
 * 
 * Design Principles Applied:
 * - Single Responsibility: Only responsible for cache operations
 * - Interface Segregation: Minimal, focused interface
 * - Dependency Inversion: Clients depend on this abstraction
 * 
 * @param <Key> The type of keys maintained by this cache
 * @param <Value> The type of cached values
 */
public interface Cache<Key, Value> {
    
    /**
     * Adds or updates a key-value pair in the cache.
     * If the cache is full, evicts a key according to the eviction policy.
     * 
     * @param key The key to add/update
     * @param value The value to associate with the key
     * @throws IllegalArgumentException if key or value is null
     */
    void put(Key key, Value value);
    
    /**
     * Retrieves the value associated with the given key.
     * Updates the eviction policy to mark this key as accessed.
     * 
     * @param key The key whose value is to be retrieved
     * @return An Optional containing the value if present, empty Optional otherwise
     * @throws IllegalArgumentException if key is null
     */
    Optional<Value> get(Key key);
    
    /**
     * Removes the key-value pair from the cache.
     * 
     * @param key The key to remove
     * @return An Optional containing the removed value if present, empty Optional otherwise
     * @throws IllegalArgumentException if key is null
     */
    Optional<Value> remove(Key key);
    
    /**
     * Checks if the cache contains the given key.
     * 
     * @param key The key to check
     * @return true if the cache contains the key, false otherwise
     * @throws IllegalArgumentException if key is null
     */
    boolean containsKey(Key key);
    
    /**
     * Returns the current number of entries in the cache.
     * 
     * @return The size of the cache
     */
    int size();
    
    /**
     * Returns the maximum capacity of the cache.
     * 
     * @return The capacity
     */
    int capacity();
    
    /**
     * Checks if the cache is empty.
     * 
     * @return true if the cache has no entries
     */
    boolean isEmpty();
    
    /**
     * Checks if the cache is full.
     * 
     * @return true if the cache has reached its capacity
     */
    boolean isFull();
    
    /**
     * Removes all entries from the cache.
     */
    void clear();
    
    /**
     * Returns statistics about the cache (hit rate, miss rate, etc.).
     * 
     * @return Cache statistics
     */
    CacheStats getStats();
}

