# Database Sharding — 50+ Questions & Answers

Interview-style questions across all difficulty levels.

---

## 🟢 Beginner Level (Foundation)

### 1. What is database sharding?
**Answer:** Horizontal partitioning of data across multiple database servers. Instead of one large database, data is split by a shard key, with each shard holding a subset of data.

### 2. What's the difference between sharding and replication?
**Answer:**
- **Replication:** Copy entire dataset across multiple servers (read scaling)
- **Sharding:** Partition dataset across servers (write scaling, storage scaling)
- Replication = same data everywhere; Sharding = different data on each server

### 3. What is a shard key?
**Answer:** The column/field used to determine which shard a record belongs to.
```
Example: user_id is shard key
user_id=1 → Shard 1
user_id=2 → Shard 3
user_id=3 → Shard 2
```

### 4. When should you shard?
**Answer:** When single database hits limits:
- Data > 100GB
- QPS > 10,000
- Storage or CPU maxed out
- Read replicas not sufficient

### 5. Can you change the shard key after sharding?
**Answer:** **No, not easily.** Changing shard key requires resharding all data (expensive, risky). Choose carefully upfront.

### 6. What are the main sharding strategies?
**Answer:**
1. **Range-based** — Ranges of shard key → shards
2. **Hash-based** — hash(key) % num_shards → shard
3. **Directory-based** — Lookup table maps key → shard
4. **Consistent hashing** — Nodes on ring, key maps to nearest node

### 7. Which sharding strategy is most common?
**Answer:** **Hash-based** — Simple, even distribution, works for most cases.

### 8. What is a hot shard?
**Answer:** One shard becomes bottleneck while others are underutilized.
```
Shard 1: 80% traffic, 95% CPU → HOT
Shard 2: 10% traffic, 20% CPU
Shard 3: 10% traffic, 15% CPU
```
Caused by skewed data distribution.

### 9. Can you do ACID transactions across shards?
**Answer:** **No.** Traditional ACID doesn't work across shards. Solutions:
- Use saga pattern (distributed transactions)
- Accept eventual consistency
- Keep transaction within single shard

### 10. What's resharding?
**Answer:** Adding more shards when existing shards become full. Requires:
1. Adding new shards
2. Redistributing data (expensive, slow)
3. Updating routing logic
4. Validating consistency

---

## 🟡 Intermediate Level (Design & Trade-offs)

### 11. Compare range-based vs hash-based sharding.

**Answer:**

| Aspect | Range-Based | Hash-Based |
|--------|-------------|-----------|
| **Distribution** | Uneven (if data skewed) | Even (if hash good) |
| **Hot shards** | Likely | Unlikely |
| **Resharding cost** | Low | High (all keys rehash) |
| **Range queries** | Fast (same shard) | Slow (scattered) |
| **When to use** | Sequential IDs | Even distribution needed |

### 12. Explain the hot shard problem with an example.

**Answer:** If sharding by country with range-based:
```
Shard 1: USA (200M users)
Shard 2: Europe (50M users)
Shard 3: APAC (30M users)

Shard 1 becomes hot (4x traffic of others)
Solution: Use hash(user_id) instead of country
```

### 13. How do you design a good shard key?

**Answer:** Must have these properties:

1. **Immutable** — Never changes
2. **High cardinality** — Many unique values
3. **Evenly distributed** — No skew
4. **Used in most queries** — Avoids cross-shard queries

Example good shard key: `user_id`
Example bad shard key: `subscription_tier` (90% free users)

### 14. What are the challenges of cross-shard queries?

**Answer:**
```
Query: "Get top 10 spending users"

Solution:
1. Query all shards
2. Aggregate results in app
3. Sort and return top 10

Cost: Must touch all shards (slow)
```

### 15. How do you handle joins across shards?

**Answer:**
```
Normal (single shard):
SELECT u.*, o.* FROM users u 
JOIN orders o ON u.id = o.user_id

Cross-shard:
-- Not supported in SQL
-- Must do application-level join:
1. Get all users (query all shards)
2. Get all orders (query all shards)
3. Join in application code
4. Return results
```

### 16. What is directory-based sharding and when to use it?

**Answer:** 
Lookup table maps shard key to shard ID.

```
Directory Table:
user_id → shard_id
1 → 2
2 → 1
3 → 3
```

**When to use:**
- Frequent resharding
- Complex mapping logic
- Multi-tenant systems

**Trade-off:** Extra lookup query (slower) but easier to manage.

### 17. How does hash-based sharding handle resharding?

