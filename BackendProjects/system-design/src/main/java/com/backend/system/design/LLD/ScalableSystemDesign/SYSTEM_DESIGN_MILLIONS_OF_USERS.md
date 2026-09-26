# Designing a System for Millions of Users - Complete Guide

## Table of Contents
1. [Core Principles](#core-principles)
2. [Scalability Fundamentals](#scalability-fundamentals)
3. [Complete Architecture](#complete-architecture)
4. [Vertical vs Horizontal Scaling](#vertical-vs-horizontal-scaling)
5. [Database Scaling Strategies](#database-scaling-strategies)
6. [Caching Layer](#caching-layer)
7. [Message Queues & Async Processing](#message-queues--async-processing)
8. [API Gateway & Load Balancing](#api-gateway--load-balancing)
9. [Data Consistency](#data-consistency)
10. [Interview Q&A](#interview-qa)

---

## Core Principles

### 1. Scalability vs Performance

```
Scalability: Ability to handle increasing load
├─ Vertical: More powerful hardware (CPU, RAM)
├─ Horizontal: More machines (distributed)
└─ Both together: Optimal solution

Performance: How fast single request completes
├─ Optimize algorithms
├─ Database indexing
├─ Caching
└─ CDN for static assets

Different problems:
  Slow application → Fix performance
  Growing users → Scale horizontally
  Both → Scale + Optimize
```

### 2. Scalability Metrics

```
Throughput: Requests per second (RPS)
  ├─ 100 RPS: Single server sufficient
  ├─ 1,000 RPS: Need load balancing
  ├─ 10,000 RPS: Database becomes bottleneck
  └─ 100,000 RPS: Need sharding + caching

Latency: Response time for single request
  ├─ < 100ms: User happy
  ├─ 100-500ms: User feels delay
  └─ > 1000ms: User frustrated

Concurrent users: How many at same time
  ├─ 1,000: One server
  ├─ 100,000: Multiple servers + load balancing
  └─ 1,000,000: Distributed system + sharding

Peak vs Average load:
  ├─ Average: 10,000 RPS
  ├─ Peak: 50,000 RPS (holiday, viral event)
  └─ Must design for peak, not average
```

### 3. CAP Theorem (Choose 2 of 3)

```
C: Consistency (all nodes see same data)
A: Availability (system always responsive)
P: Partition tolerance (works despite network failures)

Trade-offs:
├─ CA: Relational databases (ACID)
│   ├─ Strong consistency
│   ├─ Always available
│   ├─ Cannot handle network partitions
│   └─ Use: When network reliable
│
├─ CP: HBase, MongoDB
│   ├─ Strong consistency
│   ├─ Can handle partitions
│   ├─ Unavailable during partition
│   └─ Use: Financial systems (prefer consistency)
│
└─ AP: Cassandra, DynamoDB
    ├─ Available + partition tolerant
    ├─ Eventual consistency
    ├─ Always responds
    └─ Use: Social networks, real-time systems

For millions of users:
  → Must handle partitions (P is mandatory)
  → Choose between CP and AP based on use case
```

---

## Scalability Fundamentals

### Scale Horizontally vs Vertically

```
Vertical Scaling (Buy bigger server):
  ├─ Hardware upgrade: 16GB → 64GB RAM
  ├─ CPU upgrade: 2 cores → 8 cores
  ├─ Storage upgrade: 1TB → 10TB
  │
  ├─ Advantages:
  │  ├─ Simple (no code changes)
  │  └─ Good for databases
  │
  ├─ Disadvantages:
  │  ├─ Limited (server max power)
  │  ├─ Downtime for upgrades
  │  ├─ Single point of failure
  │  └─ Very expensive at scale
  │
  └─ Max users: Maybe 10,000-100,000

Horizontal Scaling (Add more servers):
  ├─ Add more machines: 1 → 2 → 4 → 8 → 16...
  ├─ Load balance across them
  │
  ├─ Advantages:
  │  ├─ Unlimited scalability
  │  ├─ No downtime (add servers incrementally)
  │  ├─ High availability (one fails, others continue)
  │  └─ Cost effective
  │
  ├─ Disadvantages:
  │  ├─ Requires load balancing
  │  ├─ Database becomes bottleneck
  │  ├─ Data consistency challenges
  │  └─ Operational complexity
  │
  └─ Max users: Millions

For Millions of Users:
  → Must use horizontal scaling
  → Supplement with vertical where possible
  → Database is critical bottleneck
```

### Stateless vs Stateful

```
STATELESS Applications (Best for scaling):
├─ No user-specific data kept in memory
├─ Each request is independent
├─ Can route to any server
├─ Easy to horizontally scale
│
├─ Example:
│  Request 1 → Server A
│  Request 2 → Server B (same user, different server)
│  Request 3 → Server A (again, doesn't matter)
│
└─ How to make stateless:
   ├─ Store session in Redis/cache (not in app memory)
   ├─ Use JWT tokens (data in token, not server)
   ├─ Store user preferences in database
   └─ Avoid in-memory caches per instance

STATEFUL Applications (Hard to scale):
├─ Keeps user-specific data in memory
├─ Must route same user to same server
├─ Requires sticky sessions (ALB routing)
├─ Harder to scale, update, or failover
│
├─ Example:
│  Request 1 → Server A (stores data in memory)
│  Request 2 → Must go to Server A (data is there!)
│
└─ When necessary:
   ├─ WebSocket connections (chat, real-time)
   ├─ Game sessions
   ├─ Live streaming

For Millions of Users:
  → Make application STATELESS
  → Move all state to external stores (cache, database)
  → Enables true horizontal scaling
```

---

## Complete Architecture

### System Design for 1 Million Concurrent Users

```
┌──────────────────────────────────────────────────────────────┐
│                     GLOBAL USERS (1M)                        │
└──────────────────────────────┬───────────────────────────────┘
                               │
                               ▼
┌──────────────────────────────────────────────────────────────┐
│           CDN (CloudFront) - Global Edge Caching             │
│  ├─ Cache static assets (JS, CSS, images)                    │
│  ├─ Reduce origin load 90%+                                  │
│  └─ Serve from nearest edge location (< 50ms latency)        │
└──────────────────────────────┬───────────────────────────────┘
                               │
                               ▼
┌──────────────────────────────────────────────────────────────┐
│              DNS (Route 53) - Geo-routing                    │
│  ├─ Route users to nearest region                            │
│  └─ Health checks → failover to backup region               │
└──────────────────────────────┬───────────────────────────────┘
                               │
                               ▼
┌──────────────────────────────────────────────────────────────┐
│           API Gateway (AWS API Gateway / Kong)               │
│  ├─ Rate limiting: 10,000 RPS per user                       │
│  ├─ Authentication: JWT, OAuth                               │
│  ├─ Request throttling: Prevent abuse                        │
│  ├─ Caching: Responses (for idempotent GETs)                │
│  └─ Logging & monitoring                                     │
└──────────────────────────────┬───────────────────────────────┘
                               │
                               ▼
┌──────────────────────────────────────────────────────────────┐
│         Load Balancer (ALB) - 3 Availability Zones           │
│  ├─ Distribute traffic across regions                        │
│  ├─ Health checks → remove unhealthy instances               │
│  ├─ Connection draining: 30-60 seconds                       │
│  └─ Sticky sessions: Only if necessary                       │
└──────────────────────────────┬───────────────────────────────┘
                               │
                ┌──────────────┼──────────────┐
                ▼              ▼              ▼
           ┌────────┐     ┌────────┐    ┌────────┐
           │ AZ-1   │     │ AZ-2   │    │ AZ-3   │
           │        │     │        │    │        │
           │        │     │        │    │        │
     ┌─────▼──┐┌────┴─────┐    ┌──┴──┐       │
     │        ││          │    │     │       │
  ┌──┴──┐  ┌──┴──┐     ┌──┴──┐  ┌───┴───┐   │
  │ App │  │ App │     │ App │  │ App   │   │
  │ Pod │  │ Pod │     │ Pod │  │ Pod   │   │
  └──┬──┘  └──┬──┘     └──┬──┘  └──┬────┘   │
     │        │           │        │        │
     └────────┼───────────┼────────┘        │
              │           │                 │
   ┌──────────▼───────────▼──────────────┐  │
   │   Service Mesh (Istio)              │  │
   │  ├─ Traffic management              │  │
   │  ├─ Service discovery               │  │
   │  ├─ Load balancing between pods     │  │
   │  ├─ Circuit breaker                 │  │
   │  └─ Rate limiting                   │  │
   └──────────┬──────────────────────────┘  │
              │                              │
              ▼                              │
   ┌──────────────────────────┐             │
   │  Redis Cache Cluster     │             │
   │  ├─ 3-node cluster       │             │
   │  ├─ Session store        │             │
   │  ├─ Rate limit counters  │             │
   │  ├─ Distributed cache    │             │
   │  └─ Pub/Sub channels     │             │
   └──────────┬───────────────┘             │
              │                             │
              ▼                             │
   ┌──────────────────────────┐             │
   │  Message Queue (Kafka)   │             │
   │  ├─ Async operations     │             │
   │  ├─ Event streaming      │             │
   │  ├─ Data replication     │             │
   │  └─ Pub/Sub decoupling   │             │
   └──────────┬───────────────┘             │
              │                             │
              ▼                             │
   ┌──────────────────────────┐             │
   │  Primary Database (SQL)  │ (Replicate)│
   │  ├─ Multi-AZ failover    │◄───────────┘
   │  ├─ Read replicas        │
   │  ├─ Sharded by user_id   │
   │  ├─ Connection pooling    │
   │  └─ Caching layer (Redis)│
   └──────────┬───────────────┘
              │
    ┌─────────┼──────────┐
    ▼         ▼          ▼
  ┌─────┐ ┌──────┐ ┌──────────┐
  │ S3  │ │Logs  │ │Analytics │
  │     │ │      │ │Database  │
  └─────┘ └──────┘ └──────────┘

Storage Tier:
  ├─ S3: Static assets, backups
  ├─ CloudWatch: Logs & metrics
  ├─ DataLake: Big data analysis
  └─ Backup: Cross-region replication
```

---

## Vertical vs Horizontal Scaling

### When to Scale Vertically

```
Good for:
├─ Database servers (RDS, PostgreSQL)
│  └─ Larger RAM → more caching
│
├─ Cache servers (Redis)
│  └─ More memory → larger cache
│
├─ During initial growth (< 10,000 users)
│  └─ Simpler, cheaper than horizontal
│
└─ Niche high-performance workloads
   └─ ML inference, real-time analytics

Limitations:
├─ Server max capacity (can't buy infinite RAM)
├─ Downtime for upgrades
├─ Single point of failure
└─ Expensive at high scale
```

### When to Scale Horizontally

```
Best for:
├─ Application servers (stateless)
│  └─ Add more instances behind load balancer
│
├─ Cache (distributed cache cluster)
│  └─ Add nodes to Redis cluster
│
├─ Databases (read replicas, sharding)
│  └─ Distribute load across multiple instances
│
├─ Message queues (Kafka, RabbitMQ)
│  └─ Add brokers, increase partitions
│
└─ For millions of users
   └─ MUST use horizontal scaling

Challenges:
├─ Load balancing complexity
├─ Database becomes bottleneck
├─ Data consistency challenges
├─ Network latency between nodes
└─ Operational complexity
```

### Scaling Strategy (As user base grows)

```
Stage 1: 100-1,000 users
├─ Single server (app + database)
├─ Vertical scaling sufficient
└─ Cost: $50-100/month

Stage 2: 1,000-10,000 users
├─ Separate app from database
├─ Add Redis cache
├─ Load balancer (2 app servers)
└─ Cost: $500-1,000/month

Stage 3: 10,000-100,000 users
├─ Horizontal app scaling (4-8 servers)
├─ Database read replicas (2-3)
├─ Distributed cache (Redis cluster)
├─ Message queue (Kafka)
└─ Cost: $2,000-5,000/month

Stage 4: 100,000-1,000,000 users
├─ Horizontal scaling (10-50 servers)
├─ Database sharding by user_id
├─ Distributed cache cluster
├─ Message queue with multiple brokers
├─ CDN for static assets
├─ Multi-region setup
└─ Cost: $10,000-50,000/month

Stage 5: 1,000,000+ users
├─ Microservices architecture
├─ Database sharding (10-100 shards)
├─ Distributed cache (100+ nodes)
├─ Message queue (Kafka cluster)
├─ Edge computing (regional processing)
├─ Analytics infrastructure
├─ Full observability (metrics, logs, traces)
└─ Cost: $50,000-500,000+/month
```

---

## Database Scaling Strategies

### Problem: Database Bottleneck

```
As users grow:

1,000 users:     1 database (read + write)    ✅ OK
10,000 users:    1 database (getting slow)    ⚠️  WARNING
100,000 users:   Database maxed out           ❌ PROBLEM
1,000,000 users: Database can't handle        ❌ CRITICAL

Why?
├─ Each query takes CPU/memory/IO
├─ Database connections limited (usually 100-1000)
├─ Disk I/O becomes bottleneck
├─ Query locking causes contention
└─ Replication lag grows
```

### Solution 1: Read Replicas

```
Architecture:
┌──────────────────────────────────────┐
│ Application Layer (Stateless)        │
│ Write requests → Primary DB          │
│ Read requests → Random read replica  │
└──────────────────────────────────────┘
           ▲         │
           │         │ Writes
           │         ▼
     Reads │    ┌─────────────┐
      from │    │ Primary DB  │ (accept writes)
      any  │    │ (repl lag=0)│
      one  └────│ Writes logs │
           │    └──────┬──────┘
           │           │
           │      Async Replication
           │           │
       ┌───┴───┐   ┌───┴─────┐   ┌──────┐
       │       │   │         │   │      │
       ▼       ▼   ▼         ▼   ▼      ▼
   ┌──────┐┌──────┐┌──────┐┌──────┐┌──────┐
   │Read  ││Read  ││Read  ││Read  ││Read  │
   │Rep1  ││Rep2  ││Rep3  ││Rep4  ││Rep5  │
   └──────┘└──────┘└──────┘└──────┘└──────┘

Benefits:
├─ Read throughput: 5-10x improvement
├─ Primary DB protected (writes only)
├─ Scale reads independently
└─ High availability (promote replica if primary fails)

Limitations:
├─ Replication lag (seconds to minutes)
├─ Write throughput unchanged
├─ Inconsistent reads (eventual consistency)
└─ Storage cost (replicas take space)

Use case:
├─ Read-heavy workloads (90% reads, 10% writes)
└─ Good starting point for scaling

For 1M users:
└─ Need 5-10 read replicas + primary
```

### Solution 2: Database Sharding (Mandatory for millions)

```
Problem with single database:
├─ Even with replicas, primary bottlenecks on writes
├─ Disk I/O limit: ~100,000 QPS max (even good hardware)
├─ For 1M users at 1 QPS each = 1M QPS needed
└─ Can't fit in single machine

Solution: Sharding (horizontal database scaling)

Concept:
├─ Divide users into groups (shards)
├─ Each shard handles subset of users
├─ Shard Key: Usually user_id (determines which shard)
├─ Query routing: Hash(user_id) % num_shards = shard_id
└─ Each shard is independent (own primary + replicas)

Example with 4 shards:
┌────────────────────────────────────────────────┐
│         Shard Router (Hash-based)              │
│  user_id % 4 = shard_id                        │
│  Directs queries to correct shard database     │
└────────────┬────────────────────────────────────┘
         ┌───┴────┬────────┬───────┬───────┐
         │        │        │       │       │
         ▼        ▼        ▼       ▼       ▼
    ┌─────┐  ┌─────┐  ┌─────┐ ┌─────┐ ┌─────┐
    │Shard│  │Shard│  │Shard│ │Shard│ │Meta │
    │ 0   │  │ 1   │  │ 2   │ │ 3   │ │DB   │
    │     │  │     │  │     │ │     │ │     │
    │U1,  │  │U2,  │  │U4,  │ │U3,  │ │Shard│
    │U5,  │  │U6,  │  │U8,  │ │U7,  │ │Map  │
    │U9.. │  │U10..│  │U12..│ │U11..│ │     │
    └────┬┘  └─────┘  └─────┘ └─────┘ └─────┘
         │
    ┌────▼──────────────┐
    │ Primary + Replicas│
    └───────────────────┘

Benefits:
├─ Write throughput: Linear with shards (4x with 4 shards)
├─ Read throughput: 4x + replication (20-40x total)
├─ Each shard independent (failure isolated)
├─ Can add new shards (resharding possible)
└─ Scales to billions of records

Limitations:
├─ Cross-shard queries hard (joins, aggregations)
├─ Hot shards problem (uneven data distribution)
├─ Resharding complex (split shard into 2)
├─ Distributed transaction complexity
└─ Operational complexity

Shard Keys (how to choose):
├─ User ID (most common, distributes well)
├─ Customer ID (for multi-tenant)
├─ Region/Geography (for location-based)
└─ NOT: Timestamp (creates hot shards)

Formula:
  shard_id = hash(user_id) % num_shards
  Example: user_id = 12345
          hash(12345) = 789456
          789456 % 4 = 0
          → Query shard 0

For 1M users with 100,000 QPS:
├─ 10 shards × 10,000 QPS each
├─ Each shard: 100,000 users
├─ Each shard database: Small & fast
└─ Total capacity: Unlimited (add shards)
```

### Solution 3: NoSQL for Specific Use Cases

```
When NOT to use SQL sharding:
├─ Unstructured data (JSON documents)
├─ Real-time analytics (time-series)
├─ High write throughput (1M writes/sec)
├─ No complex joins needed
└─ Can tolerate eventual consistency

NoSQL Options:
├─ DynamoDB: AWS-managed, auto-scaling, eventual consistency
├─ Cassandra: Distributed, ring topology, high availability
├─ MongoDB: Document-based, flexible schema, good scaling
└─ HBase: Big data, distributed, high throughput

Example: HBase cluster for real-time data
┌───────────────────────────────────┐
│  HBase Region Server              │
│ ├─ RegionA (Users 0-999,999)      │
│ ├─ RegionB (Users 1M-1,999,999)   │
│ ├─ RegionC (Users 2M-2,999,999)   │
│ └─ RegionD (Users 3M-3,999,999)   │
└───────────────────────────────────┘

Advantages:
├─ Automatic sharding by row key range
├─ No manual resharding needed
├─ Handles millions of rows
├─ Distributed by default
└─ Strong consistency

For 1M users:
└─ Consider if SQL sharding complexity too high
```

---

## Caching Layer

### Problem: Database Latency

```
Without cache:
Request → Load Balancer → App Server → Database → 100ms → Response

With cache:
Request → Load Balancer → App Server → Cache (hit) → 1ms → Response

Speed improvement: 100x faster!
```

### Caching Strategy

```
Multi-level Caching:

┌──────────────────────────────────────────────────┐
│ Browser Cache (Static assets)                    │
│ ├─ Cache-Control headers                         │
│ ├─ Expires: 1 year for versioned files          │
│ └─ Saves network bandwidth                       │
└──────────────────────────────────────────────────┘
                        │
                        ▼
┌──────────────────────────────────────────────────┐
│ CDN Cache (CloudFront, Akamai)                   │
│ ├─ Edge locations worldwide                      │
│ ├─ Cache static assets (JS, CSS, images)         │
│ ├─ TTL: 1 day - 1 year                          │
│ └─ Reduces origin load 90%+                      │
└──────────────────────────────────────────────────┘
                        │
                        ▼
┌──────────────────────────────────────────────────┐
│ API Gateway Cache                                │
│ ├─ Cache idempotent GETs                        │
│ ├─ TTL: 5-60 seconds                            │
│ └─ Reduces app load                              │
└──────────────────────────────────────────────────┘
                        │
                        ▼
┌──────────────────────────────────────────────────┐
│ Application Cache (Local/In-memory)              │
│ ├─ L1 cache (in-process, fast)                   │
│ ├─ Guava Cache, Spring Cache                     │
│ ├─ Problems:                                     │
│ │  ├─ Not shared between instances               │
│ │  ├─ Hard to invalidate                         │
│ │  └─ Not suitable for distributed systems      │
│ └─ Use only for single-instance or non-critical  │
└──────────────────────────────────────────────────┘
                        │
                        ▼
┌──────────────────────────────────────────────────┐
│ Distributed Cache (Redis)                        │
│ ├─ Shared across all app instances               │
│ ├─ Sub-millisecond latency                       │
│ ├─ Session store                                 │
│ ├─ Rate limit counters                           │
│ └─ Query result cache                            │
└──────────────────────────────────────────────────┘
                        │
                        ▼
┌──────────────────────────────────────────────────┐
│ Database Cache (Query results, indexes)          │
│ └─ Database buffer pool, query cache             │
└──────────────────────────────────────────────────┘
                        │
                        ▼
┌──────────────────────────────────────────────────┐
│ Persistent Storage                               │
└──────────────────────────────────────────────────┘
```

### Redis Architecture for 1M Users

```
Redis Cluster (6+ nodes for high availability):

┌─────────────────────────────────────┐
│  Application Layer                  │
│  (All instances connect to any node)│
└─────────┬───────────────────────────┘
          │
          ▼
┌──────────────────────────────────────────────────────────┐
│            Redis Cluster (Hash slots 0-16383)           │
│                                                          │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐              │
│  │ Node 1   │  │ Node 2   │  │ Node 3   │              │
│  │ Master   │  │ Master   │  │ Master   │              │
│  │ Slots    │  │ Slots    │  │ Slots    │              │
│  │ 0-5460   │  │ 5461-10922  │ 10923-16383 │          │
│  └────┬─────┘  └────┬─────┘  └────┬─────┘              │
│       │             │             │                     │
│   Replication   Replication   Replication              │
│       │             │             │                     │
│  ┌────▼─────┐  ┌────▼─────┐  ┌────▼─────┐             │
│  │ Node 4   │  │ Node 5   │  │ Node 6   │             │
│  │ Slave 1  │  │ Slave 2  │  │ Slave 3  │             │
│  └──────────┘  └──────────┘  └──────────┘             │
│                                                         │
│ Slot = hash_slot(key) % 16384                          │
│ Node = Ring[Slot]                                      │
│                                                         │
│ Automatic failover:                                    │
│ ├─ Master fails → Slave promoted                       │
│ └─ All data replicated → no data loss                  │
└──────────────────────────────────────────────────────────┘

Use cases in Redis:
├─ Session store (key: user_id, value: session JSON)
├─ Cache (key: query_id, value: query result, TTL: 1 hour)
├─ Rate limiting (key: user_id:minute, value: count)
├─ Leaderboard (sorted sets for top users)
├─ Pub/Sub (real-time messaging)
├─ Counter (real-time user count, online status)
└─ Lock (distributed locking for critical sections)

Example data structures:
┌─────────────────────────────────────┐
│ String: user:1:profile              │
│ {name: "Alice", age: 30}            │
│ TTL: 24 hours                       │
│ Size: 1KB                           │
│ For 1M users: 1GB total             │
├─────────────────────────────────────┤
│ Hash: user:1:settings               │
│ {theme: "dark", lang: "en"}         │
│ TTL: 7 days                         │
├─────────────────────────────────────┤
│ Sorted Set: leaderboard:daily       │
│ {user1: 1000, user2: 950, ...}      │
│ TTL: 1 day                          │
├─────────────────────────────────────┤
│ List: queue:notifications:user1     │
│ [notif1, notif2, notif3]            │
│ TTL: 7 days                         │
│ Size: 1MB (100 notifications)       │
├─────────────────────────────────────┤
│ Counter: rate_limit:user1:minute    │
│ Value: 45 (45 requests this minute) │
│ TTL: 1 minute                       │
└─────────────────────────────────────┘

Memory calculation for 1M users:
├─ Session per user: 1KB
├─ Total sessions: 1M × 1KB = 1GB
├─ Leaderboards: 10MB
├─ Rate limiters: 100MB
├─ Notifications: 100GB (10KB per user)
├─ Other cache: 50GB
└─ Total: ~150GB (need 300GB servers for redundancy)

Redis cluster with 150GB:
├─ 3 nodes × 50GB each (master + backup)
├─ Or 6 nodes × 25GB (distributed)
└─ Cost: ~$10,000-20,000/month
```

---

## Message Queues & Async Processing

### Problem: Long-running Operations

```
Synchronous (blocks user):
User request → Send email → Wait 5 seconds → Response ❌
User: "Why is it so slow?"

Asynchronous (non-blocking):
User request → Queue message → Response immediately ✅ (< 100ms)
             → Worker processes email in background

Total time same, user experience much better!
```

### Event-Driven Architecture

```
Architecture with Kafka:

┌──────────────────────────────────────────────────────────┐
│                 User Request                             │
│              (Create new account)                        │
└────────────────┬─────────────────────────────────────────┘
                 │
                 ▼
        ┌─────────────────┐
        │  App Server     │
        │  1. Validate    │
        │  2. Save to DB  │
        │  3. Publish     │
        │     event       │
        └────────┬────────┘
                 │
                 ▼
        ┌───────────────────────────────────┐
        │ Kafka (Message Queue)             │
        │ Topic: user_created               │
        │ ├─ Partition 0                    │
        │ ├─ Partition 1                    │
        │ ├─ Partition 2                    │
        │ └─ Partition 3                    │
        │ Event: {user_id, email, phone}   │
        └────────┬──────────────────────────┘
                 │
    ┌────────────┼────────────┬──────────────────┐
    │            │            │                  │
    ▼            ▼            ▼                  ▼
┌────────┐  ┌────────┐  ┌────────┐        ┌────────────┐
│Email   │  │SMS     │  │Welcome │        │Analytics   │
│Service │  │Service │  │Service │        │Service     │
│        │  │        │  │        │        │            │
│Send    │  │Send    │  │Track   │        │Record      │
│welcome │  │welcome │  │event   │        │signup      │
│email   │  │SMS     │  │        │        │            │
└────────┘  └────────┘  └────────┘        └────────────┘

Benefits:
├─ Decoupling (services don't depend on each other)
├─ Parallelization (email + SMS + analytics in parallel)
├─ Fault isolation (email service down, doesn't block signup)
├─ Scalability (add workers as needed)
├─ Replay-ability (replay messages if service fails)
└─ Ordering (Kafka maintains order per partition)

Kafka configuration for 1M users:
├─ Brokers: 5-10 (high availability)
├─ Partitions: 100-1000 (parallel processing)
├─ Replication factor: 3 (no data loss)
├─ Retention: 7 days
└─ Throughput: 1M messages/sec possible
```

### Worker Pool Pattern

```
Single service handling all async tasks:

┌───────────────────────────────┐
│  All Events                   │
│  ├─ Email                     │
│  ├─ SMS                       │
│  ├─ Analytics                 │
│  ├─ Image processing          │
│  ├─ Report generation         │
│  └─ Notifications             │
│                               │
│  All Workers (same pool)      │
│  Processes: All task types    │
└───────────────────────────────┘

Problems:
├─ Email slow → blocks image processing
├─ Hard to scale (one type bottleneck)
└─ Monitoring difficult

Better: Multiple specialized workers

┌──────────────────────────────────────────┐
│ Kafka Topics (by type)                   │
├──────────────────────────────────────────┤
│ ┌──────────────────────────────────────┐ │
│ │ emails                               │ │
│ │ Partitions: 10                       │ │
│ │ ┌─────┐┌─────┐...┌─────┐            │ │
│ │ │ P0  ││ P1  │...│ P9  │            │ │
│ │ └─────┘└─────┘...└─────┘            │ │
│ │ Workers: 30 (3 per partition)        │ │
│ └──────────────────────────────────────┘ │
│                                          │
│ ┌──────────────────────────────────────┐ │
│ │ sms                                  │ │
│ │ Partitions: 5                        │ │
│ │ ┌──────┐┌──────┐...┌──────┐         │ │
│ │ │ P0   ││ P1   │...│ P4   │         │ │
│ │ └──────┘└──────┘...└──────┘         │ │
│ │ Workers: 10 (2 per partition)        │ │
│ └──────────────────────────────────────┘ │
│                                          │
│ ┌──────────────────────────────────────┐ │
│ │ image_processing                     │ │
│ │ Partitions: 20                       │ │
│ │ Workers: 60 (3 per partition)        │ │
│ └──────────────────────────────────────┘ │
└──────────────────────────────────────────┘

Benefits:
├─ Email slow doesn't affect SMS
├─ Scale each independently
├─ Workers optimized for task type
└─ Easy to monitor & debug
```

---

## API Gateway & Load Balancing

### API Gateway Responsibilities

```
┌─────────────────────────────────────────────────┐
│           Incoming Request                      │
└────────────────┬────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────┐
│ 1. Authentication                               │
│    ├─ Verify JWT token                         │
│    ├─ Check OAuth scope                        │
│    └─ Block if invalid                         │
└────────────────┬────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────┐
│ 2. Authorization                                │
│    ├─ Check user permissions                   │
│    ├─ Verify resource access                   │
│    └─ Block if not allowed                     │
└────────────────┬────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────┐
│ 3. Rate Limiting                                │
│    ├─ User limit: 1000 req/hour                │
│    ├─ IP limit: 10000 req/hour                 │
│    ├─ Global limit: 100000 req/min             │
│    └─ Return 429 if exceeded                   │
└────────────────┬────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────┐
│ 4. Request Validation                           │
│    ├─ Schema validation                        │
│    ├─ Parameter type checking                  │
│    └─ Return 400 if invalid                    │
└────────────────┬────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────┐
│ 5. Caching (for GETs)                           │
│    ├─ Cache key = hash(method + path + params) │
│    ├─ TTL: 60 seconds for user-specific data   │
│    ├─ Return cached if fresh                   │
│    └─ Skip cache for POSTs/PUTs/DELETEs        │
└────────────────┬────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────┐
│ 6. Routing                                      │
│    ├─ Route to correct microservice            │
│    ├─ Path-based: /users/* → user-service     │
│    ├─ Path-based: /posts/* → post-service     │
│    └─ Version-based: /v1/*, /v2/*             │
└────────────────┬────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────┐
│ 7. Load Balancing (to microservice replicas)    │
│    ├─ Round-robin to healthy instances         │
│    ├─ Health checks                            │
│    └─ Fail-fast if all instances down          │
└────────────────┬────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────┐
│ 8. Monitoring & Logging                         │
│    ├─ Log all requests                         │
│    ├─ Track latency                            │
│    ├─ Monitor error rate                       │
│    └─ Send alerts on anomalies                 │
└────────────────┬────────────────────────────────┘
                 │
                 ▼
         Request to microservice
```

### Rate Limiting Algorithm (Token Bucket)

```
Problem: User spams API with 1000 requests/sec
  → Impacts other users
  → Could be DDoS attack
  → Database overloaded

Solution: Token Bucket Algorithm

Bucket for each user:
  ├─ Capacity: 1000 tokens
  ├─ Refill rate: 1000 tokens/hour (= ~0.28 tokens/sec)
  └─ Each request costs 1 token

When request arrives:
  ├─ Add tokens (based on time since last request)
  ├─ If tokens >= 1:
  │   ├─ Deduct 1 token
  │   └─ Allow request ✅
  │
  └─ If tokens < 1:
     └─ Reject request (return 429) ❌

Example:
  Time 0:00s: Bucket = 1000 tokens, user makes 10 requests
              → 10 tokens used, bucket = 990
              
  Time 0:10s: Refill 10 × 0.28 = 2.8 tokens
              → Bucket = 992.8, user makes 5 requests
              → Bucket = 987.8
              
  Time 0:00 to 10:00: User can make 1000 requests in burst
                      Then rate-limited to 28 per second

Benefits:
├─ Burst traffic allowed (first 1000 reqs fast)
├─ Smooth ratelimit after burst
├─ No artificial delays (no queuing)
└─ Fair for all users

Implementation in Redis:
├─ Key: rate_limit:user_id
├─ Stored as: last_refill_time + tokens
├─ Atomic increment (Lua script)
└─ TTL: Expire if user inactive > 1 hour
```

---

## Data Consistency

### Eventual Consistency vs Strong Consistency

```
Write to Database A → Replicate to B, C, D (async)

User 1: Writes to A, immediately reads from B
├─ B might not have data yet (replication lag)
├─ User sees stale data
└─ This is EVENTUAL CONSISTENCY

Strong Consistency:
├─ Wait for all replicas to acknowledge
├─ Then send response to user
├─ Always read fresh data
├─ But slower (must wait for all replicas)

Trade-off:
├─ Fast, but possibly stale data → Eventual
├─ Slow, but always fresh data → Strong

For 1M users:
  → Usually choose Eventual (performance matters)
  → But handle staleness in application
```

### Handling Eventual Consistency

```
Example: User creates post

❌ What NOT to do:
  1. Create post (write to primary DB)
  2. Immediately query read replica
  3. User might see empty list (data not replicated yet)

✅ What to do:

Option 1: Write-through cache
  1. Create post (write to primary)
  2. Add post to cache immediately (synchronous)
  3. Return success to user
  4. Cache serves data (always fresh)
  5. Replication happens in background
  
Option 2: Read-after-write consistency
  1. Create post (write to primary)
  2. Use connection to primary for next read (if same user)
  3. Only use replica if different user
  
Option 3: Accept and display optimistically
  1. Create post (write to primary)
  2. Display post immediately in client
  3. Actual data syncs in background (eventual)
  4. User always sees their own data
  
Option 4: Include version in read
  1. Create post (get version 100)
  2. Read from replica
  3. Check version: if < 100, retry from primary
```

### Distributed Transactions

```
Problem: Update two services atomically

❌ NOT POSSIBLE in distributed system (Brewer's CAP theorem)

Scenario: Payment to user wallet
  1. Deduct from buyer account
  2. Add to seller account
  
What if:
  ├─ Step 1 succeeds, Step 2 fails?
  │  └─ Buyer loses money, seller doesn't get it
  │
  └─ No way to guarantee both succeed

Solutions:

Option 1: Saga Pattern (Choreography)
  ├─ Service A: Deduct from buyer
  │   → Publishes event: MoneyDebited
  │
  ├─ Service B listens for MoneyDebited
  │   → Add to seller account
  │   → Publishes event: MoneyCredited
  │
  ├─ If Service B fails:
  │   → Listen for MoneyCreditFailed event
  │   → Service A refunds buyer (compensating transaction)
  │
  └─ Eventually consistent, but recoverable

Option 2: Saga Pattern (Orchestration)
  ├─ Orchestrator service controls flow
  │   1. Call Service A: Deduct money
  │   2. Wait for response
  │   3. Call Service B: Add money
  │   4. If Service B fails, call Service A: Refund
  │
  └─ More explicit, easier to debug

For 1M users:
  └─ Use Saga pattern with events
     (eventual consistency + compensation)
```

---

## Interview Q&A

### Q1: How do you handle 1M concurrent users?

**Answer:**
```
I would design a distributed, scalable system:

1. STATELESS Applications
   ├─ No user data stored in app memory
   ├─ Enables horizontal scaling (add servers)
   └─ All state in Redis or database

2. Horizontal Scaling
   ├─ Start with 10 app servers
   ├─ Auto-scale to 100-1000 based on load
   ├─ Behind load balancer (ALB/NLB)
   └─ Spread across 3 AZs

3. Database Scaling
   ├─ Read replicas for reads (10x throughput)
   ├─ Sharding for writes (by user_id, 10-100 shards)
   ├─ Each shard: independent primary + replicas
   └─ Total: 1,000+ QPS per shard

4. Caching Layer (Redis)
   ├─ Distributed cache cluster
   ├─ Session store, query cache, rate limiters
   ├─ Reduces database load 90%+
   └─ Sub-millisecond latency

5. Message Queue (Kafka)
   ├─ Decouple services
   ├─ Handle async operations
   ├─ Email, SMS, notifications in background
   └─ Parallelization

6. CDN for Static Assets
   ├─ CloudFront global edge locations
   ├─ Cache JS, CSS, images
   ├─ Reduce server load
   └─ < 50ms latency worldwide

7. API Gateway
   ├─ Rate limiting (per user)
   ├─ Authentication/authorization
   ├─ Caching idempotent requests
   ├─ Request validation
   └─ Routing to correct service

8. Monitoring & Observability
   ├─ CloudWatch for metrics/logs
   ├─ Prometheus for custom metrics
   ├─ Grafana for visualization
   ├─ Alerts on anomalies
   └─ Distributed tracing (X-Ray)

Result:
├─ Horizontal scalability (add servers/shards)
├─ 99.99% uptime (multi-AZ, auto-failover)
├─ Sub-second latency (caching, CDN)
└─ Handles 1M+ concurrent users
```

### Q2: What's the bottleneck at 1M users?

**Answer:**
```
Database writes become the primary bottleneck:

Without sharding:
  ├─ Single database primary
  ├─ All writes go to one server
  ├─ Disk I/O limit: ~100,000 writes/sec
  ├─ 1M users × 1 write/sec = 1M writes/sec
  └─ IMPOSSIBLE (need 10x capacity)

Solution: Sharding
  ├─ 10 shards × 100,000 writes each
  ├─ user_id % 10 determines shard
  ├─ Each shard handles 100,000 users
  └─ Total: 1M writes/sec ✅

Secondary bottleneck: Joins across shards
  ├─ Query: "Get user + their posts + their comments"
  ├─ User in shard 0, posts in shard 5, comments in shard 8
  ├─ Must query 3 shards (slow, complex)
  └─ Solution: Denormalization + caching

Tertiary bottleneck: Cache efficiency
  ├─ If cache hit rate drops below 80%, load on DB increases
  ├─ Cold start after deployment causes spike
  └─ Solution: Gradual cache warming, circuit breaker
```

### Q3: How do you handle resharding (adding new shards)?

**Answer:**
```
Problem: Initially 10 shards, now need 20 (double)

Naive approach: Re-hash all 1B records
  ├─ Takes hours
  ├─ Database locked
  ├─ Users can't write
  └─ Downtime ❌

Better approach: Gradual resharding

Step 1: Add new shards (11-20)
  ├─ Deploy 10 new database instances
  ├─ No downtime yet
  └─ New shards empty

Step 2: Dual write
  ├─ Deploy new code
  ├─ For each write, calculate both old shard AND new shard
  ├─ Write to BOTH shards
  ├─ Read from old shard (new shard catching up)
  └─ No downtime, no data loss

Step 3: Backfill
  ├─ Background job moves data from old shards
  ├─ Copy old shard 0 → new shards 10, 20, 30...
  ├─ Happens gradually (hours/days)
  └─ Doesn't block user operations

Step 4: Switch reads
  ├─ Deploy new code
  ├─ For reads, use new shard (has all data now)
  ├─ Still write to both
  ├─ Verify data matches
  └─ No downtime

Step 5: Cleanup
  ├─ Deploy new code
  ├─ Stop writing to old shards
  ├─ Decommission old shards
  └─ Sharding: 10 → 20 complete

Total downtime: 0 seconds
Time to complete: 1-7 days
```

### Q4: How do you scale the cache at 1M users?

**Answer:**
```
Redis cluster architecture:

Single Redis instance (not scalable):
  ├─ Max throughput: 50,000 ops/sec
  ├─ Max memory: 256GB
  ├─ 1M users × 100KB cache = 100GB
  └─ Still works, but single point of failure

Redis Cluster (truly distributed):
  ├─ 6-100 nodes
  ├─ Hash slots: 0-16383
  ├─ Each node owns subset of slots
  ├─ Automatic failover
  ├─ Horizontal scaling (add nodes)
  └─ Throughput: 100K-1M ops/sec

Memory calculation for 1M users:
  ├─ Per-user data: 100KB average
  ├─ Total: 100GB
  ├─ With replication (3x): 300GB needed
  ├─ Nodes: 6 × 50GB = 300GB
  └─ Cost: $20,000-30,000/month

Scaling strategy:
  ├─ Start: 3 nodes (15GB each, single AZ)
  ├─ At 100K users: Add 3 more nodes (3 AZ, replication)
  ├─ At 500K users: Add 6 more nodes (12 total)
  ├─ At 1M users: 20-30 nodes for redundancy
  └─ Add nodes without downtime (cluster resharding)
```

### Q5: How do you ensure high availability at 1M users?

**Answer:**
```
Multi-layer redundancy:

Application Layer:
  ├─ 10-100 servers across 3 AZs
  ├─ Load balancer (ALB) health checks every 5s
  ├─ Failed instance → replaced within 30s
  ├─ Stateless (can lose any instance)
  └─ Auto-scaling handles spikes

Database Layer:
  ├─ Primary database (writer)
  ├─ Multi-AZ standby (sync replication)
  ├─ Failover time: 1-2 minutes
  ├─ Read replicas (async) - no data loss
  └─ Cross-region replica (for DR)

Cache Layer:
  ├─ Redis cluster (6+ nodes)
  ├─ Automatic failover (slave → master)
  ├─ Replication factor: 3 (no data loss)
  ├─ Multi-AZ nodes
  └─ Failover time: < 1 second

Message Queue:
  ├─ Kafka cluster (5-10 brokers)
  ├─ Replication factor: 3
  ├─ No single point of failure
  ├─ Auto-recovery after broker failure
  └─ Data persisted to disk

Disaster Recovery:
  ├─ Primary region: us-east-1
  ├─ Secondary region: us-west-2 (warm standby)
  ├─ Route 53 failover: Primary → Secondary (< 5 min)
  ├─ Database: Primary → Replica promotion
  ├─ Cache: Replicated asynchronously
  └─ RTO: 5 minutes, RPO: 5 minutes (or less)

Result:
  ├─ Single instance failure: No impact (< 30s)
  ├─ Single AZ failure: Transparent failover (< 1 min)
  ├─ Single region failure: Geographic failover (< 5 min)
  ├─ Uptime: 99.99% (4 nines, ~43s/month downtime)
  └─ Five nines (99.999%) requires 5+ nines in each component
```

---

## Rough Numbers for 1M Concurrent Users

```
Infrastructure estimation:

Application Servers:
├─ 1 server: 1,000 users
├─ 100 servers needed: For 1M users
├─ Cost per server: $500/month
├─ Total: $50,000/month

Database (with sharding):
├─ 10 shards × (1 primary + 2 replicas) = 30 servers
├─ Server size: r5.2xlarge
├─ Cost per server: $2,000/month
├─ Total: $60,000/month

Cache (Redis cluster):
├─ 30 nodes (for redundancy)
├─ Memory per node: 16GB
├─ Cost: $500/month per node
├─ Total: $15,000/month

Message Queue (Kafka):
├─ 10 brokers
├─ Cost: $2,000/month per broker
├─ Total: $20,000/month

CDN & Load Balancers:
├─ ALB: $16 + data transfer
├─ CloudFront: Pay per GB
├─ Total: $10,000/month

Monitoring, Logging, Backup:
├─ CloudWatch, DataDog, etc.
├─ Total: $5,000/month

TOTAL COST: ~$160,000/month
With redundancy & buffer: ~$200,000-300,000/month

Per user cost: $0.20-0.30/month
(At $100 revenue per user/month, this is acceptable)
```

---

*Last Updated: 2026-04-26*
