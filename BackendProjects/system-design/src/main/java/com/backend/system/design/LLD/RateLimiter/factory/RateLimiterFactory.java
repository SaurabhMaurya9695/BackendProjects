package com.backend.system.design.LLD.RateLimiter.factory;

import com.backend.system.design.LLD.RateLimiter.RateLimiter;
import com.backend.system.design.LLD.RateLimiter.algorithms.*;
import com.backend.system.design.LLD.RateLimiter.config.RateLimiterConfig;
import com.backend.system.design.LLD.RateLimiter.model.RateLimiterType;

/**
 * Factory class to create Rate Limiter instances
 * Follows Factory Design Pattern
 */
public class RateLimiterFactory {
    
    /**
     * Creates a rate limiter based on configuration
     */
    public static RateLimiter create(RateLimiterConfig config) {
        switch (config.getType()) {
            case TOKEN_BUCKET:
                return new TokenBucketRateLimiter(
                    config.getCapacity(), 
                    config.getRate()
                );
                
            case LEAKY_BUCKET:
                return new LeakyBucketRateLimiter(
                    config.getCapacity(), 
                    config.getRate()
                );
                
            case FIXED_WINDOW_COUNTER:
                return new FixedWindowCounterRateLimiter(
                    config.getCapacity(), 
                    config.getWindowSizeSeconds()
                );
                
            case SLIDING_WINDOW_LOG:
                return new SlidingWindowLogRateLimiter(
                    config.getCapacity(), 
                    config.getWindowSizeSeconds()
                );
                
            case SLIDING_WINDOW_COUNTER:
                return new SlidingWindowCounterRateLimiter(
                    config.getCapacity(), 
                    config.getWindowSizeSeconds()
                );
                
            default:
                throw new IllegalArgumentException("Unknown rate limiter type: " + config.getType());
        }
    }
    
    /**
     * Creates a rate limiter with simple parameters
     */
    public static RateLimiter create(RateLimiterType type, int capacity, int rate) {
        RateLimiterConfig config = new RateLimiterConfig.Builder()
            .type(type)
            .capacity(capacity)
            .rate(rate)
            .build();
        
        return create(config);
    }
    
    /**
     * Creates a default token bucket rate limiter
     */
    public static RateLimiter createDefault() {
        return create(new RateLimiterConfig.Builder().build());
    }
    
    /**
     * Creates a rate limiter for social media use case
     */
    public static RateLimiter createForSocialMedia() {
        return create(RateLimiterConfig.Presets.socialMediaAPI());
    }
    
    /**
     * Creates a rate limiter for payment gateway
     */
    public static RateLimiter createForPayments() {
        return create(RateLimiterConfig.Presets.paymentGateway());
    }
    
    /**
     * Creates a rate limiter for public API
     */
    public static RateLimiter createForPublicAPI() {
        return create(RateLimiterConfig.Presets.publicAPI());
    }
    
    /**
     * Creates a rate limiter for microservices
     */
    public static RateLimiter createForMicroservice() {
        return create(RateLimiterConfig.Presets.microservice());
    }
    
    /**
     * Creates a rate limiter for video streaming
     */
    public static RateLimiter createForStreaming() {
        return create(RateLimiterConfig.Presets.videoStreaming());
    }
}

