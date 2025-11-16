package com.backend.system.design.LLD.RateLimiter.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Metrics collector for rate limiter
 * Tracks allowed/blocked requests for monitoring
 */
public class RateLimiterMetrics {
    
    private final AtomicLong totalRequests;
    private final AtomicLong allowedRequests;
    private final AtomicLong blockedRequests;
    private final long startTime;
    
    public RateLimiterMetrics() {
        this.totalRequests = new AtomicLong(0);
        this.allowedRequests = new AtomicLong(0);
        this.blockedRequests = new AtomicLong(0);
        this.startTime = System.currentTimeMillis();
    }
    
    public void recordAllowed() {
        totalRequests.incrementAndGet();
        allowedRequests.incrementAndGet();
    }
    
    public void recordBlocked() {
        totalRequests.incrementAndGet();
        blockedRequests.incrementAndGet();
    }
    
    public long getTotalRequests() {
        return totalRequests.get();
    }
    
    public long getAllowedRequests() {
        return allowedRequests.get();
    }
    
    public long getBlockedRequests() {
        return blockedRequests.get();
    }
    
    public double getBlockedPercentage() {
        long total = totalRequests.get();
        if (total == 0) return 0.0;
        return (blockedRequests.get() * 100.0) / total;
    }
    
    public long getUptimeSeconds() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }
    
    public void reset() {
        totalRequests.set(0);
        allowedRequests.set(0);
        blockedRequests.set(0);
    }
    
    @Override
    public String toString() {
        return String.format(
            "Metrics{total=%d, allowed=%d, blocked=%d, blocked%%=%.2f%%, uptime=%ds}",
            getTotalRequests(), 
            getAllowedRequests(), 
            getBlockedRequests(), 
            getBlockedPercentage(),
            getUptimeSeconds()
        );
    }
}

