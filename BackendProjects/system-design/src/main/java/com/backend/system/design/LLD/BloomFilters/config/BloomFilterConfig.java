package com.backend.system.design.LLD.BloomFilters.config;

/**
 * Configuration class for Bloom Filter parameters
 * Encapsulates all configuration logic
 */
public class BloomFilterConfig {
    
    private final int bitSetSize;
    private final int numberOfHashFunctions;
    
    private BloomFilterConfig(int bitSetSize, int numberOfHashFunctions) {
        this.bitSetSize = bitSetSize;
        this.numberOfHashFunctions = numberOfHashFunctions;
    }
    
    /**
     * Creates optimal configuration based on expected elements and false positive rate
     */
    public static BloomFilterConfig optimal(int expectedElements, double falsePositiveProbability) {
        validateInputs(expectedElements, falsePositiveProbability);
        
        int bitSetSize = calculateOptimalBitSetSize(expectedElements, falsePositiveProbability);
        int hashFunctions = calculateOptimalHashFunctions(bitSetSize, expectedElements);
        
        return new BloomFilterConfig(bitSetSize, hashFunctions);
    }
    
    /**
     * Creates custom configuration
     */
    public static BloomFilterConfig custom(int bitSetSize, int numberOfHashFunctions) {
        if (bitSetSize <= 0 || numberOfHashFunctions <= 0) {
            throw new IllegalArgumentException("Bit set size and hash functions must be positive");
        }
        return new BloomFilterConfig(bitSetSize, numberOfHashFunctions);
    }
    
    /**
     * Calculates optimal bit set size
     * Formula: m = -(n × ln(p)) / (ln(2))²
     */
    private static int calculateOptimalBitSetSize(int expectedElements, double falsePositiveProbability) {
        double size = -(expectedElements * Math.log(falsePositiveProbability)) 
                      / (Math.log(2) * Math.log(2));
        return (int) Math.ceil(size);
    }
    
    /**
     * Calculates optimal number of hash functions
     * Formula: k = (m/n) × ln(2)
     */
    private static int calculateOptimalHashFunctions(int bitSetSize, int expectedElements) {
        int k = (int) Math.round((bitSetSize / (double) expectedElements) * Math.log(2));
        return Math.max(1, k);
    }
    
    private static void validateInputs(int expectedElements, double falsePositiveProbability) {
        if (expectedElements <= 0) {
            throw new IllegalArgumentException("Expected elements must be positive");
        }
        if (falsePositiveProbability <= 0 || falsePositiveProbability >= 1) {
            throw new IllegalArgumentException("False positive probability must be between 0 and 1");
        }
    }
    
    public int getBitSetSize() {
        return bitSetSize;
    }
    
    public int getNumberOfHashFunctions() {
        return numberOfHashFunctions;
    }
}

