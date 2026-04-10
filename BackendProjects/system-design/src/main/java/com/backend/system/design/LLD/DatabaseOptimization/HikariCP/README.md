# HikariCP — What is it?

## The Problem First

Every time your application needs to talk to the database, it must:

1. Open a network connection to the DB server
2. Authenticate (username + password handshake)
3. Execute the query
4. Close the connection

Steps 1 and 2 are **expensive** — they can take 20–100ms just for the connection setup,
before your query even runs.

Now imagine 1000 users hitting your API simultaneously. That's 1000 connections
being created and destroyed every second. Your app slows down, your DB gets overwhelmed.

**This is the problem HikariCP solves.**

---

## What is HikariCP?

HikariCP is a **JDBC Connection Pool** library for Java.

Instead of creating a new DB connection for every request and closing it after,
HikariCP:
1. Creates a **pool of connections upfront** when the app starts
2. **Lends** a connection to your code when needed
3. **Takes it back** (not closes it) when you're done
4. **Reuses** the same connections for future requests

```
Without HikariCP:

Request-1  →  open connection  →  query  →  close connection   (expensive every time)
Request-2  →  open connection  →  query  →  close connection
Request-3  →  open connection  →  query  →  close connection


With HikariCP:

App starts → HikariCP creates 10 connections and keeps them ready

Request-1  →  borrow connection-1  →  query  →  return connection-1
Request-2  →  borrow connection-2  →  query  →  return connection-2
Request-3  →  borrow connection-1  →  query  →  return connection-1  ← reused!
```

The connection is never truly "closed" — it stays alive in the pool, ready for the next request.

---

## Why "Hikari"?

Hikari (光) means **"light"** in Japanese.
It's named for being the **fastest** JDBC connection pool available for Java.

Benchmarks consistently show HikariCP outperforming alternatives like:
- C3P0
- DBCP (Apache)
- Tomcat JDBC Pool

It is the **default connection pool in Spring Boot** since version 2.0.

---

## How it works internally

```
┌─────────────────────────────────────────────┐
│                 HikariCP Pool                │
│                                              │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  │
│  │  Conn-1  │  │  Conn-2  │  │  Conn-3  │  │  ← idle, waiting
│  └──────────┘  └──────────┘  └──────────┘  │
│                                              │
│  ┌──────────┐  ┌──────────┐                 │
│  │  Conn-4  │  │  Conn-5  │                 │  ← in use by requests
│  └──────────┘  └──────────┘                 │
└─────────────────────────────────────────────┘
         ▲                    ▲
    borrow/return        borrow/return
         │                    │
    Request-A            Request-B
```

**What happens when all connections are busy?**
- New requests **wait** in a queue
- If they wait longer than `connectionTimeout` → exception thrown
- This is your signal that pool size needs to be increased

---

## Key Configuration Properties

```yaml
# application.yml (Spring Boot)
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mydb
    username: root
    password: secret
    hikari:
      pool-name: MyAppPool

      # How many connections to keep in the pool
      maximum-pool-size: 10       # default: 10

      # Minimum idle connections to maintain
      minimum-idle: 5             # default: same as maximum-pool-size

      # How long a request waits for a connection before failing (ms)
      connection-timeout: 30000   # default: 30 seconds

      # How long a connection can sit idle before being removed
      idle-timeout: 600000        # default: 10 minutes

      # Maximum lifetime of a connection in the pool (ms)
      max-lifetime: 1800000       # default: 30 minutes

      # Query to test if a connection is still alive
      connection-test-query: SELECT 1
```

---

## Each property explained

### `maximum-pool-size`
The total number of connections HikariCP will create.

```
maximum-pool-size = 10

→ At most 10 simultaneous DB queries can run in parallel
→ The 11th request waits until one of the 10 finishes
```

**How to size it?**
HikariCP's own formula:
```
pool size = (number of CPU cores × 2) + number of spindle disks
```
For a 4-core machine with SSD: `4 × 2 + 1 = 9` → use 10.

Common mistake: setting it to 100+ thinking "more = better."
More connections = more DB memory used + more context switching = slower.

---

### `minimum-idle`
HikariCP will always keep at least this many connections open, even when idle.

