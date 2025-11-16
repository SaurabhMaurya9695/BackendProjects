# Rate Limiting Algorithms - Deep Dive Explanation

This document provides an in-depth explanation of how each rate limiting algorithm works, with step-by-step examples.

---

## 📚 Table of Contents

1. [Token Bucket - Step by Step](#token-bucket---step-by-step)
2. [Leaky Bucket - Step by Step](#leaky-bucket---step-by-step)
3. [Fixed Window Counter - Step by Step](#fixed-window-counter---step-by-step)
4. [Sliding Window Log - Step by Step](#sliding-window-log---step-by-step)
5. [Sliding Window Counter - Step by Step](#sliding-window-counter---step-by-step)
6. [When to Use Which Algorithm](#when-to-use-which-algorithm)

---

## 1. Token Bucket - Step by Step

### 🎯 Core Idea
Think of it like a **coin purse**:
- You start with some coins (tokens)
- Every second, new coins are added (refill rate)
- To make a purchase (request), you need 1 coin
- Maximum coins you can hold is limited (bucket capacity)

### 📖 Step-by-Step Example

**Configuration:**
- Max Bucket Size: 5 tokens
- Refill Rate: 2 tokens/second
- Cost per request: 1 token

**Timeline:**

```
Time: 0:00
┌────────────────────────────┐
│ Bucket: 🪙 🪙 🪙 🪙 🪙      │  Tokens: 5/5
│ Status: FULL               │
└────────────────────────────┘

User makes 3 requests:
┌────────────────────────────┐
│ Bucket: 🪙 🪙              │  Tokens: 2/5
│ Status: 3 requests served  │
└────────────────────────────┘
Request 4: ✓ ALLOWED (token consumed)
Request 5: ✓ ALLOWED (token consumed)
Request 6: ✗ BLOCKED (no tokens left)

Time: 0:02 (2 seconds later)
Refill: +4 tokens (2 tokens/sec × 2 sec)
┌────────────────────────────┐
│ Bucket: 🪙 🪙 🪙 🪙         │  Tokens: 4/5 (can't exceed max)
│ Status: Refilled           │
└────────────────────────────┘

Request 7: ✓ ALLOWED
Request 8: ✓ ALLOWED
Request 9: ✓ ALLOWED
Request 10: ✓ ALLOWED
Request 11: ✗ BLOCKED
```

### 💻 Code Walkthrough

```java
public boolean allowRequest(String userId) {
    long currentTime = System.currentTimeMillis();
    
    // Step 1: Get or create bucket for user
    Bucket bucket = userBuckets.computeIfAbsent(userId, 
        k -> new Bucket(maxBucketSize, currentTime));
    
    // Step 2: Calculate tokens to add
    long timeElapsed = currentTime - bucket.lastRefillTimestamp;  // milliseconds
    int tokensToAdd = (int) ((timeElapsed / 1000.0) * refillRate);
    
    // Step 3: Refill bucket (don't exceed max)
    if (tokensToAdd > 0) {
        bucket.tokens = Math.min(bucket.tokens + tokensToAdd, maxBucketSize);
        bucket.lastRefillTimestamp = currentTime;
    }
    
    // Step 4: Try to consume a token
    if (bucket.tokens > 0) {
        bucket.tokens--;
        return true;  // Request allowed
    }
    
    return false;  // Request blocked
}
```

### 🎓 Key Insights

1. **Burst Handling:** Can save up tokens for burst traffic
2. **Smooth Refill:** Tokens refill continuously, not in batches
3. **Memory Efficient:** Only stores token count and timestamp
4. **Use Case:** Perfect for APIs that allow occasional bursts (e.g., Stripe, AWS)

---

## 2. Leaky Bucket - Step by Step

### 🎯 Core Idea
Imagine a **bucket with a hole**:
- Water (requests) pours in from the top
- Water leaks out at a constant rate from the bottom
- If bucket overflows, water (requests) are lost
- Bucket has a maximum capacity

### 📖 Step-by-Step Example

**Configuration:**
- Bucket Capacity: 5 requests
- Leak Rate: 2 requests/second

**Timeline:**

```
Time: 0:00 (Empty bucket)
┌──────────────┐
│              │  Queue: []
│              │  Size: 0/5
└──────────────┘
     ↓↓ (leaking)

5 requests arrive instantly:
┌──────────────┐
│  Request 5   │
│  Request 4   │
│  Request 3   │  Queue: [R1, R2, R3, R4, R5]
│  Request 2   │  Size: 5/5 (FULL)
│  Request 1   │
└──────────────┘
     ↓↓
Request 6 arrives: ✗ BLOCKED (overflow!)

Time: 0:01 (1 second later)
Leaked: 2 requests processed
┌──────────────┐
│              │
│              │
│  Request 5   │  Queue: [R3, R4, R5]
│  Request 4   │  Size: 3/5
│  Request 3   │
└──────────────┘
     ↓↓
Request 6 arrives: ✓ ALLOWED (space available)

Time: 0:02 (2 seconds later)
Leaked: 4 more requests processed
┌──────────────┐
│              │
│              │
│              │  Queue: [R6]
│              │  Size: 1/5
│  Request 6   │
└──────────────┘
     ↓↓
```

### 💻 Code Walkthrough

```java
public boolean allowRequest(String userId) {
    long currentTime = System.currentTimeMillis();
    
    // Step 1: Get or create bucket for user
    Bucket bucket = userBuckets.computeIfAbsent(userId, 
        k -> new Bucket(currentTime));
    
    // Step 2: Calculate how many requests should leak
    long timeElapsed = currentTime - bucket.lastLeakTimestamp;
    int requestsToLeak = (int) ((timeElapsed / 1000.0) * leakRate);
    
    // Step 3: Remove (process) leaked requests
    for (int i = 0; i < requestsToLeak && !bucket.requestQueue.isEmpty(); i++) {
        bucket.requestQueue.poll();  // Remove oldest request
    }
    
    // Step 4: Update last leak time
    if (requestsToLeak > 0) {
        bucket.lastLeakTimestamp = currentTime;
    }
    
    // Step 5: Try to add new request
    if (bucket.requestQueue.size() < bucketCapacity) {
        bucket.requestQueue.offer(currentTime);
        return true;  // Request allowed
    }
    
    return false;  // Request blocked (overflow)
}
```

### 🎓 Key Insights

1. **Constant Output:** Processes requests at a fixed rate
2. **Smoothing:** Absorbs bursts and smooths them out
3. **Queue Required:** Needs to maintain a queue of requests
4. **Use Case:** Network packet scheduling, video streaming

---

## 3. Fixed Window Counter - Step by Step

### 🎯 Core Idea
Imagine a **daily visitor counter**:
- Counter starts at 0 at the beginning of the day
- Each visitor increments the counter
- Maximum 100 visitors per day
- Counter resets at midnight
- Problem: 100 at 11:59 PM + 100 at 12:00 AM = 200 in 1 minute!

### 📖 Step-by-Step Example

**Configuration:**
- Max Requests: 5
- Window Size: 60 seconds

**Timeline:**

```
Window 1: 0:00 - 1:00
┌─────────────────────────────────────┐
│ Time | Request | Counter | Status   │
├─────────────────────────────────────┤
│ 0:10 │    1    │   1/5   │ ✓ ALLOW  │
│ 0:15 │    2    │   2/5   │ ✓ ALLOW  │
│ 0:20 │    3    │   3/5   │ ✓ ALLOW  │
│ 0:30 │    4    │   4/5   │ ✓ ALLOW  │
│ 0:40 │    5    │   5/5   │ ✓ ALLOW  │
│ 0:50 │    6    │   5/5   │ ✗ BLOCK  │
│ 0:59 │    7    │   5/5   │ ✗ BLOCK  │
└─────────────────────────────────────┘

Window 2: 1:00 - 2:00 (Counter RESETS!)
┌─────────────────────────────────────┐
│ Time | Request | Counter | Status   │
├─────────────────────────────────────┤
│ 1:00 │    8    │   1/5   │ ✓ ALLOW  │
│ 1:01 │    9    │   2/5   │ ✓ ALLOW  │
│ 1:02 │   10    │   3/5   │ ✓ ALLOW  │
└─────────────────────────────────────┘

🚨 BOUNDARY PROBLEM:
Between 0:59 and 1:00 (1 second):
- At 0:59: Can send 5 requests (if counter was empty)
- At 1:00: Can send 5 requests (counter reset)
- Total: 10 requests in 1 second! (2x the limit)
```

### 💻 Code Walkthrough

```java
public boolean allowRequest(String userId) {
    long currentTime = System.currentTimeMillis();
    
    // Step 1: Calculate current window start time
    // Example: If time is 1:30 and window is 60s, window starts at 1:00
    long currentWindowStart = (currentTime / windowSizeMs) * windowSizeMs;
    
    // Step 2: Get or create window for user
    Window window = userWindows.computeIfAbsent(userId, 
        k -> new Window(0, currentWindowStart));
    
    // Step 3: Check if we're in a new window
    if (window.windowStart != currentWindowStart) {
        // Reset counter for new window
        window.count = 0;
        window.windowStart = currentWindowStart;
    }
    
    // Step 4: Check if under limit
    if (window.count < maxRequests) {
        window.count++;
        return true;  // Request allowed
    }
    
    return false;  // Request blocked
}
```

### 🎓 Key Insights

1. **Simplest Algorithm:** Very easy to implement
2. **Memory Efficient:** Just one counter per user
3. **Boundary Problem:** Can allow 2x limit at window edges
4. **Use Case:** Simple APIs, quick MVPs, non-critical rate limiting

---

## 4. Sliding Window Log - Step by Step

### 🎯 Core Idea
Think of a **rolling attendance sheet**:
- Keep timestamps of all attendees in the last hour
- For each new person, check timestamps
- Remove people who came more than 1 hour ago
- Count remaining people
- If count < limit, allow entry

### 📖 Step-by-Step Example

**Configuration:**
- Max Requests: 3
- Window Size: 60 seconds

**Timeline:**

```
Current Time: 12:00:00
Log: []
Request 1 at 12:00:00: ✓ ALLOWED
Log: [12:00:00]

Request 2 at 12:00:10: ✓ ALLOWED
Log: [12:00:00, 12:00:10]

Request 3 at 12:00:20: ✓ ALLOWED
Log: [12:00:00, 12:00:10, 12:00:20]

Request 4 at 12:00:30: ✗ BLOCKED (3 requests in window)
Log: [12:00:00, 12:00:10, 12:00:20]

Current Time: 12:00:50
Window Start: 11:59:50
Remove timestamps < 11:59:50:
Log: [12:00:00, 12:00:10, 12:00:20] (all still valid)

Current Time: 12:01:05
Window Start: 12:00:05
Remove timestamps < 12:00:05:
  - Remove 12:00:00 (too old)
Log: [12:00:10, 12:00:20] (2 requests remaining)

Request 5 at 12:01:05: ✓ ALLOWED
Log: [12:00:10, 12:00:20, 12:01:05]

Request 6 at 12:01:10: ✗ BLOCKED
Log: [12:00:10, 12:00:20, 12:01:05]

Current Time: 12:01:15
Window Start: 12:00:15
Remove timestamps < 12:00:15:
  - Remove 12:00:10 (too old)
Log: [12:00:20, 12:01:05]

Request 7 at 12:01:15: ✓ ALLOWED
Log: [12:00:20, 12:01:05, 12:01:15]
```

### 💻 Code Walkthrough

```java
public boolean allowRequest(String userId) {
    long currentTime = System.currentTimeMillis();
    
    // Step 1: Calculate sliding window start
    long windowStart = currentTime - windowSizeMs;
    
    // Step 2: Get or create request log for user
    Queue<Long> requestLog = userRequestLogs.computeIfAbsent(userId, 
        k -> new LinkedList<>());
    
    // Step 3: Remove timestamps outside the window
    while (!requestLog.isEmpty() && requestLog.peek() <= windowStart) {
        requestLog.poll();  // Remove old timestamps
    }
    
    // Step 4: Check if under limit
    if (requestLog.size() < maxRequests) {
        requestLog.offer(currentTime);  // Add current timestamp
        return true;  // Request allowed
    }
    
    return false;  // Request blocked
}
```

### 🎓 Key Insights

1. **Most Accurate:** No boundary issues
2. **High Memory:** Stores every single timestamp
3. **Precise Control:** Perfect for critical applications
4. **Use Case:** Financial APIs, payment gateways, critical systems

**Memory Analysis:**
```
Users: 1,000,000
Limit: 1,000 requests/minute per user
Memory: 1M users × 1K timestamps × 8 bytes = 8 GB!
```

---

## 5. Sliding Window Counter - Step by Step

### 🎯 Core Idea
**Best of both worlds:**
- Use two fixed windows (previous and current)
- Weight the previous window based on overlap
- More accurate than fixed window, less memory than log

### 📖 Step-by-Step Example

**Configuration:**
- Max Requests: 10
- Window Size: 60 seconds

**Scenario:**

```
Timeline:
Previous Window     Current Window
    (60s)              (60s)
┌───────────────┬───────────────┐
│               │               │
│  12:00-13:00  │  13:00-14:00  │
│               │       ↑       │
└───────────────┴───────┼───────┘
                        │
                  Current Time
                  13:00:45
                  (45s into window)

Previous Window (12:00-13:00): 8 requests
Current Window (13:00-14:00): 3 requests

Step 1: Calculate overlap percentage
- We're 45 seconds into current window
- Previous window overlap = (60 - 45) / 60 = 15 / 60 = 25%

Step 2: Calculate weighted count
Weighted Count = Current + (Previous × Overlap%)
               = 3 + (8 × 0.25)
               = 3 + 2
               = 5 requests

Step 3: Compare with limit
5 < 10 ✓ ALLOW new request

After allowing request:
Current Window Count: 4
Weighted Count: 4 + (8 × 0.25) = 6 requests
```

### 💻 Code Walkthrough

```java
public boolean allowRequest(String userId) {
    long currentTime = System.currentTimeMillis();
    
    // Step 1: Calculate current window start
    long currentWindowStart = (currentTime / windowSizeMs) * windowSizeMs;
    
    // Step 2: Get or create window data
    WindowData windowData = userWindows.computeIfAbsent(userId, 
        k -> new WindowData(0, 0, currentWindowStart));
    
    // Step 3: Check if we moved to a new window
    if (windowData.currentWindowStart != currentWindowStart) {
        // Slide window: current → previous
        windowData.previousWindowCount = windowData.currentWindowCount;
        windowData.currentWindowCount = 0;
        windowData.currentWindowStart = currentWindowStart;
    }
    
    // Step 4: Calculate time into current window
    long timeIntoCurrentWindow = currentTime - currentWindowStart;
    
    // Step 5: Calculate overlap percentage
    // How much of the previous window overlaps with our sliding window?
    double overlapPercentage = (windowSizeMs - timeIntoCurrentWindow) 
                               / (double) windowSizeMs;
    
    // Step 6: Calculate weighted count
    double weightedCount = windowData.currentWindowCount + 
                          (windowData.previousWindowCount * overlapPercentage);
    
    // Step 7: Check if under limit
    if (weightedCount < maxRequests) {
        windowData.currentWindowCount++;
        return true;  // Request allowed
    }
    
    return false;  // Request blocked
}
```

### 📊 Visual Formula Breakdown

```
Let's say:
- Window Size = 60 seconds
- Limit = 100 requests
- Current Time = 45 seconds into window

     Previous Window          Current Window
     (0-60s)                  (60-120s)
┌─────────────────────┬─────────────────────┐
│ ← 15s overlap →│                     │
│ 80 requests     │ 30 requests         │
│                 │         ↑           │
└─────────────────────┴─────────┼───────────┘
                                │
                         Current Time (105s)
                         45s into window

Overlap = 15s / 60s = 0.25 (25%)

Weighted = 30 + (80 × 0.25)
         = 30 + 20
         = 50 requests

50 < 100 ✓ Request ALLOWED
```

### 🎓 Key Insights

1. **Balanced Approach:** Better than fixed window, efficient than log
2. **Production Ready:** Used by Cloudflare, Kong
3. **Good Approximation:** ~95% accuracy in practice
4. **Use Case:** High-traffic production APIs

---

## 6. When to Use Which Algorithm

### Decision Matrix

```
┌────────────────────────────────────────────────────────────────┐
│                    ALGORITHM SELECTOR                          │
├────────────────────────────────────────────────────────────────┤
│                                                                │
│  Need to handle bursts?                                        │
│  ┌─ YES → Token Bucket                                         │
│  └─ NO  → Continue                                             │
│                                                                │
│  Need constant output rate?                                    │
│  ┌─ YES → Leaky Bucket                                         │
│  └─ NO  → Continue                                             │
│                                                                │
│  Need 100% accuracy?                                           │
│  ┌─ YES → Can handle memory? → YES → Sliding Window Log       │
│  │                           → NO  → Sliding Window Counter    │
│  └─ NO  → Continue                                             │
│                                                                │
│  Simple MVP / prototype?                                       │
│  ┌─ YES → Fixed Window Counter                                │
│  └─ NO  → Sliding Window Counter (default choice)             │
│                                                                │
└────────────────────────────────────────────────────────────────┘
```

### Use Case Table

| Scenario | Best Algorithm | Reason |
|----------|----------------|--------|
| **Social Media API** | Token Bucket | Allows burst of posts/likes |
| **Payment Gateway** | Sliding Window Log | Accuracy is critical |
| **Video Streaming** | Leaky Bucket | Constant bitrate needed |
| **Public API** | Sliding Window Counter | Balance of all factors |
| **Internal Microservice** | Fixed Window | Simple, low overhead |
| **DDoS Protection** | Token Bucket | Fast burst detection |
| **Database Queries** | Leaky Bucket | Smooth query rate |
| **Newsletter Service** | Fixed Window | Simple daily/hourly limits |
| **Financial Transactions** | Sliding Window Log | Regulatory compliance |
| **CDN Rate Limiting** | Sliding Window Counter | High scale, good accuracy |

---

## 🎓 Summary

### Quick Reference

```
Token Bucket ─────► Best for: APIs allowing bursts
    └─ Memory: ⭐⭐⭐⭐⭐ (Low)
    └─ Accuracy: ⭐⭐⭐⭐ (Good)
    └─ Examples: AWS, Stripe, GitHub

Leaky Bucket ─────► Best for: Constant output rate
    └─ Memory: ⭐⭐⭐ (Medium)
    └─ Accuracy: ⭐⭐⭐⭐⭐ (Perfect)
    └─ Examples: Network QoS, Video streaming

Fixed Window ─────► Best for: Simple cases
    └─ Memory: ⭐⭐⭐⭐⭐ (Lowest)
    └─ Accuracy: ⭐⭐ (Poor at boundaries)
    └─ Examples: Simple APIs, Prototypes

Sliding Log ──────► Best for: Critical accuracy
    └─ Memory: ⭐ (Highest)
    └─ Accuracy: ⭐⭐⭐⭐⭐ (Perfect)
    └─ Examples: Payments, Financial

Sliding Counter ──► Best for: Production APIs
    └─ Memory: ⭐⭐⭐⭐ (Low)
    └─ Accuracy: ⭐⭐⭐⭐ (Very Good)
    └─ Examples: Cloudflare, Kong, Production
```

### Learning Path

1. **Start:** Understand Fixed Window (simplest concept)
2. **Then:** Learn Token Bucket (refill logic)
3. **Next:** Study Sliding Window Log (precision)
4. **Finally:** Master Sliding Window Counter (production-ready)
5. **Advanced:** Leaky Bucket (specialized use cases)

---

**Happy Learning! 🚀**

Remember: The best algorithm depends on your specific requirements. There's no one-size-fits-all solution!

