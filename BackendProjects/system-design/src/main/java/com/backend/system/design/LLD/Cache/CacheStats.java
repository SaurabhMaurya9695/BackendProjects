package com.backend.system.design.LLD.Cache;

/**
 * Immutable class representing cache statistics.
 * Provides insights into cache performance.
 */
public class CacheStats {
    
    private final long hits;
    private final long misses;
    private final long evictions;
    private final int currentSize;
    private final int capacity;
    
    public CacheStats(long hits, long misses, long evictions, int currentSize, int capacity) {
        this.hits = hits;
        this.misses = misses;
        this.evictions = evictions;
        this.currentSize = currentSize;
        this.capacity = capacity;
    }
    
    public long getHits() {
        return hits;
    }
    
    public long getMisses() {
        return misses;
    }
    
    public long getEvictions() {
        return evictions;
    }
    
    public long getTotalRequests() {
        return hits + misses;
    }
    
    public double getHitRate() {
        long total = getTotalRequests();
        return total == 0 ? 0.0 : (double) hits / total;
    }
    
    public double getMissRate() {
        return 1.0 - getHitRate();
    }
    
    public int getCurrentSize() {
        return currentSize;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public double getUtilization() {
        return capacity == 0 ? 0.0 : (double) currentSize / capacity;
    }
    
    @Override
    public String toString() {
        return String.format(
            "CacheStats{hits=%d, misses=%d, evictions=%d, hitRate=%.2f%%, " +
            "size=%d, capacity=%d, utilization=%.2f%%}",
            hits, misses, evictions, getHitRate() * 100,
            currentSize, capacity, getUtilization() * 100
        );
    }
}

