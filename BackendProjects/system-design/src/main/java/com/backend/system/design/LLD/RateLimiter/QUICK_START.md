# Quick Start Guide

## 🚀 Getting Started with Rate Limiter

This guide will help you quickly understand and run the Rate Limiter implementations.

---

## 📁 Project Structure

```
RateLimiter/
├── README.md                           # Main documentation
├── ALGORITHMS_EXPLAINED.md             # Deep dive into each algorithm
├── DISTRIBUTED_RATE_LIMITING.md        # Redis implementation guide
├── QUICK_START.md                      # This file
│
├── RateLimiter.java                    # Interface
│
├── TokenBucketRateLimiter.java         # Algorithm 1
├── LeakyBucketRateLimiter.java         # Algorithm 2
├── FixedWindowCounterRateLimiter.java  # Algorithm 3
├── SlidingWindowLogRateLimiter.java    # Algorithm 4
├── SlidingWindowCounterRateLimiter.java # Algorithm 5
│
├── RateLimiterDemo.java                # Demo application
└── RateLimiterTest.java                # Test suite
```

---

## ⚡ Quick Run (3 Steps)

### Step 1: Navigate to the directory
```bash
cd /Users/saurabh/Desktop/Saurabh/BackendProjects/BackendProjects/system-design/src/main/java
```

### Step 2: Compile all files
```bash
javac com/backend/system/design/LLD/RateLimiter/*.java
```

### Step 3: Run the demo
```bash
# Option A: Run interactive demo
java com.backend.system.design.LLD.RateLimiter.RateLimiterDemo

# Option B: Run test suite
java com.backend.system.design.LLD.RateLimiter.RateLimiterTest
```

---

## 💻 Code Examples

### Example 1: Token Bucket (Allow Bursts)

```java
import com.backend.system.design.LLD.RateLimiter.*;

public class Example1 {
    public static void main(String[] args) {
        // Create rate limiter: 10 tokens max, refill 5 tokens/second
        RateLimiter limiter = new TokenBucketRateLimiter(10, 5);
        
        // Simulate API requests
        for (int i = 1; i <= 15; i++) {
            boolean allowed = limiter.allowRequest("user123");
            
            if (allowed) {
                System.out.println("Request " + i + ": ✓ Processing...");
                // Process the request
            } else {
                System.out.println("Request " + i + ": ✗ Rate limited - 429 Too Many Requests");
            }
        }
    }
}
```

**Output:**
```
Request 1: ✓ Processing...
Request 2: ✓ Processing...
...
Request 10: ✓ Processing...
Request 11: ✗ Rate limited - 429 Too Many Requests
Request 12: ✗ Rate limited - 429 Too Many Requests
```

---

### Example 2: Fixed Window Counter (Simple)

```java
import com.backend.system.design.LLD.RateLimiter.*;

public class Example2 {
    public static void main(String[] args) {
        // Allow 100 requests per minute
        RateLimiter limiter = new FixedWindowCounterRateLimiter(100, 60);
        
        // Check if request is allowed
        if (limiter.allowRequest("user456")) {
            // Process request
            processApiRequest();
        } else {
            // Return error
            returnError(429, "Rate limit exceeded");
        }
    }
    
    private static void processApiRequest() {
        System.out.println("Processing request...");
    }
    
    private static void returnError(int code, String message) {
        System.out.println("HTTP " + code + ": " + message);
    }
}
```

---

### Example 3: Sliding Window Counter (Production)

```java
import com.backend.system.design.LLD.RateLimiter.*;

public class Example3 {
    public static void main(String[] args) {
        // Production-ready rate limiter
        // 1000 requests per hour
        RateLimiter limiter = new SlidingWindowCounterRateLimiter(1000, 3600);
        
        // Simulate different users
        String[] users = {"user1", "user2", "user3"};
        
        for (String user : users) {
            for (int i = 0; i < 5; i++) {
                boolean allowed = limiter.allowRequest(user);
                System.out.printf("%s - Request %d: %s%n", 
                    user, i + 1, allowed ? "✓" : "✗");
            }
        }
    }
}
```

---

### Example 4: Real API Integration