```
minimum-idle = 5
→ Even at 3am with no traffic, 5 connections stay alive
→ When traffic spikes, those 5 are immediately ready
```

If `minimum-idle` = `maximum-pool-size`, the pool is **fixed size** (recommended for most apps).

---

### `connection-timeout`
How long your application waits to borrow a connection from the pool.

```
connection-timeout = 30000ms (30 seconds)

Request comes in → all 10 connections busy
→ waits up to 30 seconds for one to free up
→ if still none available → throws SQLException
```

If you see `SQLTimeoutException: Unable to acquire JDBC Connection` in logs,
this timeout is being hit — pool is too small.

---

### `idle-timeout`
A connection sitting idle for this long gets removed from the pool (to free DB resources).

```
idle-timeout = 600000ms (10 minutes)

Traffic drops at night → connections become idle
→ after 10 minutes idle → HikariCP closes them
→ pool shrinks toward minimum-idle
```

---

### `max-lifetime`
Maximum age of any connection, regardless of whether it's being used.

**Why does this matter?**
DBs and firewalls often kill connections after a certain time (e.g., MySQL's `wait_timeout`).
If HikariCP holds a connection longer than the DB's timeout, the next query on that
connection will fail with "connection closed" error.

```
max-lifetime = 1800000ms (30 minutes)

→ HikariCP proactively closes and recreates connections before the DB kills them
→ Set this slightly lower than your DB's wait_timeout
```

---

## HikariCP in Spring Boot

Spring Boot auto-configures HikariCP — you don't need to write any setup code.
Just add the dependency and set properties.

```xml
<!-- pom.xml — already included in spring-boot-starter-data-jpa -->
<dependency>
    <groupId>com.zaxxer</groupId>
    <artifactId>HikariCP</artifactId>
</dependency>
```

To verify HikariCP is being used, look for this in startup logs:
```
HikariPool-1 - Starting...
HikariPool-1 - Start completed.
```

---

## Monitoring the pool

HikariCP exposes metrics via JMX and Micrometer (if on Spring Boot + Actuator):

| Metric | What it tells you |
|--------|------------------|
| `hikaricp.connections.active` | Connections currently in use |
| `hikaricp.connections.idle` | Connections waiting in pool |
| `hikaricp.connections.pending` | Requests waiting for a connection |
| `hikaricp.connections.timeout` | How many requests timed out waiting |

**Red flags to watch:**
- `pending` is consistently > 0 → pool too small
- `timeout` is increasing → pool too small or queries too slow
- `active` always = `maximum-pool-size` → pool is saturated

---

## Common Mistakes

### 1. Pool size too large
```yaml
maximum-pool-size: 200  # ❌ wastes DB memory, causes context switching
maximum-pool-size: 10   # ✅ for most applications
```

### 2. Not setting max-lifetime lower than DB timeout
```yaml
# MySQL default wait_timeout = 8 hours (28800 seconds)
max-lifetime: 1800000   # ✅ 30 min — well below MySQL's limit
```

### 3. Connection leak — not closing connections
```java
// BAD — connection borrowed, never returned
Connection conn = dataSource.getConnection();
conn.prepareStatement("SELECT 1").execute();
// forgot to close → connection stuck in "in use" forever

// GOOD — try-with-resources auto-returns it to the pool
try (Connection conn = dataSource.getConnection()) {
    conn.prepareStatement("SELECT 1").execute();
} // returned to pool here ✅
```

If you suspect a leak, enable:
```yaml
hikari:
  leak-detection-threshold: 2000  # warn if connection held > 2 seconds
```

---

## Summary

```
Problem:   Creating a DB connection for every request is slow and expensive.

Solution:  HikariCP maintains a pool of reusable connections.
           Borrow → Use → Return. No open/close overhead per request.

Key idea:  Pool size is NOT "more = better."
           Right size = (CPU cores × 2) + 1, tuned by monitoring.

In Spring Boot: it's the default. Zero setup needed beyond properties.
```

---

## Quick Reference

```yaml
hikari:
  maximum-pool-size: 10       # total connections (tune this first)
  minimum-idle: 5             # always keep these ready
  connection-timeout: 30000   # fail fast if pool exhausted
  max-lifetime: 1800000       # recycle before DB kills the connection
  leak-detection-threshold: 2000  # catch unreturned connections
```
