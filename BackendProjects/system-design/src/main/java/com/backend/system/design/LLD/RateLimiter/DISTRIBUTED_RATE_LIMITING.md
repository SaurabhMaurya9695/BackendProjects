# Distributed Rate Limiting

When you have multiple servers, you need to implement **Distributed Rate Limiting** to share rate limit state across all servers.

---

## 🌐 The Problem

### Single Server (Simple)
```
              User
               ↓
         [API Server]
         RateLimiter
         (In-Memory)
```
✅ Works fine - all requests go to one server

### Multiple Servers (Problem!)
```
              User
               ↓
        [Load Balancer]
           /    |    \
          /     |     \
    [Server1][Server2][Server3]
    Limiter   Limiter   Limiter
    (Memory)  (Memory)  (Memory)
```
❌ Each server has its own counter!
- User can send 100 req/min to Server1
- Another 100 req/min to Server2
- Another 100 req/min to Server3
- **Total: 300 req/min instead of 100!**

---

## 🛠️ Solution: Centralized Store

### Architecture
```
              User
               ↓
        [Load Balancer]
           /    |    \
          /     |     \
    [Server1][Server2][Server3]
          \     |     /
           \    |    /
         [Redis Cluster]
         (Shared State)
```

All servers share the same rate limit counters in Redis!

---

## 📝 Implementation with Redis

### 1. Fixed Window Counter with Redis

```java
package com.backend.system.design.LLD.RateLimiter.distributed;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Distributed Fixed Window Counter using Redis
 */
public class RedisFixedWindowRateLimiter {
    
    private final JedisPool jedisPool;
    private final int maxRequests;
    private final int windowSizeSeconds;
    
    public RedisFixedWindowRateLimiter(String redisHost, int redisPort,
                                       int maxRequests, int windowSizeSeconds) {
        this.jedisPool = new JedisPool(redisHost, redisPort);
        this.maxRequests = maxRequests;
        this.windowSizeSeconds = windowSizeSeconds;
    }
    
    public boolean allowRequest(String userId) {
        try (Jedis jedis = jedisPool.getResource()) {
            long currentTime = System.currentTimeMillis();
            long windowStart = (currentTime / (windowSizeSeconds * 1000)) 
                              * (windowSizeSeconds * 1000);
            
            // Redis key: rate_limit:{userId}:{windowStart}
            String key = String.format("rate_limit:%s:%d", userId, windowStart);
            
            // Increment counter
            Long currentCount = jedis.incr(key);
            
            // Set expiry on first request in window
            if (currentCount == 1) {
                jedis.expire(key, windowSizeSeconds);
            }
            
            // Check if under limit
            return currentCount <= maxRequests;
        }
    }
}
```

**How it works:**
```
Request 1:
  Redis: INCR rate_limit:user123:1670000000
  Redis: EXPIRE rate_limit:user123:1670000000 60
  Return: 1 <= 100? ✓ YES

Request 2:
  Redis: INCR rate_limit:user123:1670000000
  Return: 2 <= 100? ✓ YES

Request 101:
  Redis: INCR rate_limit:user123:1670000000
  Return: 101 <= 100? ✗ NO
```

---

### 2. Token Bucket with Redis

```java
package com.backend.system.design.LLD.RateLimiter.distributed;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Distributed Token Bucket using Redis
 * Uses Redis Hash to store tokens and timestamp
 */
public class RedisTokenBucketRateLimiter {
    
    private final JedisPool jedisPool;
    private final int maxTokens;
    private final int refillRate;
    
    public RedisTokenBucketRateLimiter(String redisHost, int redisPort,
                                       int maxTokens, int refillRate) {
        this.jedisPool = new JedisPool(redisHost, redisPort);
        this.maxTokens = maxTokens;
        this.refillRate = refillRate;
    }
    
    public boolean allowRequest(String userId) {
        try (Jedis jedis = jedisPool.getResource()) {
            String key = "token_bucket:" + userId;
            long currentTime = System.currentTimeMillis();
            
            // Get current bucket state
            String tokensStr = jedis.hget(key, "tokens");
            String lastRefillStr = jedis.hget(key, "lastRefill");
            
            // Initialize if doesn't exist
            int tokens = (tokensStr != null) ? Integer.parseInt(tokensStr) : maxTokens;
            long lastRefill = (lastRefillStr != null) ? Long.parseLong(lastRefillStr) : currentTime;
            
            // Refill tokens
            long timeElapsed = currentTime - lastRefill;
            int tokensToAdd = (int) ((timeElapsed / 1000.0) * refillRate);
            
            if (tokensToAdd > 0) {
                tokens = Math.min(tokens + tokensToAdd, maxTokens);
                lastRefill = currentTime;
            }
            
            // Try to consume token
            if (tokens > 0) {
                tokens--;
                
                // Update Redis
                jedis.hset(key, "tokens", String.valueOf(tokens));
                jedis.hset(key, "lastRefill", String.valueOf(lastRefill));
                jedis.expire(key, 3600); // Expire after 1 hour of inactivity
                
                return true;
            }
            
            return false;
        }
    }
}
```