```java
import com.backend.system.design.LLD.RateLimiter.*;

/**
 * Example: Spring Boot REST API with Rate Limiting
 */
public class RateLimitedAPI {
    
    private final RateLimiter rateLimiter;
    
    public RateLimitedAPI() {
        // Choose based on your needs:
        
        // For APIs allowing bursts (like GitHub)
        this.rateLimiter = new TokenBucketRateLimiter(5000, 1000);
        
        // For strict rate limiting (like Stripe)
        // this.rateLimiter = new SlidingWindowCounterRateLimiter(5000, 3600);
        
        // For simple rate limiting
        // this.rateLimiter = new FixedWindowCounterRateLimiter(100, 60);
    }
    
    // @GetMapping("/api/data")
    public String getData(String userId) {
        if (!rateLimiter.allowRequest(userId)) {
            // Return 429 Too Many Requests
            return "ERROR: Rate limit exceeded. Please try again later.";
        }
        
        // Process request
        return "Data for user: " + userId;
    }
}
```

---

### Example 5: Tier-Based Rate Limiting

```java
import com.backend.system.design.LLD.RateLimiter.*;
import java.util.HashMap;
import java.util.Map;

public class TierBasedRateLimiting {
    
    private final Map<String, RateLimiter> tierLimiters;
    
    public TierBasedRateLimiting() {
        tierLimiters = new HashMap<>();
        
        // Free tier: 100 requests/hour
        tierLimiters.put("FREE", 
            new TokenBucketRateLimiter(100, 100));
        
        // Basic tier: 1,000 requests/hour
        tierLimiters.put("BASIC", 
            new TokenBucketRateLimiter(1000, 1000));
        
        // Pro tier: 10,000 requests/hour
        tierLimiters.put("PRO", 
            new TokenBucketRateLimiter(10000, 10000));
        
        // Enterprise: 100,000 requests/hour
        tierLimiters.put("ENTERPRISE", 
            new TokenBucketRateLimiter(100000, 100000));
    }
    
    public boolean allowRequest(String userId, String tier) {
        RateLimiter limiter = tierLimiters.getOrDefault(tier, 
            tierLimiters.get("FREE"));
        
        return limiter.allowRequest(userId);
    }
    
    public static void main(String[] args) {
        TierBasedRateLimiting api = new TierBasedRateLimiting();
        
        // Free user
        System.out.println("Free user: " + 
            api.allowRequest("user1", "FREE"));
        
        // Pro user
        System.out.println("Pro user: " + 
            api.allowRequest("user2", "PRO"));
        
        // Enterprise user
        System.out.println("Enterprise user: " + 
            api.allowRequest("user3", "ENTERPRISE"));
    }
}
```

---

## 🎯 Choose Your Algorithm

### Quick Decision Tree

```
START
  │
  ├─ Need to handle traffic bursts?
  │   └─ YES → Use Token Bucket
  │   └─ NO  → Continue
  │
  ├─ Need constant output rate?
  │   └─ YES → Use Leaky Bucket
  │   └─ NO  → Continue
  │
  ├─ Building a prototype?
  │   └─ YES → Use Fixed Window Counter
  │   └─ NO  → Continue
  │
  ├─ Need 100% accuracy (payments, financial)?
  │   └─ YES → Use Sliding Window Log
  │   └─ NO  → Continue
  │
  └─ Default for production APIs
      └─ Use Sliding Window Counter
```

---

## 🔧 Configuration Guide

### Common Configurations

```java
// 1. Social Media API (Twitter-like)
// Allow bursts of likes/posts
RateLimiter twitter = new TokenBucketRateLimiter(
    maxTokens: 100,     // 100 requests burst
    refillRate: 50      // 50 requests/second sustained
);

// 2. Payment Gateway (Stripe-like)
// Strict rate limiting, no bursts
RateLimiter stripe = new SlidingWindowCounterRateLimiter(
    maxRequests: 100,   // 100 requests
    windowSize: 1       // per second
);

// 3. Public API (GitHub-like)
// Hourly limits with burst capacity
RateLimiter github = new TokenBucketRateLimiter(
    maxTokens: 5000,    // 5000 requests burst
    refillRate: 5000    // 5000 requests/hour
);

// 4. Internal Microservice
// Simple rate limiting
RateLimiter internal = new FixedWindowCounterRateLimiter(
    maxRequests: 10000, // 10000 requests
    windowSize: 60      // per minute
);

// 5. Video Streaming
// Constant bitrate
RateLimiter streaming = new LeakyBucketRateLimiter(
    capacity: 1000,     // 1000 requests in queue
    leakRate: 100       // Process 100/second
);
```

---

## 📊 Testing Your Implementation

### Manual Testing

