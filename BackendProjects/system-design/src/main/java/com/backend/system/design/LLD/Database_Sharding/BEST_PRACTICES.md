# Database Sharding — Best Practices for Production

Proven patterns and anti-patterns for sharded systems.

---

## Shard Key Design Best Practices

### ✅ DO: Choose immutable shard keys

```
Good:
- user_id (never changes)
- customer_id (stable identifier)
- store_id (organizational ID)

Bad:
- country (users relocate)
- subscription_tier (users upgrade)
- email (can change)
```

### ✅ DO: Verify high cardinality

```
Count distinct values:
SELECT COUNT(DISTINCT shard_key) FROM table

High cardinality: > 1 million distinct values
Low cardinality: < 10,000 distinct values

If low, find different key or don't shard
```

### ✅ DO: Test distribution before sharding

```java
// Simulate sharding
Map<Integer, Integer> distribution = new HashMap<>();
for (long userId : allUsers) {
  int shardId = hash(userId) % NUM_SHARDS;
  distribution.merge(shardId, 1, Integer::sum);
}

// Check if balanced (should be within 10% variance)
int avgPerShard = totalUsers / NUM_SHARDS;
for (int count : distribution.values()) {
  double deviation = Math.abs(count - avgPerShard) / (double) avgPerShard;
  if (deviation > 0.1) {
    System.err.println("Unbalanced distribution!");
  }
}
```

### ❌ DON'T: Shard on time-based keys

```
Bad:
- created_at (all new records on latest shard → hot shard)
- date (same issue)

Why: New data always goes to one shard
     Old shards idle
     New shard becomes bottleneck
```

### ❌ DON'T: Shard on geography or tier if skewed

```
Bad:
- country (80% users in USA)
- subscription_tier (90% free users)

Why: Distribution uneven by definition
     Some shards always hotter than others
```

---

## Routing Layer Best Practices

### ✅ DO: Cache shard mappings

```java
// Good: In-memory cache
Map<Long, Integer> shardCache = new ConcurrentHashMap<>();

public int getShardId(long userId) {
  return shardCache.computeIfAbsent(userId, 
    id -> calculateShardId(id));
}

private int calculateShardId(long userId) {
  return Math.abs(userId.hashCode()) % NUM_SHARDS;
}
```

### ✅ DO: Handle shard misses gracefully

```java
try {
  Result result = queryShard(expectedShardId, query);
  return result;
} catch (RecordNotFoundException e) {
  // Shard ID may be wrong (e.g., after resharding)
  // Try other shards
  for (int i = 0; i < NUM_SHARDS; i++) {
    if (i == expectedShardId) continue;
    try {
      return queryShard(i, query);
    } catch (RecordNotFoundException ignored) {}
  }
  throw new RecordNotFoundException("Record not found in any shard");
}
```

### ✅ DO: Implement circuit breakers

```java
public class ShardConnection {
  private CircuitBreaker circuitBreaker = 
    CircuitBreaker.builder()
      .failureThreshold(5)
      .resetTimeout(60, TimeUnit.SECONDS)
      .build();
  
  public Result query(String sql) {
    return circuitBreaker.execute(() -> {
      return shard.execute(sql);
    });
  }
}

// If shard fails 5 times, circuit opens
// No requests sent for 60s
// Then tries again
```

### ❌ DON'T: Hard-code shard locations

```
Bad:
if (userId < 1000000) {
  return "db1.internal:3306";
}

Why:
- Can't easily change routing
- Routing logic scattered
- Hard to maintain

Good:
- Config file or service discovery
- ShardRegistry service
- Dynamic lookup
```

---

## Cross-Shard Query Best Practices

### ✅ DO: Implement cross-shard aggregation carefully

```java
public class CrossShardAggregator {
  public long countAllUsers() {
    long total = 0;
    
    // Query all shards in parallel
    ExecutorService executor = Executors.newFixedThreadPool(NUM_SHARDS);
    List<Future<Long>> futures = new ArrayList<>();
    
    for (int shardId = 0; shardId < NUM_SHARDS; shardId++) {
      futures.add(executor.submit(() -> {
        return getShardConnection(shardId)
          .query("SELECT COUNT(*) FROM users");
      }));
    }
    
    // Aggregate results
    for (Future<Long> future : futures) {
      total += future.get(5, TimeUnit.SECONDS); // Timeout per shard
    }
    
    executor.shutdownNow();
    return total;
  }
}
```