---

### 3. Sliding Window Log with Redis (Sorted Sets)

```java
package com.backend.system.design.LLD.RateLimiter.distributed;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Distributed Sliding Window Log using Redis Sorted Sets
 * Scores = timestamps
 */
public class RedisSlidingWindowLogRateLimiter {
    
    private final JedisPool jedisPool;
    private final int maxRequests;
    private final int windowSizeSeconds;
    
    public RedisSlidingWindowLogRateLimiter(String redisHost, int redisPort,
                                            int maxRequests, int windowSizeSeconds) {
        this.jedisPool = new JedisPool(redisHost, redisPort);
        this.maxRequests = maxRequests;
        this.windowSizeSeconds = windowSizeSeconds;
    }
    
    public boolean allowRequest(String userId) {
        try (Jedis jedis = jedisPool.getResource()) {
            String key = "sliding_log:" + userId;
            long currentTime = System.currentTimeMillis();
            long windowStart = currentTime - (windowSizeSeconds * 1000);
            
            // Remove old timestamps
            jedis.zremrangeByScore(key, 0, windowStart);
            
            // Count current requests in window
            long currentCount = jedis.zcard(key);
            
            // Check if under limit
            if (currentCount < maxRequests) {
                // Add current timestamp
                jedis.zadd(key, currentTime, String.valueOf(currentTime));
                jedis.expire(key, windowSizeSeconds);
                return true;
            }
            
            return false;
        }
    }
}
```

**Redis Commands:**
```redis
# Add timestamp
ZADD sliding_log:user123 1670000045123 "1670000045123"

# Remove old timestamps (older than 60 seconds ago)
ZREMRANGEBYSCORE sliding_log:user123 0 1670000000000

# Count requests in window
ZCARD sliding_log:user123

# Set expiry
EXPIRE sliding_log:user123 60
```

---

### 4. Sliding Window Counter with Redis (Lua Script)

```java
package com.backend.system.design.LLD.RateLimiter.distributed;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Distributed Sliding Window Counter using Redis with Lua Script
 * Lua script ensures atomic operations
 */
public class RedisSlidingWindowCounterRateLimiter {
    
    private final JedisPool jedisPool;
    private final int maxRequests;
    private final int windowSizeSeconds;
    
    // Lua script for atomic sliding window counter
    private static final String LUA_SCRIPT = 
        "local current_key = KEYS[1] " +
        "local previous_key = KEYS[2] " +
        "local max_requests = tonumber(ARGV[1]) " +
        "local window_size = tonumber(ARGV[2]) " +
        "local current_time = tonumber(ARGV[3]) " +
        "local window_start = current_time - window_size " +
        
        "local current_count = tonumber(redis.call('GET', current_key) or '0') " +
        "local previous_count = tonumber(redis.call('GET', previous_key) or '0') " +
        
        "local time_in_window = current_time % window_size " +
        "local overlap_percentage = (window_size - time_in_window) / window_size " +
        "local weighted_count = current_count + (previous_count * overlap_percentage) " +
        
        "if weighted_count < max_requests then " +
        "    redis.call('INCR', current_key) " +
        "    redis.call('EXPIRE', current_key, window_size * 2) " +
        "    return 1 " +
        "else " +
        "    return 0 " +
        "end";
    
    public RedisSlidingWindowCounterRateLimiter(String redisHost, int redisPort,
                                                int maxRequests, int windowSizeSeconds) {
        this.jedisPool = new JedisPool(redisHost, redisPort);
        this.maxRequests = maxRequests;
        this.windowSizeSeconds = windowSizeSeconds;
    }
    
    public boolean allowRequest(String userId) {
        try (Jedis jedis = jedisPool.getResource()) {
            long currentTime = System.currentTimeMillis();
            long windowSize = windowSizeSeconds * 1000L;
            long currentWindowStart = (currentTime / windowSize) * windowSize;
            long previousWindowStart = currentWindowStart - windowSize;
            
            String currentKey = String.format("sliding:%s:%d", userId, currentWindowStart);
            String previousKey = String.format("sliding:%s:%d", userId, previousWindowStart);
            
            // Execute Lua script atomically
            Object result = jedis.eval(
                LUA_SCRIPT,
                2,  // number of keys
                currentKey,
                previousKey,
                String.valueOf(maxRequests),
                String.valueOf(windowSize),
                String.valueOf(currentTime)
            );
            
            return result.equals(1L);
        }
    }
}
```

---

## 🔍 Comparison of Redis Approaches

| Approach | Redis Commands | Atomic? | Complexity | Performance |
|----------|---------------|---------|------------|-------------|
| **Fixed Window** | INCR, EXPIRE | ✅ Yes | ⭐ Simple | ⭐⭐⭐⭐⭐ Fast |
| **Token Bucket** | HSET, HGET | ❌ No* | ⭐⭐⭐ Medium | ⭐⭐⭐⭐ Good |
| **Sliding Log** | ZADD, ZCARD | ❌ No* | ⭐⭐⭐⭐ Complex | ⭐⭐⭐ Okay |
| **Sliding Counter** | Lua Script | ✅ Yes | ⭐⭐⭐⭐⭐ Complex | ⭐⭐⭐⭐ Good |

