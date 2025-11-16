package com.backend.system.design.LLD.RateLimiter.util;

/**
 * Time provider interface for better testability
 * Allows mocking time in unit tests
 */
public interface TimeProvider {
    
    /**
     * Returns current time in milliseconds
     */
    long currentTimeMillis();
    
    /**
     * System time provider (production)
     */
    class SystemTimeProvider implements TimeProvider {
        @Override
        public long currentTimeMillis() {
            return System.currentTimeMillis();
        }
    }
    
    /**
     * Fixed time provider (testing)
     */
    class FixedTimeProvider implements TimeProvider {
        private long fixedTime;
        
        public FixedTimeProvider(long fixedTime) {
            this.fixedTime = fixedTime;
        }
        
        public void setTime(long time) {
            this.fixedTime = time;
        }
        
        public void advance(long millis) {
            this.fixedTime += millis;
        }
        
        @Override
        public long currentTimeMillis() {
            return fixedTime;
        }
    }
}

