package com.backend.system.design.LLD.BloomFilters;

import com.backend.system.design.LLD.BloomFilters.config.BloomFilterStats;

import java.util.ArrayList;
import java.util.List;

/**
 * Scalable Bloom Filter - Automatically grows when capacity is reached
 * <p>
 * Maintains multiple internal Bloom Filters. Creates new filters
 * when false positive rate exceeds target. Each filter grows exponentially.
 *
 * @param <T> the type of elements to store
 */
public class ScalableBloomFilter<T> {
    
    private static final double DEFAULT_GROWTH_FACTOR = 2.0;
    private static final double FP_DECAY_RATE = 0.8;
    
    private final List<BloomFilter<T>> filters;
    private final double targetFalsePositiveProbability;
    private final int initialCapacity;
    private final double growthFactor;
    private int totalElementCount;
    
    /**
     * Creates a Scalable Bloom Filter
     */
    public ScalableBloomFilter(int initialCapacity, double targetFalsePositiveProbability, double growthFactor) {
        this.filters = new ArrayList<>();
        this.targetFalsePositiveProbability = targetFalsePositiveProbability;
        this.initialCapacity = initialCapacity;
        this.growthFactor = growthFactor;
        this.totalElementCount = 0;
        
        addNewFilter();
    }
    
    /**
     * Creates a Scalable Bloom Filter with default growth factor (2.0)
     */
    public ScalableBloomFilter(int initialCapacity, double targetFalsePositiveProbability) {
        this(initialCapacity, targetFalsePositiveProbability, DEFAULT_GROWTH_FACTOR);
    }
    
    /**
     * Adds an element (creates new filter if needed)
     */
    public synchronized void add(T element) {
        validateElement(element);
        
        BloomFilter<T> currentFilter = getCurrentFilter();
        
        if (shouldCreateNewFilter(currentFilter)) {
            addNewFilter();
            currentFilter = getCurrentFilter();
        }
        
        currentFilter.add(element);
        totalElementCount++;
    }
    
    /**
     * Checks if element might be present (checks all filters)
     */
    public boolean mightContain(T element) {
        if (element == null) {
            return false;
        }
        
        for (BloomFilter<T> filter : filters) {
            if (filter.mightContain(element)) {
                return true;
            }
        }
        return false;
    }
    
    // Helper methods
    
    private BloomFilter<T> getCurrentFilter() {
        return filters.get(filters.size() - 1);
    }
    
    private boolean shouldCreateNewFilter(BloomFilter<T> filter) {
        return filter.getCurrentFalsePositiveProbability() > targetFalsePositiveProbability;
    }
    
    private void addNewFilter() {
        int filterIndex = filters.size();
        int capacity = (int) (initialCapacity * Math.pow(growthFactor, filterIndex));
        double filterFP = targetFalsePositiveProbability * Math.pow(FP_DECAY_RATE, filterIndex);
        
        filters.add(new BloomFilter<>(capacity, filterFP));
    }
    
    private void validateElement(T element) {
        if (element == null) {
            throw new IllegalArgumentException("Element cannot be null");
        }
    }
    
    /**
     * Clears all filters and resets
     */
    public void clear() {
        filters.clear();
        totalElementCount = 0;
        addNewFilter();
    }
    
    // Getters and Statistics
    
    public int getFilterCount() {
        return filters.size();
    }
    
    public int getTotalElementCount() {
        return totalElementCount;
    }
    
    public int getTotalBitSize() {
        return filters.stream().mapToInt(BloomFilter::getBitSetSize).sum();
    }
    
    public String getStatistics() {
        StringBuilder sb = new StringBuilder();
        sb.append("Scalable Bloom Filter Statistics:\n");
        sb.append(String.format("  Total Filters: %d\n", getFilterCount()));
        sb.append(String.format("  Total Elements: %d\n", totalElementCount));
        sb.append(String.format("  Total Bit Size: %d bits\n\n", getTotalBitSize()));
        
        for (int i = 0; i < filters.size(); i++) {
            appendFilterStats(sb, i);
        }
        
        return sb.toString();
    }
    
    private void appendFilterStats(StringBuilder sb, int index) {
        BloomFilterStats stats = filters.get(index).getStats();
        sb.append(String.format("Filter %d:\n", index + 1));
        sb.append(String.format("  Elements: %d\n", stats.getElementCount()));
        sb.append(String.format("  Bit Size: %d\n", stats.getBitSetSize()));
        sb.append(String.format("  Utilization: %.2f%%\n", stats.getBitUtilization() * 100));
        sb.append(String.format("  False Positive Prob: %.6f\n\n", stats.getFalsePositiveProbability()));
    }
}

