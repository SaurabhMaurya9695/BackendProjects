package com.backend.system.design.LLD.Cache;

import com.backend.system.design.LLD.Cache.policy.*;
import com.backend.system.design.LLD.Cache.storage.HashMapStorage;
import com.backend.system.design.LLD.Cache.storage.Storage;

/**
 * Factory class for creating Cache instances with different configurations.
 * 
 * This demonstrates the Factory Pattern, which:
 * - Encapsulates object creation logic
 * - Provides a simple API for creating complex objects
 * - Makes it easy to create caches with different policies
 * 
 * Benefits:
 * - Client code doesn't need to know about concrete implementations
 * - Easy to add new cache configurations
 * - Centralized cache creation logic
 */
public class CacheFactory {
    
    /**
     * Enum representing available eviction policy types.
     */
    public enum EvictionPolicyType {
        LRU,    // Least Recently Used
        FIFO,   // First In First Out
        LFU     // Least Frequently Used
    }
    
    /**
     * Creates a cache with the specified capacity and eviction policy.
     * Uses HashMapStorage as the default storage implementation.
     * 
     * @param capacity The maximum number of entries the cache can hold
     * @param policyType The eviction policy to use
     * @param <K> The type of keys
     * @param <V> The type of values
     * @return A new Cache instance
     * @throws IllegalArgumentException if capacity is not positive or policyType is null
     */
    public static <K, V> Cache<K, V> createCache(int capacity, EvictionPolicyType policyType) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive, got: " + capacity);
        }
        if (policyType == null) {
            throw new IllegalArgumentException("Policy type cannot be null");
        }
        
        // Create storage
        Storage<K, V> storage = new HashMapStorage<>(capacity);
        
        // Create eviction policy based on type
        EvictionPolicy<K> evictionPolicy = createEvictionPolicy(capacity, policyType);
        
        // Create and return cache
        return new CacheImpl<>(storage, evictionPolicy);
    }
    
    /**
     * Creates a cache with LRU eviction policy (most common use case).
     * 
     * @param capacity The maximum number of entries the cache can hold
     * @param <K> The type of keys
     * @param <V> The type of values
     * @return A new Cache instance with LRU eviction
     */
    public static <K, V> Cache<K, V> createLRUCache(int capacity) {
        return createCache(capacity, EvictionPolicyType.LRU);
    }
    
    /**
     * Creates a cache with FIFO eviction policy.
     * 
     * @param capacity The maximum number of entries the cache can hold
     * @param <K> The type of keys
     * @param <V> The type of values
     * @return A new Cache instance with FIFO eviction
     */
    public static <K, V> Cache<K, V> createFIFOCache(int capacity) {
        return createCache(capacity, EvictionPolicyType.FIFO);
    }
    
    /**
     * Creates a cache with LFU eviction policy.
     * 
     * @param capacity The maximum number of entries the cache can hold
     * @param <K> The type of keys
     * @param <V> The type of values
     * @return A new Cache instance with LFU eviction
     */
    public static <K, V> Cache<K, V> createLFUCache(int capacity) {
        return createCache(capacity, EvictionPolicyType.LFU);
    }
    
    /**
     * Advanced factory method that allows specifying custom storage and eviction policy.
     * This provides maximum flexibility for advanced use cases.
     * 
     * @param storage The storage implementation to use
     * @param evictionPolicy The eviction policy to use
     * @param <K> The type of keys
     * @param <V> The type of values
     * @return A new Cache instance
     */
    public static <K, V> Cache<K, V> createCache(
            Storage<K, V> storage, 
            EvictionPolicy<K> evictionPolicy) {
        return new CacheImpl<>(storage, evictionPolicy);
    }
    
    /**
     * Helper method to create eviction policy based on type.
     * 
     * @param capacity The capacity for the eviction policy
     * @param policyType The type of policy to create
     * @param <K> The type of keys
     * @return An EvictionPolicy instance
     */
    private static <K> EvictionPolicy<K> createEvictionPolicy(
            int capacity, 
            EvictionPolicyType policyType) {
        
        switch (policyType) {
            case LRU:
                return new LRUEvictionPolicy<>(capacity);
            case FIFO:
                return new FIFOEvictionPolicy<>(capacity);
            case LFU:
                return new LFUEvictionPolicy<>(capacity);
            default:
                throw new IllegalArgumentException("Unknown policy type: " + policyType);
        }
    }
}

