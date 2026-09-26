# Database Sharding — Decision Framework & Real-World Scenarios

When to shard, how to choose strategy, and detailed examples.

---

## Quick Decision Tree

```
Start: "My database is slow"
│
├─ Data size > 100GB?
│  └─ NO → Use read replicas, caching (not sharding)
│
├─ QPS > 10,000?
│  └─ NO → Add cache layer (Redis, Memcached)
│
├─ Most queries use same field (shard key)?
│  └─ NO → Fix queries, denormalize, redesign
│
├─ Ready for distributed complexity?
│  └─ NO → Scale vertically, add hardware first
│
├─ Consistent distribution possible?
│  └─ NO → Find different shard key
│
└─ YES to all → SHARD ✅
```

---

## Scoring Matrix

Rate your system 1-5 for each factor.

### Factors Favoring Sharding

| Factor | Weight | Score | Reasoning |
|--------|--------|-------|-----------|
| **Data size** | 4 | ___ | > 500GB = 5, < 50GB = 1 |
| **QPS** | 4 | ___ | > 50K = 5, < 5K = 1 |
| **Cardinality** | 3 | ___ | High cardinality = 5, low = 1 |
| **Even distribution** | 3 | ___ | Perfectly even = 5, skewed = 1 |
| **Cross-shard queries** | 2 | ___ | Rare = 5, common = 1 |
| **Team readiness** | 2 | ___ | Expert = 5, inexperienced = 1 |

**Sharding Score = (sum of score × weight) / total weight**

**Decision:**
- Score > 3.5 → SHARD
- Score 2-3.5 → MAYBE (investigate first)
- Score < 2 → USE ALTERNATIVES

---

## Real-World Scenarios

### Scenario 1: E-Commerce Platform (100M Products)

**Requirements:**
- 100M products
- 50K QPS peak
- Many queries: `SELECT * FROM products WHERE category=?`
- Geographic customers (USA, EU, APAC)

**Analysis:**

| Factor | Value | Score |
|--------|-------|-------|
| Data size | 500GB | 4 |
| QPS | 50K | 5 |
| Cardinality | product_id (100M) | 5 |
| Distribution | Even (product_id) | 5 |
| Cross-shard | Rare (queries by product) | 4 |
| Team readiness | Medium | 3 |
| **Total Score** | | **4.3** |

**Decision: SHARD ✅**

**Implementation:**

```
Shard Key: product_id
Strategy: Hash-based
Number of shards: 50K / 5K = 10 shards

Calculation:
hash(product_id) % 10 = shard_id

Distribution:
- Each shard: ~10M products, ~5K QPS
- Replicas per shard for HA
```

**Routing:**
```
client query: "SELECT * FROM products WHERE id=12345"
→ shard_id = hash(12345) % 10 = 7
→ connect to Shard 7
→ execute query
```

**Cross-shard queries:**
```
"Get top 10 products by rating"
→ Query all 10 shards
→ Aggregate in application
→ Return top 10
(Slow but necessary)
```

---

### Scenario 2: Social Network (1B Users, 500K QPS)

**Requirements:**
- 1 billion users
- 500K QPS sustained
- Queries: followers, posts, timeline
- Must scale to billions

**Analysis:**

| Factor | Value | Score |
|--------|-------|-------|
| Data size | 5TB | 5 |
| QPS | 500K | 5 |
| Cardinality | user_id (1B) | 5 |
| Distribution | Even (hash) | 5 |
| Cross-shard | Common (timeline) | 2 |
| Team readiness | Expert (Twitter/Instagram) | 5 |
| **Total Score** | | **4.5** |

**Decision: SHARD ✅ (Essential)**

**Implementation:**

```
Shard Key: user_id
Strategy: Hash-based
Number of shards: 500K / 5K = 100 shards

Actually: Use 1024 for future growth (power of 2)

Calculation:
shard_id = hash(user_id) % 1024

Distribution:
- Each shard: ~1M users, ~488 QPS (under capacity)
- Replicas for HA: 1 primary + 2 replicas per shard
- Total servers: 1024 × 3 = 3,072 servers
```

