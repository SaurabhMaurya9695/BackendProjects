package com.backend.system.design.LLD.Cache.policy;

import java.util.*;

/**
 * LFU (Least Frequently Used) Eviction Policy.
 * 
 * Implementation Details:
 * - Tracks the frequency of access for each key
 * - Evicts the key with the lowest access frequency
 * - If multiple keys have the same frequency, evicts the least recently used among them
 * 
 * Time Complexity:
 * - keyAccessed: O(1)
 * - keyAdded: O(1)
 * - keyRemoved: O(1)
 * - evictKey: O(1)
 * 
 * Space Complexity: O(n) where n is the cache capacity
 * 
 * Use Case:
 * - When you want to keep frequently accessed items in cache
 * - Useful for read-heavy workloads with "hot" data
 * 
 * @param <Key> The type of keys tracked by this policy
 */
public class LFUEvictionPolicy<Key> implements EvictionPolicy<Key> {

    // Track frequency count for each key
    private final Map<Key, Integer> keyFrequency;
    
    // Map from frequency -> list of keys with that frequency (in LRU order)
    private final Map<Integer, LinkedHashSet<Key>> frequencyKeys;
    
    // Track the minimum frequency
    private int minFrequency;

    /**
     * Creates a new LFU eviction policy with the specified capacity.
     * 
     * @param capacity The maximum number of keys this policy can track
     */
    public LFUEvictionPolicy(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.keyFrequency = new HashMap<>((int) (capacity / 0.75) + 1);
        this.frequencyKeys = new HashMap<>();
        this.minFrequency = 0;
    }

    /**
     * When a key is accessed, increment its frequency and update tracking.
     * 
     * @param key The key that was accessed
     */
    @Override
    public void keyAccessed(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        
        if (!keyFrequency.containsKey(key)) {
            // Key doesn't exist - this shouldn't happen in normal flow
            return;
        }
        
        // Increment frequency
        int currentFreq = keyFrequency.get(key);
        int newFreq = currentFreq + 1;
        keyFrequency.put(key, newFreq);
        
        // Remove from current frequency bucket
        frequencyKeys.get(currentFreq).remove(key);
        
        // If this was the last key with minFrequency, increment minFrequency
        if (currentFreq == minFrequency && frequencyKeys.get(currentFreq).isEmpty()) {
            minFrequency = newFreq;
        }
        
        // Add to new frequency bucket
        frequencyKeys.computeIfAbsent(newFreq, k -> new LinkedHashSet<>()).add(key);
    }

    /**
     * When a key is added, initialize its frequency to 1.
     * 
     * @param key The key that was added
     */
    @Override
    public void keyAdded(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        
        // Initialize frequency to 1
        keyFrequency.put(key, 1);
        frequencyKeys.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
        
        // Reset min frequency to 1 since we just added a new key
        minFrequency = 1;
    }

    /**
     * When a key is removed, remove it from all tracking structures.
     * 
     * @param key The key that was removed
     */
    @Override
    public void keyRemoved(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        
        if (!keyFrequency.containsKey(key)) {
            return;
        }
        
        int freq = keyFrequency.get(key);
        keyFrequency.remove(key);
        frequencyKeys.get(freq).remove(key);
        
        // Clean up empty frequency bucket
        if (frequencyKeys.get(freq).isEmpty()) {
            frequencyKeys.remove(freq);
        }
    }

    /**
     * Returns the least frequently used key for eviction.
     * If multiple keys have the same frequency, returns the least recently used.
     * 
     * @return The LFU key, or null if no keys are tracked
     */
    @Override
    public Key evictKey() {
        if (keyFrequency.isEmpty()) {
            return null;
        }
        
        // Get the set of keys with minimum frequency
        LinkedHashSet<Key> keysWithMinFreq = frequencyKeys.get(minFrequency);
        
        if (keysWithMinFreq == null || keysWithMinFreq.isEmpty()) {
            return null;
        }
        
        // Get the first key (least recently used among least frequent)
        Key keyToEvict = keysWithMinFreq.iterator().next();
        
        // Remove from tracking
        keysWithMinFreq.remove(keyToEvict);
        keyFrequency.remove(keyToEvict);
        
        // Clean up empty frequency bucket
        if (keysWithMinFreq.isEmpty()) {
            frequencyKeys.remove(minFrequency);
        }
        
        return keyToEvict;
    }

    @Override
    public String getPolicyName() {
        return "LFU (Least Frequently Used)";
    }
}

