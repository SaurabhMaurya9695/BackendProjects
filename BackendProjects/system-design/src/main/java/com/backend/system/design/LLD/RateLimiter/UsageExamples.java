package com.backend.system.design.LLD.RateLimiter;

import com.backend.system.design.LLD.RateLimiter.config.RateLimiterConfig;
import com.backend.system.design.LLD.RateLimiter.factory.RateLimiterFactory;
import com.backend.system.design.LLD.RateLimiter.model.RateLimiterType;
import com.backend.system.design.LLD.RateLimiter.util.RateLimiterMetrics;

/**
 * Real-world usage examples showing modular design
 */
public class UsageExamples {
    
    /**
     * Example 1: Simple API Rate Limiting using Factory
     */
    public static class SimpleAPIExample {
        private final RateLimiter rateLimiter;
        
        public SimpleAPIExample() {
            // Create rate limiter using factory
            this.rateLimiter = RateLimiterFactory.create(
                RateLimiterType.TOKEN_BUCKET,
                100,  // 100 requests
                10    // 10 per second refill
            );
        }
        
        public String handleRequest(String userId) {
            if (rateLimiter.allowRequest(userId)) {
                return processRequest(userId);
            } else {
                return "429 Too Many Requests";
            }
        }
        
        private String processRequest(String userId) {
            return "Success for " + userId;
        }
    }
    
    /**
     * Example 2: Tier-Based Rate Limiting using Config Builder
     */
    public static class TierBasedAPIExample {
        private final RateLimiter freeTier;
        private final RateLimiter basicTier;
        private final RateLimiter proTier;
        
        public TierBasedAPIExample() {
            // Free tier: 100 requests/hour
            RateLimiterConfig freeConfig = new RateLimiterConfig.Builder()
                .type(RateLimiterType.FIXED_WINDOW_COUNTER)
                .capacity(100)
                .windowSizeSeconds(3600)
                .build();
            this.freeTier = RateLimiterFactory.create(freeConfig);
            
            // Basic tier: 1000 requests/hour
            RateLimiterConfig basicConfig = new RateLimiterConfig.Builder()
                .type(RateLimiterType.SLIDING_WINDOW_COUNTER)
                .capacity(1000)
                .windowSizeSeconds(3600)
                .build();
            this.basicTier = RateLimiterFactory.create(basicConfig);
            
            // Pro tier: 10000 requests/hour
            RateLimiterConfig proConfig = new RateLimiterConfig.Builder()
                .type(RateLimiterType.TOKEN_BUCKET)
                .capacity(10000)
                .rate(10000)
                .build();
            this.proTier = RateLimiterFactory.create(proConfig);
        }
        
        public boolean allowRequest(String userId, String tier) {
            switch (tier.toUpperCase()) {
                case "FREE":
                    return freeTier.allowRequest(userId);
                case "BASIC":
                    return basicTier.allowRequest(userId);
                case "PRO":
                    return proTier.allowRequest(userId);
                default:
                    return freeTier.allowRequest(userId);
            }
        }
    }
    
    /**
     * Example 3: API with Metrics using Presets
     */
    public static class MonitoredAPIExample {
        private final RateLimiter rateLimiter;
        private final RateLimiterMetrics metrics;
        
        public MonitoredAPIExample() {
            // Use preset configuration
            this.rateLimiter = RateLimiterFactory.createForPublicAPI();
            this.metrics = new RateLimiterMetrics();
        }
        
        public String handleRequest(String userId) {
            boolean allowed = rateLimiter.allowRequest(userId);
            
            if (allowed) {
                metrics.recordAllowed();
                return processRequest(userId);
            } else {
                metrics.recordBlocked();
                logRateLimitExceeded(userId);
                return "429 Too Many Requests";
            }
        }
        
        public RateLimiterMetrics getMetrics() {
            return metrics;
        }
        
        private String processRequest(String userId) {
            return "Success for " + userId;
        }
        
        private void logRateLimitExceeded(String userId) {
            System.out.println("Rate limit exceeded for user: " + userId);
        }
    }
    
    /**
     * Example 4: REST API Controller Pattern
     */
    public static class RESTAPIController {
        private final RateLimiter globalLimiter;
        private final RateLimiter endpointLimiter;
        
