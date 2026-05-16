# REST vs GraphQL — Decision Matrix

A practical framework to choose the right API style for your specific use case.

---

## Quick Decision Tree

```
Start: "I need to build an API"
├─ Multiple simple, cacheable endpoints (GET /api/users, /api/orders, etc.)?
│  └─ YES → REST ✅
│
├─ Diverse clients with different data needs (web, mobile, IoT)?
│  └─ YES → GraphQL ✅
│
├─ Real-time updates critical?
│  └─ YES → GraphQL (subscriptions) ✅
│
├─ Caching is critical for performance?
│  └─ YES → REST (HTTP caching) ✅
│
├─ Complex, interconnected data with many joins?
│  └─ YES → GraphQL ✅
│
├─ Team familiar with REST?
│  └─ YES & Time is critical → REST ✅
│
└─ Building internal tool for power users?
   └─ YES → GraphQL ✅
```

---

## Scoring Matrix

Score each factor 1-5 (5 = strongly favors this approach).

### Factors That Favor REST

| Factor | Weight | How to Score |
|--------|--------|-------------|
| **Data is mostly CRUD operations** | 3 | Simple CRUD = 5, Complex joins = 1 |
| **Caching is critical** | 4 | HTTP caching essential = 5, Not important = 1 |
| **High request volume** | 3 | 100K+ req/sec = 5, <1K = 1 |
| **Bandwidth not a constraint** | 2 | Bandwidth abundant = 5, Limited (mobile) = 1 |
| **Team knows REST well** | 2 | Expert = 5, Never used = 1 |
| **Public API, third-party devs** | 2 | Yes = 5, Internal only = 1 |
| **Simple querying patterns** | 3 | Fixed shapes = 5, Flexible needs = 1 |
| **Debugging/observability needed** | 2 | HTTP tools sufficient = 5, Need query-level = 1 |

**REST Score = Sum of (Factor Score × Weight) / Total Weight**

### Factors That Favor GraphQL

| Factor | Weight | How to Score |
|--------|--------|-------------|
| **Diverse client needs** | 4 | Many client types = 5, Single client = 1 |
| **Over/under-fetching problematic** | 4 | Major pain = 5, Not an issue = 1 |
| **Complex, interconnected data** | 3 | Highly connected = 5, Flat data = 1 |
| **Real-time updates needed** | 3 | Subscriptions critical = 5, Polling OK = 1 |
| **Internal/controlled clients** | 2 | Internal only = 5, Public API = 1 |
| **Developer experience important** | 2 | Introspection, tools valuable = 5, Not important = 1 |
| **Schema evolution without versioning** | 2 | Important = 5, Not important = 1 |
| **Query flexibility matters** | 3 | Clients need exact control = 5, Server decides = 1 |

**GraphQL Score = Sum of (Factor Score × Weight) / Total Weight**

**Decision:** If REST Score > GraphQL Score → REST. Otherwise → GraphQL.

---

## Scenario Examples

### Scenario 1: E-commerce Product Catalog

```
Requirements:
✓ Mostly read-heavy (GET products, reviews, inventory)
✓ Simple data model (Products have attributes, reviews)
✓ Caching important (product pages cached 1 hour)
✓ Mobile app with bandwidth concerns
✓ Multiple clients: web, mobile app, admin dashboard
✓ REST API already exists, team comfortable with it

Scoring:
REST Factors:
- Data is mostly CRUD: 4 (mostly read)
- Caching is critical: 4 (product pages cached)
- High request volume: 4 (popular site)
- Bandwidth not a constraint: 2 (mobile cares)
- Team knows REST: 5 (existing)
REST Score = (4×3 + 4×4 + 4×3 + 2×2 + 5×2) / 14 = 3.9

GraphQL Factors:
- Diverse client needs: 4 (web, mobile, admin)
- Over/under-fetching: 3 (some concern)
- Complex interconnected: 2 (mostly flat)
- Real-time updates: 1 (not critical)
GraphQL Score = (4×4 + 3×4 + 2×3 + 1×3) / 12 = 2.9

Decision: REST ✅

Solution:
- Keep REST for public product endpoints
- Implement sparse fieldsets for mobile: GET /api/products?fields=id,name,price
- Heavy HTTP caching with ETags
- If admin needs complex queries later, add GraphQL internal tool
```

### Scenario 2: Real-time Chat Application