*Can be made atomic using Lua scripts or Redis transactions

---

## 🎯 Best Practices

### 1. **Use Redis Cluster for High Availability**

```java
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.HostAndPort;

Set<HostAndPort> nodes = new HashSet<>();
nodes.add(new HostAndPort("redis1", 6379));
nodes.add(new HostAndPort("redis2", 6379));
nodes.add(new HostAndPort("redis3", 6379));

JedisCluster cluster = new JedisCluster(nodes);
```

### 2. **Handle Redis Failures Gracefully**

```java
public boolean allowRequest(String userId) {
    try {
        // Try Redis
        return checkRateLimitInRedis(userId);
    } catch (JedisConnectionException e) {
        // Fallback: Allow request but log error
        logger.error("Redis connection failed, allowing request", e);
        return true; // Or use in-memory fallback
    }
}
```

### 3. **Use Lua Scripts for Atomicity**

```java
// BAD: Race condition possible
long count = jedis.incr(key);
if (count == 1) {
    jedis.expire(key, 60); // Another request might happen here!
}

// GOOD: Atomic operation with Lua
String luaScript = 
    "local count = redis.call('INCR', KEYS[1]) " +
    "if count == 1 then " +
    "    redis.call('EXPIRE', KEYS[1], ARGV[1]) " +
    "end " +
    "return count";
    
jedis.eval(luaScript, 1, key, "60");
```

### 4. **Monitor Redis Performance**

```java
// Add monitoring
long startTime = System.currentTimeMillis();
boolean result = rateLimiter.allowRequest(userId);
long duration = System.currentTimeMillis() - startTime;

if (duration > 100) {
    logger.warn("Redis rate limit check took {}ms", duration);
}
```

### 5. **Set Appropriate TTLs**

```java
// Clean up inactive users
jedis.expire(key, windowSizeSeconds * 2); // 2x window size

// For token bucket, use longer TTL
jedis.expire(key, 3600); // 1 hour for inactive users
```

---

## 📊 Architecture Patterns

### Pattern 1: Gateway Rate Limiting
```
         User
          ↓
    [API Gateway]
    Rate Limiter
          ↓
       [Redis]
          ↓
    [Microservices]
```
✅ Centralized control
✅ Easy to configure
❌ Single point of failure

### Pattern 2: Distributed Rate Limiting
```
    User → [Service A] → [Redis]
           ↓
    User → [Service B] → [Redis]
           ↓
    User → [Service C] → [Redis]
```
✅ Each service has control
✅ More flexible
❌ More complex

### Pattern 3: Hybrid
```
    User → [API Gateway] → [Redis Cluster]
                ↓
           [Service A] → [Local Cache]
           [Service B] → [Local Cache]
```
✅ Fast local checks
✅ Centralized limits
✅ Best performance

---

## 🚀 Production Considerations

### 1. **Connection Pooling**
```java
JedisPool pool = new JedisPool(
    new JedisPoolConfig(),
    "redis-host",
    6379,
    2000,  // connection timeout
    "password",
    0,     // database
    "client-name",
    false, // SSL
    null,
    null,
    null
);
```

### 2. **Redis Key Naming Convention**
```
rate_limit:{algorithm}:{userId}:{window}

Examples:
- rate_limit:fixed:user123:1670000000
- rate_limit:token:user456
- rate_limit:sliding:user789:1670000000
```

### 3. **Monitoring Metrics**
- Request success rate
- Redis latency
- Rate limit hit rate
- Memory usage

### 4. **Cost Optimization**
```java
// Use shorter keys to save memory
String key = "rl:" + userId; // Instead of "rate_limit:" + userId

// Batch operations when possible
Pipeline pipeline = jedis.pipelined();
pipeline.incr(key1);
pipeline.incr(key2);
pipeline.sync();
```

---

## 🎓 Summary

**For Production Systems, Recommended Approach:**

1. **API Gateway:** Redis Sliding Window Counter with Lua scripts
2. **Microservices:** Local + Redis hybrid
3. **Critical APIs:** Redis Sliding Window Log
4. **High Scale:** Redis Cluster with connection pooling

**Redis Commands Cheatsheet:**
```redis
# Fixed Window
INCR rate_limit:user123:window
EXPIRE rate_limit:user123:window 60

# Token Bucket
HSET bucket:user123 tokens 10
HGET bucket:user123 tokens

# Sliding Log
ZADD log:user123 timestamp timestamp
ZREMRANGEBYSCORE log:user123 0 old_timestamp
ZCARD log:user123

# Sliding Counter
EVAL lua_script 2 current_key previous_key args
```

---

**Happy Scaling! 🚀**

