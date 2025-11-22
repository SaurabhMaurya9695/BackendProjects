package com.backend.system.design.LLD.BloomFilters.config;

/**
 * Statistics and metrics for a Bloom Filter
 */
public class BloomFilterStats {
    
    private final int bitSetSize;
    private final int numberOfHashFunctions;
    private final int elementCount;
    private final int setBitsCount;
    private final double bitUtilization;
    private final double falsePositiveProbability;
    
    public BloomFilterStats(int bitSetSize, int numberOfHashFunctions, int elementCount,
                           int setBitsCount, double bitUtilization, double falsePositiveProbability) {
        this.bitSetSize = bitSetSize;
        this.numberOfHashFunctions = numberOfHashFunctions;
        this.elementCount = elementCount;
        this.setBitsCount = setBitsCount;
        this.bitUtilization = bitUtilization;
        this.falsePositiveProbability = falsePositiveProbability;
    }
    
    public int getBitSetSize() {
        return bitSetSize;
    }
    
    public int getNumberOfHashFunctions() {
        return numberOfHashFunctions;
    }
    
    public int getElementCount() {
        return elementCount;
    }
    
    public int getSetBitsCount() {
        return setBitsCount;
    }
    
    public double getBitUtilization() {
        return bitUtilization;
    }
    
    public double getFalsePositiveProbability() {
        return falsePositiveProbability;
    }
    
    @Override
    public String toString() {
        return String.format(
            "BloomFilter Statistics:\n" +
            "  Bit Set Size: %d bits\n" +
            "  Number of Hash Functions: %d\n" +
            "  Elements Added: %d\n" +
            "  Set Bits: %d\n" +
            "  Bit Utilization: %.2f%%\n" +
            "  False Positive Probability: %.6f (%.4f%%)",
            bitSetSize, numberOfHashFunctions, elementCount, setBitsCount,
            bitUtilization * 100, falsePositiveProbability, falsePositiveProbability * 100
        );
    }
}