### ✅ DO: Set timeouts on cross-shard queries

```java
// Timeout per shard query
timeout = 5 seconds  // Can't wait forever for one slow shard

// Overall timeout
totalTimeout = 30 seconds  // Give up if takes too long total
```

### ❌ DON'T: Join across shards in database

```
Bad:
SELECT u.*, o.* 
FROM users u 
JOIN orders o ON u.id = o.user_id
WHERE u.id IN (1, 2, 3, ...)
-- If users and orders on different shards, this fails

Good:
1. Query users from Shard A
2. Query orders from Shard B
3. Join in application code
```

---

## Consistency Best Practices

### ✅ DO: Use saga pattern for distributed transactions

```java
public void transferMoney(long fromUserId, long toUserId, double amount) {
  long fromShardId = getShardId(fromUserId);
  long toShardId = getShardId(toUserId);
  
  try {
    // Step 1: Debit
    debit(fromShardId, fromUserId, amount);
    
    // Step 2: Credit
    credit(toShardId, toUserId, amount);
    
  } catch (Exception e) {
    // Compensation: Reverse debit
    credit(fromShardId, fromUserId, amount);
    throw e;
  }
}
```

### ✅ DO: Use event-driven for eventual consistency

```java
// Event: MoneyTransferred
class MoneyTransferredEvent {
  long fromUserId;
  long toUserId;
  double amount;
  long timestamp;
}

// Producer
publishEvent(new MoneyTransferredEvent(...));

// Consumers
// 1. Update from-user balance (Shard A)
// 2. Update to-user balance (Shard B)
// Asynchronous, eventually consistent
```

### ❌ DON'T: Expect strong consistency across shards

```
Bad: Assuming this works across shards
BEGIN;
  UPDATE users SET balance = balance - 100 WHERE id = 1;
  UPDATE users SET balance = balance + 100 WHERE id = 2;
COMMIT;

Why: If users on different shards, not atomic
     If Shard 2 commits but Shard 1 fails, inconsistent

Good: Use saga pattern or events
```

---

## Resharding Best Practices

### ✅ DO: Plan resharding in advance

```
Current: 10 shards handling 50K QPS
Each shard: 5K QPS capacity
Utilization: 100%

When to reshard:
- Add shards when > 80% utilization
- 50K QPS × 1.25 / 5K = 12.5 → 16 shards

Schedule: During low-traffic hours
         Or implement zero-downtime resharding
```

### ✅ DO: Implement dual-write pattern

```
Phase 1 (Write to both):
for (Record record : sourceShards) {
  writeToNewShard(record);  // Background
}
readFrom(oldShard);  // Still reading old

Phase 2 (Verify):
compareData(oldShard, newShard);  // Ensure identical

Phase 3 (Read from new):
readFrom(newShard);  // Switch reads

Phase 4 (Stop writing to old):
stopWriting(oldShard);
```

### ✅ DO: Take backups before resharding

```
Backup plan:
1. Full backup of all shards
2. Store backup for 1 week
3. After resharding validated, delete backup
4. If something wrong, restore from backup
```

### ❌ DON'T: Reshard without validation

```
Bad:
1. Reshard
2. Hope it worked
3. Decommission old shards

Good:
1. Reshard
2. Verify: COUNT(*), checksums, spot checks
3. Run queries and compare results
4. Load test new shards
5. Monitor for 1 week
6. Then decommission
```

---

## Monitoring Best Practices

### ✅ DO: Monitor per-shard metrics

```
For each shard:
- QPS (queries per second)
- Latency (p50, p95, p99)
- CPU utilization
- Memory usage
- Disk usage
- Replication lag
- Connection count

Alert thresholds:
- QPS > 2x average shard → hot shard
- Latency p99 > 500ms → investigate
- Replication lag > 10s → alert
- CPU > 80% → prepare to scale
```

