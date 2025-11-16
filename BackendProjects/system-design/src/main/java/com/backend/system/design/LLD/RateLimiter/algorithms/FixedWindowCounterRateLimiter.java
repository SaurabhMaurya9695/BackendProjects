package com.backend.system.design.LLD.RateLimiter.algorithms;

import com.backend.system.design.LLD.RateLimiter.RateLimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Fixed Window Counter-Rate Limiter Implementation
 * <p>
 * CONCEPT:
 * - Time is divided into fixed windows (e.g., 1 minute)
 * - Each window has a counter starting at 0
 * - Each request increments the counter
 * - If counter exceeds the limit, request is denied
 * - Counter resets at the start of each new window
 * <p>
 * ADVANTAGES:
 * - Very simple to implement
 * - Memory efficient (just a counter per user)
 * - Easy to understand
 * <p>
 * DISADVANTAGES:
 * - Burst traffic at window boundaries (can get 2x limit at edges)
 * - Unfair to users who request at window boundaries
 * <p>
 * USE CASES:
 * - Simple API rate limiting
 * - Basic protection against spammed
 * - Quick implementation when accuracy isn't critical
 * <p>
 * EXAMPLE PROBLEM:
 * If limit is 10 requests/minute:
 * - 10 requests at 0:59
 * - 10 requests at 1:00
 * - Total: 20 requests in 1 second!
 */
public class FixedWindowCounterRateLimiter implements RateLimiter {

    private final int maxRequests;     // Maximum requests per window
    private final long windowSizeMs;   // Window size in milliseconds
    private final Map<String, Window> userWindows;

    private static class Window {

        int count;
        long windowStart;

        Window(int count, long windowStart) {
            this.count = count;
            this.windowStart = windowStart;
        }
    }

    /**
     * @param maxRequests       Maximum number of requests allowed per window
     * @param windowSizeSeconds Size of the time window in seconds
     */
    public FixedWindowCounterRateLimiter(int maxRequests, int windowSizeSeconds) {
        this.maxRequests = maxRequests;
        this.windowSizeMs = windowSizeSeconds * 1000L;
        this.userWindows = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized boolean allowRequest(String userId) {
        long currentTime = System.currentTimeMillis();
        long currentWindowStart = (currentTime / windowSizeMs) * windowSizeMs;

        Window window = userWindows.computeIfAbsent(userId, k -> new Window(0, currentWindowStart));

        // Check if we're in a new window
        if (window.windowStart != currentWindowStart) {
            window.count = 0;
            window.windowStart = currentWindowStart;
        }

        // Check if we're under the limit
        if (window.count < maxRequests) {
            window.count++;
            return true;
        }

        return false;
    }

    @Override
    public void reset(String userId) {
        userWindows.remove(userId);
    }
}

