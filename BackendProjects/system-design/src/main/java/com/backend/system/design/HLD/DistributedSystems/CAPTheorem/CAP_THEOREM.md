# CAP Theorem: The Foundation of Distributed Systems

**Table of Contents**
1. [Introduction](#introduction)
2. [The Three Guarantees](#the-three-guarantees)
3. [The Fundamental Trade-off](#the-fundamental-trade-off)
4. [Proof & Why It Matters](#proof--why-it-matters)
5. [Real-World Examples](#real-world-examples)
6. [CAP Implications for System Design](#cap-implications-for-system-design)
7. [Beyond CAP: PACELC & Modern Systems](#beyond-cap-pacelc--modern-systems)

---

## Introduction

The **CAP Theorem** (also called **Brewer's Theorem**) is one of the most fundamental principles in distributed systems. Proposed by Eric Brewer in 2000, it states that it's **impossible for a distributed system to simultaneously guarantee all three** of these properties:

1. **Consistency (C)** - All nodes see the same data
2. **Availability (A)** - The system responds to every request
3. **Partition Tolerance (P)** - System continues despite network partitions

In practice, you can pick **at most 2 out of 3**.

---

## The Three Guarantees

### 1. Consistency (C)

**Definition**: Every read returns the most recent write.

All nodes in the system have the **same data at the same time**. If you write value `X` to one node, any subsequent read from any node will return `X`.

**Example:**
```
Bank Account: $1000
├─ Write $500 to Node A
└─ Read from Node B → Should get $1000 (not yet updated)
   Wait for sync
└─ Read from Node B → Should get $500 (updated)
```

**Cost**: High latency (must wait for sync across all nodes)

---

### 2. Availability (A)

**Definition**: Every request gets a response (success or failure).

The system is **always responsive**. Even if the database is updating, clients get a response immediately (it might be stale data, but it's a response).

**Example:**
```
During Node A update:
├─ Request to Node B → Immediate response (even if stale)
└─ Request to Node C → Immediate response (even if stale)

No request ever times out or is refused.
```

**Cost**: May return stale/outdated data

---

### 3. Partition Tolerance (P)

**Definition**: System continues operating despite network partitions.

When the network between nodes is broken and they can't communicate, the system should still function. This is **mandatory in modern distributed systems**.

**Example - Network Partition:**
```
Before:  Node A ←→ Node B
After:   Node A     Node B
         (network is broken)

System must continue working despite this.
```

**Why it's mandatory**: In any distributed system, network partitions WILL happen. It's not a question of "if", but "when". You cannot prevent network failures, so you must tolerate them.

---

## The Fundamental Trade-off

Since **Partition Tolerance is mandatory** (you can't prevent network failures), you must choose between **C** and **A**.

### Three Possible Combinations

| Type | Sacrifices | Meaning |
|------|-----------|---------|
| **CA** | Partition Tolerance | Centralized systems (impossible to partition) |
| **CP** | Availability | Consistent but may be unavailable during partitions |
| **AP** | Consistency | Available but may serve stale data during partitions |

---

### CA Systems (No Partition Tolerance)

**When**: You have a single, centralized database.

**Example**: Traditional SQL databases with single server

```
Database (Central)
│
├─ Client 1
├─ Client 2
└─ Client 3

If network fails between clients and database:
→ System is DOWN (not available)
→ No partition happens (all clients can't reach)
```

**Problem**: Single point of failure. Not suitable for distributed systems.

---

### CP Systems (Available = No, Consistent = Yes)

**When**: You prioritize data consistency over availability.

**Sacrifice**: Availability (system may be temporarily unavailable)

**Example**: HBase, MongoDB with strict consistency, Zookeeper

```
Network Partition:
Node A ←→ Node B ←→ Node C (normal)
Node A     Node B ←→ Node C (partition)

Option:
- Node A: Refuses requests (blocks writes) → UNAVAILABLE
- Nodes B-C: Serve requests → AVAILABLE but smaller quorum

To maintain consistency: majority-based quorum required.
If you have 3 nodes and partition is 1 vs 2, the minority (1 node) becomes unavailable.
```

**When to use**:
- Banking systems (must be consistent)
- Financial transactions
- Booking systems (can't oversell)

**Trade-off Code Example**:
```
CP System Response:
┌─ Write request
├─ Check quorum (majority consensus needed)
├─ If partition: minority partition REJECTS writes
│  Error: "Cannot reach majority, refusing write"
├─ Clients on minority partition: WAIT or ERROR
└─ Availability is sacrificed for consistency
```

---

### AP Systems (Consistent = No, Available = Yes)

**When**: You prioritize availability over consistency.

**Sacrifice**: Consistency (system may serve stale data)

**Example**: Dynamo, Cassandra, Eventually Consistent systems, DNS

```
Network Partition:
Node A ←→ Node B ←→ Node C (normal)
Node A     Node B ←→ Node C (partition)

Option:
- All nodes continue accepting writes!
- Node A writes X=5
- Nodes B,C write X=10
- When partition heals: CONFLICT (both are correct writes!)

Solution: Eventually consistent
- Write succeeds immediately (available)
- Later: sync and resolve conflicts (eventual consistency)
- Data is temporarily inconsistent
```

**When to use**:
- Social media feeds (stale content OK)
- Caching layers (stale data acceptable)
- User profile caches
- Real-time analytics

**Trade-off Code Example**:
```
AP System Response:
┌─ Write request
├─ Immediately acknowledge write
│  Return: "Write accepted"
├─ Replicate in background
├─ If partition: replication fails
│  But write still succeeded locally
├─ When partition heals: conflict resolution
└─ Data is eventually consistent
```

---

## Proof & Why It Matters

### Why CAP Must Be True

**Proof Sketch** (Informal):

Imagine a distributed system with 2 nodes in different partitions:

```
Partition 1: Node A
Partition 2: Node B
(Network broken)

Client in Partition 1: "Write value X"
Client in Partition 2: "Read the value"
```

**The Dilemma**:

1. **If you choose C (Consistency)**:
   - Node B must know about X before responding to read
   - But network is broken! B doesn't know about X
   - B must WAIT or REJECT the read request
   - **Result: Not Available** → You lose A

2. **If you choose A (Availability)**:
   - Node B immediately responds with whatever it knows (maybe X=5)
   - Node A just wrote X=10
   - B returns stale data
   - **Result: Not Consistent** → You lose C

**Conclusion**: In a network partition, you MUST sacrifice either C or A.

---

## Real-World Examples

### Google Spanner: CP System

**Choice**: Consistency > Availability

```java
// Spanner guarantees:
// 1. Strong consistency across globe
// 2. Uses atomic clocks (TrueTime) to order transactions
// 3. During partition: waits until consensus is reached

Transaction {
    write data
    → Synchronous replication to majority
    → Consensus before returning success
    → If partition: WAITS (unavailable temporarily)
}

Cost: Higher latency (must wait for sync)
Benefit: No inconsistent reads ever
```

---

### Amazon DynamoDB: AP System

**Choice**: Availability > Consistency

```java
// DynamoDB guarantees:
// 1. Always available (eventually consistent)
// 2. Writes replicate asynchronously
// 3. Returns immediately even if replication pending

DynamoDB API {
    PutItem {
        ├─ Accept write immediately
        ├─ Return to client (SUCCESS)
        ├─ Replicate to other regions async
        ├─ If network partition: local write still succeeds
        └─ Eventually consistent when partition heals
    }
}

Cost: May read stale data
Benefit: Always fast response
```

---

### MongoDB: Configurable (CP by default, can be AP)

```java
// Default: CP (Replica Set with majority quorum)
db.insertOne(doc, {
    writeConcern: { w: "majority" }  // Waits for majority
    // If partition: minority replicas become unavailable
});

// Can change to AP (acknowledge immediately)
db.insertOne(doc, {
    writeConcern: { w: 1 }  // Acknowledge on primary only
    // Available but potentially inconsistent
});
```

---

## CAP Implications for System Design

### Decision Tree for Choosing C or A

```
┌─ Is data loss unacceptable?
│  ├─ YES → Choose CP
│  │  Examples: Banking, Insurance, Medical records
│  │  System: Can tolerate downtime, not data inconsistency
│  │
│  └─ NO → Choose AP
│     Examples: Social media, Cache, Analytics
│     System: Can tolerate stale data, not downtime
│

┌─ Can your business afford downtime?
│  ├─ NO (must stay online) → Choose AP
│  │  Example: Netflix, Twitter (downtime costs millions)
│  │
│  └─ YES (brief unavailability OK) → Choose CP
│     Example: Banks (can go offline briefly)
```

---

### Real Design Examples

#### Example 1: User Registration (CP Approach)

```java
// Need: No duplicate usernames (must be consistent)

interface UserRegistration_CP {
    // Synchronous write to primary + replicas
    Result registerUser(String username) {
        ├─ Acquire lock on username
        ├─ Check all replicas (majority quorum)
        ├─ If duplicate: REJECT (consistent)
        ├─ If partition: WAIT (unavailable)
        └─ Write to all nodes
    }
}
```

**Why CP**: Duplicate usernames would be inconsistent business logic.

---

#### Example 2: User Timeline Feed (AP Approach)

```java
// Need: Timeline always available (fast feed loading)

interface Timeline_AP {
    Result getFeed(userId) {
        ├─ Fetch from nearest cache
        ├─ Return immediately (might be 5 mins old)
        ├─ Refresh asynchronously
        └─ Client occasionally sees stale tweets (OK)
    }
}
```

**Why AP**: Seeing a tweet 5 minutes late is acceptable for user experience.

---

## Beyond CAP: PACELC Theorem

CAP is too simplistic for modern systems. **PACELC** extends it:

```
PACELC: Partition→Availability/Consistency, Else→Latency/Consistency

IF   network partition exists:
     THEN  choose between Availability or Consistency
ELSE (normal operation, no partition):
     THEN  choose between Latency or Consistency
```

### Example: Dynamodb

```
During Partition → Choose Availability (AP)
During Normal → Choose Latency over Consistency
                (eventual consistency is default)

Result: High availability always, but eventual consistency
```

---

## Modern Reality

Most production systems are **not pure AP or CP**. They use:

### 1. **Multi-Region Replication**
```
Region 1 (CP mode)  ←→  Region 2 (CP mode)
  ↓ (network fails)
Region 1 (AP mode)  ✗   Region 2 (AP mode)

When regions can't sync: Each becomes AP internally
When regions reconnect: Sync and resolve conflicts
```

### 2. **Tunable Consistency**
```
MongoDB:
- Default: CP (waits for majority)
- Can configure: Acknowledge immediately (AP)

Cassandra:
- Read preference: ONE (AP), QUORUM (stronger), ALL (CP)
- Write preference: ANY (AP), ONE, QUORUM, ALL (CP)
```

### 3. **Hybrid Approach**
```
System with:
- Consistency layer: CP (Zookeeper, Etcd) for critical data
- Availability layer: AP (Cache, CDN) for non-critical data

Example: LinkedIn
- User identity: CP (strong consistency)
- User timeline: AP (eventual consistency)
```

---

## Summary Table

| Property | CP System | AP System | Best For |
|----------|-----------|-----------|----------|
| **Consistency** | ✅ Always up-to-date | ❌ May be stale | |
| **Availability** | ❌ May be unavailable | ✅ Always responds | |
| **Partition Tolerance** | ✅ Required | ✅ Required | |
| **Latency** | Higher | Lower | |
| **Data Loss Risk** | Low | High | |
| **Use Case** | Banking, Booking | Social, Cache | |
| **Examples** | Spanner, PostgreSQL | DynamoDB, Cassandra | |

---

## Key Takeaways

1. **CAP is a constraint, not a choice**: You must pick C or A when partition occurs.

2. **Partition Tolerance is non-negotiable**: Network failures happen. You must handle them.

3. **Your choice depends on business requirements**:
   - Lose money if inconsistent? → CP
   - Lose users if unavailable? → AP

4. **Most systems are hybrid**: Different data types get different guarantees.

5. **Modern databases are tunable**: You can adjust consistency level per operation.

6. **CAP is not the whole story**: PACELC gives more nuance for normal operations.

---

## Practice Questions

1. **Why can't a distributed system have all three (C, A, P)?**
   - Answer: In a network partition, serving different versions of data makes it inconsistent. Refusing requests during partition makes it unavailable.

2. **Is Partition Tolerance optional?**
   - Answer: No. Network failures are inevitable in distributed systems. You must handle them.

3. **Which databases are CP?**
   - Answer: Google Spanner, HBase, MongoDB (with strict quorum), PostgreSQL + custom replication

4. **Which databases are AP?**
   - Answer: DynamoDB, Cassandra, Riak, CouchDB

5. **How does MongoDB balance CAP?**
   - Answer: By default CP (replica set with quorum), but writes to primary immediately can act AP

6. **Can you have all three?**
   - Answer: Only if network never partitions (CA), but that's not distributed.

---

## Most Common Interview Questions on CAP Theorem

### Q1: "Explain the CAP Theorem"

**Expected Answer Structure**:
```
1. Definition (1 minute)
   - CAP stands for Consistency, Availability, Partition Tolerance
   - States: In distributed system, can pick at most 2 of 3

2. Explain each (3 minutes)
   - Consistency: All nodes see same data at same time
   - Availability: Every request gets response
   - Partition Tolerance: System works despite network partition

3. The Constraint (2 minutes)
   - P is mandatory (network failures are inevitable)
   - Must choose between C and A
   - Cannot have both

4. Trade-off (2 minutes)
   - CP: Waiting for consensus (unavailable)
   - AP: Serving stale data (inconsistent)
```

**Red Flags** (Things NOT to say):
- ❌ "Partition Tolerance is optional"
- ❌ "Modern systems can have all three"
- ❌ "It's not relevant anymore"
- ❌ "I don't remember the details"

**Green Flags** (Things to say):
- ✅ "Partition Tolerance is mandatory, so you choose C or A"
- ✅ "Business requirements drive the choice"
- ✅ "Different data types have different needs"
- ✅ "Real systems are tunable/hybrid"

---

### Q2: "Describe a system that is CP and one that is AP"

**Expected Answer**:

**CP System - Bank Transfer**:
```
Requirement: No duplicate debits (must be consistent)

Design:
├─ Client initiates transfer
├─ Write to primary database
├─ Wait for majority replication (2 out of 3)
└─ Return success only if consensus reached

During Network Partition:
├─ Partition A (1 node): Rejects writes → UNAVAILABLE
├─ Partition B (2 nodes): Accepts writes → AVAILABLE
└─ System is unavailable on minority partition (OK)

Why CP: Financial consistency > availability
```

**AP System - Twitter Timeline**:
```
Requirement: Timeline always available (fast UX)

Design:
├─ Client posts tweet
├─ Save locally immediately
├─ Replicate to cache/replicas asynchronously
└─ Return success immediately

During Network Partition:
├─ Each cache continues serving requests
├─ Different caches have different data
├─ Eventually consistent when partition heals

Why AP: Availability > perfect consistency
```

---

### Q3: "How would you design a system for Twitter? CP or AP?"

**Expected Answer Process**:

**Step 1: Understand Requirements**
```
Read:
├─ 500M daily active users
├─ Real-time feed updates
├─ High traffic, global distribution
└─ Downtime = revenue loss

Analysis:
└─ Must prioritize availability
```

**Step 2: Choose AP**
```
Decision: AP (Availability > Consistency)

Reasoning:
├─ Seeing tweets 5 mins late: ACCEPTABLE
├─ Downtime for 1 minute: UNACCEPTABLE
├─ Users bounce to competitors if unavailable
└─ Must stay online 99.99%
```

**Step 3: Design Architecture**
```
Architecture:
├─ Primary database: Cassandra (AP)
├─ Cache layer: Redis (AP)
├─ Message queue: Kafka (eventual consistency)
├─ CDN: Cloudflare (cached content)
└─ Multiple regions: Each acts independently during partition

Eventual Consistency Handling:
├─ Timestamps on all data
├─ Idempotent operations
├─ Conflict resolution (last-write-wins)
└─ Periodic sync when partition heals
```

**Step 4: Address Concerns**
```
Interviewer: "What if tweets are duplicated?"
Answer: 
├─ Idempotent tweet IDs
├─ Deduplication on read
├─ Eventually consistent view

Interviewer: "What about data loss?"
Answer:
├─ Persistence to disk immediately
├─ Multiple replicas (replication factor = 3)
├─ Backup to S3
└─ Loss acceptable < data unavailable
```

---

### Q4: "How would you design a payment system? CP or AP?"

**Expected Answer Process**:

**Step 1: Understand Requirements**
```
Critical Needs:
├─ No duplicate charges (consistency critical)
├─ No lost payments (data integrity)
├─ High availability (must process payments)
└─ Low latency (UX matter)
```

**Step 2: Choose CP (with optimization)**
```
Decision: CP (with availability optimization)

Why:
├─ Duplicate charge = customer loses money
├─ Lost payment = company loses revenue
├─ Both are worse than brief unavailability
├─ Customers accept 5 second wait for payment
```

**Step 3: Design Architecture**
```
Architecture:
├─ Primary Database: PostgreSQL (strong consistency)
├─ Replication: Synchronous quorum
├─ Write Ahead Logging: Disk persistence
├─ Idempotency: Request IDs prevent duplicates
└─ Circuit Breaker: Fail fast if consensus fails

Quorum Approach:
├─ 3 replicas: Write to 2 minimum
├─ During partition: Minority can't write
├─ Majority partition processes payments
└─ Minority partition rejects (unavailable but safe)
```

**Step 4: Optimize for Availability**
```
While Maintaining Consistency:
├─ Asynchronous audit logging (eventual)
├─ Caching for frequently accessed data
├─ Read from replica (eventual consistency OK for reads)
├─ Write to primary + majority (strong consistency)
└─ Timeout = fail fast (don't hang)

Result: ~99.9% availability while maintaining CP
```

---

### Q5: "What happens during a network partition?"

**Expected Answer**:

**Scenario**: 5-node Cassandra cluster (AP) partitions into 3 vs 2

```
Before Partition:
Nodes: [1] ←→ [2] ←→ [3] ←→ [4] ←→ [5]
All synchronized

Network Partition Occurs:
Partition A: [1] [2] [3]
             ✗ (broken) ✗
Partition B: [4] [5]

What Happens:
├─ AP System (Cassandra):
│  ├─ Partition A: Writes succeed locally
│  ├─ Partition B: Writes succeed locally
│  ├─ Data diverges (different versions)
│  └─ When healed: Conflict resolution needed
│
└─ CP System (MongoDB with strict quorum):
   ├─ Partition A (majority): Accepts writes
   ├─ Partition B (minority): Rejects writes
   └─ When healed: Sync from majority

During Partition (AP):
┌─ Client writes X=5 to Partition A
├─ Client writes X=10 to Partition B
├─ Both succeed (available)
└─ Data is now inconsistent

When Partition Heals (AP):
┌─ Nodes detect each other
├─ Find conflicting versions (X=5 vs X=10)
├─ Use resolution strategy:
│  ├─ Last-Write-Wins: X=10
│  ├─ Custom logic: Merge values
│  └─ Application decides: Ask user
└─ Eventually consistent
```

**Recovery Time**:
```
CP System: 
└─ Immediate once partition heals
   (minority was not accepting writes)

AP System:
└─ Time depends on:
   ├─ Data size
   ├─ Conflict resolution logic
   └─ Replication factor
```

---

### Q6: "Compare MongoDB and DynamoDB"

**Expected Answer**:

| Aspect | MongoDB | DynamoDB |
|--------|---------|----------|
| **CAP Choice** | CP (by default) | AP |
| **Consistency** | Strong (quorum writes) | Eventual |
| **Partition Behavior** | Minority partition unavailable | All partitions available |
| **Read Latency** | Higher (waits for sync) | Lower (local reads) |
| **Use Case** | Relational data, ACID | Real-time analytics, caching |
| **Trade-off** | Slower, safer | Faster, eventually consistent |

**Deep Dive**:

```
MongoDB (CP):
├─ Default: Write concern "majority"
├─ Wait for replication before returning
├─ Minority replicas become unavailable during partition
└─ Benefit: No inconsistent reads

DynamoDB (AP):
├─ Default: Write to one replica immediately
├─ Replicate asynchronously
├─ All regions available during partition
└─ Cost: Reads might be stale
```

**When to use each**:

```
Use MongoDB when:
├─ Consistency > Availability
├─ Examples: User accounts, Inventory
└─ Latency acceptable (100-200ms)

Use DynamoDB when:
├─ Availability > Consistency
├─ Examples: Session cache, Analytics
└─ Latency critical (<10ms)
```

---

### Q7: "Can we have both strong consistency AND high availability?"

**Expected Answer - "The Nuanced View"**:

```
Short Answer: Not during network partitions.

But in practice:
├─ 99.9% of time: No partition (normal operation)
│  ├─ Can have strong consistency
│  ├─ Can have high availability
│  └─ Normal operation is fast
│
└─ 0.1% of time: Network partition
   ├─ Must choose: C or A
   ├─ If CP: Becomes unavailable
   └─ If AP: Becomes inconsistent

Modern Approach (Hybrid):
├─ Read from replica: Eventual consistency OK
├─ Write to majority: Strong consistency
├─ Caching layer: Fast reads, eventual consistency
├─ Critical data: CP (user identity)
└─ Non-critical data: AP (cache)

Result: High availability most of time, consistency where it matters
```

**Real Example - Netflix**:
```
Strong Consistency Layer:
├─ User identity: CP
├─ Payment info: CP
└─ Subscription status: CP

Eventual Consistency Layer:
├─ Recommendations: AP
├─ Viewing history: AP
├─ User preferences: AP

Result: Fast UX + safe payments
```

---

### Q8: "What is PACELC? How does it improve on CAP?"

**Expected Answer**:

```
PACELC = "Partition → Availability/Consistency, Else → Latency/Consistency"

Two Scenarios:

Scenario 1 - Network Partition (P):
└─ Choose: Availability (A) OR Consistency (C)

Scenario 2 - Normal Operation, No Partition (E = Else):
└─ Choose: Latency (L) OR Consistency (C)
   (Can't have both fast AND consistent all the time)

Example: DynamoDB
├─ During Partition: Choose Availability (A)
├─ During Normal: Choose Latency (L) over Consistency
│  └─ Default: "eventual consistency" for speed
└─ Type: PA/EL (AP + EL)

Example: Google Spanner
├─ During Partition: Choose Consistency (C)
├─ During Normal: Choose Consistency (C)
│  └─ Uses TrueTime: atomic clocks for ordering
└─ Type: CP/EC (CP + EC - most expensive)
```

---

### Q9: "Design a distributed cache. CP or AP?"

**Expected Answer**:

```
Requirement Analysis:
├─ Cache should be fast (availability critical)
├─ Stale data acceptable (cache is secondary)
├─ Downtime = go to database (OK)
└─ Consistency = nice but not required

Decision: AP (Availability)

Design:
├─ Multiple cache nodes (replicas)
├─ Async replication between nodes
├─ Each node serves requests independently
├─ TTL-based expiration

Partition Scenario:
├─ Partition A: Cache serves from local data
├─ Partition B: Cache serves from local data
├─ Both return fastest response (might be stale)
└─ Application handles cache misses by querying DB

Recovery:
├─ When partition heals: Sync cache
├─ TTL automatically expired stale data
└─ Eventually consistent

Technology: Redis Cluster, Memcached
```

---

### Q10: "What are the hidden costs of CP vs AP?"

**Expected Answer**:

```
CP System Costs:
├─ Latency: Wait for majority confirmation
│  └─ 100ms → 500ms round trip
├─ Throughput: Sequential quorum writes
│  └─ 10K writes/sec → 3K writes/sec
├─ Availability: Minority partition down
│  └─ 99.99% → 99% (worse SLA)
└─ Complexity: Handle unavailability gracefully
   └─ Retry logic, fallbacks, UX messaging

AP System Costs:
├─ Data Loss Risk: Async replication can fail
│  └─ Write succeeds locally, then network dies
├─ Eventual Consistency Complexity:
│  └─ Conflict resolution, merging data
├─ Stale Data: Customers see old information
│  └─ Outdated pricing, inventory, balance
└─ Debugging: Eventually consistent bugs are hard to find
   └─ Race conditions appear randomly

Decision Framework:
├─ If cost of consistency loss > cost of latency
│  └─ Choose CP
└─ If cost of latency > cost of temporary inconsistency
   └─ Choose AP
```

---

### Q11: "How do you handle consistency in an AP system?"

**Expected Answer**:

```
Challenge: AP systems are eventually consistent
Solution: Multiple strategies

1. Idempotent Operations
├─ Same request = same result
├─ Implementation:
│  ├─ Unique request ID
│  ├─ Idempotency key in database
│  └─ Return previous result if duplicate
└─ Prevents: Duplicate charges, duplicate posts

2. Vector Clocks / Timestamps
├─ Track causality of events
├─ Compare: Which update is newer
├─ Resolve: Take latest timestamp
└─ Problem: Clock skew, not deterministic

3. Conflict Resolution
├─ Last-Write-Wins (LWW): Simple, may lose data
├─ Custom Logic: Domain-specific merge
├─ Application Decides: Ask user to choose
└─ Timestamp: Include in every update

4. Read Repair
├─ On read: Compare replicas
├─ If stale: Update with fresh version
├─ Lazy consistency: Fix on demand

5. Anti-Entropy / Merkle Trees
├─ Background sync job
├─ Merkle tree: Find differing data
├─ Repair: Sync missing data
└─ Periodic full reconciliation

Example - Cassandra:
├─ Write timestamp with data
├─ Read from multiple replicas
├─ Choose version with highest timestamp
├─ Write back to stale replicas (read repair)
```

---

### Q12: "When should I use EACH database type?"

**Expected Answer**:

```
PostgreSQL (CP):
├─ Reason: Strong ACID, relational
├─ Use: User data, Orders, Accounts
└─ When: Consistency > Latency

MongoDB (Configurable CP/AP):
├─ Reason: Flexible, tunable
├─ Use: Document storage, User profiles
└─ When: Need both consistency and flexibility

Cassandra (AP):
├─ Reason: High availability, distributed
├─ Use: Time-series data, Logs, Analytics
└─ When: Availability > Consistency

DynamoDB (AP):
├─ Reason: Managed, serverless, fast
├─ Use: Sessions, Cache, Real-time data
└─ When: Availability > Consistency + AWS ecosystem

Redis (AP):
├─ Reason: In-memory, extremely fast
├─ Use: Cache, Sessions, Leaderboards
└─ When: Speed > Consistency

HBase (CP):
├─ Reason: Consistency + Big Data
├─ Use: Consistency + huge scale
└─ When: Hadoop ecosystem + CP needs
```

---

## References

- Eric Brewer's "Towards Robust Distributed Systems" (2000)
- "CAP Twelve Years Later" - Eric Brewer (2012) - revisiting CAP
- PACELC Theorem - Abadi (2010)
- Designing Data-Intensive Applications - Martin Kleppmann

