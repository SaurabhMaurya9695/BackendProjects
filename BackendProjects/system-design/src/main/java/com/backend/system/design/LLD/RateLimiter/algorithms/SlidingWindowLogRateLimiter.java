package com.backend.system.design.LLD.RateLimiter.algorithms;

import com.backend.system.design.LLD.RateLimiter.RateLimiter;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Sliding Window Log Rate Limiter Implementation
 * <p>
 * CONCEPT:
 * - Keep a log (queue) of all request timestamps
 * - For each new request, remove timestamps older than the window
 * - Count remaining timestamps
 * - Allow request if count < limit
 * <p>
 * ADVANTAGES:
 * - Very accurate - no boundary issues
 * - Fair to all users regardless of when they make requests
 * - No burst traffic problems
 * <p>
 * DISADVANTAGES:
 * - High memory usage (stores all timestamps)
 * - More complex than fixed window
 * - Can be slow with high traffic
 * <p>
 * USE CASES:
 * - Critical APIs where accuracy is paramount
 * - Financial applications
 * - When you need precise rate limiting
 * <p>
 * MEMORY CONSIDERATION:
 * If limit is 10000 requests/hour per user, you store 10000 timestamps per user
 */
public class SlidingWindowLogRateLimiter implements RateLimiter {

    private final int maxRequests;     // Maximum requests per window
    private final long windowSizeMs;   // Window size in milliseconds
    private final Map<String, Queue<Long>> userRequestLogs;

    /**
     * @param maxRequests       Maximum number of requests allowed per window
     * @param windowSizeSeconds Size of the time window in seconds
     */
    public SlidingWindowLogRateLimiter(int maxRequests, int windowSizeSeconds) {
        this.maxRequests = maxRequests;
        this.windowSizeMs = windowSizeSeconds * 1000L;
        this.userRequestLogs = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized boolean allowRequest(String userId) {
        long currentTime = System.currentTimeMillis();
        long windowStart = currentTime - windowSizeMs;

        Queue<Long> requestLog = userRequestLogs.computeIfAbsent(userId, k -> new LinkedList<>());

        // Remove timestamps outside the sliding window
        while (!requestLog.isEmpty() && requestLog.peek() <= windowStart) {
            requestLog.poll();
        }

        // Check if we're under the limit
        if (requestLog.size() < maxRequests) {
            requestLog.offer(currentTime);
            return true;
        }

        return false;
    }

    @Override
    public void reset(String userId) {
        userRequestLogs.remove(userId);
    }
}

