# GET API Design — Enterprise Checklist

Key points to think through **before** you write a single line of GET API code
in an enterprise application. Each section covers the "what", "why", and
"what to decide" so you can make conscious choices rather than discovering
problems in production.

---

## Table of Contents

1. [Pagination](#1-pagination)
2. [Filtering](#2-filtering)
3. [Sorting](#3-sorting)
4. [Field Selection (Sparse Fieldsets)](#4-field-selection-sparse-fieldsets)
5. [Caching](#5-caching)
6. [Authentication & Authorization](#6-authentication--authorization)
7. [Response Structure (Envelope)](#7-response-structure-envelope)
8. [Versioning](#8-versioning)
9. [Performance & Query Design](#9-performance--query-design)
10. [Security](#10-security)
11. [Rate Limiting & Throttling](#11-rate-limiting--throttling)
12. [Error Handling](#12-error-handling)
13. [Idempotency & Safety](#13-idempotency--safety)
14. [Compression & Payload Size](#14-compression--payload-size)
15. [Observability](#15-observability)
16. [Documentation](#16-documentation)
17. [Naming & Consistency](#17-naming--consistency)
18. [Quick Decision Checklist](#quick-decision-checklist)

---

## 1. Pagination

**Why it matters:** Never return unbounded lists. A table with 10 million rows
will crash your app or time out if you return everything.

### Two strategies

| Strategy | How it works | Use when |
|----------|-------------|----------|
| **Offset-based** | `?page=2&size=20` → `LIMIT 20 OFFSET 40` | Simple UIs, small datasets |
| **Cursor-based** | `?cursor=eyJpZCI6MTAwfQ==` → `WHERE id > 100 LIMIT 20` | Large datasets, infinite scroll, real-time data |

### Offset problems (know these)
- `OFFSET 100000` is slow — DB still scans 100,000 rows and discards them
- If a row is inserted/deleted between pages, you get **duplicate or skipped records**
- Does not scale for millions of rows

### Cursor advantages
- Consistent — no skips/duplicates even if data changes
- O(1) regardless of how deep the page is
- The cursor encodes the "position" (usually last seen `id` or `created_at`)

### Decide before building
- What is the max page size? (Enforce a hard cap, e.g., 100)
- Does the consumer need total count? (Expensive on large tables — consider making it optional)
- Will data change frequently? → prefer cursor

```
# Offset
GET /api/orders?page=3&size=20

# Cursor
GET /api/orders?cursor=eyJpZCI6MjAwfQ==&size=20

# Response should always include next page info
{
  "data": [...],
  "pagination": {
    "nextCursor": "eyJpZCI6MjIwfQ==",
    "hasMore": true
  }
}
```

---

## 2. Filtering

**Why it matters:** Clients always need to narrow down data. Without a clear
filtering contract, every team builds ad-hoc query params inconsistently.

### Common patterns

```
# Simple equality
GET /api/orders?status=PENDING

# Range
GET /api/orders?createdAfter=2024-01-01&createdBefore=2024-06-01

# Multiple values (IN clause)
GET /api/orders?status=PENDING,SHIPPED

# Nested field filter
GET /api/orders?customer.city=Mumbai
```

### Advanced: Filter DSL (for complex enterprise APIs)
Some APIs use a structured filter syntax:
```
GET /api/orders?filter=status:PENDING AND amount>1000
```

### Decide before building
- Which fields are filterable? (Not all fields should be — unindexed filters are slow)
- Are filters ANDed or ORed by default?
- Will you support range queries (`gt`, `lt`, `between`)?
- Are filter values validated server-side? (Always yes — never trust raw input)
- What happens with an unknown filter key — error or ignore?

### Security note
Never interpolate filter values directly into SQL. Always use parameterized queries.

---

## 3. Sorting

**Why it matters:** Clients need predictable ordering. Inconsistent sort defaults
cause bugs in UIs (items jumping between refreshes).

```
GET /api/orders?sort=createdAt,desc
GET /api/orders?sort=amount,asc&sort=createdAt,desc   ← multi-field sort
```

### Decide before building
- What is the **default sort**? (Always define one — never rely on DB natural order)
- Which fields are sortable? (Only indexed fields — sorting on unindexed columns is a full table scan)
- Multi-field sort supported?
- Is sort direction `asc/desc` or `+/-` prefix? Pick one and stick to it.

### Stability note
For pagination + sorting to work correctly, you need a **stable sort**.
Always include a unique field (like `id`) as the tiebreaker:
```sql
ORDER BY created_at DESC, id DESC
```

---

## 4. Field Selection (Sparse Fieldsets)

**Why it matters:** Mobile clients on slow networks don't need 40 fields when
they only display 3. Over-fetching wastes bandwidth and serialization time.

```
GET /api/orders?fields=id,status,totalAmount
```

Response only includes requested fields:
```json
{
  "id": 101,
  "status": "PENDING",
  "totalAmount": 5000
}
```

### Decide before building
- Do your consumers have bandwidth constraints? (Mobile apps, IoT)
- Will you support it? If yes, define a whitelist of selectable fields
- Never allow selecting sensitive fields not meant for the consumer

### Alternative: GraphQL
If field selection is a core requirement across many APIs, GraphQL is designed for this.

---

## 5. Caching

**Why it matters:** GET APIs are the best candidates for caching. Without it,
identical requests hit the DB every time unnecessarily.

### Layers of caching

```
Client → CDN → API Gateway → Application Cache (Redis) → Database
```

### HTTP Cache Headers (built into HTTP — free to use)

| Header | Purpose | Example |
|--------|---------|---------|
| `Cache-Control` | Controls who caches and for how long | `Cache-Control: max-age=60, public` |
| `ETag` | Version fingerprint of the response | `ETag: "abc123"` |
| `Last-Modified` | When the resource last changed | `Last-Modified: Tue, 01 Jan 2025 10:00:00 GMT` |

**ETag flow:**
```
1. Client: GET /api/orders/101
2. Server: 200 OK, ETag: "v1"
3. Client: GET /api/orders/101, If-None-Match: "v1"
4. Server: 304 Not Modified  ← no body sent, saves bandwidth
```

### Application-level cache (Redis)
Cache DB query results in Redis with a TTL:
```
Key:   orders:list:page=1:status=PENDING
Value: [serialized JSON]
TTL:   60 seconds
```

### Decide before building
- Is this data user-specific or shared? (User-specific → private cache; shared → public/CDN)
- How stale is acceptable? (Real-time data → no cache; reference data → long TTL)
- What triggers cache invalidation? (Write operations must clear relevant keys)
- Are you using ETags? (Especially valuable for large payloads)

---

## 6. Authentication & Authorization

**Why it matters:** GET ≠ safe to expose to everyone. You must control
**who** can call the API and **what data** they can see.

### Authentication (who are you?)
- JWT Bearer token in `Authorization` header (stateless, scalable)
- API Key for service-to-service
- OAuth2 for third-party delegated access

### Authorization (what can you access?)

| Level | Description | Example |
|-------|-------------|---------|
| **Role-based (RBAC)** | Role determines what you can do | ADMIN sees all orders, USER sees own orders |
| **Attribute-based (ABAC)** | Fine-grained policies based on attributes | User in region=INDIA can only see India orders |
| **Row-level security** | Filter data by ownership | Always add `WHERE user_id = :currentUserId` |

### Decide before building
- Is this a public, authenticated, or internal API?
- What data should each role see? (Design auth filters at the query level, not presentation layer)
- Are there multi-tenant concerns? (Tenant ID must be enforced in every query)
- Should authorization failures return `401` or `403`? (401 = not authenticated, 403 = authenticated but not allowed)

---

## 7. Response Structure (Envelope)

**Why it matters:** Inconsistent response shapes across APIs force consumers
to write different parsing logic everywhere.

### Recommended structure

```json
{
  "status": "success",
  "data": {
    "id": 101,
    "status": "PENDING",
    "totalAmount": 5000
  },
  "meta": {
    "requestId": "req-abc-123",
    "timestamp": "2025-01-01T10:00:00Z"
  }
}
```

For lists:
```json
{
  "status": "success",
  "data": [...],
  "pagination": {
    "page": 1,
    "size": 20,
    "totalElements": 500,
    "hasMore": true
  },
  "meta": {
    "requestId": "req-abc-123"
  }
}
```

### Decide before building
- Envelope vs raw response? (Raw is simpler but harder to extend; envelope is more flexible)
- Where does pagination metadata live? (In `meta`, in `pagination`, as response headers?)
- Standard field names — agree on `createdAt` vs `created_at` vs `createdDate` across all APIs

---

## 8. Versioning

**Why it matters:** APIs in enterprise systems are consumed by multiple teams
and clients. Breaking changes without versioning breaks everyone.

### Strategies

| Strategy | Example | Notes |
|----------|---------|-------|
| **URI versioning** | `/api/v1/orders` | Most visible, easiest to test |
| **Header versioning** | `Accept: application/vnd.api.v1+json` | Clean URLs, harder to test in browser |
| **Query param** | `/api/orders?version=1` | Simple but pollutes query string |

### URI versioning is the most common in enterprise

```
/api/v1/orders   ← stable, old clients stay here
/api/v2/orders   ← new version with breaking changes
```

### Decide before building
- Which versioning strategy for your org? (Pick one and enforce it)
- What is your deprecation policy? (e.g., old versions supported for 6 months after new release)
- Are breaking changes clearly defined? (Adding a field = non-breaking; removing/renaming = breaking)

---

## 9. Performance & Query Design

**Why it matters:** A poorly written GET API can bring down your DB under load.

### Checklist
- [ ] **Indexes** — every filtered/sorted column must be indexed
- [ ] **N+1 queries** — use JOIN FETCH or batch loading (see `NPlusOneQuery/`)
- [ ] **SELECT only needed columns** — avoid `SELECT *`
- [ ] **Query timeout** — set a max execution time to prevent slow queries blocking threads
- [ ] **Connection pool sizing** — know your DB connection limits
- [ ] **Read replicas** — route GET queries to read replicas to offload the primary DB
- [ ] **Explain plan** — run `EXPLAIN ANALYZE` on every significant query before shipping

### Slow query indicators
- Response time > 200ms under normal load
- DB CPU spikes on read traffic
- `OFFSET` on large tables
- Missing indexes on `WHERE` / `ORDER BY` columns

---

## 10. Security

**Why it matters:** GET APIs can leak sensitive data and be abused at scale.

### Key concerns

**1. Over-exposure (OWASP API3: Excessive Data Exposure)**
Never return fields the client doesn't need. Define explicit response DTOs —
never serialize entity objects directly.

```java
// Bad — exposes internal fields, passwords, audit columns
return userRepository.findById(id);

// Good — explicit projection
return UserResponseDTO.from(user); // only id, name, email
```

**2. Broken Object Level Authorization (OWASP API1: BOLA)**
Always verify the requester owns the resource:
```java
// Bad
GET /api/orders/456   // any logged-in user can see any order

// Good — enforce ownership
if (!order.getUserId().equals(currentUser.getId())) {
    throw new ForbiddenException();
}
```

**3. Mass enumeration**
Don't expose sequential integer IDs — they allow attackers to iterate all records:
```
GET /api/orders/1
GET /api/orders/2  ...
```
Use UUIDs or opaque IDs instead.

**4. Input validation**
Validate all query params — type, range, allowed values. Reject unknown params.

---

## 11. Rate Limiting & Throttling

**Why it matters:** Without limits, a single client can flood your API and
starve other users. Also protects against scraping and abuse.

```
HTTP 429 Too Many Requests
Retry-After: 60
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 0
X-RateLimit-Reset: 1700000060
```

### Decide before building
- Limit per user, per IP, or per API key?
- What are the limits? (e.g., 100 req/min for normal, 1000 req/min for premium)
- What is the response when limit is hit? (Always `429`, never silently drop)
- See `RateLimiter/` in this repo for algorithm implementations

---

## 12. Error Handling

**Why it matters:** Clients need to know what went wrong and how to fix it.
Inconsistent errors force consumers to write fragile parsing code.

### Standard error response

```json
{
  "status": "error",
  "error": {
    "code": "ORDER_NOT_FOUND",
    "message": "Order with id 101 does not exist",
    "requestId": "req-abc-123",
    "timestamp": "2025-01-01T10:00:00Z"
  }
}
```

### HTTP status codes for GET APIs

| Status | When to use |
|--------|-------------|
| `200 OK` | Successful response |
| `204 No Content` | Success but no body (rare for GET) |
| `304 Not Modified` | ETag/cache hit |
| `400 Bad Request` | Invalid query params / filter values |
| `401 Unauthorized` | Missing or invalid auth token |
| `403 Forbidden` | Authenticated but not allowed |
| `404 Not Found` | Resource doesn't exist |
| `429 Too Many Requests` | Rate limit exceeded |
| `500 Internal Server Error` | Unexpected server error |

### Decide before building
- Use machine-readable error codes (not just messages) — clients can react to codes
- Never leak stack traces or internal details in error responses
- Always include a `requestId` so you can trace the request in logs

---

## 13. Idempotency & Safety

**Why it matters:** GET must be safe and idempotent by definition (HTTP spec).
This has architectural consequences.

- **Safe** = calling the API does not change server state
- **Idempotent** = calling it once or 100 times produces the same result

### What this means for you
- Never perform side effects in a GET handler (no writes, no state changes)
- GET responses should be consistent for the same input (within cache TTL)
- If you need to log "user viewed this resource" → do it asynchronously, not in the request path

---

## 14. Compression & Payload Size

**Why it matters:** Large JSON responses are slow to transfer, especially on
mobile networks.

### Enable gzip compression
```
Request:  Accept-Encoding: gzip
Response: Content-Encoding: gzip
```
Typical compression ratio: **60–80% size reduction** on JSON.

### Other size optimizations
- Return only needed fields (see Field Selection)
- Avoid deeply nested objects — flatten where possible
- Paginate — never return full collections
- Consider returning URLs for large binary fields instead of embedding them

---

## 15. Observability

**Why it matters:** In production, you can't debug what you can't see.

### What to capture per request
- `requestId` — unique ID, propagated through all services (correlation ID)
- `userId` / `tenantId` — who made the request
- Response time (ms)
- HTTP status code
- Endpoint + query params (sanitized — no sensitive values)
- DB query count and time

### Structured logging (not plain text)
```json
{
  "requestId": "req-abc-123",
  "method": "GET",
  "path": "/api/orders",
  "userId": "user-42",
  "statusCode": 200,
  "durationMs": 45,
  "dbQueryCount": 2
}
```

### Metrics to monitor
- P50 / P95 / P99 latency (not just average)
- Error rate (5xx / total requests)
- Cache hit rate
- DB query time per endpoint

---

## 16. Documentation

**Why it matters:** Undocumented APIs force consumers to read source code or
ask the owning team for every integration.

### Use OpenAPI (Swagger)
Define your API contract in OpenAPI spec:
- Request params (type, required, description, example)
- Response schema with examples
- Error responses
- Authentication requirements

```yaml
/api/orders:
  get:
    summary: List orders
    parameters:
      - name: status
        in: query
        schema:
          type: string
          enum: [PENDING, SHIPPED, DELIVERED]
      - name: page
        in: query
        schema:
          type: integer
          default: 0
    responses:
      200:
        description: Paginated list of orders
      400:
        description: Invalid filter parameters
```

### Decide before building
- Is Swagger UI exposed in non-prod environments?
- Who maintains the spec — code-first (annotations) or spec-first?

---

## 17. Naming & Consistency

**Why it matters:** Inconsistent naming increases cognitive load for every team
that consumes your API.

### URL conventions
```
# Resource names are plural nouns
GET /api/orders           ← list
GET /api/orders/101       ← single resource
GET /api/orders/101/items ← nested resource

# kebab-case for multi-word paths
GET /api/order-items      ✅
GET /api/orderItems       ❌
GET /api/order_items      ❌ (debatable, but pick one)
```

### Query param conventions
```
# camelCase params
?pageSize=20&sortBy=createdAt

# Consistent date format — always ISO 8601
?createdAfter=2024-01-01T00:00:00Z
```

### Response field conventions
- Pick `camelCase` or `snake_case` and never mix
- Consistent date format: ISO 8601 (`2024-01-01T10:00:00Z`)
- Boolean fields: `isActive`, `isDeleted` — consistent prefix
- IDs: always `string` type in response (even if stored as `long` — avoids JS precision issues)

---

## Quick Decision Checklist

Before writing the first line of code, answer these:

```
PAGINATION
[ ] Offset or cursor-based?
[ ] Default page size? Max page size?
[ ] Return total count? (expensive — optional?)

FILTERING & SORTING
[ ] Which fields are filterable? (Must be indexed)
[ ] Which fields are sortable? (Must be indexed)
[ ] Default sort order defined?

RESPONSE
[ ] Envelope format agreed with consumers?
[ ] Field naming convention (camelCase/snake_case)?
[ ] Sensitive fields excluded from response DTO?

CACHING
[ ] Cache-Control headers set?
[ ] ETags for large/expensive resources?
[ ] Redis caching needed? What's the TTL?

SECURITY
[ ] Auth required? JWT / API Key / Public?
[ ] Row-level ownership enforced?
[ ] UUIDs instead of sequential IDs?
[ ] Input validation on all query params?

PERFORMANCE
[ ] All filter/sort columns indexed?
[ ] N+1 queries checked?
[ ] Read replica configured for GET traffic?
[ ] Query timeout set?

OPERATIONS
[ ] Rate limiting configured?
[ ] Structured logging with requestId?
[ ] Error responses use standard format?
[ ] OpenAPI spec written?
[ ] API versioned?
```

---

## Key Takeaway

> A GET API looks simple — it just reads data.  
> But in enterprise systems, how you read data determines  
> **performance under load, security posture, client developer experience,  
> and how much pain you have 6 months later.**  
> Answer the checklist before you start, not after.