**Answer:**
```
Old: 4 shards
hash(user_id) % 4

New: 8 shards
hash(user_id) % 8

Result: ALL keys must be rehashed and moved
Cost: Expensive, requires copying all data

Example:
user_id=10
Old: hash(10) % 4 = 2 → Shard 2
New: hash(10) % 8 = 6 → Shard 6
User 10 must move from Shard 2 to Shard 6
```

### 18. What is consistent hashing and why use it?

**Answer:**
Hash keys and shards on a ring. Keys map to nearest shard on ring.

```
        Shard 1 (hash=0)
             |
    ---------+---------
   /                   \
  /                     \
Shard 4            Shard 2
(hash=300)        (hash=100)
```

**Advantage:** When resharding, only 1/N keys move (not all keys rehash).

**Disadvantage:** Complex to implement, can have uneven distribution.

### 19. Can you do cross-shard transactions?

**Answer:** Not natively. Solutions:

**Option 1: Saga Pattern**
```
Step 1: Debit Account A (Shard 1) ✓
Step 2: Credit Account B (Shard 2) ✓
If Step 2 fails, compensate Step 1 (refund)
```

**Option 2: Event-Driven**
```
Event: "Transfer from A to B"
1. Record event (Shard 1)
2. Process asynchronously
3. Update Shard 2
Decoupled, eventually consistent
```

### 20. How would you shard a user-follower relationship?

**Answer:** Two options:

**Option 1: Shard by user_id**
```
User 1 (Shard 1): has followers [2, 3, 5]
User 2 (Shard 2): has followers [1]

Problem: "Get all followers" requires querying all shards
```

**Option 2: Denormalize in cache**
```
Redis:
followers:user_1 = [2, 3, 5]
followers:user_2 = [1]
```

Best approach: Shard by user_id + cache for fast reads.

---

## 🔴 Advanced Level (System Design)

### 21. Design a sharding strategy for Twitter (500M users, 100K QPS).

**Answer:**

```
Shard Key: user_id
Strategy: Hash-based (user_id % 1024)
Shards: 1024 shards

Rationale:
- Even distribution (hash function)
- High cardinality (500M unique users)
- Immutable (user_id never changes)
- Used in most queries (timeline, tweets)

Data distribution:
- Each shard: ~500K users
- Each shard handles: ~100 QPS
- Total: 1024 × 100 = 102.4K QPS ✓
```

### 22. How would you handle uneven shard distribution?

**Answer:** Multiple solutions:

**Solution 1: Secondary Sharding**
```
Shard 1 is hot (80% traffic)
Split Shard 1 into:
- Shard 1a
- Shard 1b
- Shard 1c
```

**Solution 2: Dedicated Shard for Hot Key**
```
Celebrity user with 1M followers
Dedicated shard: celebrities
Normal users: regular shards
```

**Solution 3: Better Shard Key**
```
Old: country (skewed, 80% USA)
New: hash(user_id) (even)
```

### 23. Design resharding for a system scaling from 10 to 50 shards.

**Answer:**

**Phase 1: Preparation (Week 1)**
- Provision new hardware (50 servers)
- Set up replication from old to new shards
- Create backup

**Phase 2: Dual-Write (Week 2)**
- New shards ready, replication lag = 0
- Start dual-write: writes go to both old and new
- Reads still from old shards

**Phase 3: Validation (Week 3)**
- Compare data: old shards vs new shards
- Verify consistency
- Smoke test queries on new shards

**Phase 4: Cut-Over (Week 4)**
- Switch reads to new shards
- Stop writing to old shards
- Monitor for errors
- Decommission old shards after 1 week

**Tools:**
- Binlog replication
- Checksum tools (compare data)
- Dual-write middleware

### 24. What's the maximum QPS a single shard can handle?

**Answer:** Depends on:
- Database (MySQL, PostgreSQL, etc.)
- Query type (read vs write)
- Hardware (CPU, disk speed, memory)
- Indexes

**Typical:**
- MySQL: 5-10K QPS per server
- With SSD: 10-20K QPS
- With in-memory DB: 100K+ QPS

**Example:**
```
If single shard can handle 5K QPS
And total system needs 100K QPS
Then need: 100K / 5K = 20 shards
Add 25% buffer: 20 × 1.25 = 25 shards
```

### 25. How do you prevent hotspots in a sharded database?

**Answer:**

1. **Choose good shard key**
   - High cardinality
   - Even distribution
   - Avoid time-based, geography, or tier

2. **Monitor shard metrics**
   ```
   CPU, memory, disk usage per shard
   If one shard > 80% → problem
   ```

