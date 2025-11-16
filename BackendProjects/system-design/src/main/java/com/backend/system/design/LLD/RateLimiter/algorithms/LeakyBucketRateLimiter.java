package com.backend.system.design.LLD.RateLimiter.algorithms;

import com.backend.system.design.LLD.RateLimiter.RateLimiter;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Leaky Bucket Rate Limiter Implementation
 * 
 * CONCEPT:
 * - Imagine a bucket with a hole at the bottom
 * - Requests are added to the bucket (queue)
 * - Requests leak out at a constant rate
 * - If bucket overflows, requests are rejected
 * 
 * ADVANTAGES:
 * - Guarantees a constant output rate
 * - Smooths out bursts
 * - Simple to implement
 * 
 * DISADVANTAGES:
 * - Can waste capacity if traffic is low
 * - Requires queue management
 * - Potential memory issues with large queues
 * 
 * USE CASES:
 * - Network packet scheduling
 * - Video streaming rate control
 * - Preventing system overload
 */
public class LeakyBucketRateLimiter implements RateLimiter {
    
    private final int bucketCapacity;  // Maximum queue size
    private final int leakRate;        // Requests processed per second
    private final Map<String, Bucket> userBuckets;
    
    private static class Bucket {
        Queue<Long> requestQueue;
        long lastLeakTimestamp;
        
        Bucket(long timestamp) {
            this.requestQueue = new LinkedList<>();
            this.lastLeakTimestamp = timestamp;
        }
    }
    
    /**
     * @param bucketCapacity Maximum number of requests in queue
     * @param leakRate Number of requests processed per second
     */
    public LeakyBucketRateLimiter(int bucketCapacity, int leakRate) {
        this.bucketCapacity = bucketCapacity;
        this.leakRate = leakRate;
        this.userBuckets = new ConcurrentHashMap<>();
    }
    
    @Override
    public synchronized boolean allowRequest(String userId) {
        long currentTime = System.currentTimeMillis();
        
        Bucket bucket = userBuckets.computeIfAbsent(userId, 
            k -> new Bucket(currentTime));
        
        // Leak requests from the bucket
        leakRequests(bucket, currentTime);
        
        // Check if we can add the request to the bucket
        if (bucket.requestQueue.size() < bucketCapacity) {
            bucket.requestQueue.offer(currentTime);
            return true;
        }
        
        return false;
    }
    
    private void leakRequests(Bucket bucket, long currentTime) {
        long timeElapsed = currentTime - bucket.lastLeakTimestamp;
        int requestsToLeak = (int) ((timeElapsed / 1000.0) * leakRate);
        
        for (int i = 0; i < requestsToLeak && !bucket.requestQueue.isEmpty(); i++) {
            bucket.requestQueue.poll();
        }
        
        if (requestsToLeak > 0) {
            bucket.lastLeakTimestamp = currentTime;
        }
    }
    
    @Override
    public void reset(String userId) {
        userBuckets.remove(userId);
    }
}

