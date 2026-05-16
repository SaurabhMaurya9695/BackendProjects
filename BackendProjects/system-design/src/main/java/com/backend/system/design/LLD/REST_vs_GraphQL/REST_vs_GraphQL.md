# REST vs GraphQL — Deep Dive

A comprehensive architectural comparison of the two dominant API paradigms.

## Table of Contents

1. [Design Philosophy](#design-philosophy)
2. [Architecture](#architecture)
3. [Data Fetching](#data-fetching)
4. [Caching](#caching)
5. [Versioning](#versioning)
6. [Real-time Communication](#real-time-communication)
7. [Observability & Debugging](#observability--debugging)
8. [Security](#security)
9. [Performance](#performance)
10. [Operations & Scaling](#operations--scaling)
11. [Strengths & Weaknesses](#strengths--weaknesses)

---

## 1. Design Philosophy

### REST (Representational State Transfer)

**Core Principle:** Everything is a **resource**. Resources are identified by URIs. Operations on resources follow standard HTTP methods.

```
Resource: Order #101
Operations:
  GET    /api/orders/101       → Retrieve order
  POST   /api/orders           → Create order
  PUT    /api/orders/101       → Update order
  DELETE /api/orders/101       → Delete order
```

**Design Constraints (Fielding, 2000):**
1. **Client-Server** — Separation of concerns
2. **Statelessness** — Each request is independent
3. **Uniform Interface** — Standard methods (GET, POST, PUT, DELETE)
4. **Cacheability** — Responses are cacheable by default
5. **Layered System** — Intermediaries (proxies, load balancers) can improve scalability
6. **Code on Demand** — Optional; rarely used

**Consequence:** REST leverages **the HTTP protocol itself** as the API contract.

### GraphQL (A Query Language for APIs)

**Core Principle:** API is a **graph of types and fields**. The client specifies **exactly which fields** it wants.

```graphql
query {
  order(id: 101) {
    id
    status
    customer {
      name
      email
    }
    items {
      productId
      quantity
    }
  }
}
```

**Design Goals:**
1. **Declarative** — Client declares what it needs
2. **Strongly Typed** — Schema defines all possible queries
3. **Single Endpoint** — `/graphql` handles all queries
4. **Introspectable** — Schema is queryable at runtime
5. **Precise Fetching** — No over/under-fetching

**Consequence:** GraphQL is **transport-agnostic** (can run over HTTP, WebSocket, custom protocol).

---

## 2. Architecture

### REST Architecture

```
Typical REST API:
GET  /api/users                 → List users
GET  /api/users/101             → User details
GET  /api/users/101/orders      → User's orders
GET  /api/orders/202            → Order details
GET  /api/orders/202/items      → Order items
POST /api/users                 → Create user
```

**Characteristics:**
- **Multiple endpoints** — One per resource type
- **URL structure encodes intent** — `/users/101/orders` means "101's orders"
- **HTTP verbs convey operation** — GET = safe & idempotent, POST = create, PUT = update
- **Stateless** — Server doesn't track client session (except auth token)
- **Link-driven** — Response includes URLs to related resources (HATEOAS)

### GraphQL Architecture

```
Single endpoint:
POST /graphql
{
  "query": "{ order(id:101) { id status } }"
}
```

**Characteristics:**
- **Single endpoint** — All queries go to `/graphql`
- **Query language** — Client sends a query (not URL)
- **Schema-driven** — Server defines all possible queries in schema
- **Stateless** — Like REST, no session tracking
- **Introspective** — Can query the schema itself

---

## 3. Data Fetching

### REST: Fixed Response Shapes

**The Problem: Over/Under-fetching**

```javascript
// REST GET /api/orders/101
{
  "id": 101,
  "status": "PENDING",
  "totalAmount": 5000,
  "customerId": 42,
  "shippingAddress": "...",
  "billingAddress": "...",
  "createdAt": "2025-01-01T10:00:00Z",
  "updatedAt": "2025-01-05T15:30:00Z",
  "metadata": {...}      // ← Mobile client doesn't need this
}
```

**Scenario 1: Mobile Client (Over-fetching)**
Mobile app only needs `id`, `status`, `totalAmount`. But REST returns everything.
- Wasted bandwidth
- Slower serialization/deserialization
- Slower on slow networks (3G, rural areas)

**Scenario 2: Dashboard (Under-fetching)**
Dashboard needs order + customer name + shipping tracking status. But these are in separate resources.
```
REST requires:
1. GET /api/orders/101          → order details
2. GET /api/customers/42        → customer details
3. GET /api/shipments?orderId=101 → tracking status

Total: 3 requests (N+1 problem at scale)
```

**REST Solutions:**
1. **Sparse Fieldsets** (not standard)
   ```
   GET /api/orders/101?fields=id,status,totalAmount
   ```

2. **Custom Endpoints** (pollutes API surface)
   ```
   GET /api/orders/101/summary     → minimal fields
   GET /api/orders/101/full        → all fields
   ```

3. **GraphQL Wrapper** (adds complexity)
   Keep REST, add GraphQL layer on top

### GraphQL: Precise Field Selection

**Client specifies exactly what it needs:**

```graphql
// Mobile client query
query {
  order(id: 101) {
    id
    status
    totalAmount
  }
}

// Response (only these 3 fields)
{
  "data": {
    "order": {
      "id": 101,
      "status": "PENDING",
      "totalAmount": 5000
    }
  }
}
```

```graphql
// Dashboard query (with joins)
query {
  order(id: 101) {
    id
    status
    totalAmount
    customer {                    // ← Join to customer
      name
    }
    shipment {                    // ← Join to shipment
      trackingStatus
    }
  }
}

// Single request, all data
```

**Benefits:**
- ✅ Precise bandwidth usage
- ✅ No over-fetching (mobile, IoT)
- ✅ No under-fetching (complex queries in one request)
- ✅ Optimal for diverse clients (web, mobile, watch, TV)

**Downside:**
- ⚠️ Client complexity (must write queries)
- ⚠️ Query engine complexity (server must join correctly)

---

## 4. Caching

### REST: HTTP Caching (Native)

REST leverages **HTTP caching** built into browsers, proxies, and CDNs.

```
Server Response:
HTTP/1.1 200 OK
Cache-Control: max-age=3600, public    ← Cache for 1 hour
ETag: "abc123"                         ← Version hash
Last-Modified: Tue, 01 Jan 2025 10:00:00 GMT

Client Request (Cache Hit):
GET /api/orders/101
If-None-Match: "abc123"

Server Response:
HTTP/1.1 304 Not Modified              ← No body sent, saves bandwidth
```

**Advantages:**
- ✅ Native HTTP caching (CDN support, browser cache, intermediary caches)
- ✅ Standardized and well-understood
- ✅ Works at scale (reverse proxies, edge servers)
- ✅ ETags automatically manage stale data

**Disadvantages:**
- ⚠️ Caching by URL (coarse-grained)
  - `GET /api/users?page=1&status=ACTIVE` has different cache key than `GET /api/users?page=1&status=INACTIVE`
  - URL explosion for complex filters

### GraphQL: Application-Level Caching

GraphQL uses a **single endpoint**, so HTTP caching is less effective.

```
All queries go to POST /graphql
  Same URL
  Different request body
  → Can't cache by URL
```

**Solutions:**

1. **Apollo Client Cache** (client-side)
   ```javascript
   const cache = new InMemoryCache();
   // Automatically caches query results by field path
   ```

2. **Persisted Queries + HTTP Caching**
   ```
   GET /graphql?id=query123   ← Use query ID in URL
   → Now cacheable by URL
   ```

3. **Application Cache** (Redis)
   ```
   Query hash → Redis → Result TTL
   ```

**Trade-off:**
- ⚠️ No native HTTP caching (need workarounds)
- ⚠️ Usually app-level caching (less efficient)
- ✅ But can cache at field level (Apollo cache handles this)

---

## 5. Versioning

### REST: Explicit Versioning

**Problem:** Breaking changes must be managed explicitly.

```
Old version stays alive:
GET /api/v1/orders/101
{
  "id": 101,
  "amount": 5000,
  "createdDate": "2024-01-01"
}

New version with breaking changes:
GET /api/v2/orders/101
{
  "id": 101,
  "totalAmount": 5000,              ← renamed from 'amount'
  "createdAt": "2024-01-01T10:00:00Z"  ← changed format
}
```

**Versioning Strategies:**
1. **URI versioning** (most common) — `/api/v1/`, `/api/v2/`
2. **Header versioning** — `Accept: application/vnd.api.v2+json`
3. **Query param** — `/api/orders?version=2`

**Deprecation Policy:**
- v1 supported for 6–12 months
- Clear timeline announced in docs
- Clients forced to upgrade eventually

### GraphQL: Schema Evolution (No Versions)

**Philosophy:** API evolves, old clients continue working.

```graphql
# Schema v1
type Order {
  id: ID!
  amount: Float!
  createdDate: String!
}

# Schema v2 (additive)
type Order {
  id: ID!
  amount: Float!              ← kept for backward compatibility
  totalAmount: Float!         ← new field
  createdDate: String!        ← kept
  createdAt: DateTime!        ← new field (better type)
}

# Old client query still works:
query { order(id: 101) { amount createdDate } }

# New client query uses new fields:
query { order(id: 101) { totalAmount createdAt } }
```

**Rules:**
1. ✅ **Add new fields** (safe)
2. ✅ **Add new types** (safe)
3. ✅ **Deprecate fields** with `@deprecated` (safe)
4. ❌ **Remove fields** (breaking)
5. ❌ **Change field type** (breaking)

**Advantage:**
- ✅ No explicit versions
- ✅ Clients upgrade at their own pace
- ✅ Removes coordination overhead

**Requirement:**
- ⚠️ Schema must be designed for additive changes
- ⚠️ Can't remove fields (accumulate deprecated ones)

---

## 6. Real-time Communication

### REST: Polling or WebSockets

**Polling (Simple but Inefficient)**
```javascript
// Client polls every 2 seconds
setInterval(() => {
  fetch('/api/orders/101/status')
    .then(r => r.json())
    .then(data => updateUI(data));
}, 2000);
```

**Problems:**
- ⚠️ Latency (up to 2 seconds behind real state)
- ⚠️ Wasted requests (most polling calls return no change)
- ⚠️ Network overhead (bandwidth)

**WebSocket (Better):**
```javascript
const ws = new WebSocket('/api/orders/101/stream');
ws.onmessage = (event) => {
  const order = JSON.parse(event.data);
  updateUI(order);
};
```

**Problems:**
- ⚠️ Manual protocol design (no standard)
- ⚠️ Connection management complexity
- ⚠️ Load balancing challenges (sticky sessions needed)

### GraphQL: Subscriptions (Native)

```graphql
subscription {
  orderUpdated(id: 101) {
    id
    status
    totalAmount
  }
}
```

**Server (under the hood):**
```
1. Establish WebSocket connection
2. Server pushes updates to subscribed clients
3. Client receives only fields specified in subscription
```

**Advantages:**
- ✅ Same query language as queries
- ✅ Built-in framework support (Apollo, Spring GraphQL)
- ✅ Precise field selection (only get what you need)
- ✅ Natural fit for event-driven systems

---

## 7. Observability & Debugging

### REST: HTTP-Level Visibility

**Easy to debug (HTTP level):**
```bash
curl -v GET https://api.example.com/api/orders/101
# Shows headers, status code, response body
```

**Logs are simple:**
```json
{
  "method": "GET",
  "path": "/api/orders/101",
  "status": 200,
  "durationMs": 45
}
```

**Challenges:**
- ⚠️ Multiple requests for complex operations (hard to correlate)
- ⚠️ No insight into which fields were needed

### GraphQL: Query-Level Visibility

**Harder to debug (query level):**
```bash
curl -X POST https://api.example.com/graphql \
  -H "Content-Type: application/json" \
  -d '{"query": "{ order(id: 101) { id status } }"}'
```

**Logs need context:**
```json
{
  "query": "{ order(id: 101) { id status } }",
  "operationName": "GetOrder",
  "variables": {},
  "durationMs": 45,
  "queryComplexity": 5
}
```

**Benefits:**
- ✅ Single request for complex operations (easier to correlate)
- ✅ Can measure query complexity
- ✅ Introspection provides automatic documentation

**Tools:**
- Apollo DevTools (browser extension)
- GraphQL Playground
- Voyager (schema explorer)

---

## 8. Security

### REST: Standard HTTP Security

```javascript
// Authentication
GET /api/orders/101
Authorization: Bearer eyJhbGc...   ← JWT token

// CORS (Cross-Origin)
Access-Control-Allow-Origin: https://app.example.com

// HTTPS
All requests must use HTTPS
```

**Common Attack Vectors:**
1. **BOLA (Broken Object-Level Authorization)**
   ```
   GET /api/orders/456   ← Any logged-in user can see any order
   ```
   Solution: Enforce ownership check

2. **Mass Enumeration**
   ```
   GET /api/orders/1, 2, 3, 4... (sequential IDs)
   ```
   Solution: Use UUIDs

3. **CORS Misconfiguration**
   ```
   Access-Control-Allow-Origin: *   ← Too permissive
   ```

### GraphQL: Unique Challenges

```graphql
query {
  # Attack: deeply nested query (DoS)
  order(id: 101) {
    customer {
      orders {
        items {
          product {
            reviews {
              author {
                # ... very deep
              }
            }
          }
        }
      }
    }
  }
}
```

**GraphQL-Specific Threats:**

1. **Query Complexity Attack** (DoS)
   ```graphql
   query {
     users(first: 999999) {  ← Request millions of users
       orders { items { ... } }
     }
   }
   ```
   Solution: Query complexity analysis, rate limiting

2. **Introspection Abuse**
   ```graphql
   query { __schema { types { name } } }
   # Reveals entire schema (for reconnaissance)
   ```
   Solution: Disable introspection in production

3. **Batch Query Attack**
   ```graphql
   [
     { "query": "{ ... }" },
     { "query": "{ ... }" },
     ... (1000s of queries)
   ]
   ```
   Solution: Batch query limits

**Protection Strategies:**
- ✅ Query depth limiting
- ✅ Query complexity analysis
- ✅ Rate limiting per query
- ✅ Disable introspection in production
- ✅ Query cost analysis
- ✅ Timeout on slow queries

---

## 9. Performance

### REST Performance Characteristics

**Advantages:**
- ✅ Caching (HTTP-level, very efficient)
- ✅ Simple queries (minimal processing)
- ✅ Connection pooling (HTTP/2 multiplexing)

**Disadvantages:**
- ⚠️ Multiple round-trips (N+1 queries at network level)
- ⚠️ Over-fetching wastes bandwidth
- ⚠️ No query optimization (server doesn't know what client needs)

**Example: Loading a user with orders**
```
Request 1: GET /api/users/101              (50 bytes response)
Request 2: GET /api/users/101/orders       (500 bytes response)
Request 3: GET /api/orders/201/items       (300 bytes response)
Request 4: GET /api/orders/202/items       (300 bytes response)
Total: 4 requests, 1150 bytes (if items are paginated, can be 10+ requests)
```

### GraphQL Performance Characteristics

**Advantages:**
- ✅ Single request (solves N+1 at network level)
- ✅ Precise fetching (no over-fetching)
- ✅ Query optimization possible (server knows exact needs)
- ✅ Subscriptions for real-time (efficient push)

**Disadvantages:**
- ⚠️ Complex queries are expensive (resolve deeply nested fields)
- ⚠️ Query engine overhead (parsing, validating, executing)
- ⚠️ N+1 at database level (each field is a resolver)
- ⚠️ No HTTP caching (cached query pattern required)

**Example: Same data with GraphQL**
```graphql
query {
  user(id: 101) {
    name
    orders {
      id
      items { productId quantity }
    }
  }
}

# Single request, only needed data
```

**Performance Metrics:**
- REST: Lower latency for simple queries, higher for complex ones
- GraphQL: More consistent latency, flexible for client needs

---

## 10. Operations & Scaling

### REST Scaling

**Horizontal Scaling:**
```
Requests → Load Balancer
           → Server 1
           → Server 2
           → Server 3
           → Cache (Redis) → DB (Read Replica)
```

**Challenges:**
- Multiple endpoints → different cache strategies
- Rate limiting per endpoint
- Monitoring many endpoints

### GraphQL Scaling

**Horizontal Scaling:**
```
POST /graphql → Load Balancer
               → Server 1 (GraphQL engine)
               → Server 2 (GraphQL engine)
               → Server 3 (GraphQL engine)
               → Data sources (REST APIs, DBs, etc.)
```

**Challenges:**
- Single endpoint bottleneck (mitigated with query complexity limits)
- N+1 queries at DB level (mitigated with DataLoader)
- Query complexity can spike (need safeguards)

---

## 11. Strengths & Weaknesses

### REST Strengths ✅
| Strength | Reason |
|----------|--------|
| **Simplicity** | URL-driven, HTTP verbs familiar |
| **Caching** | Native HTTP caching |
| **Stateless** | Easy to scale horizontally |
| **Debugging** | HTTP tools (curl, browser) understand REST |
| **Standardization** | Consistent across APIs |
| **Maturity** | Well-understood, proven at scale |

### REST Weaknesses ❌
| Weakness | Problem |
|----------|---------|
| **Fixed response shape** | Over/under-fetching |
| **Multiple endpoints** | More complex client integration |
| **N+1 queries** | Client must make multiple requests |
| **Versioning overhead** | Multiple API versions to maintain |
| **No real-time** | Polling inefficient, WebSockets ad-hoc |

### GraphQL Strengths ✅
| Strength | Reason |
|----------|--------|
| **Precise fetching** | Client specifies exact fields |
| **Single endpoint** | Simpler integration |
| **Joined queries** | Avoid N+1 at network level |
| **No versioning** | Schema evolution, old clients work |
| **Subscriptions** | Real-time, efficient |
| **Self-documenting** | Schema is introspectable |
| **Developer experience** | Auto-completion, instant feedback |

### GraphQL Weaknesses ❌
| Weakness | Problem |
|----------|---------|
| **Learning curve** | New mental model for many devs |
| **Caching** | Single endpoint, needs workarounds |
| **Query complexity** | DoS risk if not managed |
| **N+1 at DB level** | Resolvers can cause database avalanche |
| **File uploads** | Not natively supported (GraphQL spec) |
| **Monitoring** | Less standard (tool ecosystem growing) |

---

## Summary Matrix

```
┌─────────────────────┬──────────────┬──────────────┐
│ Dimension           │ REST         │ GraphQL      │
├─────────────────────┼──────────────┼──────────────┤
│ Learning Curve      │ ⭐⭐⭐⭐    │ ⭐⭐⭐      │
│ Caching             │ ⭐⭐⭐⭐    │ ⭐⭐       │
│ Real-time          │ ⭐⭐       │ ⭐⭐⭐⭐⭐  │
│ Precise Fetching   │ ⭐⭐       │ ⭐⭐⭐⭐⭐  │
│ Multiple Clients   │ ⭐⭐       │ ⭐⭐⭐⭐⭐  │
│ Simplicity         │ ⭐⭐⭐⭐⭐  │ ⭐⭐⭐     │
│ Security Defaults  │ ⭐⭐⭐⭐    │ ⭐⭐⭐     │
│ Developer Tools    │ ⭐⭐⭐      │ ⭐⭐⭐⭐⭐  │
└─────────────────────┴──────────────┴──────────────┘
```

---

## Key Takeaway

**REST and GraphQL are complementary, not competitors.**

- **REST is best for:** Simple, high-volume, cacheable operations
- **GraphQL is best for:** Complex, interconnected, flexible data needs
- **Production systems often use both:** REST for public, simple APIs; GraphQL for internal, complex queries

Neither is "better" — the question is "better for what?"