3. **Cache hot data**
   ```
   Popular user data → Redis
   Reduces shard load
   ```

4. **Replication per shard**
   ```
   Reads distributed across replicas
   Reduces primary load
   ```

5. **Periodic rebalancing**
   ```
   Analyze data distribution
   Move data to balance load
   Run during off-peak hours
   ```

### 26. Explain the tradeoff between sharding strategies.

**Answer:**

| Strategy | Pros | Cons | Best For |
|----------|------|------|----------|
| **Range** | Simple, range queries fast | Hot shards, uneven | Sequential IDs |
| **Hash** | Even distribution, simple | Expensive resharding | Random distribution |
| **Directory** | Flexible, easy resharding | Extra lookup, SPOF | Multi-tenant, frequent changes |
| **Consistent** | Minimal resharding | Complex, uneven possible | Elastic systems, caches |

### 27. How would you implement application-level routing?

**Answer:**

```java
public class ShardRouter {
    private int numShards = 10;
    private List<Database> shards = new ArrayList<>();
    
    public int getShardId(long userId) {
        return Math.abs(userId.hashCode()) % numShards;
    }
    
    public Result query(long userId, String sql) {
        int shardId = getShardId(userId);
        Database shard = shards.get(shardId);
        return shard.execute(sql);
    }
}

// Usage
ShardRouter router = new ShardRouter();
Result result = router.query(12345, "SELECT * FROM users WHERE id = 12345");
```

### 28. What happens if your shard key cardinality changes?

**Answer:** 

**Scenario:** Shard key was user_id, but you want to shard by (user_id, country).

**Solution:** Resharding required
```
Old: user_id % 10 → Shard ID
New: hash(user_id + country) % 10 → Shard ID

All data must be recalculated and moved
Expensive operation, requires downtime or complex dual-write
```

**Lesson:** Choose shard key carefully, don't change it.

### 29. How do you handle schema migrations with sharding?

**Answer:**

**Problem:** ALTER TABLE on all shards
```
ALTER TABLE users ADD COLUMN verified BOOLEAN DEFAULT FALSE;
This runs on Shard 1, 2, 3, ... sequentially or in parallel
```

**Approach:**

1. **Parallel execution** (with care)
   ```
   Run ALTER on all shards simultaneously
   Risk: If one fails, inconsistent state
   ```

2. **Rolling migration**
   ```
   Shard 1: ALTER TABLE (downtime on Shard 1 only)
   Shard 2: ALTER TABLE
   ... (sequential)
   
   Benefit: Only one shard down at a time
   Cost: Takes longer
   ```

3. **Online schema change tools**
   ```
   Percona pt-online-schema-change
   GitHub gh-ost
   No lock, no downtime
   ```

### 30. What's the relationship between sharding and microservices?

**Answer:**

**Microservices for separation:** Different teams own different services.
**Sharding for scale:** One service's database scales horizontally.

**Example:**
```
Order Service (microservice)
└─ Order Database (sharded by order_id)
   ├─ Shard 1
   ├─ Shard 2
   └─ Shard 3

User Service (microservice)
└─ User Database (sharded by user_id)
   ├─ Shard 1
   ├─ Shard 2
   └─ Shard 3
```

Both can be used together (not mutually exclusive).

---

## 🎯 Scenario-Based Questions

### 31. You're designing a payment system for 10M merchants. How would you shard?

**Answer:**
```
Shard Key: merchant_id
Strategy: Hash-based (merchant_id % 100)
Why: 
- High cardinality (10M merchants)
- Immutable (merchant_id never changes)
- Even distribution (hash function)
- Most queries by merchant_id

Shards: 100 shards
- Each shard: ~100K merchants
- Each shard handles: payments, settlements, refunds

Scale later:
- If reaches limits, split into 200 shards
- Redistributes 50% of merchants to new shards
```

### 32. Design a sharding strategy for a chat application (100M users).

**Answer:**
```
Two separate shardings:

1. Users table
   Shard Key: user_id
   Strategy: Hash-based
   Reason: User profile queries

2. Messages table
   Shard Key: conversation_id
   Strategy: Hash-based
   Reason: Messages for conversation together
   
Challenge: Cross-shard queries
"Get all conversations for user_id X"
Solution: Index in Redis
redis: user_conversations:X = [conv_1, conv_2, conv_3]
```

### 33. Your hot shard has 80% of traffic. How do you fix it?

**Answer:**

**Root cause analysis:**
```
Is it bad shard key?
Is it celebrity user?
Is it business hours?
```

**Depending on cause:**

