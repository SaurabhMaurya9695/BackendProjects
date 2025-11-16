# Architecture and Design Documentation

## 🏗️ System Architecture

### High-Level Architecture

```
┌──────────────────────────────────────────────────────────────┐
│                     CLIENT APPLICATION                        │
│  (REST API, Microservice, Spring Boot, etc.)                 │
└───────────────────────────────┬──────────────────────────────┘
                                │
                                ↓
┌──────────────────────────────────────────────────────────────┐
│                    RATE LIMITER FACADE                        │
│                   RateLimiterFactory                          │
│              (Factory Pattern Entry Point)                    │
└───────────┬──────────────────────────────────────────────────┘
            │
            ├─────→ RateLimiterConfig (Builder Pattern)
            │       • Type selection
            │       • Capacity configuration
            │       • Rate settings
            │       • Presets
            │
            ↓
┌──────────────────────────────────────────────────────────────┐
│                 RATE LIMITER INTERFACE                        │
│                    (Strategy Pattern)                         │
│    boolean allowRequest(String userId)                        │
│    void reset(String userId)                                  │
└───┬─────────┬────────┬────────┬──────────┬────────────────────┘
    │         │        │        │          │
    ↓         ↓        ↓        ↓          ↓
┌────────┐ ┌──────┐ ┌─────┐ ┌───────┐ ┌──────────┐
│ Token  │ │Leaky │ │Fixed│ │Sliding│ │ Sliding  │
│ Bucket │ │Bucket│ │Window│ │Window │ │  Window  │
│        │ │      │ │      │ │ Log   │ │  Counter │
└────────┘ └──────┘ └─────┘ └───────┘ └──────────┘
    │         │        │        │          │
    └─────────┴────────┴────────┴──────────┘
                    │
                    ↓
            ┌──────────────┐
            │  UTILITIES   │
            ├──────────────┤
            │ TimeProvider │
            │ Metrics      │
            └──────────────┘
```

---

## 📦 Module Dependencies

```
┌─────────────────────────────────────────────────────────┐
│                       model/                            │
│  ┌────────────────┐    ┌─────────────────┐            │
│  │RateLimiterType │    │RateLimitResult  │            │
│  │   (enum)       │    │  (data class)   │            │
│  └────────────────┘    └─────────────────┘            │
└──────────────────────┬──────────────────────────────────┘
                       │
                       ↓
┌─────────────────────────────────────────────────────────┐
│                      config/                            │
│  ┌──────────────────────────────────────────┐          │
│  │      RateLimiterConfig                   │          │
│  │  ┌──────────────┐  ┌─────────────────┐  │          │
│  │  │   Builder    │  │    Presets      │  │          │
│  │  │              │  │ • socialMedia() │  │          │
│  │  │ • type()     │  │ • payment()     │  │          │
│  │  │ • capacity() │  │ • publicAPI()   │  │          │
│  │  │ • rate()     │  │ • microservice()│  │          │
│  │  │ • build()    │  │ • streaming()   │  │          │
│  │  └──────────────┘  └─────────────────┘  │          │
│  └──────────────────────────────────────────┘          │
└──────────────────────┬──────────────────────────────────┘
                       │
                       ↓
┌─────────────────────────────────────────────────────────┐
│                     factory/                            │
│  ┌──────────────────────────────────────────┐          │
│  │      RateLimiterFactory                  │          │
│  │  • create(config)                        │          │
│  │  • create(type, capacity, rate)          │          │
│  │  • createForSocialMedia()                │          │
│  │  • createForPayments()                   │          │
│  │  • createForPublicAPI()                  │          │
│  │  • createForMicroservice()               │          │
│  │  • createForStreaming()                  │          │
│  └──────────────────────────────────────────┘          │
└──────────────────────┬──────────────────────────────────┘
                       │
                       ↓
┌─────────────────────────────────────────────────────────┐
│                   algorithms/                           │
│  ┌──────────────────────────────────────────┐          │
│  │ • TokenBucketRateLimiter                 │          │
│  │ • LeakyBucketRateLimiter                 │          │
│  │ • FixedWindowCounterRateLimiter          │          │
│  │ • SlidingWindowLogRateLimiter            │          │
│  │ • SlidingWindowCounterRateLimiter        │          │
│  └──────────────────────────────────────────┘          │
└──────────────────────┬──────────────────────────────────┘
                       │
                       ↓
┌─────────────────────────────────────────────────────────┐
│                      util/                              │
│  ┌──────────────────┐    ┌─────────────────┐          │
│  │  TimeProvider    │    │RateLimiterMetrics│         │
│  │ • system()       │    │ • recordAllowed()│         │
│  │ • fixed()        │    │ • recordBlocked()│         │
│  └──────────────────┘    └─────────────────┘          │
└─────────────────────────────────────────────────────────┘
```