```
Requirements:
✓ Real-time message delivery (subscriptions)
✓ Complex queries (user + messages + reactions + etc)
✓ Diverse clients (web, mobile, desktop)
✓ Message search with complex filters
✓ Internal application (not public)
✓ Bandwidth matters on mobile

REST Factors:
- Data is mostly CRUD: 3 (messages are CRUD but real-time is critical)
- Caching is critical: 1 (messages constantly changing)
- High request volume: 3 (medium)
- Bandwidth not a constraint: 2 (mobile)
- Team knows REST: 3 (some)
REST Score = (3×3 + 1×4 + 3×3 + 2×2 + 3×2) / 14 = 2.3

GraphQL Factors:
- Diverse client needs: 5 (web, mobile, desktop different needs)
- Over/under-fetching: 5 (users need messages + reactions + authors)
- Complex interconnected: 4 (users → messages → reactions)
- Real-time updates: 5 (subscriptions essential)
- Internal clients: 5 (internal only)
GraphQL Score = (5×4 + 5×4 + 4×3 + 5×3 + 5×2) / 12 = 4.6

Decision: GraphQL ✅

Solution:
- GraphQL for all query operations
- Subscriptions for real-time messages
- DataLoader to prevent N+1 queries
- Persisted queries for performance
- Client caching (Apollo Client)
```

### Scenario 3: Mobile App Backend

```
Requirements:
✓ Multiple platforms (iOS, Android, web)
✓ Bandwidth constraints (cellular networks)
✓ Battery efficiency (fewer requests)
✓ Complex user profile (user + settings + feed + notifications)
✓ Need precise field selection

REST Factors:
- Data is mostly CRUD: 2 (interconnected)
- Caching is critical: 2 (user data changing)
- High request volume: 3 (many devices)
- Bandwidth not a constraint: 1 (mobile on 4G)
- Team knows REST: 2 (adopting best practices)
REST Score = (2×3 + 2×4 + 3×3 + 1×2 + 2×2) / 14 = 2.1

GraphQL Factors:
- Diverse client needs: 5 (iOS, Android, web different)
- Over/under-fetching: 5 (critical for mobile)
- Complex interconnected: 4 (user → feed → posts)
- Real-time updates: 3 (feeds, notifications)
- Bandwidth constraints: 5 (cellular)
GraphQL Score = (5×4 + 5×4 + 4×3 + 3×3 + 5×2) / 12 = 4.3

Decision: GraphQL ✅

Solution:
- Pure GraphQL backend
- Persistent queries to reduce network payload
- Apollo Client for caching
- Subscriptions for notifications
- Query cost analysis to prevent overload
```

### Scenario 4: High-Frequency Trading Platform

```
Requirements:
✓ 10M+ requests per second
✓ Sub-100ms latency critical
✓ Simple queries (stock prices, orders)
✓ Heavy caching at CDN level
✓ Public API for traders

REST Factors:
- Data is mostly CRUD: 5 (prices, orders - simple)
- Caching is critical: 5 (must cache heavily)
- High request volume: 5 (10M/sec)
- Bandwidth not a constraint: 4 (traders willing to pay)
- Team knows REST: 4 (industry standard)
REST Score = (5×3 + 5×4 + 5×3 + 4×2 + 4×2) / 14 = 4.6

GraphQL Factors:
- Diverse client needs: 2 (mostly same data)
- Over/under-fetching: 2 (not a problem)
- Complex interconnected: 1 (flat data)
- Real-time updates: 4 (stock feeds)
- Public API: 3 (yes, but traders expect REST)
GraphQL Score = (2×4 + 2×4 + 1×3 + 4×3 + 3×2) / 12 = 2.5

Decision: REST ✅

Solution:
- REST endpoints for maximum cache efficiency
- CDN caching at geographic edge
- WebSocket for real-time stock feeds (separate)
- Heavy pagination to limit bandwidth
- Aggressive HTTP caching (Cache-Control: public, max-age=60)
```

### Scenario 5: Internal Operational Dashboard

```
Requirements:
✓ Internal tool for ops team
✓ Diverse query patterns (different users need different data)
✓ Complex data (orders + customers + inventory + logistics)
✓ Real-time updates (order tracking, inventory changes)
✓ Schema evolution without breaking (team adding features)
✓ Developer experience matters (ops team building custom dashboards)

REST Factors:
- Data is mostly CRUD: 2 (complex queries)
- Caching is critical: 1 (real-time critical)
- High request volume: 1 (internal tool)
- Bandwidth not a constraint: 5 (internal network)
- Team knows REST: 2 (learning new things)
REST Score = (2×3 + 1×4 + 1×3 + 5×2 + 2×2) / 14 = 2.1

GraphQL Factors:
- Diverse client needs: 5 (each user different queries)
- Over/under-fetching: 5 (major pain with REST)
- Complex interconnected: 5 (orders → customers → history)
- Real-time updates: 5 (subscriptions for tracking)
- Internal clients: 5 (internal only)
- Developer experience: 5 (tools, introspection valuable)
GraphQL Score = (5×4 + 5×4 + 5×3 + 5×3 + 5×2 + 5×2) / 12 = 4.8

Decision: GraphQL ✅

Solution:
- Pure GraphQL backend
- Subscriptions for real-time order tracking
- Schema designed for evolution (add fields, not remove)
- Apollo Server for rich developer experience
- GraphQL Playground for custom queries
```

---

## Organization-Specific Factors