**Sharded Tables:**
```
users
├─ Sharded by user_id
├─ Contains: profile, settings, preferences

user_posts
├─ Sharded by user_id
├─ Contains: tweets, likes count, etc

user_followers
├─ Sharded by user_id
├─ Contains: followers list

user_feed
├─ Sharded by user_id
├─ Contains: personalized feed (denormalized)
```

**Cross-shard challenges:**
```
"Get user timeline"
Problem: User's posts from their shard (Shard 42)
         + Posts from followed users (scattered across all shards)
         
Solution: Denormalize
- Materialized feed: user:42:feed = [post_123, post_456, ...]
- Update asynchronously when followed user posts
```

---

### Scenario 3: SaaS Multi-Tenant (10K Customers)

**Requirements:**
- 10,000 customers
- 50GB total data
- Low QPS (5K total)
- Strong data isolation needed

**Analysis:**

| Factor | Value | Score |
|--------|-------|-------|
| Data size | 50GB | 1 |
| QPS | 5K | 1 |
| Cardinality | customer_id (10K) | 2 |
| Distribution | Skewed (large customers) | 2 |
| Cross-shard | None (per-tenant) | 5 |
| Team readiness | Startup | 2 |
| **Total Score** | | **2.0** |

**Decision: DON'T SHARD ❌**

**Alternative: Directory-based (One DB per tenant)**

```
Each customer gets dedicated database
customer_id → database address mapping

Advantages:
- Complete isolation
- Easy backup per customer
- Can optimize per customer
- No sharding complexity

Database mapping:
customer_1 → db1.customers.example.com
customer_2 → db2.customers.example.com
customer_3 → db1.customers.example.com (share for small customers)
```

---

### Scenario 4: Time-Series Metrics (100K servers, 1M events/sec)

**Requirements:**
- 100K servers sending metrics
- 1M events per second
- Must store metrics forever
- Query: metrics for server X in time range Y

**Analysis:**

| Factor | Value | Score |
|--------|-------|-------|
| Data size | 10TB+/month | 5 |
| QPS | 1M events/sec | 5 |
| Cardinality | High (timestamps, servers) | 4 |
| Distribution | Uneven (peak vs off-peak) | 2 |
| Cross-shard | Common (aggregate metrics) | 2 |
| Team readiness | High (DataOps) | 4 |
| **Total Score** | | **3.7** |

**Decision: SHARD (Time-based) ⚠️**

**Implementation:**

```
Shard Key: Time-based (date) + Server ID
Strategy: Directory-based

Tables by day:
metrics_2024_01_01 (Shard 1)
metrics_2024_01_02 (Shard 2)
metrics_2024_01_03 (Shard 3)
...

Within shard, partition by server_id
```

**Challenges & Solutions:**

```
Challenge: Time-based sharding creates hot shard
- Today's metrics get all writes
- Old metrics get reads only
Solution:
- More resources on current day shard
- Archive old shards
- Auto-create new shard at midnight
```

**Query patterns:**
```
"Get metrics for server_123 on 2024-01-15"
→ Query metrics_2024_01_15
→ Filter by server_id=123
(Local query, fast)

"Get metrics across all servers on 2024-01-15"
→ Query metrics_2024_01_15
→ No WHERE clause
(Scan entire shard)

"Get metrics for server_123 for past 30 days"
→ Query 30 different shards
→ Aggregate results
(Slow but necessary)
```

---

### Scenario 5: High-Frequency Trading (1M orders/sec, 10 locations)

**Requirements:**
- 1M orders per second
- Must be extremely fast (sub-millisecond latency)
- 10 geographic locations
- Data isolation by location

**Analysis:**

| Factor | Value | Score |
|--------|-------|-------|
| Data size | 100GB/day | 4 |
| QPS | 1M/sec | 5 |
| Cardinality | High | 4 |
| Distribution | Uneven (NYC 40%, UK 30%, others 30%) | 2 |
| Cross-shard | None (local orders) | 5 |
| Team readiness | Expert (FinTech) | 5 |
| **Total Score** | | **4.2** |

**Decision: SHARD (Geographic) ✅**

**Implementation:**