---

## 🎯 Design Patterns Explained

### 1. Factory Pattern

**Intent:** Create objects without specifying exact class

**Implementation:**
```java
// Client doesn't need to know about concrete classes
RateLimiter limiter = RateLimiterFactory.create(
    RateLimiterType.TOKEN_BUCKET, 100, 50
);

// Factory handles the instantiation
switch (type) {
    case TOKEN_BUCKET:
        return new TokenBucketRateLimiter(capacity, rate);
    case LEAKY_BUCKET:
        return new LeakyBucketRateLimiter(capacity, rate);
    // ...
}
```

**Benefits:**
- ✅ Loose coupling
- ✅ Single creation point
- ✅ Easy to extend
- ✅ Hides complexity

---

### 2. Builder Pattern

**Intent:** Construct complex objects step by step

**Implementation:**
```java
RateLimiterConfig config = new RateLimiterConfig.Builder()
    .type(RateLimiterType.TOKEN_BUCKET)  // Step 1
    .capacity(100)                        // Step 2
    .rate(50)                             // Step 3
    .windowSizeSeconds(60)                // Step 4
    .build();                             // Final step
```

**Benefits:**
- ✅ Fluent, readable API
- ✅ Immutable objects
- ✅ Validation in one place
- ✅ Optional parameters

---

### 3. Strategy Pattern

**Intent:** Define family of algorithms, make them interchangeable

**Implementation:**
```java
// All algorithms implement same interface
public interface RateLimiter {
    boolean allowRequest(String userId);
    void reset(String userId);
}

// Client uses interface, not concrete class
RateLimiter strategy = chooseAlgorithm();
boolean allowed = strategy.allowRequest(userId);
```

**Benefits:**
- ✅ Algorithms are interchangeable
- ✅ Easy to add new algorithms
- ✅ Follows Open/Closed Principle
- ✅ Testable in isolation

---

### 4. Facade Pattern

**Intent:** Provide simple interface to complex subsystem

**Implementation:**
```java
// Simple facade for complex subsystem
public class RateLimiterFactory {
    // Hides complexity of:
    // - Choosing algorithm
    // - Configuring parameters
    // - Creating instances
    
    public static RateLimiter createForPublicAPI() {
        // Behind the scenes: complex config, validation, creation
        return create(RateLimiterConfig.Presets.publicAPI());
    }
}
```

**Benefits:**
- ✅ Simple API
- ✅ Hides complexity
- ✅ Reduces coupling
- ✅ Easy to use

---

## 🔄 Request Flow

### Sequence Diagram

```
Client          Factory         Config          Algorithm       Metrics
  │               │               │                 │              │
  │─────create───→│               │                 │              │
  │               │────build─────→│                 │              │
  │               │←──config──────│                 │              │
  │               │                                 │              │
  │               │────new Algorithm────────────────→│              │
  │               │←──────instance──────────────────│              │
  │←──limiter─────│               │                 │              │
  │               │               │                 │              │
  │                                                                │
  │──allowRequest──────────────────────────────────→│              │
  │               │               │                 │              │
  │               │               │    [check rate] │              │
  │               │               │                 │              │
  │←──────true/false──────────────────────────────  │              │
  │               │               │                 │              │
  │──recordAllowed/recordBlocked───────────────────────────────────→│
  │               │               │                 │              │
```

### Flow Steps

1. **Creation Phase**
   ```
   Client → Factory.create()
          → Config.Builder.build()
          → Algorithm instantiation
          → Return to client
   ```

2. **Request Phase**
   ```
   Client → RateLimiter.allowRequest(userId)
          → Algorithm checks limits
          → Update internal state
          → Return true/false
          → Record metrics (optional)
   ```

3. **Monitoring Phase**
   ```
   Client → Metrics.getMetrics()
          → Return statistics
          → Log/Alert if needed
   ```

---

## 🧩 Class Relationships

### UML Class Diagram

