package com.backend.system.design.LLD.Cache.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * HashMap-based implementation of Storage interface.
 * Provides O(1) average time complexity for get, put, and remove operations.
 * 
 * This implementation is NOT thread-safe. For concurrent access, use ConcurrentHashMapStorage.
 * 
 * @param <Key> The type of keys maintained by this storage
 * @param <Value> The type of mapped values
 */
public class HashMapStorage<Key, Value> implements Storage<Key, Value> {
    
    private final Map<Key, Value> storage;
    private final int capacity;
    
    /**
     * Creates a new HashMapStorage with the specified capacity.
     * 
     * @param capacity The maximum number of entries this storage can hold
     * @throws IllegalArgumentException if capacity is not positive
     */
    public HashMapStorage(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive, got: " + capacity);
        }
        this.capacity = capacity;
        // Initialize with capacity and load factor 0.75 for optimal performance
        this.storage = new HashMap<>((int) (capacity / 0.75) + 1);
    }
    
    @Override
    public void put(Key key, Value value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        storage.put(key, value);
    }
    
    @Override
    public Optional<Value> get(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        return Optional.ofNullable(storage.get(key));
    }
    
    @Override
    public Optional<Value> remove(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        return Optional.ofNullable(storage.remove(key));
    }
    
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
        return capacity;
    }
    
    @Override
    public boolean isFull() {
        return storage.size() >= capacity;
    }
    
    @Override
    public void clear() {
        storage.clear();
    }
}