```
Shard Key: location_id
Strategy: Directory-based (custom, not generic)

Shards:
Shard[NYC] → Orders from NYC traders
Shard[London] → Orders from London traders
Shard[Tokyo] → Orders from Tokyo traders
Shard[Singapore] → Orders from Singapore traders
...

Each shard:
- Dedicated server in that location (low latency)
- Sub-millisecond response time (local DB)
- Replicated for HA (within location)
```

**Why this sharding:**

```
Not by order_id: Would scatter orders, increase latency
By location_id: Keeps local orders local
                Reduces network hops
                Ensures low latency
                Natural business boundary
```

**Challenges:**

```
Cross-location orders:
"NYSE trader wants to execute on LSE"
Problem: Order split across shards

Solution:
- Route to NYC shard
- Publish event: "ExecuteOnLSE"
- LSE shard processes asynchronously
- Accept slightly higher latency (100ms) for cross-location
```

---

## Choosing Shard Strategy

| Strategy | Best For | Avoid |
|----------|----------|-------|
| **Hash-based** | Even distribution needed, simple implementation | Frequent resharding |
| **Range-based** | Sequential IDs, range queries important | Uneven distribution |
| **Directory-based** | Flexibility, frequent resharding, multi-tenant | Extra lookup cost |
| **Consistent Hash** | Elastic systems, caches, nodes added/removed frequently | Complex, possible hotspots |
| **Geographic** | Data residency, locality important | If locations unbalanced |

---

## When NOT to Shard

❌ **Don't shard if:**

1. **Data < 100GB**
   ```
   Use: Single well-configured database
   Or: Read replicas for scaling reads
   ```

2. **QPS < 10,000**
   ```
   Use: Caching layer (Redis)
   Or: Database optimization (indexes, query tuning)
   ```

3. **Many cross-shard queries**
   ```
   Use: Different data model
   Or: Different shard key
   Or: Accept complexity
   ```

4. **Team inexperienced**
   ```
   Use: External managed service (GCP Cloud Spanner, DynamoDB)
   Or: Hire expertise first
   Or: Scale vertically
   ```

5. **Shard key low cardinality**
   ```
   Use: Find different key
   Or: Don't shard
   ```

---

## Migration Path Example: Instagram

**Stage 1 (2M users): Single Database**
```
one_db.instagram.com
├─ users table (2M rows)
├─ photos table (10M rows)
├─ likes table (100M rows)
```

**Stage 2 (10M users): Read Replicas**
```
Primary DB (writes)
├─ Replica 1 (reads)
├─ Replica 2 (reads)
└─ Replica 3 (reads)
```

**Stage 3 (100M users): Sharding**
```
Shard by user_id
├─ Shard 1: users 1-10M, their photos, their likes
├─ Shard 2: users 10M-20M
├─ ...
└─ Shard 10: users 90M-100M

Each shard has replicas
```

**Stage 4 (1B users): Resharding**
```
From 10 to 100 shards
Using dual-write pattern
Zero-downtime resharding
```

---

## Monitoring Readiness

Before sharding, ensure:

```
✅ Monitoring per-shard QPS
✅ Monitoring per-shard latency
✅ Hot shard detection
✅ Replication lag alerts
✅ Backup procedures tested
✅ Failover procedures tested
✅ Resharding runbook documented
✅ Team trained
```

---

## Checklist: Should We Shard?

- [ ] Data size > 100GB? (Must be YES)
- [ ] QPS > 10,000? (Must be YES)
- [ ] Have good shard key candidate? (Must be YES)
- [ ] Tried read replicas, caching, optimization? (Must be YES)
- [ ] Team has sharding expertise? (Strongly prefer YES)
- [ ] Acceptable cross-shard query latency? (Should be YES)
- [ ] Resharding process planned? (Should be YES)
- [ ] Monitoring in place? (Should be YES)

**All YES → SHARD**
**Any NO → Investigate alternatives first**

---

## Final Decision Matrix Summary

| System Scale | QPS | Data | Recommendation |
|--------------|-----|------|-----------------|
| Startup | < 1K | < 10GB | Single DB |
| Growth | 1-10K | 10-100GB | Read replicas + caching |
| Scale | 10-100K | 100GB-1TB | SHARD (required) |
| Mega | > 100K | > 1TB | SHARD + optimize |

Remember: **"Shard as late as possible, but not later."**
