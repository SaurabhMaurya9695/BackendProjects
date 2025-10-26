package com.backend.system.design.LLD.Cache.storage;

import java.util.Optional;

/**
 * Storage interface for cache data.
 * This abstraction allows for different storage implementations (HashMap, ConcurrentHashMap, TreeMap, etc.)
 * 
 * SOLID Principles:
 * - Interface Segregation: Focused interface with only essential operations
 * - Dependency Inversion: Cache depends on this abstraction, not concrete implementations
 * 
 * @param <Key> The type of keys maintained by this storage
 * @param <Value> The type of mapped values
 */
public interface Storage<Key, Value> {
    
    /**
     * Associates the specified value with the specified key.
     * 
     * @param key The key with which the specified value is to be associated
     * @param value The value to be associated with the specified key
     */
    void put(Key key, Value value);
    
    /**
     * Returns the value to which the specified key is mapped.
     * 
     * @param key The key whose associated value is to be returned
     * @return An Optional containing the value if present, empty Optional otherwise
     */
    Optional<Value> get(Key key);
    
    /**
     * Removes the mapping for the specified key.
     * 
     * @param key The key whose mapping is to be removed
     * @return An Optional containing the previous value if present, empty Optional otherwise
     */
    Optional<Value> remove(Key key);
    
    /**
     * Returns true if this storage contains a mapping for the specified key.
     * 
     * @param key The key whose presence is to be tested
     * @return true if this storage contains the key
     */
    boolean containsKey(Key key);
    
    /**
     * Returns the number of key-value mappings in this storage.
     * 
     * @return The number of mappings
     */
    int size();
    
    /**
     * Returns the maximum capacity of this storage.
     * 
     * @return The capacity
     */
    int capacity();
    
    /**
     * Returns true if this storage is at full capacity.
     * 
     * @return true if size equals capacity
     */
    boolean isFull();
    
    /**
     * Removes all mappings from this storage.
     */
    void clear();
}

