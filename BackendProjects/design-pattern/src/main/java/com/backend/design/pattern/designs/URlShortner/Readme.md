# URL Shortener - System Design (Like Bitly)

> Design a URL shortener where a long URL converts to a short URL and clicking it redirects the user to the original location.

---

## Table of Contents
1. [Functional Requirements](#functional-requirements)
2. [Non-Functional Requirements](#non-functional-requirements)
3. [Capacity Estimation](#capacity-estimation)
4. [Identifying Entities](#identifying-entities)
5. [API Design](#api-design)
6. [User Flow](#user-flow)
7. [High-Level Design (HLD)](#high-level-design-hld)
8. [Low-Level Design (LLD)](#low-level-design-lld)
9. [Database Design](#database-design)
10. [Deep Dives](#deep-dives)
11. [Interview Questions & Answers](#interview-questions--answers)

---

## Functional Requirements

1. **Create Short URL from Long URL**
   - Optional: Support custom short URL (e.g. `/my-link`)
   - Optional: Support expiration date (premium users only)

2. **Redirect Short URL → Original Long URL**
   - Clicking/pasting the short URL should redirect to original location

> **Interviewer might ask**: "Should we support analytics — e.g., how many times a link was clicked?" Scope it as optional/premium.

---

## Non-Functional Requirements

| Requirement | Detail |
|-------------|--------|
| **Low Latency** | Redirect must happen within **200ms** |
| **Scale** | **100M Daily Active Users** → ~1157 requests/second |
| **Uniqueness** | Every short URL must be globally unique |
| **CAP Theorem** | **AP (Availability > Consistency)** — stale data briefly is OK, downtime is not |
| **Durability** | Short URLs should not be lost |
| **Read Heavy** | Read:Write ratio is roughly **100:1** (people click more than they create) |

---

## Capacity Estimation

```
Daily Active Users: 100M

Write (URL creation):
├─ Assume 10% of users create 1 link/day
├─ 10M writes/day
├─ 10M / 86400 sec ≈ 115 writes/sec

Read (URL redirects):
├─ Read:Write = 100:1
├─ 1B reads/day
├─ 1B / 86400 ≈ 11,574 reads/sec (peak: ~30K/sec)

Storage:
├─ Each URL entry ≈ 500 bytes
├─ 10M writes/day × 365 days × 5 years = 18.25B URLs
├─ 18.25B × 500 bytes ≈ ~9 TB

Short URL Length:
├─ Base62 (a-z, A-Z, 0-9) = 62 characters
├─ 7 characters = 62^7 = 3.5 Trillion unique URLs
└─ Enough for billions of users
```

> **Interviewer Question**: "Why Base62 and not Base64?"
> **Answer**: Base64 uses `+` and `/` which are special characters in URLs and cause issues. Base62 (alphanumeric only) is URL-safe.

---

## Identifying Entities

```
1. URL
   ├─ long_url
   ├─ short_url (hash)
   ├─ user_id (who created it)
   ├─ created_at
   └─ expires_at (optional, for premium users)

2. User
   ├─ user_id
   ├─ email
   └─ is_premium (to unlock expiry + custom URL)
```

> **Interviewer Question**: "Do we need a User entity?"
> **Answer**: Depends on scope. For anonymous shortening (like TinyURL), no. For Bitly (account features, history, analytics), yes.

---

## API Design

### 1. Create Short URL
```
POST /v1/urls

Request Body:
{
  "long_url": "https://very-long-website.com/some/deep/path?q=1",
  "custom_alias": "my-link",       // optional (premium)
  "expires_at": "2025-12-31"       // optional (premium)
}

Response (201 Created):
{
  "short_url": "https://short.ly/xK92mP",
  "long_url": "https://very-long-website.com/...",
  "expires_at": "2025-12-31",
  "created_at": "2024-01-01"
}
```

### 2. Redirect Short URL
```
GET /v1/{short_code}      e.g. GET /v1/xK92mP

Response:
  HTTP 301 or 302 → Location: https://very-long-website.com/...
```

### 3. (Optional) Delete URL
```
DELETE /v1/urls/{short_code}

Response:
  HTTP 204 No Content
```

> **Interviewer Question**: "Should this be a GET or POST for creation?"
> **Answer**: POST. GET should be idempotent (no side effects). Creating a URL changes state → POST.

> **Interviewer Question**: "What HTTP status code for redirect?"
> **Answer**: 301 or 302 (discussed in depth below).

---

## User Flow

```
CREATE:
1. User opens Bitly
2. Enters long URL → POST /v1/urls
3. Backend generates 7-char hash
4. Saves (hash → long_url) to DB
5. Returns short URL to user (within 200ms)

REDIRECT:
1. User pastes short URL in browser tab
2. Browser hits → GET /v1/xK92mP
3. Backend looks up xK92mP in cache (Redis)
4. Cache hit → returns long URL
5. Cache miss → hits DB, updates cache, returns long URL
6. Backend responds with HTTP 301/302
7. Browser redirects user to original URL
```

---

## High-Level Design (HLD)

```
                         CREATE FLOW
                         ──────────
User → Load Balancer → App Server (generate hash) → Save to DB
                                                   → Cache in Redis

                         REDIRECT FLOW
                         ─────────────
User → Load Balancer → App Server → Redis Cache ──(hit)──→ Return long URL
                                              │
                                              └──(miss)──→ DB → Update Cache → Return long URL

─────────────────────────────────────────────────────────────
                      FULL ARCHITECTURE
─────────────────────────────────────────────────────────────

               ┌─────────────────────────────────┐
               │            Client               │
               └──────────────┬──────────────────┘
                              │
               ┌──────────────▼──────────────────┐
               │         Load Balancer           │
               │       (Round-Robin)             │
               └──────┬─────────────┬────────────┘
                      │             │
               ┌──────▼────┐ ┌──────▼────┐
               │App Server │ │App Server │   (multiple pods, horizontal scale)
               └──────┬────┘ └──────┬────┘
                      │             │
               ┌──────▼─────────────▼──────────────┐
               │         Redis Cache Cluster        │
               │   (Primary + Replica for HA)       │
               └──────────────┬────────────────────┘
                              │ (cache miss)
               ┌──────────────▼────────────────────┐
               │          Database (DB)             │
               │    (NoSQL: Cassandra / DynamoDB)   │
               └───────────────────────────────────┘
```

---

## Low-Level Design (LLD)

### How to Generate a Unique Short URL?

#### ❌ Option 1: Encryption / Hashing (MD5, SHA256)

```
longURL → MD5 Hash → Take first 6 chars

PROS:
├─ Simple, stateless
└─ Same long URL always gives same hash

CONS:
├─ MD5 produces long hex string → not short
├─ Collision: Two different long URLs can produce same 6-char prefix
├─ Race condition: Two servers compute same hash simultaneously
└─ Hard to guarantee uniqueness at scale
```

> **Interviewer Question**: "What's a collision and why is it a problem here?"
> **Answer**: Two different long URLs generating the same short code. This would mean the short URL redirects to the wrong destination — a correctness bug.

---

#### ✅ Option 2: Snowflake ID → Base62 Encode

```
Step 1: Generate globally unique 64-bit integer (Snowflake ID)
        ┌──────────────────────────────────────────────────────┐
        │ 41 bits timestamp │ 10 bits machine ID │ 12 bits seq │
        └──────────────────────────────────────────────────────┘

Step 2: Convert that integer to Base62
        62^7 = 3.5 Trillion unique short URLs

PROS:
├─ Globally unique (no collision)
├─ No DB lookup needed to check uniqueness
├─ Sortable by time (Snowflake includes timestamp)
└─ Distributed: Each machine generates its own IDs

CONS:
└─ Needs each app server to have unique machine ID
```

---

#### ✅ Option 3: Counter-Based (Centralized ID Generator)

```
Approach:
├─ Maintain a global atomic counter
├─ Each URL creation gets next counter value
├─ Convert counter to Base62 (7 chars)
└─ Guaranteed unique

Problem: Single counter = single point of failure

Solution: Range-based distributed counters
├─ Server 1 gets range: 1 - 1,000,000
├─ Server 2 gets range: 1,000,001 - 2,000,000
├─ Each server uses its own range (no coordination)
└─ When exhausted: request next range from coordinator (Zookeeper)
```

---

#### ✅ Option 4: Redis INCR (Atomic Counter)

```
Redis INCR atomically returns next unique integer
→ Convert to Base62
→ Guaranteed unique, fast, no collisions

PROS:
├─ Atomic (no race conditions)
├─ Fast (in-memory)
└─ Simple

CONS:
├─ Redis becomes critical dependency
├─ If Redis fails → system fails
└─ Solution: Redis Cluster (master + replicas)
```

> **Interviewer Question**: "What happens if Redis fails?"
> **Answer**: Use Redis Cluster with master/replica. If master fails, a replica is promoted. During brief failover: fall back to DB-based counter or pre-allocated ranges.

---

### 301 vs 302 Redirect

| | HTTP 301 (Permanent) | HTTP 302 (Temporary) |
|--|---------------------|---------------------|
| **Meaning** | URL permanently moved | URL temporarily moved |
| **Browser Caching** | Browser caches redirect forever | No caching |
| **Subsequent Requests** | Browser skips your server | Every request goes to your server |
| **Analytics** | ❌ Can't track repeat clicks | ✅ Can track every click |
| **Server Load** | ✅ Low (browser handles) | ❌ Higher (every click hits server) |

**Bitly uses 301 by default** (performance), but **302 if analytics is needed**.

> **Interviewer Question**: "If we use 301, what's the problem?"
> **Answer**: After the first redirect, the browser caches it and never hits your server again for that URL. You lose all click analytics.

---

### Redis Cache Layer

```
Cache Strategy: Cache-Aside (Lazy Loading)

REDIRECT FLOW:
1. App Server → Check Redis (hash → long_url)
2. Cache Hit → Return long_url immediately (fast path)
3. Cache Miss:
   a. Query DB for long_url
   b. Store in Redis with TTL
   c. Return long_url

Cache Eviction: LRU (Least Recently Used)
├─ Hot URLs stay cached
└─ Cold URLs evicted when memory fills

TTL:
├─ Non-expiring URLs: Cache for 24 hours (refresh on access)
└─ Expiring URLs: TTL = expiration_time - now
```

> **Interviewer Question**: "What cache eviction policy would you use?"
> **Answer**: LRU — we want frequently clicked URLs to stay in cache. The 80/20 rule applies: 20% of URLs get 80% of traffic.

---

### Handling Custom URLs

```
POST /v1/urls
{
  "long_url": "...",
  "custom_alias": "my-sale"    // premium feature
}

Validation:
├─ Check if "my-sale" is already taken (DB lookup)
├─ If taken → 409 Conflict
└─ If free → save it

DB:
├─ custom_aliases table (or unique constraint on short_code)
└─ Flag to mark as "custom" vs "generated"
```

---

### Handling Expiration

```
On URL Creation:
├─ Store expires_at in DB
└─ If Redis-based TTL: Set TTL = (expires_at - now) in seconds

On Redirect:
├─ Check if expires_at < NOW
├─ If expired → return 410 Gone (not 404)
└─ If valid → redirect normally

Background Cleanup Job:
├─ Periodic job (daily/hourly)
└─ DELETE FROM urls WHERE expires_at < NOW
```

---

## Database Design

### Schema

**urls table**
```sql
CREATE TABLE urls (
    short_code    VARCHAR(10)   PRIMARY KEY,
    long_url      TEXT          NOT NULL,
    user_id       VARCHAR(36),
    created_at    TIMESTAMP     DEFAULT NOW(),
    expires_at    TIMESTAMP,
    is_custom     BOOLEAN       DEFAULT FALSE,
    click_count   BIGINT        DEFAULT 0
);
```

**users table** (if account-based)
```sql
CREATE TABLE users (
    user_id       VARCHAR(36)   PRIMARY KEY,
    email         VARCHAR(255)  UNIQUE NOT NULL,
    is_premium    BOOLEAN       DEFAULT FALSE,
    created_at    TIMESTAMP     DEFAULT NOW()
);
```

### SQL vs NoSQL?

```
Our access pattern:
├─ Write: INSERT one row (hash → long_url)
├─ Read: GET by primary key (short_code)
└─ No complex joins or relational queries needed

Conclusion: NoSQL is a better fit

Choice: Cassandra or DynamoDB
├─ Key-value style access
├─ Horizontal scaling
├─ High availability (AP)
└─ Tunable consistency
```

> **Interviewer Question**: "Why not use a relational DB like PostgreSQL?"
> **Answer**: We only do key-value lookups (short_code → long_url). No joins. At 10M+ writes/day, we need horizontal scaling which NoSQL handles better. That said, for smaller scale, PostgreSQL works fine.

---

## Deep Dives

### 1. Handling Race Conditions (Duplicate Short Codes)

```
Problem: Two app servers generate same short_code at same time

Solutions:
├─ DB unique constraint (let DB reject duplicates, retry with new code)
├─ Redis INCR (atomic counter, guaranteed unique)
└─ Snowflake ID (machine ID + timestamp + sequence = no collision)

Best approach: Snowflake ID or Range-based counters
```

### 2. Scaling Reads (11,574 reads/sec)

```
Read-heavy system (100:1 read:write)

Strategy:
├─ Redis cache (handles 99% of reads)
├─ DB only for cache misses
├─ Multiple read replicas for DB
└─ CDN for redirect responses (edge caching)

With Redis:
├─ Redis handles 100K+ ops/sec easily
└─ DB sees only ~1% of traffic (cache misses)
```

### 3. URL Validation

```
Before creating short URL:
├─ Is the URL valid? (regex check)
├─ Is the URL reachable? (optional HTTP HEAD request)
├─ Is it a malicious/phishing URL? (check against blocklist)
└─ Is it already in our system? (deduplication)

Deduplication:
├─ Store hash(long_url) in DB
├─ Before creating new short code: check if hash exists
└─ If exists: return existing short code (no duplicates)
```

### 4. Security

```
Rate Limiting:
├─ 10 URL creations per IP per minute
├─ Use Redis sliding window counter
└─ Prevent spam/abuse

Malicious URL Blocking:
├─ Check against Google Safe Browsing API
└─ Maintain internal blocklist
```

---

## Interview Questions & Answers

---

### Level 1: Basic Understanding

**Q: Why do we need a URL shortener? Why not just use the long URL?**
> Long URLs break in emails, SMS, social media. Short URLs are trackable, cleaner, and easier to share. Analytics on click counts is a business value.

---

**Q: What's the core technical challenge here?**
> Generating a globally unique 7-character code at high throughput (115 writes/sec) without collisions, and serving 11,574 redirects/sec with <200ms latency.

---

**Q: How many unique short URLs can you generate with 7 Base62 characters?**
> 62^7 = **3.5 Trillion**. At 10M URLs/day, that's enough for ~958 years.

---

### Level 2: Architecture

**Q: Why is this system read-heavy and how does that affect your design?**
> Read:Write ≈ 100:1 — people click links far more than they create them. So we optimize reads with Redis caching and DB read replicas. Write path can afford slightly more latency (async replication is OK).

---

**Q: You said AP over CP. What consistency issue could arise?**
> If a user creates a URL and immediately shares it, and another region hasn't synced yet, someone clicking early might get a "not found". This is a brief window (milliseconds to seconds). Acceptable trade-off for 99.99% availability.

---

**Q: What happens when the Redis cache goes down?**
> All requests fall through to the DB. DB must handle the full 11K reads/sec. It degrades in performance but doesn't fail (the DB can handle it with replicas). When Redis recovers, cache is lazily repopulated. For robustness, use Redis Cluster with automatic failover.

---

**Q: Why would you choose NoSQL over SQL here?**
> Access pattern is purely key-value (short_code → long_url). No joins, no transactions across rows. At 10M+ writes/day and billions of rows over 5 years, horizontal scaling of NoSQL (Cassandra/DynamoDB) is a better fit.

---

### Level 3: Deep Dives

**Q: Walk me through exactly what happens when a user clicks a short URL.**
```
1. Browser DNS resolves short.ly → IP address
2. Browser sends GET /xK92mP to Load Balancer
3. Load Balancer picks an App Server (round-robin)
4. App Server queries Redis: GET xK92mP
5a. Cache HIT: App Server gets long_url → returns HTTP 302, Location: <long_url>
5b. Cache MISS: App Server queries DB → finds long_url → writes to Redis → returns HTTP 302
6. Browser follows redirect → user lands on original page
Total time: <50ms (cache hit), <200ms (cache miss)
```

---

**Q: How do you handle hash collisions?**
> With Snowflake IDs or range-based counters, collisions are mathematically impossible because each server has a unique ID range. If using MD5/hash-based approach: check DB before saving, retry with different input on collision. Better: switch to counter-based approach.

---

**Q: What's the difference between 301 and 302 redirect, and which would you use?**
> 301 = permanent, browser caches it (good for performance, bad for analytics). 302 = temporary, browser always asks server (good for click tracking). If analytics is a product feature → 302. If performance is priority → 301.

---

**Q: How would you implement the expiration feature?**
> Store `expires_at` in DB and Redis TTL. On redirect: check if `expires_at < NOW` and return `410 Gone`. Background cron job daily deletes expired rows from DB. For Redis: TTL automatically evicts expired entries.

---

**Q: How would you add click analytics?**
```
On each redirect (async, non-blocking):
├─ Publish event to Kafka: { short_code, timestamp, ip, user_agent }
├─ Consumer reads Kafka → writes to analytics DB (ClickHouse / BigQuery)
└─ Dashboard queries analytics DB for counts, trends, geography

Key point: Analytics must be ASYNC — don't slow down the 200ms redirect
           with synchronous DB writes to analytics table.
```

---

**Q: How would you scale this to 10x (1 Billion DAU)?**
```
Current bottlenecks at 10x:
├─ Load Balancer → Add more LB nodes (AWS ALB auto-scales)
├─ App Servers → Horizontal pod scaling (Kubernetes HPA)
├─ Redis → Redis Cluster (shard by short_code hash)
├─ Database → Cassandra with more nodes (auto-sharding)
└─ ID Generation → Distributed Snowflake nodes per region

Geographic scaling:
├─ Deploy in multiple regions (US, EU, APAC)
├─ DNS-based routing to nearest region
└─ Eventual consistency across regions (AP system)
```

---

**Q: What if the same long URL is submitted by two users — do you create two short codes or reuse one?**
> Design decision:
> - **Reuse** (deduplication): Store `hash(long_url)` in DB. If same hash exists, return existing short code. Saves storage, but different users share the same link (privacy issue).
> - **Always create new**: Each user gets their own short code. More storage, but private. Bitly creates new URLs per user — two people shortening the same link get different short codes because analytics are per-user.

---

**Q: How do you prevent someone from creating millions of URLs to abuse the system?**
```
Rate Limiting:
├─ Per IP: 10 URL creations per minute (Redis sliding window)
├─ Per User (authenticated): 1000/day free, unlimited premium
├─ Block known abusive IPs/ranges
└─ CAPTCHA for anonymous requests after threshold
```

---

## What You Had Right ✅ + What Was Missing ❌

| Topic | You Had | Missing / Added |
|-------|---------|-----------------|
| Functional Requirements | ✅ Create + Redirect | ✅ Added Delete, Analytics |
| Non-Functional | ✅ Latency, Scale, CAP | ✅ Added Read:Write ratio, Durability |
| Capacity Estimation | ✅ 1157 req/sec | ✅ Added storage, URL length math |
| Entities | ✅ URL, User | ✅ Added detailed schema |
| APIs | ✅ POST + GET | ✅ Added request/response payloads |
| HLD | ✅ LB + Backend + DB + Cache | ✅ Added detailed architecture diagram |
| Short URL Generation | ✅ Encryption (bad), Snowflake | ✅ Added Base62, range counters, Redis INCR |
| Redis | ✅ Cache + Cluster failover | ✅ Added LRU, TTL, cache-aside pattern |
| 301 vs 302 | ✅ Status codes | ✅ Added analytics trade-off explanation |
| Custom URL | ✅ Mentioned | ✅ Added collision handling, 409 response |
| Expiration | ✅ Mentioned | ✅ Added TTL, 410 Gone, cleanup job |
| DB Choice | ❌ Not discussed | ✅ Added SQL vs NoSQL reasoning |
| Race Conditions | ❌ Not discussed | ✅ Added collision handling strategies |
| URL Validation | ❌ Not discussed | ✅ Added regex, blocklist, dedup |
| Analytics | ❌ Not discussed | ✅ Added Kafka async pattern |
| Security / Rate Limiting | ❌ Not discussed | ✅ Added per-IP rate limiting |
