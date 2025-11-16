package com.backend.system.design.LLD.RateLimiter.algorithms;

import com.backend.system.design.LLD.RateLimiter.RateLimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Sliding Window Counter Rate Limiter Implementation
 * 
 * CONCEPT:
 * - Hybrid of Fixed Window and Sliding Window Log
 * - Uses two windows: current and previous
 * - Calculates weighted count based on overlap with previous window
 * - Formula: currentWindowCount + (previousWindowCount * overlapPercentage)
 * 
 * ADVANTAGES:
 * - More accurate than Fixed Window
 * - Less memory than Sliding Window Log
 * - Good balance between accuracy and efficiency
 * 
 * DISADVANTAGES:
 * - Still has some approximation
 * - More complex than Fixed Window
 * - Requires tracking two windows
 * 
 * USE CASES:
 * - Production APIs (used by Cloudflare, AWS)
 * - High-traffic applications
 * - When you need better accuracy than Fixed Window but less memory than Log
 * 
 * EXAMPLE:
 * Window size: 60 seconds, Limit: 10 requests
 * Current time: 15 seconds into current window
 * Previous window had 8 requests, Current window has 3 requests
 * Overlap = 75% (45 seconds of previous window overlap)
 * Weighted count = 3 + (8 * 0.75) = 9 requests
 */
public class SlidingWindowCounterRateLimiter implements RateLimiter {
    
    private final int maxRequests;
    private final long windowSizeMs;
    private final Map<String, WindowData> userWindows;
    
    private static class WindowData {
        int currentWindowCount;
        int previousWindowCount;
        long currentWindowStart;
        
        WindowData(int currentWindowCount, int previousWindowCount, long currentWindowStart) {
            this.currentWindowCount = currentWindowCount;
            this.previousWindowCount = previousWindowCount;
            this.currentWindowStart = currentWindowStart;
        }
    }
    
    /**
     * @param maxRequests Maximum number of requests allowed per window
     * @param windowSizeSeconds Size of the time window in seconds
     */
    public SlidingWindowCounterRateLimiter(int maxRequests, int windowSizeSeconds) {
        this.maxRequests = maxRequests;
        this.windowSizeMs = windowSizeSeconds * 1000L;
        this.userWindows = new ConcurrentHashMap<>();
    }
    
    @Override
    public synchronized boolean allowRequest(String userId) {
        long currentTime = System.currentTimeMillis();
        long currentWindowStart = (currentTime / windowSizeMs) * windowSizeMs;
        
        WindowData windowData = userWindows.computeIfAbsent(userId, 
            k -> new WindowData(0, 0, currentWindowStart));
        
        // Check if we've moved to a new window
        if (windowData.currentWindowStart != currentWindowStart) {
            // Move current window data to previous
            windowData.previousWindowCount = windowData.currentWindowCount;
            windowData.currentWindowCount = 0;
            windowData.currentWindowStart = currentWindowStart;
        }
        
        // Calculate weighted count
        long timeIntoCurrentWindow = currentTime - currentWindowStart;
        double overlapPercentage = (windowSizeMs - timeIntoCurrentWindow) / (double) windowSizeMs;
        double weightedCount = windowData.currentWindowCount + 
                               (windowData.previousWindowCount * overlapPercentage);
        
        // Check if we're under the limit
        if (weightedCount < maxRequests) {
            windowData.currentWindowCount++;
            return true;
        }
        
        return false;
    }
    
    @Override
    public void reset(String userId) {
        userWindows.remove(userId);
    }
}