```java
public class ManualTest {
    public static void main(String[] args) throws InterruptedException {
        RateLimiter limiter = new TokenBucketRateLimiter(3, 1);
        
        System.out.println("Test 1: Rapid requests");
        for (int i = 1; i <= 5; i++) {
            boolean allowed = limiter.allowRequest("testUser");
            System.out.println("Request " + i + ": " + 
                (allowed ? "✓ ALLOWED" : "✗ BLOCKED"));
        }
        
        System.out.println("\nWaiting 2 seconds...");
        Thread.sleep(2000);
        
        System.out.println("\nTest 2: After refill");
        for (int i = 1; i <= 3; i++) {
            boolean allowed = limiter.allowRequest("testUser");
            System.out.println("Request " + i + ": " + 
                (allowed ? "✓ ALLOWED" : "✗ BLOCKED"));
        }
    }
}
```

### Load Testing

```java
import java.util.concurrent.*;

public class LoadTest {
    public static void main(String[] args) throws InterruptedException {
        RateLimiter limiter = new TokenBucketRateLimiter(100, 50);
        
        ExecutorService executor = Executors.newFixedThreadPool(10);
        AtomicInteger allowed = new AtomicInteger(0);
        AtomicInteger blocked = new AtomicInteger(0);
        
        // Simulate 1000 concurrent requests
        for (int i = 0; i < 1000; i++) {
            executor.submit(() -> {
                if (limiter.allowRequest("user123")) {
                    allowed.incrementAndGet();
                } else {
                    blocked.incrementAndGet();
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        
        System.out.println("Allowed: " + allowed.get());
        System.out.println("Blocked: " + blocked.get());
        System.out.println("Total: " + (allowed.get() + blocked.get()));
    }
}
```

---

## 🐛 Common Issues & Solutions

### Issue 1: All requests are blocked

**Problem:**
```java
RateLimiter limiter = new TokenBucketRateLimiter(0, 10);  // ❌ Wrong!
```

**Solution:**
```java
RateLimiter limiter = new TokenBucketRateLimiter(10, 10); // ✅ Correct!
// Initial tokens should be > 0
```

---

### Issue 2: Rate limit not working across servers

**Problem:** Using in-memory storage with multiple servers

**Solution:** Use Redis for distributed rate limiting (see DISTRIBUTED_RATE_LIMITING.md)

---

### Issue 3: Thread safety issues

**Problem:**
```java
// Not thread-safe!
public boolean allowRequest(String userId) {
    int count = getCount(userId);
    if (count < limit) {
        incrementCount(userId);  // Race condition!
        return true;
    }
    return false;
}
```

**Solution:**
```java
// Thread-safe with synchronized
public synchronized boolean allowRequest(String userId) {
    // Atomic operation
}
```

---

## 📚 Learning Path

### Beginner
1. ✅ Read README.md
2. ✅ Run RateLimiterDemo.java
3. ✅ Understand Fixed Window Counter

### Intermediate
4. ✅ Study Token Bucket algorithm
5. ✅ Implement your own rate limiter
6. ✅ Read ALGORITHMS_EXPLAINED.md

### Advanced
7. ✅ Learn Sliding Window Counter
8. ✅ Implement distributed rate limiting
9. ✅ Read DISTRIBUTED_RATE_LIMITING.md

---

## 🎓 Interview Preparation

### Must-Know Concepts

1. **Why rate limiting?**
   - Prevent abuse
   - Fair resource distribution
   - Cost control
   - DDoS protection

2. **Which algorithm when?**
   - Bursts → Token Bucket
   - Constant rate → Leaky Bucket
   - Simple → Fixed Window
   - Accuracy → Sliding Log
   - Production → Sliding Counter

3. **Distributed challenges:**
   - Race conditions
   - Clock synchronization
   - Redis failure handling
   - Atomic operations

### Common Questions

**Q: How would you implement rate limiting for 1 billion users?**

A: Use Redis Cluster with Sliding Window Counter:
```
- Shard users across Redis nodes
- Use consistent hashing
- Local cache for hot users
- Lua scripts for atomicity
- Monitor with metrics
```

**Q: What if Redis goes down?**

A: Implement fallback strategy:
```java
try {
    return checkRateLimitInRedis(userId);
} catch (Exception e) {
    // Option 1: Allow (fail open)
    return true;
    
    // Option 2: Deny (fail closed)
    // return false;
    
    // Option 3: Use local cache
    // return checkLocalRateLimit(userId);
}
```

---

## 🚀 Next Steps

1. ✅ Run the demo
2. ✅ Read the algorithm explanations
3. ✅ Try different configurations
4. ✅ Implement in your project
5. ✅ Learn distributed rate limiting
6. ✅ Practice interview questions

---

## 📞 Need Help?

- Read: README.md for overview
- Study: ALGORITHMS_EXPLAINED.md for deep dive
- Scale: DISTRIBUTED_RATE_LIMITING.md for production

**Happy Learning! 🎉**