1. **Bad shard key** → Reshard with better key (hash(user_id))
2. **Celebrity user** → Dedicated shard + caching
3. **Business hours** → Accept as expected, monitor
4. **Uneven data** → Secondary sharding for hot shard

### 34. Design resharding from 10 to 20 shards with zero downtime.

**Answer:**

```
Week 1: Dual-Write Phase
├─ Set up 10 new shards (replicas running)
├─ Start dual-write: all writes go to Shard old + Shard new
├─ Copy existing data from old to new (background)

Week 2: Validation
├─ Verify all data copied correctly (checksum)
├─ Run smoke tests on new shards
├─ Fix any inconsistencies

Week 3: Read Cutover
├─ Start routing reads to new shards (50%)
├─ Gradually increase read percentage
├─ Monitor for errors
├─ Rollback if issues

Week 4: Write Cutover
├─ Stop dual-write
├─ All writes to new shards
├─ All reads from new shards

Week 5: Cleanup
├─ Keep old shards for 1 week as backup
├─ Decommission old shards
```

---

## 🏆 Expert-Level Insights

### 35. When do you know sharding is the wrong choice?

**Answer:** Stop sharding if:

1. **Most queries are cross-shard**
   ```
   "Get top 10 users"
   Requires querying all shards
   Resharding didn't help, made worse
   ```

2. **Joins are too complex**
   ```
   Need to join 5+ tables across shards
   Application-level joins too slow
   Consider moving to NoSQL
   ```

3. **Resharding too frequent**
   ```
   Resharding every month
   Sign that shard key is wrong
   Or traffic patterns unpredictable
   ```

4. **Data not naturally distributed**
   ```
   Impossible to shard evenly
   Accept eventual sharding, use other scaling
   ```

### 36. What's the difference between sharding and partitioning?

**Answer:**

- **Partitioning:** Dividing data within single database
  ```
  SELECT * FROM users_2024
  SELECT * FROM users_2025
  ```
  Same database, different tables.

- **Sharding:** Partitioning across multiple servers
  ```
  Shard 1: MySQL server 1
  Shard 2: MySQL server 2
  ```
  Different servers.

Sharding is horizontal partitioning + distribution.

### 37. How do you choose between consistency models?

**Answer:**

| Model | When | Trade-off |
|-------|------|-----------|
| **Strong** | Critical data (payments, inventory) | Slow, complex |
| **Eventual** | Non-critical data (likes, views) | Fast, temporarily inconsistent |

**Example:**
```
Payment system: Strong consistency within shard
   "Debit must succeed before credit"
   Use transactions

Social media: Eventual consistency
   "Like counts eventually updated"
   Event-driven updates OK
```

### 38. What metrics should you monitor for a sharded database?

**Answer:**

```
Per shard:
- QPS (queries per second)
- Latency (p50, p95, p99)
- CPU utilization
- Memory usage
- Disk usage
- Replication lag

Across all shards:
- Hot shard detection (if one > 2x average)
- Shard size distribution (should be even)
- Query distribution
- Resharding necessity (if uneven)

Alerts:
- Any shard > 80% CPU
- Replication lag > 10s
- Shard size difference > 20%
- Hot shard detected
```

### 39. Can you dynamically change shard key?

**Answer:** **Not practical.** Changing shard key = resharding all data.

**Why it's hard:**
```
Old shard key: user_id
New shard key: (user_id, country)

Every record's shard assignment changes
Must move all data
Can't be done incrementally
Requires complete downtime or complex dual-write
```

**Lesson:** Choose shard key for 5+ years ahead. Plan for scale.

### 40. What's the ideal shard size?

**Answer:**

**Too small:**
```
Many shards = overhead
More connections to manage
Harder to failover
```

**Too large:**
```
Few shards = still hot
Resharding frequent
Takes long to copy
```

**Ideal:**
```
Shard size: 100GB - 500GB
Rule: Can copy shard in < 1 hour (for failover)
       Can operate with: 5-10K QPS per shard

Example:
- Total data: 10TB
- Shard size: 500GB
- Number of shards: 10TB / 500GB = 20 shards
```

---

## Summary Checklist

- ✅ Understand when to shard (not before)
- ✅ Choose shard key carefully (immutable, high cardinality, even)
- ✅ Know sharding strategies (hash, range, directory)
- ✅ Understand hot shard problem
- ✅ Know how to handle cross-shard queries
- ✅ Understand resharding complexity
- ✅ Know consistency trade-offs
- ✅ Can design shard architecture for real systems
- ✅ Monitor shard metrics
- ✅ Know when sharding is wrong choice
