package com.backend.system.design.LLD.RateLimiter.config;

import com.backend.system.design.LLD.RateLimiter.model.RateLimiterType;

/**
 * Configuration class for Rate Limiter
 * Holds all configuration parameters
 */
public class RateLimiterConfig {
    
    private final RateLimiterType type;
    private final int capacity;        // maxTokens/bucketSize/maxRequests
    private final int rate;            // refillRate/leakRate
    private final int windowSizeSeconds;
    
    private RateLimiterConfig(Builder builder) {
        this.type = builder.type;
        this.capacity = builder.capacity;
        this.rate = builder.rate;
        this.windowSizeSeconds = builder.windowSizeSeconds;
    }
    
    public RateLimiterType getType() {
        return type;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public int getRate() {
        return rate;
    }
    
    public int getWindowSizeSeconds() {
        return windowSizeSeconds;
    }
    
    /**
     * Builder pattern for fluent configuration
     */
    public static class Builder {
        private RateLimiterType type = RateLimiterType.TOKEN_BUCKET;
        private int capacity = 100;
        private int rate = 10;
        private int windowSizeSeconds = 60;
        
        public Builder type(RateLimiterType type) {
            this.type = type;
            return this;
        }
        
        public Builder capacity(int capacity) {
            this.capacity = capacity;
            return this;
        }
        
        public Builder rate(int rate) {
            this.rate = rate;
            return this;
        }
        
        public Builder windowSizeSeconds(int windowSizeSeconds) {
            this.windowSizeSeconds = windowSizeSeconds;
            return this;
        }
        
        public RateLimiterConfig build() {
            validate();
            return new RateLimiterConfig(this);
        }
        
        private void validate() {
            if (capacity <= 0) {
                throw new IllegalArgumentException("Capacity must be positive");
            }
            if (rate <= 0) {
                throw new IllegalArgumentException("Rate must be positive");
            }
            if (windowSizeSeconds <= 0) {
                throw new IllegalArgumentException("Window size must be positive");
            }
        }
    }
    
    /**
     * Predefined configurations for common use cases
     */
    public static class Presets {
        
        public static RateLimiterConfig socialMediaAPI() {
            return new Builder()
                .type(RateLimiterType.TOKEN_BUCKET)
                .capacity(100)
                .rate(50)
                .build();
        }
        
        public static RateLimiterConfig paymentGateway() {
            return new Builder()
                .type(RateLimiterType.SLIDING_WINDOW_LOG)
                .capacity(100)
                .windowSizeSeconds(1)
                .build();
        }
        
        public static RateLimiterConfig publicAPI() {
            return new Builder()
                .type(RateLimiterType.SLIDING_WINDOW_COUNTER)
                .capacity(5000)
                .windowSizeSeconds(3600)
                .build();
        }
        
        public static RateLimiterConfig microservice() {
            return new Builder()
                .type(RateLimiterType.FIXED_WINDOW_COUNTER)
                .capacity(10000)
                .windowSizeSeconds(60)
                .build();
        }
        
        public static RateLimiterConfig videoStreaming() {
            return new Builder()
                .type(RateLimiterType.LEAKY_BUCKET)
                .capacity(1000)
                .rate(100)
                .build();
        }
    }
    
    @Override
    public String toString() {
        return String.format("RateLimiterConfig{type=%s, capacity=%d, rate=%d, windowSize=%ds}", 
            type, capacity, rate, windowSizeSeconds);
    }
}

