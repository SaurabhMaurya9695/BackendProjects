package com.backend.system.design.LLD.RateLimiter.algorithms;

import com.backend.system.design.LLD.RateLimiter.RateLimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Token Bucket Rate Limiter
 * <p>
 * HOW IT WORKS:
 * - Each user gets a bucket that holds tokens.
 * - Every request needs one token.
 * - Tokens are refilled over time.
 * - If the bucket has tokens, → request is allowed.
 * - If the bucket is empty, → request is rejected.
 * <p>
 * This helps control how many requests a user can send per second.
 */
public class TokenBucketRateLimiter implements RateLimiter {

    private final int _maxTokens;            // Maximum tokens allowed in the bucket
    private final int _refillTokensPerSec;   // How many tokens to add per second
    private final Map<String, Bucket> _buckets = new ConcurrentHashMap<>();

    /**
     * Represents the bucket for each user
     */
    private static class Bucket {

        int _tokens;
        long _lastRefillTime;

        Bucket(int tokens, long lastRefillTime) {
            this._tokens = tokens;
            this._lastRefillTime = lastRefillTime;
        }
    }

    /**
     * Creates a rate limiter.
     *
     * @param maxTokens          Maximum tokens the bucket can store
     * @param refillTokensPerSec How many tokens to add every second
     */
    public TokenBucketRateLimiter(int maxTokens, int refillTokensPerSec) {
        this._maxTokens = maxTokens;
        this._refillTokensPerSec = refillTokensPerSec;
    }

    /**
     * Checks whether a request from a user is allowed.
     *
     * @param userId Unique ID for the user (or IP)
     * @return true if request is allowed, false otherwise
     */
    @Override
    public synchronized boolean allowRequest(String userId) {
        long now = System.currentTimeMillis();

        // Get bucket for this user. If not exists, create full bucket.
        Bucket bucket = _buckets.computeIfAbsent(userId, id -> new Bucket(_maxTokens, now));

        // Refill tokens first
        refill(bucket, now);

        // If tokens available → allow request
        if (bucket._tokens > 0) {
            bucket._tokens--;
            return true;
        }

        return false; // No tokens → reject request
    }

    /**
     * Adds tokens to the bucket based on how much time has passed.
     */
    private void refill(Bucket bucket, long now) {
        long elapsed = now - bucket._lastRefillTime;

        // Tokens to add based on seconds passed
        int tokensToAdd = (int) ((elapsed / 1000.0) * _refillTokensPerSec);

        if (tokensToAdd > 0) {
            bucket._tokens = Math.min(_maxTokens, bucket._tokens + tokensToAdd);
            bucket._lastRefillTime = now;
        }
    }

    /**
     * Clears the bucket for a user.
     * Useful for testing or resetting limits.
     */
    @Override
    public void reset(String userId) {
        _buckets.remove(userId);
    }
}