```
┌─────────────────────────────────────────────────────────┐
│              <<interface>> RateLimiter                  │
├─────────────────────────────────────────────────────────┤
│ + allowRequest(userId: String): boolean                 │
│ + reset(userId: String): void                           │
└───────────────────────┬─────────────────────────────────┘
                        △
                        │ implements
         ┌──────────────┼──────────────┐
         │              │              │
┌────────┴────────┐ ┌───┴────┐ ┌──────┴──────┐
│ TokenBucket     │ │ Leaky  │ │ Fixed       │
│ RateLimiter     │ │ Bucket │ │ Window      │
└─────────────────┘ └────────┘ └─────────────┘
                        │
            ┌───────────┴───────────┐
            │                       │
    ┌───────┴─────────┐  ┌─────────┴─────────┐
    │ Sliding Window  │  │ Sliding Window    │
    │ Log             │  │ Counter           │
    └─────────────────┘  └───────────────────┘

┌─────────────────────────────────────────────────────────┐
│           RateLimiterFactory                            │
├─────────────────────────────────────────────────────────┤
│ + create(config): RateLimiter           <<static>>      │
│ + create(type, capacity, rate): RL      <<static>>      │
│ + createForSocialMedia(): RL            <<static>>      │
└────────────────────┬────────────────────────────────────┘
                     │ uses
                     ↓
┌─────────────────────────────────────────────────────────┐
│           RateLimiterConfig                             │
├─────────────────────────────────────────────────────────┤
│ - type: RateLimiterType                                 │
│ - capacity: int                                         │
│ - rate: int                                             │
│ - windowSizeSeconds: int                                │
├─────────────────────────────────────────────────────────┤
│ + Builder                                               │
│   + type(RateLimiterType): Builder                      │
│   + capacity(int): Builder                              │
│   + rate(int): Builder                                  │
│   + build(): RateLimiterConfig                          │
│                                                         │
│ + Presets                                               │
│   + socialMediaAPI(): RateLimiterConfig   <<static>>    │
│   + paymentGateway(): RateLimiterConfig   <<static>>    │
└─────────────────────────────────────────────────────────┘
```

---

## 📊 Data Flow

### Token Bucket Data Flow

```
User Request
     │
     ↓
┌─────────────────────────────────────┐
│  TokenBucketRateLimiter             │
│                                     │
│  Step 1: Get current time           │
│  Step 2: Calculate elapsed time     │
│  Step 3: Calculate tokens to add    │
│    tokensToAdd = elapsed × rate     │
│  Step 4: Refill bucket              │
│    tokens = min(tokens+added, max)  │
│  Step 5: Check if token available   │
│    if (tokens > 0) {                │
│      tokens--                       │
│      return true                    │
│    }                                │
│    return false                     │
└─────────────────────────────────────┘
     │
     ↓
Allow/Deny Response
```

---

## 🎯 SOLID Principles Applied

### Single Responsibility Principle (SRP)
- ✅ Each class has one reason to change
- `RateLimiterConfig` - Only configuration
- `RateLimiterFactory` - Only creation
- `RateLimiterMetrics` - Only metrics
- Each algorithm - Only its rate limiting logic

### Open/Closed Principle (OCP)
- ✅ Open for extension, closed for modification
- Add new algorithms without changing existing code
- Add new presets without changing factory
- Add new metrics without changing algorithms

### Liskov Substitution Principle (LSP)
- ✅ All algorithms implement RateLimiter interface
- Can swap any algorithm without breaking code
- Client doesn't know which algorithm is used

### Interface Segregation Principle (ISP)
- ✅ Small, focused interfaces
- `RateLimiter` has only essential methods
- `TimeProvider` has single method
- No fat interfaces

### Dependency Inversion Principle (DIP)
- ✅ Depend on abstractions, not concretions
- Factory returns `RateLimiter` interface, not concrete class
- Client depends on `RateLimiter`, not `TokenBucketRateLimiter`
- Easy to mock for testing

---

## 🧪 Testing Strategy

### Unit Testing
```java
// Test each algorithm independently
@Test
public void testTokenBucket() {
    RateLimiter limiter = new TokenBucketRateLimiter(3, 2);
    assertTrue(limiter.allowRequest("user1"));
    assertTrue(limiter.allowRequest("user1"));
    assertTrue(limiter.allowRequest("user1"));
    assertFalse(limiter.allowRequest("user1"));
}
```

### Integration Testing
```java
// Test factory and config together
@Test
public void testFactoryWithConfig() {
    RateLimiterConfig config = new RateLimiterConfig.Builder()
        .type(RateLimiterType.TOKEN_BUCKET)
        .capacity(10)
        .build();
    
    RateLimiter limiter = RateLimiterFactory.create(config);
    assertNotNull(limiter);
}
```

### Time-based Testing
```java
// Use FixedTimeProvider for deterministic tests
FixedTimeProvider timeProvider = new FixedTimeProvider(1000);
RateLimiter limiter = new TokenBucketRateLimiter(10, 5, timeProvider);

// Advance time
timeProvider.advance(5000);  // 5 seconds
assertTrue(limiter.allowRequest("user1"));
```

---

## 📚 Further Reading

- **Design Patterns**: Gang of Four
- **Clean Architecture**: Robert C. Martin
- **Effective Java**: Joshua Bloch
- **Rate Limiting in Production**: Google SRE Book

---

**Happy Learning! 🚀**

