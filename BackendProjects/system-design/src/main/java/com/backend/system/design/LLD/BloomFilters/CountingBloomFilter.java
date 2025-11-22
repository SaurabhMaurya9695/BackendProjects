package com.backend.system.design.LLD.BloomFilters;

import com.backend.system.design.LLD.BloomFilters.config.BloomFilterConfig;
import com.backend.system.design.LLD.BloomFilters.hash.HashFunction;
import com.backend.system.design.LLD.BloomFilters.hash.MurmurHashFunction;

/**
 * Counting Bloom Filter - Supports element deletion
 * <p>
 * Uses counters instead of bits. Counters increment on add, decrement on remove.
 * <p>
 * Trade-offs:
 * - Supports deletion (unlike standard Bloom Filter)
 * - Uses more space (int counters vs bits)
 * - Same time complexity O(k)
 *
 * @param <T> the type of elements to store
 */
public class CountingBloomFilter<T> {
    
    private final int[] counters;
    private final BloomFilterConfig config;
    private final HashFunction<T> hashFunction;
    private int elementCount;
    
    /**
     * Creates a Counting Bloom Filter with optimal configuration
     */
    public CountingBloomFilter(int expectedElements, double falsePositiveProbability) {
        this(BloomFilterConfig.optimal(expectedElements, falsePositiveProbability), new MurmurHashFunction<>());
    }
    
    /**
     * Creates a Counting Bloom Filter with custom configuration
     */
    public CountingBloomFilter(BloomFilterConfig config, HashFunction<T> hashFunction) {
        this.config = config;
        this.counters = new int[config.getBitSetSize()];
        this.hashFunction = hashFunction;
        this.elementCount = 0;
    }
    
    /**
     * Adds an element
     */
    public void add(T element) {
        validateElement(element);
        
        for (int i = 0; i < config.getNumberOfHashFunctions(); i++) {
            int position = hashFunction.hash(element, i, config.getBitSetSize());
            if (counters[position] < Integer.MAX_VALUE) {
                counters[position]++;
            }
        }
        elementCount++;
    }
    
    /**
     * Removes an element
     *
     * @return true if removed, false if not present
     */
    public boolean remove(T element) {
        if (!mightContain(element)) {
            return false;
        }
        
        for (int i = 0; i < config.getNumberOfHashFunctions(); i++) {
            int position = hashFunction.hash(element, i, config.getBitSetSize());
            if (counters[position] > 0) {
                counters[position]--;
            }
        }
        
        if (elementCount > 0) {
            elementCount--;
        }
        return true;
    }
    
    /**
     * Checks if element might be present
     */
    public boolean mightContain(T element) {
        if (element == null) {
            return false;
        }
        
        for (int i = 0; i < config.getNumberOfHashFunctions(); i++) {
            int position = hashFunction.hash(element, i, config.getBitSetSize());
            if (counters[position] == 0) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Clears all counters
     */
    public void clear() {
        for (int i = 0; i < counters.length; i++) {
            counters[i] = 0;
        }
        elementCount = 0;
    }
    
    // Counter Statistics
    
    public int getNonZeroCounters() {
        int count = 0;
        for (int counter : counters) {
            if (counter > 0) count++;
        }
        return count;
    }
    
    public int getMaxCounter() {
        int max = 0;
        for (int counter : counters) {
            max = Math.max(max, counter);
        }
        return max;
    }
    
    public double getAverageCounterValue() {
        int sum = 0;
        int nonZeroCount = 0;
        for (int counter : counters) {
            if (counter > 0) {
                sum += counter;
                nonZeroCount++;
            }
        }
        return nonZeroCount > 0 ? (double) sum / nonZeroCount : 0.0;
    }
    
    // Helper methods
    
    private void validateElement(T element) {
        if (element == null) {
            throw new IllegalArgumentException("Element cannot be null");
        }
    }
    
    // Getters
    
    public int getBitSetSize() {
        return config.getBitSetSize();
    }
    
    public int getNumberOfHashFunctions() {
        return config.getNumberOfHashFunctions();
    }
    
    public int getElementCount() {
        return elementCount;
    }
}

