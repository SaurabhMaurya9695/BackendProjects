# Database Sharding — Comprehensive Learning Module

A complete guide to horizontal database partitioning, sharding strategies, and scaling databases.

---

## 🌟 Module Overview

**Database Sharding** is the technique of partitioning data horizontally across multiple database servers. Instead of one massive database, data is split by a shard key, with each shard holding a subset of data.

**Use Case:** When a single database can't handle:
- 🔥 Millions of QPS (queries per second)
- 📦 Petabytes of data
- ⚡ Latency requirements (split load across servers)

**Real-world Examples:**
- Twitter/X (users sharded by user_id)
- Instagram (photos sharded by user_id)
- Uber (trips sharded by location/driver_id)
- Shopify (stores sharded by store_id)

---

## 📋 Table of Contents

1. [README.md](#) ← You are here
2. [DATABASE_SHARDING_GUIDE.md](#) — Deep dive into sharding
3. [QUESTIONS_AND_ANSWERS.md](#) — 40+ Q&A (beginner to expert)
4. [BEST_PRACTICES.md](#) — Production patterns
5. [DECISION_MATRIX.md](#) — When & how to shard
6. [INDEX.md](#) — Navigation guide

---

## 🎯 Quick Comparison: Single DB vs Sharded

| Aspect | Single Database | Sharded Database |
|--------|-----------------|------------------|
| **Scale** | ~1 million users | 10+ million users |
| **QPS** | ~10K QPS | 100K+ QPS |
| **Data Size** | ~100GB | TB+ |
| **Latency** | Low (same server) | Higher (cross-shard queries) |
| **Complexity** | Simple | Complex (routing, rebalancing) |
| **Cost** | Lower | Higher (multiple servers) |
| **Consistency** | Transactional | Eventual (cross-shard) |
| **Failover** | Single point | Distributed |

---

## 🔑 Core Concepts

### What is a Shard?
A **shard** is an independent database server holding a subset of data. Data is partitioned by a **shard key**.

```
User Database
├─ Shard 1 (user_id: 1-100000)
│  ├─ User 1, User 50, User 100000
│  └─ Their orders, preferences, etc.
│
├─ Shard 2 (user_id: 100001-200000)
│  ├─ User 100001, User 150000, User 200000
│  └─ Their data
│
└─ Shard N (user_id: Xn-Yn)
   └─ More users and their data
```

### Shard Key
The **shard key** determines which shard a record belongs to.

```
Example: User ID 12345
Shard ID = hash(user_id) % num_shards
         = hash(12345) % 10
         = 3 → stored in Shard 3
```

### Common Shard Keys
| Key | Use Case | Example |
|-----|----------|---------|
| **User ID** | User data, orders, preferences | Twitter, Instagram, Uber |
| **Customer ID** | E-commerce data | Shopify, Amazon |
| **Organization ID** | Multi-tenant SaaS | Slack, Figma |
| **Geographic** | Location-based | Uber (city), weather data |
| **Time-based** | Time series | Metrics, logs |
| **Hash of Primary Key** | Uniform distribution | Generic sharding |

---

## 🏗️ Sharding Strategies

### 1. Range-Based Sharding
Partition data by ranges of the shard key.

```
Shard 1: user_id 1-1000000
Shard 2: user_id 1000001-2000000
Shard 3: user_id 2000001-3000000
```

**Pros:** ✅ Simple, fast lookups
**Cons:** ❌ Hot shards (if data is skewed)

### 2. Hash-Based Sharding
Hash the shard key, mod by number of shards.

```
shard_id = hash(user_id) % num_shards

Uniform distribution across shards
No hot shards (if hash function is good)
```

**Pros:** ✅ Even distribution, no hot shards
**Cons:** ❌ Resharding difficult (all keys rehashed)

### 3. Directory-Based Sharding
Lookup table maps shard key to shard.

```
┌─────────────────────┐
│ Shard Directory     │
├─────────────────────┤
│ user_id 1 → Shard 1 │
│ user_id 2 → Shard 3 │
│ user_id 3 → Shard 2 │
│ ...                 │
└─────────────────────┘
```

**Pros:** ✅ Easy resharding, flexible
**Cons:** ❌ Extra lookup, directory is SPOF

### 4. Geographic Sharding
Shard by location/region.

```
Shard (US):    users in USA
Shard (EU):    users in Europe
Shard (APAC):  users in Asia-Pacific
```

**Pros:** ✅ Low latency (local DB), data residency
**Cons:** ❌ Uneven load, complex cross-region queries

---

## ⚠️ Sharding Challenges

### 1. Hot Shards
If shard key distribution is uneven:
```
If most users are in one region → that shard becomes hot
CPU/memory/disk gets overwhelmed
Other shards are underutilized
```

### 2. Cross-Shard Queries
Queries that need data from multiple shards are expensive:
```
"Get all orders from all users" → Must query all shards
"Get top 10 users by spending" → Aggregate from all shards
```

### 3. Distributed Transactions
Can't use traditional ACID across shards:
```
Transaction: Transfer money from User A (Shard 1) to User B (Shard 2)
Challenge: What if Shard 1 commits but Shard 2 fails?
Solution: Eventual consistency, saga pattern
```

### 4. Rebalancing/Resharding
Adding new shards requires moving data:
```
Old: 4 shards
New: 8 shards
Must rehash/redistribute ALL data
Downtime if not done carefully
```

### 5. Joins Across Shards
SQL joins across shards not possible:
```
SELECT u.*, o.* FROM users u JOIN orders o ON u.id = o.user_id
If u and o are on different shards → Must do application-level join
```

---

## 🎓 When to Shard

**Shard when:**
- ✅ Single database exceeds 100GB
- ✅ QPS exceeds ~10K sustained
- ✅ Cross-shard queries acceptable (slow)
- ✅ Team has expertise in distributed systems

**Don't shard when:**
- ❌ Data < 50GB, QPS < 1K (use read replicas first)
- ❌ Many cross-shard transactions needed (use distributed TX)
- ❌ Team inexperienced with distributed systems

**Alternative before sharding:**
1. ✅ Read replicas (distribute reads)
2. ✅ Caching layer (Redis, Memcached)
3. ✅ Vertical scaling (bigger hardware)
4. ✅ Database partitioning (within single DB)
5. ✅ Separate databases by feature (microservices)

**Then shard** (as last resort)

---

## 🔄 Sharding Process

### Step 1: Choose Shard Key
```
Must be:
✅ Immutable (shouldn't change)
✅ High cardinality (many unique values)
✅ Evenly distributed
✅ Used in most queries

Bad shard keys:
❌ Country (only 200 values, hot shards)
❌ Gender (male/female, extreme skew)
❌ Subscription tier (most free users)
```

### Step 2: Design Shard Schema
```
Shard Count = 1.25 × (Peak QPS / Single DB QPS)

Example:
Peak QPS = 50K
Single DB can handle = 5K QPS
Shards needed = 1.25 × (50K / 5K) = 12.5 → 16 shards
```

### Step 3: Implement Routing Logic
```
client_request
  ↓
routing_layer (calculate shard_id)
  ↓
shard_id = hash(shard_key) % num_shards
  ↓
connect to shard[shard_id]
  ↓
query shard
  ↓
return result
```

### Step 4: Handle Cross-Shard Queries
```
"Get top 10 spending users"

1. Query all shards in parallel
2. Aggregate results in application
3. Sort & return top 10

Slow but works
```

### Step 5: Plan for Resharding
```
When shards become full:
1. Add more shards
2. Redistribute data (rebalance)
3. Update routing layer

Requires:
- Live migration (zero-downtime)
- Or scheduled maintenance
```

---

## 📊 Sharding Architecture

```
┌──────────────────────────────────────────┐
│         Application Layer                 │
│  (Routing logic, shard key extraction)    │
└──────────────────┬───────────────────────┘
                   │
        ┌──────────┼──────────┐
        │          │          │
    ┌───▼──┐   ┌──▼───┐   ┌──▼───┐
    │Shard1│   │Shard2│   │ShardsN│
    │(DB)  │   │(DB)  │   │(DB)   │
    │Users │   │Users │   │Users  │
    │1-M   │   │M-2M  │   │... N-M│
    └──────┘   └──────┘   └───────┘
        │          │          │
    ┌───▼──┐   ┌──▼───┐   ┌──▼───┐
    │Replica│  │Replica│  │Replica│
    │DB1   │   │DB2   │   │DBN   │
    └──────┘   └──────┘   └───────┘

Legend:
- Each shard is a complete database
- Replicas for high availability
- Application routes requests to correct shard
```

---

## 💡 Key Insights

### 1. Shard Key is Critical
> **"Choose your shard key carefully — you can't change it easily."**
- Most important decision
- Affects load distribution
- Determines query patterns
- Hard to change after sharding

### 2. Avoid Distributed Transactions
> **"Embrace eventual consistency, don't fight it."**
- ACID across shards is expensive
- Use saga pattern or event-driven
- Accept temporary inconsistency

### 3. Query Patterns Matter
> **"Design schema knowing cross-shard queries will be slow."**
- Avoid queries that need all shards
- Pre-compute aggregates
- Use denormalization
- Cache results

### 4. Resharding is Hard
> **"Plan for resharding from day 1."**
- Adding shards requires data migration
- Zero-downtime resharding is complex
- Consider consistent hashing
- Plan for 2-3x expected load

---

## 🚀 Learning Path

### Beginner (2 hours)
1. README.md (this file) — 15 min
2. DATABASE_SHARDING_GUIDE.md — Sections 1-3 — 45 min
3. QUESTIONS_AND_ANSWERS.md — Beginner level — 30 min
4. DECISION_MATRIX.md — Quick overview — 15 min

### Intermediate (4 hours)
1. DATABASE_SHARDING_GUIDE.md — Full read — 90 min
2. QUESTIONS_AND_ANSWERS.md — Intermediate level — 60 min
3. BEST_PRACTICES.md — Core section — 30 min

### Advanced (6+ hours)
1. All documentation — 120 min
2. QUESTIONS_AND_ANSWERS.md — Advanced + scenarios — 60 min
3. BEST_PRACTICES.md — Full section — 60 min
4. DECISION_MATRIX.md — Detailed scenarios — 60 min

---

## 📂 Files in This Module

| File | Purpose |
|------|---------|
| README.md | Overview, core concepts, when to shard |
| DATABASE_SHARDING_GUIDE.md | Deep architectural guide |
| QUESTIONS_AND_ANSWERS.md | 40+ questions all levels |
| BEST_PRACTICES.md | Production patterns & solutions |
| DECISION_MATRIX.md | When/how to shard + scenarios |
| INDEX.md | Navigation & quick reference |

---

## ✨ Next Steps

👉 **Quick Start:** Go to `INDEX.md` for navigation
👉 **Deep Dive:** Read `DATABASE_SHARDING_GUIDE.md`
👉 **Interview Prep:** Study `QUESTIONS_AND_ANSWERS.md`
👉 **Design Your System:** Use `DECISION_MATRIX.md`

---

⭐ **Key Takeaway:** Sharding is powerful but complex. Understand the trade-offs. Use read replicas and caching first. Shard only when necessary.

Happy learning! 🚀
