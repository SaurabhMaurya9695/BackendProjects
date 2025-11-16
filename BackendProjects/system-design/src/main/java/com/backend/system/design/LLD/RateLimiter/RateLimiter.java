package com.backend.system.design.LLD.RateLimiter;

/**
 * Interface for all Rate Limiter implementations
 * Defines the contract that all rate limiting algorithms must follow
 */
public interface RateLimiter {

    /**
     * Attempts to allow a request through the rate limiter
     *
     * @param userId The unique identifier for the user making the request
     * @return true if the request is allowed, false if it should be rate limited
     */
    boolean allowRequest(String userId);

    /**
     * Resets the rate limiter state for a specific user
     *
     * @param userId The unique identifier for the user
     */
    void reset(String userId);
}

