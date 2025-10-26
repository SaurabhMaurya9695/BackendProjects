package com.backend.system.design.LLD.Cache;

import com.backend.system.design.LLD.Cache.exception.InvalidCapacityException;
import com.backend.system.design.LLD.Cache.policy.EvictionPolicy;
import com.backend.system.design.LLD.Cache.storage.Storage;

import java.util.Optional;

/**
 * Implementation of the Cache interface.
 * 
 * This class demonstrates all SOLID principles:
 * 
 * 1. Single Responsibility Principle (SRP):
 *    - This class only coordinates between Storage and EvictionPolicy
 *    - Actual storage is delegated to Storage interface
 *    - Eviction logic is delegated to EvictionPolicy interface
 * 
 * 2. Open/Closed Principle (OCP):
 *    - Open for extension: Can add new eviction policies without modifying this class
 *    - Closed for modification: Core logic doesn't change when adding new policies
 * 
 * 3. Liskov Substitution Principle (LSP):
 *    - Any Storage implementation can be used
 *    - Any EvictionPolicy implementation can be used
 * 
 * 4. Interface Segregation Principle (ISP):
 *    - Uses focused interfaces (Storage, EvictionPolicy, Cache)
 * 
 * 5. Dependency Inversion Principle (DIP):
 *    - Depends on abstractions (interfaces), not concrete implementations
 *    - Dependencies are injected via constructor
 * 
 * @param <Key> The type of keys maintained by this cache
 * @param <Value> The type of cached values
 */
public class CacheImpl<Key, Value> implements Cache<Key, Value> {
    
    private final Storage<Key, Value> storage;
    private final EvictionPolicy<Key> evictionPolicy;
    
    // Statistics tracking
    private long hits = 0;
    private long misses = 0;
    private long evictions = 0;
    
    /**
     * Creates a new Cache with the given storage and eviction policy.
     * 
     * This constructor demonstrates Dependency Injection and Dependency Inversion Principle.
     * The cache doesn't create its own dependencies; they are provided from outside.
     * 
     * @param storage The storage implementation to use
     * @param evictionPolicy The eviction policy to use
     * @throws IllegalArgumentException if storage or evictionPolicy is null
     */
    public CacheImpl(Storage<Key, Value> storage, EvictionPolicy<Key> evictionPolicy) {
        if (storage == null) {
            throw new IllegalArgumentException("Storage cannot be null");
        }
        if (evictionPolicy == null) {
            throw new IllegalArgumentException("Eviction policy cannot be null");
        }
        if (storage.capacity() <= 0) {
            throw new InvalidCapacityException(storage.capacity());
        }
        
        this.storage = storage;
        this.evictionPolicy = evictionPolicy;
    }
    
    /**
     * Adds or updates a key-value pair in the cache.
     * If the cache is full, evicts a key according to the eviction policy before adding.
     * 
     * Time Complexity: O(1) for LRU and LFU policies
     * 
     * @param key The key to add/update
     * @param value The value to associate with the key
     * @throws IllegalArgumentException if key or value is null
     */
    @Override
    public void put(Key key, Value value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        
        // If key already exists, update it and mark as accessed
        if (storage.containsKey(key)) {
            storage.put(key, value);
            evictionPolicy.keyAccessed(key);
            return;
        }
        
        // If cache is full, evict a key first
        if (storage.isFull()) {
            Key keyToEvict = evictionPolicy.evictKey();
            if (keyToEvict != null) {
                storage.remove(keyToEvict);
                evictions++;
            }
        }
        
        // Add the new key-value pair
        storage.put(key, value);
        evictionPolicy.keyAdded(key);
    }
    
    /**
     * Retrieves the value associated with the given key.
     * Updates the eviction policy to mark this key as accessed.
     * 
     * Time Complexity: O(1)
     * 
     * @param key The key whose value is to be retrieved
     * @return An Optional containing the value if present, empty Optional otherwise
     * @throws IllegalArgumentException if key is null
     */
    @Override
    public Optional<Value> get(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        
        Optional<Value> value = storage.get(key);
        
        if (value.isPresent()) {
            // Cache hit - update eviction policy
            evictionPolicy.keyAccessed(key);
            hits++;
        } else {
            // Cache miss
            misses++;
        }
        
        return value;
    }
    
    /**
     * Removes the key-value pair from the cache.
     * 
     * Time Complexity: O(1) for LRU and LFU, O(n) for FIFO
     * 
     * @param key The key to remove
     * @return An Optional containing the removed value if present, empty Optional otherwise
     * @throws IllegalArgumentException if key is null
     */
    @Override
    public Optional<Value> remove(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        
        Optional<Value> value = storage.remove(key);
        
        if (value.isPresent()) {
            evictionPolicy.keyRemoved(key);
        }
        
        return value;
    }
    
    /**
     * Checks if the cache contains the given key.
     * 
     * Time Complexity: O(1)
     * 
     * @param key The key to check
     * @return true if the cache contains the key, false otherwise
     * @throws IllegalArgumentException if key is null
     */
    @Override
    public boolean containsKey(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        return storage.containsKey(key);
    }
    
    @Override
    public int size() {
        return storage.size();
    }
    
    @Override
    public int capacity() {
        return storage.capacity();
    }
    
    @Override
    public boolean isEmpty() {
        return storage.size() == 0;
    }
    
    @Override
    public boolean isFull() {
        return storage.isFull();
    }
    
    /**
     * Removes all entries from the cache.
     * Note: This doesn't reset statistics.
     */
    @Override
    public void clear() {
        storage.clear();
        // Note: We don't reset the eviction policy here as it will be reconstructed on new adds
    }
    
    /**
     * Returns statistics about the cache.
     * 
     * @return Cache statistics including hits, misses, evictions, and utilization
     */
    @Override
    public CacheStats getStats() {
        return new CacheStats(hits, misses, evictions, storage.size(), storage.capacity());
    }
}

