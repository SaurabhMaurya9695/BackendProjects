package com.backend.system.design.LLD.BloomFilters.config;

/**
 * Mathematical utilities for Bloom Filter calculations
 */
public class BloomFilterMath {
    
    /**
     * Calculates current false positive probability
     * Formula: (1 - e^(-k×n/m))^k
     * 
     * @param numHashFunctions Number of hash functions (k)
     * @param numElements Number of elements inserted (n)
     * @param bitSetSize Size of bit array (m)
     * @return False positive probability
     */
    public static double calculateFalsePositiveProbability(
            int numHashFunctions, int numElements, int bitSetSize) {
        
        if (numElements == 0) {
            return 0.0;
        }
        
        double exponent = -((double) numHashFunctions * numElements) / bitSetSize;
        return Math.pow(1 - Math.exp(exponent), numHashFunctions);
    }
    
    /**
     * Calculates bits per element ratio
     */
    public static double calculateBitsPerElement(int bitSetSize, int numElements) {
        if (numElements == 0) {
            return bitSetSize;
        }
        return (double) bitSetSize / numElements;
    }
    
    /**
     * Estimates memory usage in bytes
     */
    public static long estimateMemoryBytes(int bitSetSize) {
        return (long) Math.ceil(bitSetSize / 8.0);
    }
    
    /**
     * Estimates memory usage in kilobytes
     */
    public static double estimateMemoryKB(int bitSetSize) {
        return estimateMemoryBytes(bitSetSize) / 1024.0;
    }
}

