package com.backend.system.design.LLD.RateLimiter.model;

/**
 * Result object for rate limit checks
 * Provides more information than just boolean
 */
public class RateLimitResult {

    private final boolean allowed;
    private final long remainingRequests;
    private final long resetTimeMillis;
    private final String message;

    private RateLimitResult(boolean allowed, long remainingRequests, long resetTimeMillis, String message) {
        this.allowed = allowed;
        this.remainingRequests = remainingRequests;
        this.resetTimeMillis = resetTimeMillis;
        this.message = message;
    }

    public static RateLimitResult allowed(long remainingRequests, long resetTimeMillis) {
        return new RateLimitResult(true, remainingRequests, resetTimeMillis, "Request allowed");
    }

    public static RateLimitResult denied(long resetTimeMillis) {
        return new RateLimitResult(false, 0, resetTimeMillis, "Rate limit exceeded");
    }

    public boolean isAllowed() {
        return allowed;
    }

    public long getRemainingRequests() {
        return remainingRequests;
    }

    public long getResetTimeMillis() {
        return resetTimeMillis;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("RateLimitResult{allowed=%s, remaining=%d, resetTime=%d, message='%s'}", allowed,
                remainingRequests, resetTimeMillis, message);
    }
}

