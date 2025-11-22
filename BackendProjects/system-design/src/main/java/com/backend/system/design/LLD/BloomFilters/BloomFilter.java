package com.backend.system.design.LLD.BloomFilters;

import com.backend.system.design.LLD.BloomFilters.config.BloomFilterConfig;
import com.backend.system.design.LLD.BloomFilters.config.BloomFilterMath;
import com.backend.system.design.LLD.BloomFilters.config.BloomFilterStats;
import com.backend.system.design.LLD.BloomFilters.hash.HashFunction;
import com.backend.system.design.LLD.BloomFilters.hash.MurmurHashFunction;

import java.util.BitSet;

/**
 * A space-efficient probabilistic data structure for set membership testing.
 * <p>
 * Characteristics:
 * - No false negatives (if absent, definitely not in set)
 * - Possible false positives (if present, might be in set)
 * - O(k) insert and lookup time
 * - Cannot remove elements
 *
 * @param <T> the type of elements to store
 */
public class BloomFilter<T> {

    private final BitSet bitSet;
    private final BloomFilterConfig config;
    private final HashFunction<T> hashFunction;
    private int elementCount;

    /**
     * Creates a Bloom Filter with optimal configuration
     *
     * @param expectedElements         Expected number of elements
     * @param falsePositiveProbability Desired false positive rate (0.0 to 1.0)
     */
    public BloomFilter(int expectedElements, double falsePositiveProbability) {
        this(BloomFilterConfig.optimal(expectedElements, falsePositiveProbability), new MurmurHashFunction<>());
    }

    /**
     * Creates a Bloom Filter with custom configuration
     *
     * @param config       Bloom filter configuration
     * @param hashFunction Hash function to use
     */
    public BloomFilter(BloomFilterConfig config, HashFunction<T> hashFunction) {
        this.config = config;
        this.bitSet = new BitSet(config.getBitSetSize());
        this.hashFunction = hashFunction;
        this.elementCount = 0;
    }

    /**
     * Adds an element to the filter
     *
     * @param element The element to add
     */
    public void add(T element) {
        validateElement(element);

        for (int i = 0; i < config.getNumberOfHashFunctions(); i++) {
            int position = hashFunction.hash(element, i, config.getBitSetSize());
            bitSet.set(position);
        }
        elementCount++;
    }

    /**
     * Checks if an element might exist in the set
     *
     * @param element The element to check
     * @return true if possibly present, false if definitely absent
     */
    public boolean mightContain(T element) {
        if (element == null) {
            return false;
        }

        for (int i = 0; i < config.getNumberOfHashFunctions(); i++) {
            int position = hashFunction.hash(element, i, config.getBitSetSize());
            if (!bitSet.get(position)) {
                return false; // Definitely not present
            }
        }
        return true; // Might be present
    }

    /**
     * Clears all elements
     */
    public void clear() {
        bitSet.clear();
        elementCount = 0;
    }

    /**
     * Gets comprehensive statistics
     */
    public BloomFilterStats getStats() {
        double fpProbability = BloomFilterMath.calculateFalsePositiveProbability(config.getNumberOfHashFunctions(),
                elementCount, config.getBitSetSize());

        return new BloomFilterStats(config.getBitSetSize(), config.getNumberOfHashFunctions(), elementCount,
                bitSet.cardinality(), getBitUtilization(), fpProbability);
    }

    // Helper methods

    private void validateElement(T element) {
        if (element == null) {
            throw new IllegalArgumentException("Element cannot be null");
        }
    }

    private double getBitUtilization() {
        return (double) bitSet.cardinality() / config.getBitSetSize();
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

    public int getSetBitsCount() {
        return bitSet.cardinality();
    }

    public double getCurrentFalsePositiveProbability() {
        return BloomFilterMath.calculateFalsePositiveProbability(config.getNumberOfHashFunctions(), elementCount,
                config.getBitSetSize());
    }
}

