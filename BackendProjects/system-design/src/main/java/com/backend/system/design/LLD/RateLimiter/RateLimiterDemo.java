package com.backend.system.design.LLD.RateLimiter;

import com.backend.system.design.LLD.RateLimiter.config.RateLimiterConfig;
import com.backend.system.design.LLD.RateLimiter.factory.RateLimiterFactory;
import com.backend.system.design.LLD.RateLimiter.model.RateLimiterType;
import com.backend.system.design.LLD.RateLimiter.util.RateLimiterMetrics;

/**
 * Improved Demo class showcasing modular design with Factory Pattern
 */
public class RateLimiterDemo {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("╔═══════════════════════════════════════════════════════╗");
        System.out.println("║   MODULAR RATE LIMITER DEMO                          ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝\n");
        
        demoFactoryPattern();
//        demoConfigBuilder();
//        demoPresets();
//        demoMetrics();
    }
    
    /**
     * Demo 1: Using Factory Pattern
     */
    private static void demoFactoryPattern() {
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("1. FACTORY PATTERN DEMO");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        
        // Create rate limiter using factory
        RateLimiter tokenBucket = RateLimiterFactory.create(
            RateLimiterType.TOKEN_BUCKET, 
            2,  // capacity
            5   // rate
        );
        
        System.out.println("✓ Created Token Bucket using Factory");
        System.out.println("  Configuration: 5 tokens, 2 tokens/sec refill\n");
        
        // Test requests
        System.out.println("Testing 7 rapid requests:");
        for (int i = 1; i <= 7; i++) {
            boolean allowed = tokenBucket.allowRequest("user1");
            System.out.printf("  Request %d: %s%n", i, 
                allowed ? "✓ ALLOWED" : "✗ BLOCKED");
        }
        System.out.println();
    }
    
    /**
     * Demo 2: Using Config Builder Pattern
     */
    private static void demoConfigBuilder() {
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("2. CONFIG BUILDER PATTERN DEMO");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        
        // Build configuration using builder pattern
        RateLimiterConfig config = new RateLimiterConfig.Builder()
            .type(RateLimiterType.SLIDING_WINDOW_COUNTER)
            .capacity(10)
            .windowSizeSeconds(60)
            .build();
        
        System.out.println("✓ Created configuration using Builder:");
        System.out.println("  " + config);
        
        // Create rate limiter from config
        RateLimiter rateLimiter = RateLimiterFactory.create(config);
        System.out.println("✓ Created Rate Limiter from config\n");
        
        // Test requests
        System.out.println("Testing 5 requests:");
        for (int i = 1; i <= 5; i++) {
            boolean allowed = rateLimiter.allowRequest("user2");
            System.out.printf("  Request %d: %s%n", i, 
                allowed ? "✓ ALLOWED" : "✗ BLOCKED");
        }
        System.out.println();
    }
    
    /**
     * Demo 3: Using Preset Configurations
     */
    private static void demoPresets() {
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("3. PRESET CONFIGURATIONS DEMO");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        
        System.out.println("Available Presets:");
        System.out.println("  • Social Media API");
        System.out.println("  • Payment Gateway");
        System.out.println("  • Public API");
        System.out.println("  • Microservice");
        System.out.println("  • Video Streaming\n");
        
        // Use preset for social media
        RateLimiter socialMedia = RateLimiterFactory.createForSocialMedia();
        System.out.println("✓ Created Social Media Rate Limiter");
        System.out.println("  (Token Bucket: 100 capacity, 50/sec refill)");
        
        // Use preset for payments
        RateLimiter payment = RateLimiterFactory.createForPayments();
        System.out.println("✓ Created Payment Gateway Rate Limiter");
        System.out.println("  (Sliding Window Log: 100 req/sec)");
        
        // Use preset for public API
        RateLimiter publicAPI = RateLimiterFactory.createForPublicAPI();
        System.out.println("✓ Created Public API Rate Limiter");
        System.out.println("  (Sliding Window Counter: 5000 req/hour)\n");
    }
    
    /**
     * Demo 4: Using Metrics
     */
    private static void demoMetrics() throws InterruptedException {
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("4. METRICS TRACKING DEMO");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        
        RateLimiter limiter = RateLimiterFactory.create(
            RateLimiterType.TOKEN_BUCKET, 3, 1
        );
        
        RateLimiterMetrics metrics = new RateLimiterMetrics();
        
        System.out.println("Sending 10 requests with metrics tracking:");
        for (int i = 1; i <= 10; i++) {
            boolean allowed = limiter.allowRequest("user3");
            
            if (allowed) {
                metrics.recordAllowed();
            } else {
                metrics.recordBlocked();
            }
            
            System.out.printf("  Request %2d: %s%n", i, 
                allowed ? "✓ ALLOWED" : "✗ BLOCKED");
        }
        
        System.out.println("\n📊 Metrics Summary:");
        System.out.println("  " + metrics);
        System.out.println();
        
        // Wait and try again
        System.out.println("Waiting 2 seconds for token refill...");
        Thread.sleep(2000);
        
        System.out.println("Sending 3 more requests:");
        for (int i = 1; i <= 3; i++) {
            boolean allowed = limiter.allowRequest("user3");
            if (allowed) {
                metrics.recordAllowed();
            } else {
                metrics.recordBlocked();
            }
            System.out.printf("  Request %2d: %s%n", i, 
                allowed ? "✓ ALLOWED" : "✗ BLOCKED");
        }
        
        System.out.println("\n📊 Final Metrics:");
        System.out.println("  " + metrics);
        System.out.println();
    }
}