### ✅ DO: Detect hot shards automatically

```java
public void detectHotShards() {
  double avgQps = getTotalQps() / NUM_SHARDS;
  double threshold = avgQps * 2;  // 2x average
  
  for (int shardId = 0; shardId < NUM_SHARDS; shardId++) {
    double shardQps = getShardQps(shardId);
    if (shardQps > threshold) {
      alertManager.alert("Hot shard detected: Shard " + shardId);
    }
  }
}
```

### ❌ DON'T: Monitor only total database metrics

```
Bad: Only monitoring total QPS, total latency
     Hides hot shard problems

Good: Monitor each shard independently
      Compare distributions
      Alert on anomalies
```

---

## High Availability Best Practices

### ✅ DO: Replicate each shard

```
Shard 1
├─ Primary (writes)
├─ Replica 1 (reads)
└─ Replica 2 (reads + backup)

Shard 2
├─ Primary
├─ Replica 1
└─ Replica 2
```

### ✅ DO: Implement automatic failover

```
Primary fails → 
  Promote Replica 1 to Primary →
  Create new Replica 2 →
  Update routing layer →
  Done

Should be automatic, not manual
```

### ✅ DO: Distribute replicas across datacenters

```
Shard 1 Primary: DC1
Shard 1 Replica 1: DC2
Shard 1 Replica 2: DC3

If DC1 fails, Replica 2 in DC3 takes over
No single datacenter loss
```

### ❌ DON'T: Have single point of failure

```
Bad:
- Only 1 replica per shard
- All shards in same datacenter
- No backup

Good:
- 2-3 replicas per shard
- Across multiple datacenters
- Continuous backups
```

---

## Anti-Patterns to Avoid

### ❌ Anti-Pattern 1: Sharding too early

**Problem:** Complexity without benefit
```
Case: 10GB data, 100 QPS
Sharding: Unnecessary, causes problems
Solution: Use single DB until 100GB, 10K QPS
```

**When to shard:** Only when hitting limits

### ❌ Anti-Pattern 2: Wrong shard key

**Problem:** Hot shards, uneven distribution
```
Case: Shard by country (80% USA users)
Result: USA shard 10x busier than others
Solution: Reshard with hash(user_id)
         (expensive, avoid by choosing right key)
```

### ❌ Anti-Pattern 3: Distributed transactions

**Problem:** Slow, deadlock-prone, failure-prone
```
BAD:
BEGIN;
  UPDATE users WHERE id = 1;  // Shard A
  UPDATE orders WHERE user_id = 1;  // Shard B
COMMIT;  // Can fail! No rollback!

GOOD: Saga pattern or events
```

### ❌ Anti-Pattern 4: Cross-shard joins in SQL

**Problem:** Not supported, causes errors
```
BAD:
SELECT u.*, o.* FROM users u JOIN orders o

GOOD: Query separately, join in application
```

### ❌ Anti-Pattern 5: Not planning for resharding

**Problem:** Can't scale beyond current shards
```
Current: 10 shards
When full: Can't add shards without complex resharding
Solution: Design with resharding in mind from start
```

---

## Checklist for Production Sharding

- [ ] Shard key chosen and validated (high cardinality, even distribution)
- [ ] Routing layer implemented and tested
- [ ] Cross-shard queries handled (slow but work)
- [ ] Consistency model chosen (strong within shard, eventual across)
- [ ] Replicas configured for HA
- [ ] Monitoring per-shard metrics
- [ ] Hot shard detection in place
- [ ] Backup strategy planned
- [ ] Resharding process documented
- [ ] Failover tested
- [ ] Load testing completed
- [ ] Runbooks for common operations
- [ ] Team trained on sharded operations

---

✨ **Golden Rules:**

1. **"Shard as late as possible"** — Use replicas and caching first
2. **"Choose shard key carefully"** — Hard to change, critical decision
3. **"Accept eventual consistency"** — Distributed transactions expensive
4. **"Monitor everything"** — Per-shard metrics essential
5. **"Plan to reshard"** — Will be necessary eventually
