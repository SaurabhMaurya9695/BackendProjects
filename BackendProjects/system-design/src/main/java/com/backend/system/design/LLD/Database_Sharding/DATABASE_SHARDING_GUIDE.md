# Database Sharding — Comprehensive Technical Guide

Deep technical exploration of horizontal database partitioning.

---

## Table of Contents

1. [Fundamentals](#fundamentals)
2. [Sharding Strategies](#sharding-strategies)
3. [Shard Key Design](#shard-key-design)
4. [Routing Layer](#routing-layer)
5. [Consistency Models](#consistency-models)
6. [Cross-Shard Operations](#cross-shard-operations)
7. [Scaling & Resharding](#scaling--resharding)
8. [Hot Shard Problem](#hot-shard-problem)
9. [High Availability](#high-availability)
10. [Real-World Examples](#real-world-examples)

---

## 1. Fundamentals

### What is Database Sharding?

**Sharding** = Horizontal partitioning of data across multiple database instances.

```
Before Sharding:
┌─────────────────────────────────┐
│       Single Database            │
│  All 100M users, all data       │
│  10TB storage, 50K QPS limit    │
└─────────────────────────────────┘

After Sharding:
┌────────────────┐  ┌────────────────┐  ┌────────────────┐
│ Shard 1        │  │ Shard 2        │  │ Shard N        │
│ Users 1-33M    │  │ Users 33M-66M  │  │ Users 66M-100M │
│ 3TB storage    │  │ 3TB storage    │  │ 3TB storage    │
│ 17K QPS        │  │ 17K QPS        │  │ 17K QPS        │
└────────────────┘  └────────────────┘  └────────────────┘
```

### Why Shard?

**Problem:** Single database hits limits
- **Storage:** Can't fit all data (terabytes)
- **QPS:** Can't handle all queries (50K+ per second)
- **Latency:** Queries become slow (disk I/O)
- **CPU:** All CPU on one server is maxed

**Solution:** Split across multiple servers
- Distribute load horizontally
- Each server handles subset of data
- Parallelize queries
- Scale linearly

### Key Terms

| Term | Definition |
|------|-----------|
| **Shard** | One partition/database holding subset of data |
| **Shard Key** | Column used to determine which shard owns a record |
| **Shard ID** | Identifier of which shard a record belongs to |
| **Shard Aware Routing** | Logic to route requests to correct shard |
| **Global Secondary Index** | Index across all shards (complex) |
| **Resharding** | Adding/removing shards, rebalancing data |

---

## 2. Sharding Strategies

### Strategy 1: Range-Based Sharding

**How it works:** Shard key values in contiguous ranges map to shards.

```
Shard 1: user_id ∈ [1, 1M)
Shard 2: user_id ∈ [1M, 2M)
Shard 3: user_id ∈ [2M, 3M)
...
Shard N: user_id ∈ [Xn, Yn)

shard_id = findShard(user_id)  // binary search or lookup
```

**Example:**
```java
int shard_id = 0;
if (user_id < 1000000) shard_id = 1;
else if (user_id < 2000000) shard_id = 2;
else if (user_id < 3000000) shard_id = 3;
```

**Advantages:**
- ✅ Simple to implement
- ✅ Range queries efficient (same shard)
- ✅ Easy to add new shards

**Disadvantages:**
- ❌ **Hot shards:** If data skewed (most users in range 1-100k)
- ❌ **Uneven distribution:** Non-uniform ID generation
- ❌ **Sequential scans:** Range [1, 10k] only on Shard 1

**When to use:**
- Sequential ID generation (auto-increment)
- Uniform distribution expected
- Range queries important

---

### Strategy 2: Hash-Based Sharding

**How it works:** Hash the shard key, modulo number of shards.

```
shard_id = hash(shard_key) % num_shards

Example:
user_id = 12345
shard_id = hash(12345) % 4
         = 2839201 % 4
         = 1  →  Shard 1
```

**Advantages:**
- ✅ **Even distribution** (good hash function)
- ✅ **No hot shards** (uniform load)
- ✅ **Simple to implement**

**Disadvantages:**
- ❌ **Resharding is expensive** (all keys rehash)
- ❌ **Range queries problematic** (keys scattered)
- ❌ **Not monotonic** (cache locality lost)

**Example in Java:**
```java
int shard_id = Math.abs(shard_key.hashCode()) % num_shards;
```

**When to use:**
- Write-heavy workloads
- Even distribution critical
- Rarely resharding
- Random access patterns

---

### Strategy 3: Directory-Based Sharding

**How it works:** Lookup table maps shard key to shard ID.

```
┌──────────────────────────────────┐
│   Shard Directory Table           │
├──────────────────────────────────┤
│ shard_key  │  shard_id            │
├──────────────────────────────────┤
│ 1          │  2                   │
│ 2          │  1                   │
│ 3          │  3                   │
│ ...        │  ...                 │
└──────────────────────────────────┘

Routing:
user_id = 5
lookup(5) → shard_id = 2
connect to Shard 2
```

**Advantages:**
- ✅ **Flexible** (any mapping, no constraints)
- ✅ **Easy resharding** (just update directory)
- ✅ **Support any shard key**
- ✅ **Can move data without re-hashing**

**Disadvantages:**
- ❌ **Extra lookup query** (performance cost)
- ❌ **Directory is SPOF** (single point of failure)
- ❌ **Cache directory in memory** (memory usage)

**Implementation:**
```java
Map<Long, Integer> shardDirectory = new ConcurrentHashMap<>();
// Load from database or cache

int shard_id = shardDirectory.get(user_id);
// If not found, return error
```

**When to use:**
- Frequent resharding
- Complex mapping logic needed
- Multi-tenant systems
- Enterprise systems with flexibility needs

---

### Strategy 4: Consistent Hashing

**How it works:** Hash space arranged in ring. Nodes placed on ring. Keys map to nearest node.

```
        Shard 1 (hash=0)
             |
    ---------+---------
   /                   \
  /                     \
Shard 4            Shard 2
(hash=300)        (hash=100)
 \                   /
  \                 /
    --------+--------
        Shard 3 (hash=200)

Key 50 → maps to Shard 1
Key 150 → maps to Shard 2
Key 250 → maps to Shard 3
```

**Advantages:**
- ✅ **Minimal resharding** (only 1/N keys remapped)
- ✅ **Load balancing** (can add/remove nodes)
- ✅ **Replicas easy** (multiple nodes on ring)

**Disadvantages:**
- ❌ **Complex to implement**
- ❌ **Hot nodes still possible** (uneven ring placement)
- ❌ **Skewed distribution** (if nodes few)

**When to use:**
- Frequent resharding needed
- Load balancing across many shards
- Cache clusters
- Distributed systems requiring elasticity

---

## 3. Shard Key Design

### Critical Factors

**A shard key must be:**

1. **Immutable**
   - Never changes for a record
   - If it changes, record must move to different shard
   - Example: ✅ user_id, ❌ user_country (can change)

2. **High Cardinality**
   - Many unique values
   - Distributes data across shards
   - Example: ✅ user_id (billions), ❌ gender (2 values)

3. **Evenly Distributed**
   - No skew towards certain values
   - If uniform distribution impossible, choose different key
   - Example: ✅ hash(email), ❌ country (80% USA)

4. **Used in Most Queries**
   - Should be in WHERE clause
   - Avoids cross-shard queries
   - Example: ✅ "SELECT * FROM orders WHERE user_id=?", ❌ "SELECT * FROM orders WHERE order_date=?"

### Good Shard Keys

| Key | Domain | Why Good |
|-----|--------|----------|
| user_id | Social networks | High cardinality, immutable, in most queries |
| customer_id | E-commerce | Same as user_id |
| store_id | Multi-tenant | Divides data by tenant |
| org_id | SaaS | Data isolation, security |
| geographic region | Location services | Reduces latency |

### Bad Shard Keys

| Key | Why Bad | Problem |
|-----|---------|---------|
| country | 200 values, skewed | Hot shards (80% USA) |
| subscription_tier | Free/Pro/Premium | 90% free users → hot |
| gender | Male/Female | Extreme skew (50/50 at best) |
| zip_code | Depends on geography | Can be skewed |
| created_at | Time-based | All new records on last shard |

### Choosing Shard Key

**Process:**
1. Identify primary queries
2. What's the most common WHERE clause?
3. Is that column immutable?
4. Is cardinality high?
5. Is distribution even?
6. If all yes → it's a good shard key

**Example: Uber**
```
Primary queries:
- "Get trips for driver_id X"
- "Get trips for rider_id Y"
- "Get trips in location Z"

Shard key choice: geographic region (city) + driver_id
Why: Keeps local trips together, reduces cross-shard queries
```

---

## 4. Routing Layer

### Architecture

```
Application
    ↓
Routing Layer
├─ Extract shard key from request
├─ Calculate shard ID
├─ Connect to shard
└─ Execute query
    ↓
Shard Database
    ↓
Response
```

### Routing Algorithm

```pseudocode
function route(request):
  shard_key = extract_shard_key(request)
  shard_id = calculate_shard_id(shard_key)
  
  connection = get_shard_connection(shard_id)
  result = execute_on_shard(connection, request)
  
  return result
```

### Routing Strategies

**Strategy 1: Application-Level Routing**
```java
// In application code
int shard_id = hash(user_id) % num_shards;
Connection conn = connectionPool[shard_id];
Statement stmt = conn.createStatement();
stmt.execute("SELECT * FROM users WHERE id = " + user_id);
```

**Pros:** ✅ Control, ✅ No proxy overhead
**Cons:** ❌ Every app server implements routing logic, ❌ Hard to maintain

**Strategy 2: Proxy-Based Routing**
```
Application → Sharding Proxy → Shard 1
                            → Shard 2
                            → Shard 3
```

**Pros:** ✅ Centralized, ✅ Easy to update
**Cons:** ❌ Proxy is bottleneck, ❌ Single point of failure

**Strategy 3: Middleware Routing**
```
Application → Middleware (ORM) → Shard 1
  (ORM-aware)                  → Shard 2
                               → Shard 3
```

**Pros:** ✅ Transparent to app, ✅ Handles cross-shard queries
**Cons:** ❌ ORM must support sharding

### Handling Shard Misses

**Problem:** What if shard_id calculation wrong?

**Solution 1: Directory Lookup**
```
"This key should be on Shard 1 based on hash"
"But directory says it's on Shard 2"
"Query both, merge results"
→ Slower but correct
```

**Solution 2: Gossiping**
```
Shards gossip metadata
If key found elsewhere, update directory
```

---

## 5. Consistency Models

### Strong Consistency (Within Shard)

```
Single shard = single database = ACID
User 1, 2, 3 on Shard 1
Transactions on Shard 1 are ACID

Problem: Cross-shard transactions
User 1 on Shard 1, User 2 on Shard 2
Transfer money between them → Not ACID
```

### Eventual Consistency (Across Shards)

```
Operation 1: Debit User 1 (Shard 1) ✓ Complete
Operation 2: Credit User 2 (Shard 2) ✓ Complete (delayed)

Temporary inconsistency, eventual consistency
```

### Trade-Offs

| Model | Consistency | Latency | Complexity |
|-------|-------------|---------|-----------|
| **Strong** | ✅ ACID | ❌ Slow | ✅ Simple |
| **Eventual** | ❌ Temporary inconsistency | ✅ Fast | ❌ Complex |

### Patterns for Cross-Shard Consistency

**Pattern 1: Saga Pattern**
```
Distributed transaction split into steps:
1. Debit User 1 (Shard 1) → Success
2. Credit User 2 (Shard 2) → Success
If step 2 fails, compensate step 1 (reverse debit)
```

**Pattern 2: Event-Driven**
```
Event: "Money transferred from User1 to User2"
1. Save event (Shard 1)
2. Process event asynchronously
3. Update Shard 2
Decouples operations, allows eventual consistency
```

---

## 6. Cross-Shard Operations

### Cross-Shard Joins

```
"SELECT u.name, o.total FROM users u JOIN orders o ON u.id = o.user_id"

If users sharded by user_id, orders sharded by user_id:
Same shard = local join (fast)

If orders sharded by order_id, users sharded by user_id:
Different shards = application join (slow)
```

**Solution:**
```java
// Query all shards for users
Map<Integer, User> users = new HashMap<>();
for (Shard shard : allShards) {
  List<User> shardUsers = shard.query("SELECT * FROM users");
  for (User u : shardUsers) {
    users.put(u.id, u);
  }
}

// Query all shards for orders
List<Order> orders = new ArrayList<>();
for (Shard shard : allShards) {
  orders.addAll(shard.query("SELECT * FROM orders"));
}

// Application-level join
List<UserOrder> results = new ArrayList<>();
for (Order o : orders) {
  results.add(new UserOrder(users.get(o.user_id), o));
}
```

### Cross-Shard Aggregates

```
"SELECT SUM(total) FROM orders WHERE status='SHIPPED'"

Solution:
1. Query all shards
2. Sum results in application
```

### Cross-Shard Ordering/Pagination

```
"SELECT * FROM orders ORDER BY created_at LIMIT 10"

Solution:
1. Query all shards for top 10 (with higher limit for safety)
2. Merge sorted results
3. Return top 10
```

---

## 7. Scaling & Resharding

### When to Add Shards?

**Metrics:**
- Shard size > target size (e.g., 500GB)
- QPS per shard > target QPS (e.g., 10K)
- CPU/Memory utilization > threshold (e.g., 70%)

**Rule of Thumb:**
```
Number of Shards = 1.25 × (Peak Load / Single DB Capacity)

Example:
Peak QPS = 100K
Single DB capacity = 5K QPS
Shards needed = 1.25 × (100K / 5K) = 25 shards
```

### Resharding Process

**Step 1: Add New Shards**
```
Old: Shard 1-10
New: Shard 1-10, Shard 11-20
```

**Step 2: Rebalance Data**
```
Recalculate shard_id for all records
Move records to new shards
Verify data integrity
```

**Step 3: Update Routing**
```
Update routing layer config
Point traffic to new shard layout
```

**Step 4: Validate**
```
Compare old vs new shard data
Check queries work
Monitor for errors
```

### Techniques for Zero-Downtime Resharding

**Technique 1: Dual-Write**
```
Phase 1:
- Write to both old and new shards
- Read from old shard
- Copy old data to new shards in background

Phase 2:
- Write to both
- Read from new shard
- Verify consistency

Phase 3:
- Stop writing to old shard
- Decommission old shard
```

**Technique 2: Log-Based**
```
1. Copy initial snapshot of data to new shards
2. Replay transaction log (binlog) to catch up
3. Verify consistency
4. Switch traffic
5. Decommission old shards
```

---

## 8. Hot Shard Problem

### What is a Hot Shard?

One shard becomes bottleneck while others are underutilized.

```
Shard 1: 80% traffic, 95% CPU → Hot
Shard 2: 10% traffic, 20% CPU
Shard 3: 10% traffic, 15% CPU

Result: Queries slow, users complain
```

### Causes

| Cause | Example |
|-------|---------|
| **Skewed shard key** | 80% users in USA, 10% in Europe |
| **Skewed query patterns** | Most queries for active users (on Shard 1) |
| **Time-based hotness** | Business hours → Shard for users timezone hot |
| **Popular key** | Celebrity user ID gets millions of queries |

### Solutions

**Solution 1: Better Shard Key**
```
Instead of: country (skewed)
Use: hash(user_id) (even distribution)
```

**Solution 2: Secondary Sharding**
```
Shard 1 (hot) → Shard 1a, Shard 1b, Shard 1c
Split hot shard further
```

**Solution 3: Dedicated Shard for Hot Key**
```
"User X is a celebrity with 1M followers"
Normal users → sharded normally
User X → dedicated shard
```

**Solution 4: Caching**
```
Cache hot data (Redis)
Reduce shard load
```

---

## 9. High Availability

### Replication

```
Shard 1 (Primary)
├─ Replica 1
└─ Replica 2

Shard 2 (Primary)
├─ Replica 1
└─ Replica 2
```

**Write:** Primary
**Read:** Primary or Replicas (eventual consistency)

### Failover

```
Shard 1 Primary fails
├─ Promote Replica 1 to Primary
├─ Point traffic to new Primary
└─ Create new Replica
```

### Disaster Recovery

```
Shard 1 Primary Data Center fails
├─ Fail over to Replica in different DC
├─ Recreate backups
└─ Test recovery
```

---

## 10. Real-World Examples

### Twitter/X

**Data:** Users, tweets, timelines, follows
**Scale:** 500M users, 100K+ QPS

**Shard Key:** user_id
**Strategy:** Hash-based
**Why:** User data follows user, even distribution

```
Shards:
user_id % 1024 → Shard 0-1023
Each shard handles ~500K users
```

### Instagram

**Data:** Users, photos, likes, comments
**Scale:** 1B users, 10K+ QPS

**Shard Key:** user_id (for user data), photo_id (for photos)
**Strategy:** Hash-based
**Why:** Distribute load evenly, many shards for scale

### Uber

**Data:** Drivers, riders, trips, locations
**Scale:** 10M active users

**Shard Key:** geographic region
**Strategy:** Directory-based
**Why:** Local trips together, reduces cross-shard queries, data residency

```
Shard[San Francisco]: Driver 1, 2, 3... (SF-based)
Shard[NYC]: Driver 100, 101, 102... (NYC-based)
```

### Shopify

**Data:** Stores, products, orders, customers
**Scale:** 4M stores

**Shard Key:** store_id
**Strategy:** Directory-based
**Why:** Data isolation by tenant, security, easy to shard

```
Store[store_id=123] → Shard[store_123]
Store[store_id=456] → Shard[store_456]
Complete data isolation
```

---

## Summary

| Aspect | Key Point |
|--------|-----------|
| **When** | Scale beyond single DB (100GB+, 10K+ QPS) |
| **Shard Key** | Most critical decision |
| **Strategy** | Hash for even distribution, Directory for flexibility |
| **Challenges** | Cross-shard queries, hot shards, resharding |
| **Consistency** | Accept eventual consistency |
| **HA** | Replicas per shard + failover |
| **Complexity** | High, but necessary for scale |

---

⭐ **Golden Rule:** "Shard as late as possible. Use read replicas, caching, and better hardware first."