### Startup (Fast Growth)
**Favor:** REST initially, add GraphQL later if needed
- **Why:** Speed to market beats flexibility
- **Strategy:** REST for MVP, add GraphQL when N+1 becomes painful

### Enterprise (Multiple Teams)
**Favor:** Hybrid (REST + GraphQL)
- **Why:** Different teams have different needs
- **Strategy:** REST for stable, public interfaces; GraphQL for complex internal tools

### Mobile-First Company
**Favor:** GraphQL
- **Why:** Bandwidth and battery efficiency matter
- **Strategy:** Pure GraphQL backend optimized for mobile clients

### Real-time Company (Chat, Notifications)
**Favor:** GraphQL
- **Why:** Subscriptions built-in
- **Strategy:** GraphQL with WebSocket subscriptions

### Data-Heavy Company (Analytics, BI)
**Favor:** Hybrid
- **Why:** Simple queries use REST (fast, cached); complex queries use GraphQL
- **Strategy:** REST for dashboards, GraphQL for ad-hoc analysis

---

## Migration Paths

### REST → REST + GraphQL

```
Phase 1 (Months 1-2): Run Both in Parallel
  REST /api/v1/... → Existing clients
  GraphQL /graphql → New clients only

Phase 2 (Months 2-4): Migration
  Document GraphQL equivalents for each REST endpoint
  Provide SDKs/libraries for GraphQL
  Encourage new clients to use GraphQL

Phase 3 (Months 4-6): Sunsetting
  Set deprecation date for REST v1
  Continue supporting for 6 months
  Remove REST code after sunset
```

### GraphQL → GraphQL + REST

```
Phase 1: Add REST Layer
  Expose most-common queries as REST endpoints
  Keep GraphQL for complex queries

Phase 2: Monitor
  See which queries most common
  Convert top 20% to REST endpoints

Phase 3: Optimize
  REST handles 80% traffic (cached at CDN)
  GraphQL handles 20% traffic (complex internal)
```

---

## The "Both" Decision

Sometimes the answer is: **Use both.**

```
Architecture:
┌─────────────────────────────────────┐
│ REST API                            │
│ • GET /api/products (CDN cached)    │
│ • GET /api/orders (public)          │
│ → High volume, cacheable            │
└─────────────────────────────────────┘
              ↑
         Load Balancer
              ↑
┌──────────────────────────────────────┐
│ GraphQL API                          │
│ • POST /graphql (internal)           │
│ → Complex queries, diverse clients   │
└──────────────────────────────────────┘

Backend services (shared):
  └─ Database, cache, auth, ...
```

**When to use both:**
- Large, complex systems
- Mix of simple and complex operations
- Public API (REST) + Internal tools (GraphQL)
- Gradual migration strategy

---

## Checklist Before Decision

- [ ] What are the primary use cases? (CRUD? Complex joins? Real-time?)
- [ ] Who are the consumers? (Internal? Public? Mobile?)
- [ ] What are the constraints? (Bandwidth? Caching? Real-time?)
- [ ] Team experience? (REST experts? GraphQL expertise?)
- [ ] Existing infrastructure? (REST CDN? GraphQL tools?)
- [ ] Timeline? (MVP in 1 month? 6 months of planning?)
- [ ] Support burden? (Can team maintain both?)
- [ ] Future growth? (Will requirements change?)

---

## Final Recommendation Framework

```
IF:
  "I need something working in 2 weeks"
THEN:
  Use REST
  (Simpler, faster to implement)

IF:
  "I have multiple clients with different needs"
THEN:
  Use GraphQL
  (Precise fetching, fewer requests)

IF:
  "Caching at scale is critical"
THEN:
  Use REST
  (HTTP caching is unbeatable)

IF:
  "Real-time updates are essential"
THEN:
  Use GraphQL
  (Subscriptions, less polling)

IF:
  "I'm not sure / it's complex"
THEN:
  Use both
  (REST for simple, GraphQL for complex)
```

---

## Summary

| Decision | Use REST | Use GraphQL | Use Both |
|----------|----------|-------------|---------|
| Simple CRUD APIs | ✅✅✅ | ⭐⭐ | |
| High-volume, cached | ✅✅✅ | ⭐ | |
| Multiple clients | ✅ | ✅✅✅ | ✅✅ |
| Real-time critical | ⭐⭐ | ✅✅✅ | ✅✅ |
| Complex queries | ⭐ | ✅✅✅ | ✅ |
| Internal tools | ✅ | ✅✅✅ | |
| Public API | ✅✅✅ | ⭐⭐ | ✅ |
| Startup MVP | ✅✅✅ | ⭐ | |
| Enterprise scale | ✅✅ | ✅✅ | ✅✅✅ |

**✅✅✅ = Strongly recommended**  
**✅✅ = Recommended**  
**✅ = Possible**  
**⭐⭐⭐ = Also works**  
**⭐⭐ = Workaround needed**  
**⭐ = Not ideal**
