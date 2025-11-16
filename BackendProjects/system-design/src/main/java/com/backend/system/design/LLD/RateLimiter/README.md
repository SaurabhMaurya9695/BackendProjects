# Rate Limiter - Modular Low Level Design

A comprehensive, **production-ready** implementation of various **Rate Limiting Algorithms** in Java, demonstrating **modular design patterns** including Factory, Builder, and Strategy patterns.

## 📚 Table of Contents

- [Architecture](#architecture)
- [Quick Start](#quick-start)
- [Module Overview](#module-overview)
- [Usage Examples](#usage-examples)
- [Rate Limiting Algorithms](#rate-limiting-algorithms)
- [Design Patterns Used](#design-patterns-used)
- [How to Run](#how-to-run)
- [Real-World Usage](#real-world-usage)

---

## 🏗️ Architecture

### Modular Package Structure

```
RateLimiter/
│
├── 📦 model/                          # Data models and enums
│   ├── RateLimitResult.java         # Result object with metadata
│   └── RateLimiterType.java         # Enum for algorithm types
│
├── ⚙️  config/                        # Configuration management
│   └── RateLimiterConfig.java       # Builder pattern for config
│       ├── Builder                   # Fluent configuration API
│       └── Presets                   # Pre-configured setups
│
├── 🏭 factory/                        # Factory pattern
│   └── RateLimiterFactory.java      # Creates rate limiters
│
├── 🧮 algorithms/                     # Algorithm implementations
│   ├── TokenBucketRateLimiter.java
│   ├── LeakyBucketRateLimiter.java
│   ├── FixedWindowCounterRateLimiter.java
│   ├── SlidingWindowLogRateLimiter.java
│   └── SlidingWindowCounterRateLimiter.java
│
├── 🛠️  util/                          # Utility classes
│   ├── TimeProvider.java            # Time abstraction for testing
│   └── RateLimiterMetrics.java      # Metrics tracking
│
├── 📋 RateLimiter.java               # Main interface
├── 🎯 RateLimiterDemo.java           # Demonstration
├── ✅ RateLimiterTest.java           # Test suite
└── 📝 UsageExamples.java             # Real-world examples
```

---

## 🚀 Quick Start

### Option 1: Using Factory Pattern (Recommended)

```java
import com.backend.system.design.LLD.RateLimiter.*;
import com.backend.system.design.LLD.RateLimiter.factory.*;
import com.backend.system.design.LLD.RateLimiter.model.*;

// Create rate limiter using factory
RateLimiter limiter = RateLimiterFactory.create(
    RateLimiterType.TOKEN_BUCKET,
    100,  // capacity
    50    // rate
);

// Use it
if (limiter.allowRequest("user123")) {
    // Process request
} else {
    // Return 429 Too Many Requests
}
```

### Option 2: Using Config Builder Pattern

```java
import com.backend.system.design.LLD.RateLimiter.config.*;

// Build configuration
RateLimiterConfig config = new RateLimiterConfig.Builder()
    .type(RateLimiterType.SLIDING_WINDOW_COUNTER)
    .capacity(1000)
    .windowSizeSeconds(60)
    .build();

// Create from config
RateLimiter limiter = RateLimiterFactory.create(config);
```

### Option 3: Using Presets

```java
// Use pre-configured setups
RateLimiter socialMedia = RateLimiterFactory.createForSocialMedia();
RateLimiter payment = RateLimiterFactory.createForPayments();
RateLimiter publicAPI = RateLimiterFactory.createForPublicAPI();
```

---

## 📦 Module Overview

### 1. **Model Package** (`model/`)

Defines data structures and enums.

**RateLimitResult**
```java
RateLimitResult result = RateLimitResult.allowed(remaining, resetTime);
boolean isAllowed = result.isAllowed();
long remaining = result.getRemainingRequests();
```

**RateLimiterType**
```java
public enum RateLimiterType {
    TOKEN_BUCKET,
    LEAKY_BUCKET,
    FIXED_WINDOW_COUNTER,
    SLIDING_WINDOW_LOG,
    SLIDING_WINDOW_COUNTER
}
```

### 2. **Config Package** (`config/`)

Configuration management with Builder pattern.

```java
RateLimiterConfig config = new RateLimiterConfig.Builder()
    .type(RateLimiterType.TOKEN_BUCKET)
    .capacity(100)
    .rate(50)
    .windowSizeSeconds(60)
    .build();

// Use presets
RateLimiterConfig socialConfig = RateLimiterConfig.Presets.socialMediaAPI();
```

**Available Presets:**
- `socialMediaAPI()` - Token Bucket for burst traffic
- `paymentGateway()` - Sliding Log for accuracy
- `publicAPI()` - Sliding Counter for production
- `microservice()` - Fixed Window for simplicity
- `videoStreaming()` - Leaky Bucket for constant rate

### 3. **Factory Package** (`factory/`)

Creates rate limiter instances using Factory pattern.

```java
// Simple creation
RateLimiter limiter = RateLimiterFactory.create(type, capacity, rate);

// From config
RateLimiter limiter = RateLimiterFactory.create(config);

// Using convenience methods
RateLimiter limiter = RateLimiterFactory.createForSocialMedia();
```

### 4. **Algorithms Package** (`algorithms/`)

Contains all rate limiting algorithm implementations.

- **TokenBucketRateLimiter** - Allows bursts, refills tokens
- **LeakyBucketRateLimiter** - Constant output rate
- **FixedWindowCounterRateLimiter** - Simple time windows
- **SlidingWindowLogRateLimiter** - Most accurate, stores timestamps
- **SlidingWindowCounterRateLimiter** - Production-ready balance

### 5. **Util Package** (`util/`)

Utility classes for metrics and testing.

**RateLimiterMetrics**
```java
RateLimiterMetrics metrics = new RateLimiterMetrics();
metrics.recordAllowed();
metrics.recordBlocked();
System.out.println(metrics);  // Prints stats
```

**TimeProvider**
```java
// Production: uses system time
TimeProvider timeProvider = new SystemTimeProvider();

// Testing: use fixed/mockable time
FixedTimeProvider testTime = new FixedTimeProvider(1000);
testTime.advance(5000);  // Advance by 5 seconds
```

---

## 💡 Usage Examples

### Example 1: Simple REST API

```java
public class SimpleAPI {
    private final RateLimiter limiter;
    
    public SimpleAPI() {
        this.limiter = RateLimiterFactory.createForPublicAPI();
    }
    
    public Response handleRequest(String userId) {
        if (!limiter.allowRequest(userId)) {
            return Response.status(429).entity("Too Many Requests").build();
        }
        return Response.ok(processRequest()).build();
    }
}
```

### Example 2: Tier-Based Limits

```java
public class TierBasedAPI {
    private final Map<String, RateLimiter> tierLimiters;
    
    public TierBasedAPI() {
        tierLimiters = new HashMap<>();
        
        // Free: 100 req/hour
        tierLimiters.put("FREE", RateLimiterFactory.create(
            RateLimiterType.FIXED_WINDOW_COUNTER, 100, 3600));
        
        // Pro: 10,000 req/hour  
        tierLimiters.put("PRO", RateLimiterFactory.create(
            RateLimiterType.TOKEN_BUCKET, 10000, 10000));
    }
    
    public boolean allowRequest(String userId, String tier) {
        return tierLimiters.get(tier).allowRequest(userId);
    }
}
```

### Example 3: With Metrics

```java
public class MonitoredAPI {
    private final RateLimiter limiter;
    private final RateLimiterMetrics metrics;
    
    public MonitoredAPI() {
        this.limiter = RateLimiterFactory.createForPublicAPI();
        this.metrics = new RateLimiterMetrics();
    }
    
    public Response handleRequest(String userId) {
        if (limiter.allowRequest(userId)) {
            metrics.recordAllowed();
            return processRequest();
        } else {
            metrics.recordBlocked();
            logAlert("Rate limit hit for: " + userId);
            return tooManyRequests();
        }
    }
    
    public void printMetrics() {
        System.out.println(metrics);
        // Output: Metrics{total=1000, allowed=900, blocked=100, blocked%=10.00%, uptime=3600s}
    }
}
```

---

## 🎯 Design Patterns Used

### 1. **Factory Pattern**
Creates appropriate rate limiter without exposing instantiation logic.

```java
RateLimiter limiter = RateLimiterFactory.create(
    RateLimiterType.TOKEN_BUCKET, 100, 50
);
```

**Benefits:**
- Decouples client code from concrete classes
- Easy to add new algorithms
- Centralized creation logic

### 2. **Builder Pattern**
Constructs complex configuration objects step by step.

```java
RateLimiterConfig config = new RateLimiterConfig.Builder()
    .type(RateLimiterType.TOKEN_BUCKET)
    .capacity(100)
    .rate(50)
    .build();
```

**Benefits:**
- Fluent, readable API
- Validates configuration
- Immutable config objects

### 3. **Strategy Pattern**
Different rate limiting algorithms implement same interface.

```java
RateLimiter strategy = chooseStrategy();
boolean allowed = strategy.allowRequest(userId);
```

**Benefits:**
- Algorithms are interchangeable
- Easy to test individually
- Open for extension

### 4. **Singleton (Metrics)**
Single metrics instance tracks system-wide stats.

```java
RateLimiterMetrics metrics = new RateLimiterMetrics();
// Shared across all requests
```

---

## 📊 Algorithm Comparison

| Algorithm | Memory | Accuracy | Burst | Complexity | Use Case |
|-----------|--------|----------|-------|------------|----------|
| **Token Bucket** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ | Social Media, AWS |
| **Leaky Bucket** | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐ | ⭐⭐⭐ | Video Streaming |
| **Fixed Window** | ⭐⭐⭐⭐⭐ | ⭐⭐ | ⭐ | ⭐⭐⭐⭐⭐ | Simple APIs |
| **Sliding Log** | ⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐ | Payments |
| **Sliding Counter** | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐ | **Production** |

---

## 🚀 How to Run

### Compile

```bash
cd /Users/saurabh/Desktop/Saurabh/BackendProjects/BackendProjects/system-design/src/main/java

javac com/backend/system/design/LLD/RateLimiter/*.java \
      com/backend/system/design/LLD/RateLimiter/**/*.java
```

### Run Demo

```bash
# Main demo
java com.backend.system.design.LLD.RateLimiter.RateLimiterDemo

# Test suite
java com.backend.system.design.LLD.RateLimiter.RateLimiterTest

# Usage examples
java com.backend.system.design.LLD.RateLimiter.UsageExamples
```

---

## 🏢 Real-World Usage

### 1. **Spring Boot Integration**

```java
@Configuration
public class RateLimiterConfig {
    
    @Bean
    public RateLimiter apiRateLimiter() {
        return RateLimiterFactory.createForPublicAPI();
    }
}

@RestController
public class APIController {
    
    @Autowired
    private RateLimiter rateLimiter;
    
    @GetMapping("/api/data")
    public ResponseEntity<?> getData(@RequestHeader("User-Id") String userId) {
        if (!rateLimiter.allowRequest(userId)) {
            return ResponseEntity.status(429).body("Too Many Requests");
        }
        return ResponseEntity.ok(fetchData());
    }
}
```

### 2. **Microservice Gateway**

```java
public class APIGateway {
    private final RateLimiter globalLimiter;
    private final Map<String, RateLimiter> serviceLimiters;
    
    public APIGateway() {
        this.globalLimiter = RateLimiterFactory.create(
            RateLimiterType.SLIDING_WINDOW_COUNTER, 10000, 60);
        
        this.serviceLimiters = Map.of(
            "user-service", RateLimiterFactory.create(
                RateLimiterType.TOKEN_BUCKET, 1000, 100),
            "payment-service", RateLimiterFactory.create(
                RateLimiterType.SLIDING_WINDOW_LOG, 100, 1)
        );
    }
    
    public boolean allowRequest(String userId, String service) {
        return globalLimiter.allowRequest(userId) && 
               serviceLimiters.get(service).allowRequest(userId);
    }
}
```

---

## 📈 Benefits of Modular Design

### ✅ Maintainability
- Each module has single responsibility
- Easy to locate and fix bugs
- Clear separation of concerns

### ✅ Extensibility
- Add new algorithms without changing existing code
- New configuration presets easily added
- Custom metrics can be plugged in

### ✅ Testability
- Mock TimeProvider for deterministic tests
- Test each algorithm independently
- Factory makes it easy to swap implementations

### ✅ Reusability
- Config objects can be shared
- Factory methods reduce boilerplate
- Metrics can be reused across services

---

## 📚 Additional Resources

- [ALGORITHMS_EXPLAINED.md](./ALGORITHMS_EXPLAINED.md) - Deep dive into each algorithm
- [DISTRIBUTED_RATE_LIMITING.md](./DISTRIBUTED_RATE_LIMITING.md) - Redis implementation
- [QUICK_START.md](./QUICK_START.md) - Getting started guide

---

## 🎓 Learning Path

1. ✅ Understand the modular structure
2. ✅ Run the demo and tests
3. ✅ Study each algorithm in `algorithms/`
4. ✅ Learn design patterns used
5. ✅ Implement in your project
6. ✅ Explore distributed setup with Redis

---

## 📝 License

This project is for educational purposes. Feel free to use and modify.

---

## 👨‍💻 Author

Created as a comprehensive learning resource for understanding Rate Limiting algorithms and modular design patterns in System Design.

**Happy Learning! 🚀**

---

## 🔑 Key Takeaways

1. **Modular design** makes code maintainable and extensible
2. **Factory pattern** decouples creation from usage
3. **Builder pattern** provides fluent configuration API
4. **Strategy pattern** allows algorithm swapping
5. **Separation of concerns** improves testability
6. **Production-ready** with metrics and monitoring