        public RESTAPIController() {
            // Global rate limit: 1000 req/min
            this.globalLimiter = RateLimiterFactory.create(
                RateLimiterType.SLIDING_WINDOW_COUNTER,
                1000,
                60
            );
            
            // Per-endpoint limit: 100 req/min
            this.endpointLimiter = RateLimiterFactory.create(
                RateLimiterType.TOKEN_BUCKET,
                100,
                100
            );
        }
        
        // Simulated REST endpoint
        public String getData(String userId, String endpoint) {
            // Check global limit
            if (!globalLimiter.allowRequest(userId)) {
                return error(429, "Global rate limit exceeded");
            }
            
            // Check endpoint-specific limit
            String endpointKey = userId + ":" + endpoint;
            if (!endpointLimiter.allowRequest(endpointKey)) {
                return error(429, "Endpoint rate limit exceeded");
            }
            
            return success("Data for " + userId);
        }
        
        private String success(String data) {
            return "200 OK: " + data;
        }
        
        private String error(int code, String message) {
            return code + " " + message;
        }
    }
    
    /**
     * Example 5: Microservice with Different Limits
     */
    public static class MicroserviceExample {
        private final RateLimiter readLimiter;
        private final RateLimiter writeLimiter;
        private final RateLimiter deleteLimiter;
        
        public MicroserviceExample() {
            // Read operations: High limit (fast, cheap)
            this.readLimiter = RateLimiterFactory.create(
                new RateLimiterConfig.Builder()
                    .type(RateLimiterType.TOKEN_BUCKET)
                    .capacity(1000)
                    .rate(1000)
                    .build()
            );
            
            // Write operations: Medium limit (slower, expensive)
            this.writeLimiter = RateLimiterFactory.create(
                new RateLimiterConfig.Builder()
                    .type(RateLimiterType.SLIDING_WINDOW_COUNTER)
                    .capacity(100)
                    .windowSizeSeconds(60)
                    .build()
            );
            
            // Delete operations: Low limit (dangerous)
            this.deleteLimiter = RateLimiterFactory.create(
                new RateLimiterConfig.Builder()
                    .type(RateLimiterType.SLIDING_WINDOW_LOG)
                    .capacity(10)
                    .windowSizeSeconds(60)
                    .build()
            );
        }
        
        public boolean canRead(String userId) {
            return readLimiter.allowRequest(userId);
        }
        
        public boolean canWrite(String userId) {
            return writeLimiter.allowRequest(userId);
        }
        
        public boolean canDelete(String userId) {
            return deleteLimiter.allowRequest(userId);
        }
    }
    
    /**
     * Main method to run examples
     */
    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════");
        System.out.println("  MODULAR RATE LIMITER USAGE EXAMPLES");
        System.out.println("═══════════════════════════════════════════\n");
        
        // Example 1: Simple API
        System.out.println("1. Simple API Example:");
        SimpleAPIExample simpleAPI = new SimpleAPIExample();
        System.out.println("   " + simpleAPI.handleRequest("user1"));
        System.out.println();
        
        // Example 2: Tier-Based
        System.out.println("2. Tier-Based API Example:");
        TierBasedAPIExample tierAPI = new TierBasedAPIExample();
        System.out.println("   Free user: " + tierAPI.allowRequest("user1", "FREE"));
        System.out.println("   Pro user: " + tierAPI.allowRequest("user2", "PRO"));
        System.out.println();
        
        // Example 3: Monitored API
        System.out.println("3. Monitored API Example:");
        MonitoredAPIExample monitoredAPI = new MonitoredAPIExample();
        for (int i = 0; i < 5; i++) {
            monitoredAPI.handleRequest("user3");
        }
        System.out.println("   Metrics: " + monitoredAPI.getMetrics());
        System.out.println();
        
        // Example 4: REST API Controller
        System.out.println("4. REST API Controller Example:");
        RESTAPIController controller = new RESTAPIController();
        System.out.println("   " + controller.getData("user4", "/api/data"));
        System.out.println();
        
        // Example 5: Microservice
        System.out.println("5. Microservice Example:");
        MicroserviceExample microservice = new MicroserviceExample();
        System.out.println("   Can read: " + microservice.canRead("user5"));
        System.out.println("   Can write: " + microservice.canWrite("user5"));
        System.out.println("   Can delete: " + microservice.canDelete("user5"));
        System.out.println();
    }
}

