# REST vs GraphQL — Questions & Answers

50+ interview and conceptual questions to test and deepen your understanding.

---

## 🟢 Beginner Level (Foundation)

### 1. What is an API?
**Answer:** An Application Programming Interface is a contract between a server and client specifying how to request and receive data. REST and GraphQL are two architectural styles for building APIs.

### 2. What does REST stand for?
**Answer:** Representational State Transfer. It's an architectural style (not a protocol) that treats data as resources identified by URLs, using HTTP methods (GET, POST, PUT, DELETE) for operations.

### 3. What is GraphQL?
**Answer:** A query language and runtime for APIs. Clients send queries specifying exactly which fields they need, and the server responds with only that data.

### 4. Why do we have two different API styles?
**Answer:** Different problems require different solutions. REST excels at simple, cacheable operations. GraphQL excels at complex, flexible data fetching. Many systems use both.

### 5. In REST, what does each HTTP method do?
**Answer:**
- `GET` — Retrieve data (safe, idempotent)
- `POST` — Create new data (not idempotent, can have side effects)
- `PUT` — Replace entire resource (idempotent)
- `PATCH` — Partially update resource (idempotent)
- `DELETE` — Remove resource (idempotent)

### 6. What is idempotence?
**Answer:** An operation is idempotent if calling it multiple times produces the same result as calling it once.
- `GET /api/users/101` — Idempotent (reading doesn't change state)
- `DELETE /api/users/101` — Idempotent (deleting twice = deleted once)
- `POST /api/users` — NOT idempotent (creates new user each time)

### 7. How many endpoints does a typical REST API have?
**Answer:** Multiple endpoints, one per resource type:
```
GET /api/users              GET /api/orders
GET /api/users/101          GET /api/orders/202
POST /api/users             POST /api/orders
PUT /api/users/101          DELETE /api/orders/202
DELETE /api/users/101       ...
```

### 8. How many endpoints does GraphQL have?
**Answer:** Typically **one** — `/graphql`. All queries and mutations go to this single endpoint.

### 9. What is over-fetching in REST?
**Answer:** When the server returns more data than the client needs.
```
Client wants: { id, name }
REST returns: { id, name, email, phone, address, ... }
```

### 10. What is under-fetching in REST?
**Answer:** When the server returns insufficient data, forcing the client to make additional requests.
```
Client needs: User + their orders
REST requires: 
  1. GET /api/users/101
  2. GET /api/users/101/orders
```

---

## 🟡 Intermediate Level (Design & Trade-offs)

### 11. Explain the "N+1 query problem" in the context of REST vs GraphQL.

**Answer:**

**REST Example (N+1 at network level):**
```
Client needs: 1 user with 10 orders
GET /api/users/101              → returns user
GET /api/users/101/orders       → returns 10 order summaries
for each order, client calls:
GET /api/orders/201/items       → 1st order items
GET /api/orders/202/items       → 2nd order items
... (10 requests total)
Total: 12 API calls
```

**GraphQL Example (Single request, but N+1 at DB level if not careful):**
```graphql
query {
  user(id: 101) {
    name
    orders {              # N+1 at DB: 1 query for user + N queries for orders
      id
      items {            # N+1 again: N queries for items
        productId
      }
    }
  }
}
```

**GraphQL solves network N+1 but creates DB N+1 without DataLoader:**
```java
// Without DataLoader: N+1 queries
for (Order order : user.getOrders()) {
  List<Item> items = itemRepository.findByOrderId(order.getId());
}

// With DataLoader: Batch queries
DataLoader<Long, List<Item>> itemLoader = new DataLoader<>((orderIds) -> {
  return itemRepository.findByOrderIds(orderIds);  // Single query with IN clause
});
```

### 12. How does caching work differently in REST vs GraphQL?

**Answer:**

**REST (HTTP-level caching):**
```
Server: Cache-Control: max-age=3600, public
↓
Browser cache, CDN, proxy cache all understand this
↓
GET /api/users/101  → Cached at multiple layers
```

**GraphQL (No native HTTP caching because of single endpoint):**
```
All queries → POST /graphql (same URL)
↓
URL-based cache key is useless
↓
Solutions:
1. Persisted queries: GET /graphql?id=abc123 (enable HTTP caching)
2. Apollo cache: Client-side field-level caching
3. Redis: Application-level caching
```

### 13. Why is GraphQL harder to cache than REST?

**Answer:** Because GraphQL uses a single endpoint with request body as payload:

```
Request 1: POST /graphql { query: "{ user(id: 101) { name } }" }
Request 2: POST /graphql { query: "{ user(id: 102) { name } }" }

Both hit the same URL but different request bodies.
→ HTTP cache keys are URL-based, so both cache miss.

REST has different URLs:
GET /api/users/101
GET /api/users/102
→ Each can be cached separately at HTTP level.
```

### 14. What is versioning and how does REST and GraphQL handle it differently?

**Answer:**

**REST Versioning (Explicit):**
```
GET /api/v1/users/101     → Old clients here
GET /api/v2/users/101     → New clients here (breaking changes)

Disadvantage: Multiple versions to maintain
Advantage: Clear separation, old clients unaffected
```

**GraphQL Versioning (Schema Evolution):**
```graphql
# V1
type User {
  id: ID!
  name: String!
  email: String!
}

# V2 (additive only)
type User {
  id: ID!
  name: String!
  email: String!
  displayName: String    # ← New field
  profile: UserProfile   # ← New field
}

# Old queries still work, new queries can use new fields
```

### 15. Can you remove a field from a GraphQL schema?

**Answer:** **Yes, but you shouldn't** (unless necessary). The proper way:

```graphql
type User {
  id: ID!
  oldField: String @deprecated(reason: "Use newField instead")
  newField: String!
}
```

This allows:
- Old clients to continue using `oldField`
- New clients to migrate to `newField`
- Server can remove `oldField` after deprecation period

### 16. Explain REST's HATEOAS principle.

**Answer:** HATEOAS = "Hypermedia As The Engine Of Application State"

It means the response includes links to related resources and actions:

```json
{
  "id": 101,
  "name": "Alice",
  "links": {
    "self": "/api/users/101",
    "orders": "/api/users/101/orders",
    "update": { "href": "/api/users/101", "method": "PUT" },
    "delete": { "href": "/api/users/101", "method": "DELETE" }
  }
}
```

**Why:** Client discovers available actions dynamically (less tightly coupled to API structure).

**In practice:** Often omitted (too verbose) or partially implemented.

### 17. What is a GraphQL subscription?

**Answer:** A way to subscribe to real-time updates using WebSockets.

```graphql
subscription {
  orderStatusChanged(id: 101) {
    id
    status
    updatedAt
  }
}
```

When the order status changes on the server, this subscription receives an update:
```json
{
  "data": {
    "orderStatusChanged": {
      "id": 101,
      "status": "SHIPPED",
      "updatedAt": "2025-01-06T10:30:00Z"
    }
  }
}
```

**Advantages over REST WebSocket:**
- Same query language
- Precise field selection (only get what you asked for)
- Server handles subscription management
- Built-in framework support

### 18. How would you handle real-time updates in a REST API?

**Answer:**

**Option 1: Polling (Inefficient)**
```javascript
setInterval(() => {
  fetch('/api/orders/101/status');
}, 1000);  // Check every 1 second
```
Problems: Latency, wasted requests, bandwidth

**Option 2: WebSocket (Ad-hoc)**
```javascript
const ws = new WebSocket('/api/orders/101/stream');
ws.onmessage = (msg) => {
  const order = JSON.parse(msg.data);
  updateUI(order);
};
```
Problems: Non-standard protocol, connection management complexity

**Option 3: Server-Sent Events (Better)**
```javascript
const eventSource = new EventSource('/api/orders/101/stream');
eventSource.onmessage = (event) => {
  const order = JSON.parse(event.data);
  updateUI(order);
};
```
Good middle ground: HTTP-based, one-way (server → client)

### 19. What is a GraphQL resolver?

**Answer:** A function that computes the value for a field in a GraphQL query.

```graphql
type Query {
  user(id: ID!): User
}

type User {
  id: ID!
  name: String!
  orders: [Order!]!  # This field needs a resolver
}
```

```java
// Resolvers:
public User user(Long id) {
  return userRepository.findById(id);  // Resolves Query.user
}

public List<Order> orders(User user) {
  return orderRepository.findByUserId(user.getId());  // Resolves User.orders
}
```

**Common mistake (N+1 queries):**
```java
// Bad: Each user.orders resolver calls DB individually
public List<Order> orders(User user) {
  return orderRepository.findByUserId(user.getId());
}

// For 100 users, this runs 101 queries (1 for users + 100 for orders)
```

**Solution: DataLoader**
```java
// Good: Batch load all order IDs together
DataLoader<Long, List<Order>> ordersLoader = new DataLoader<>(userIds -> {
  return orderRepository.findByUserIds(userIds);  // Single query with IN clause
});
```

### 20. What is introspection in GraphQL?

**Answer:** The ability to query the schema itself.

```graphql
query {
  __schema {
    types {
      name
      fields {
        name
        type { name }
      }
    }
  }
}
```

**Advantages:**
- Auto-completion in IDEs
- API documentation generation
- Schema exploration tools (Voyager, GraphQL Playground)

**Security concern:**
- Reveals entire schema (reconnaissance for attackers)
- Solution: Disable introspection in production

---

## 🔴 Advanced Level (System Design & Production)

### 21. How would you design an API for a mobile app with bandwidth constraints?

**Answer:**

**REST Approach:**
```
1. Sparse fieldsets: GET /api/users/101?fields=id,name,avatar
2. Pagination: GET /api/orders?page=1&size=10
3. Caching: Use ETags and Cache-Control headers
4. Compression: gzip all responses
5. ImageOpts: Return image URLs, not embedded images
```

**GraphQL Approach (Better suited):**
```graphql
query {
  user(id: 101) {
    id
    name
    avatar(size: "small")   # Request specific size
  }
  orders(first: 10) {       # Pagination via GraphQL
    id
    totalAmount
  }
}
```

**GraphQL is superior for mobile because:**
- ✅ Precise field selection (minimal bandwidth)
- ✅ No over-fetching
- ✅ Single request for complex needs
- ✅ Client cache (Apollo Client)

### 22. Design a system that handles 1M requests/second. Would you use REST or GraphQL?

**Answer:** **Primarily REST, with strategic GraphQL**

```
Requirements:
- 1M req/sec = High throughput
- Need caching at scale
- Simple, predictable queries

Architecture:
                    ┌─────────────┐
                    │ CDN (Edges) │ ← Cache simple REST endpoints
                    └──────┬──────┘
                           │
                    ┌──────┴──────┐
                    │ Load Balancer│
                    └──────┬──────┘
                    ┌──────┴──────────────┐
                    │                     │
            ┌───────▼───────┐     ┌──────▼───────┐
            │ REST API      │     │ GraphQL API  │
            │ (80% traffic) │     │ (20% traffic)│
            │ GET /api/*    │     │ POST /graphql│
            │ (cacheable)   │     │ (complex)    │
            └───────┬───────┘     └──────┬───────┘
                    │                     │
                    └─────────┬───────────┘
                              │
                        ┌─────▼─────┐
                        │ Redis Cache│
                        └─────┬─────┘
                              │
                        ┌─────▼─────┐
                        │ Database  │
                        │(Read Rep) │
                        └───────────┘
```

**Strategy:**
- 80% traffic: Simple REST endpoints (fully cacheable)
- 20% traffic: GraphQL for internal tools, complex queries
- Heavy caching at CDN + Redis level
- Read replicas for DB scaling

### 23. You're building an internal tool for operations. REST or GraphQL?

**Answer:** **GraphQL** (preferred for internal tools)

**Reasons:**
1. **Flexible Queries** — Different operations teams need different fields
   ```graphql
   // Team A: Billing wants specific fields
   query { order { id amount customer { name email } } }
   
   // Team B: Support wants different fields
   query { order { id status timeline customer { phone address } } }
   ```

2. **One Endpoint** — Simpler integration
3. **Subscriptions** — Real-time updates for dashboards
4. **No Caching Pressure** — Internal tool, not public, so caching less critical
5. **Dev Experience** — Auto-completion, schema exploration

### 24. How would you prevent a GraphQL query complexity attack?

**Answer:**

**Attack Example:**
```graphql
query {
  users(first: 999999) {
    orders {
      items {
        product {
          reviews {
            author {
              orders { ... 50 levels deep
            }
          }
        }
      }
    }
  }
}
```

**Defenses:**

1. **Query Depth Limiting**
```java
if (queryDepth > 15) {
  throw new QueryExecutionException("Query too deep");
}
```

2. **Query Complexity Analysis**
```java
// Assign cost to each field
User.orders = cost 2
Order.items = cost 3
Item.product = cost 1

// users(first: 999999) = 999999 * 2 * 3 * 1 = 5,999,994
// Reject if complexity > threshold (e.g., 100,000)
```

3. **Query Timeout**
```java
executionInput.dataLoaderRegistry(dataLoaderRegistry)
             .timeout(5_000);  // 5 second timeout
```

4. **Rate Limiting (Query-based)**
```
User: Max 100 complexity units per minute
      Max 50 queries per minute
```

5. **Persisted Queries**
```javascript
// Only allow pre-approved queries
POST /graphql { id: "query-abc123", variables: {...} }
// Prevents ad-hoc attacks
```

### 25. Explain N+1 at the database level in GraphQL and how to fix it.

**Answer:**

**The Problem:**
```java
// Schema
type Query {
  users: [User!]!
}

type User {
  id: ID!
  name: String!
  orders: [Order!]!    // ← This field causes N+1
}

// Resolver
public List<Order> orders(User user) {
  return orderRepository.findByUserId(user.getId());  // 1 query per user
}

// Query
query {
  users {              // 1 query for all users
    id
    orders {           // N queries (1 per user)
      id
    }
  }
}

// If 1000 users: 1 + 1000 = 1001 database queries!
```

**Solution 1: DataLoader (Batching)**
```java
DataLoader<Long, List<Order>> orderLoader = new DataLoader<>(userIds -> {
  // Single query instead of N queries
  return orderRepository.findByUserIds(userIds);  // SELECT * FROM orders WHERE user_id IN (...)
});

// In resolver
public List<Order> orders(User user) {
  return orderLoader.load(user.getId());  // Batched
}

// For 1000 users: 1 + 1 = 2 queries ✅
```

**Solution 2: Query Optimization (JOIN)**
```java
// Instead of resolver-based approach, fetch eagerly
@Query("SELECT u FROM User u LEFT JOIN FETCH u.orders WHERE u.id IN :ids")
List<User> findWithOrders(@Param("ids") List<Long> ids);

// Single query with JOIN, much faster
```

### 26. Design a public REST API for third-party developers. What would you include?

**Answer:**

```
Key Components:
1. Versioning Strategy
   /api/v1/... (URI versioning, most visible)
   
2. Authentication
   Authorization: Bearer <JWT token>
   Or: X-API-Key: <key> (for service-to-service)
   
3. Rate Limiting
   X-RateLimit-Limit: 1000
   X-RateLimit-Remaining: 850
   X-RateLimit-Reset: 1704892800
   
4. Pagination
   Cursor-based (preferred for public APIs)
   GET /api/v1/orders?cursor=eyJpZCI6IDEwMH0=&limit=20
   
5. Filtering & Sorting
   GET /api/v1/orders?status=PENDING&sort=-createdAt
   
6. Response Format (Envelope)
   {
     "status": "success",
     "data": [...],
     "pagination": {...},
     "meta": { "requestId": "req-123", "timestamp": "..." }
   }
   
7. Error Handling
   {
     "status": "error",
     "error": {
       "code": "ORDER_NOT_FOUND",
       "message": "Order 101 does not exist",
       "requestId": "req-123"
     }
   }
   
8. Caching Headers
   Cache-Control: max-age=300, public
   ETag: "v1-hash"
   
9. Documentation
   OpenAPI/Swagger spec (machine-readable)
   Developer portal (human-readable)
   
10. Webhooks (async notifications)
    POST https://client-app.com/hooks/order-shipped
    
11. SLA & Support
    Max latency: 100ms (p95)
    Uptime: 99.95%
    Support: support@api.example.com
```

### 27. When would you choose GraphQL over REST for a public API?

**Answer:**

**Rare, but valid scenarios:**

1. **Diverse Clients (Web, Mobile, IoT, TV)**
   ```
   Each client needs different fields → GraphQL avoids over-fetching
   Example: Netflix mobile vs web vs TV app
   ```

2. **Complex, Interconnected Data** (Social graph, e-commerce)
   ```graphql
   query {
     user {
       friends {
         friendsOfFriends {
           posts { comments { likes } }
         }
       }
     }
   }
   ```

3. **Third-party Integrations** (Shopify, GitHub)
   ```
   Partners build custom integrations → GraphQL flexibility is invaluable
   Instead of "partners asking for custom REST endpoints", they write custom queries
   ```

4. **Bandwidth-Critical** (IoT, developing countries)
   ```
   Precise field selection saves significant bandwidth
   ```

**Examples in the wild:**
- GitHub GraphQL API (v4, internal & public)
- Shopify Admin API (GraphQL + REST both offered)
- AWS AppSync
- Stripe (REST + GraphQL)

### 28. What are the security risks of exposing a GraphQL API publicly?

**Answer:**

1. **Introspection Exposure**
   ```graphql
   query { __schema { types { name fields { name } } } }
   # Returns entire schema → reconnaissance for attackers
   ```
   Fix: Disable in production

2. **Query Complexity Attack (DoS)**
   ```graphql
   query {
     users(first: 999999) {
       orders { items { product { reviews { author { orders { ... } } } } } }
     }
   }
   ```
   Fix: Depth limiting, complexity analysis, timeouts

3. **Batch Query Attack**
   ```
   POST /graphql
   [
     {"query": "..."},
     {"query": "..."},
     ... (1000s)
   ]
   ```
   Fix: Limit batch size

4. **Alias Attack**
   ```graphql
   query {
     a: user(id: 1) { id }
     b: user(id: 1) { id }
     c: user(id: 1) { id }
     ... (10000 aliases)
   }
   ```
   Fix: Limit aliases per query

5. **BOLA (Broken Object Level Auth)**
   ```graphql
   query {
     order(id: "someone-elses-order-id") { secret }
   }
   ```
   Fix: Enforce authorization at resolver level

### 29. How would you migrate from REST to GraphQL?

**Answer:**

**Phase 1: Parallel (3 months)**
```
Existing clients: REST /api/v1/...
New clients: GraphQL /graphql
Both backed by same database
```

**Phase 2: Migration (3-6 months)**
```
Encourage existing clients to migrate
Publish migration guide
Provide GraphQL client libraries
Monitor REST endpoint usage
```

**Phase 3: Sunsetting (6-12 months)**
```
Deprecate REST endpoints
Set sunset date (e.g., "REST v1 retired in Jan 2026")
Provide support for last holdouts
Monitor errors
Remove REST code
```

**Key points:**
- ✅ Don't force migration; clients need time
- ✅ Provide wrapper library for GraphQL to ease migration
- ✅ Detailed migration guide
- ✅ Support both for extended period

### 30. Design a GraphQL schema for an e-commerce platform.

**Answer:**

```graphql
# Core types
type Product {
  id: ID!
  name: String!
  description: String
  price: Float!
  stock: Int!
  category: Category!
  reviews: [Review!]!
  images: [Image!]!
}

type Order {
  id: ID!
  customer: Customer!
  items: [OrderItem!]!
  status: OrderStatus!
  totalAmount: Float!
  createdAt: DateTime!
  updatedAt: DateTime!
}

type OrderItem {
  id: ID!
  product: Product!
  quantity: Int!
  priceAtPurchase: Float!
}

type Customer {
  id: ID!
  name: String!
  email: String!
  orders(first: Int, after: String): OrderConnection!
  addresses: [Address!]!
}

# Connections for pagination
type OrderConnection {
  edges: [OrderEdge!]!
  pageInfo: PageInfo!
}

type OrderEdge {
  node: Order!
  cursor: String!
}

type PageInfo {
  hasNextPage: Boolean!
  endCursor: String
}

# Queries
type Query {
  product(id: ID!): Product
  products(first: Int, after: String, search: String): ProductConnection!
  order(id: ID!): Order
  customer(id: ID!): Customer
  me: Customer  # Current logged-in customer
}

# Mutations
type Mutation {
  createOrder(items: [OrderItemInput!]!): Order!
  updateOrder(id: ID!, status: OrderStatus!): Order!
  addProductReview(productId: ID!, rating: Int!, text: String!): Review!
}

# Subscriptions
type Subscription {
  orderStatusChanged(orderId: ID!): Order!
  productRestocked(productId: ID!): Product!
}

# Enums
enum OrderStatus {
  PENDING
  PROCESSING
  SHIPPED
  DELIVERED
  CANCELLED
}

input OrderItemInput {
  productId: ID!
  quantity: Int!
}
```

### 31. What is the difference between PUT and PATCH in REST?

**Answer:**

**PUT (Replace entire resource):**
```
Request:
PUT /api/users/101
{ "name": "Bob", "email": "bob@example.com" }

Result: Replaces entire user
If fields not provided, they become null/deleted
```

**PATCH (Partial update):**
```
Request:
PATCH /api/users/101
{ "name": "Bob" }

Result: Only updates `name`, other fields unchanged
If fields not provided, they're ignored
```

**Example:**
```
Original: { id: 101, name: "Alice", email: "alice@example.com", phone: "555-1234" }

PUT /api/users/101 { "name": "Bob", "email": "bob@example.com" }
Result: { id: 101, name: "Bob", email: "bob@example.com", phone: null }

PATCH /api/users/101 { "name": "Bob" }
Result: { id: 101, name: "Bob", email: "alice@example.com", phone: "555-1234" }
```

### 32. How do you handle pagination in GraphQL?

**Answer:**

**Cursor-based Pagination (Recommended):**
```graphql
query {
  orders(first: 10, after: "cursor-from-prev-page") {
    edges {
      node {
        id
        totalAmount
      }
      cursor
    }
    pageInfo {
      hasNextPage
      endCursor
    }
  }
}
```

**Advantages:**
- ✅ Stable (no skipped/duplicate results if data changes)
- ✅ Efficient (don't scan to offset)
- ✅ Works with real-time data

**Offset-based (Simple but less efficient):**
```graphql
query {
  orders(skip: 100, limit: 10) {
    id
    totalAmount
  }
}
```

**Disadvantage:** ⚠️ Slow for deep pages, can have skips/duplicates

### 33. How would you handle file uploads in GraphQL?

**Answer:**

**Challenge:** GraphQL spec doesn't natively support file uploads.

**Solution: GraphQL Multipart Request Spec**

```javascript
// Client
const file = document.getElementById('file').files[0];
const formData = new FormData();

formData.append('operations', JSON.stringify({
  query: `mutation {
    uploadFile(file: null) {
      id
      url
    }
  }`,
}));

formData.append('map', JSON.stringify({ '1': ['variables.file'] }));
formData.append('1', file);

fetch('/graphql', {
  method: 'POST',
  body: formData
});
```

```java
// Server
@MutationMapping
public FileResponse uploadFile(@RequestParam("file") MultipartFile file) {
  String fileUrl = storageService.save(file);
  return new FileResponse(UUID.randomUUID().toString(), fileUrl);
}
```

**Alternative: Upload URLs**
```graphql
mutation {
  requestUploadUrl {
    uploadUrl
    fileId
  }
}

# Client uploads directly to S3 presigned URL
# Then notifies server: mutation { confirmUpload(fileId: "...") }
```

### 34. How would you implement role-based access control (RBAC) in GraphQL?

**Answer:**

```java
@QueryMapping
public User user(
    @Argument Long id,
    @ContextValue Authentication auth) {  // Get current user from context
  
  User requestedUser = userRepository.findById(id).orElseThrow();
  
  if (!canViewUser(auth.getPrincipal(), requestedUser)) {
    throw new ForbiddenException("Cannot view this user");
  }
  
  return requestedUser;
}

private boolean canViewUser(UserPrincipal principal, User requestedUser) {
  // RBAC logic
  if (principal.hasRole("ADMIN")) return true;
  if (principal.getId().equals(requestedUser.getId())) return true;
  return false;
}
```

**Field-level authorization (mask sensitive fields):**
```java
@SchemaDirective(locations = DirectiveLocation.FIELD_DEFINITION)
public class AuthDirec tive {
  @Override
  public GraphQLFieldDefinition onField(/* ... */) {
    return fieldDef.transform(builder ->
      builder.dataFetcher(environment -> {
        if (!hasPermission(environment.getContext(), "READ_EMAIL")) {
          return null;  // Don't return email field
        }
        return originalFetcher.get(environment);
      })
    );
  }
}
```

### 35. What is a REST API idempotency key and when is it used?

**Answer:**

An **idempotency key** prevents duplicate operations when requests are retried.

**Problem:**
```
POST /api/orders { "items": [...] }
Network timeout → Client doesn't know if order was created
Client retries → Creates second order!
```

**Solution: Idempotency Key**
```
POST /api/orders
Idempotency-Key: unique-id-123
{ "items": [...] }

Server response:
{
  "id": 101,
  "items": [...],
  "Idempotency-Key": "unique-id-123"
}

Retry with same key:
POST /api/orders
Idempotency-Key: unique-id-123
→ Server returns cached response (order 101), doesn't create new one
```

**Implementation:**
```java
Map<String, CachedResponse> idempotencyCache = new ConcurrentHashMap<>();

@PostMapping("/orders")
public OrderResponse createOrder(
    @Header("Idempotency-Key") String key,
    @RequestBody OrderRequest req) {
  
  // Check if already processed
  if (idempotencyCache.containsKey(key)) {
    return idempotencyCache.get(key);  // Return cached
  }
  
  // Process new order
  Order order = orderService.create(req);
  OrderResponse response = new OrderResponse(order);
  
  // Cache for future retries (24 hours TTL)
  idempotencyCache.put(key, response);
  
  return response;
}
```

---

## 🎯 Scenario-Based Questions

### 36. You have 3 types of clients: web, mobile, smart watch. Design the API.

**Answer:** **Hybrid Approach**

```
Web Client:
  ✅ REST for simple CRUD
  ✅ GraphQL for complex queries (dashboard)
  
Mobile Client:
  ✅ GraphQL for bandwidth efficiency
  ✅ Sparse field selection critical
  
Smart Watch:
  ✅ GraphQL with strict field limits
  ✅ Only essential data (id, status, notification)
```

**Example:**

```graphql
# Web: Full dashboard query
query {
  user {
    profile { avatar bio followers }
    feed { 50 posts with comments }
    notifications { all }
  }
}

# Mobile: Minimal query
query {
  user {
    profile { avatar }
    feed(first: 10) { id title thumbnail }
  }
}

# Watch: Ultra-minimal
query {
  user {
    unreadCount
  }
  notifications(unread: true, first: 1) { title }
}
```

### 37. A team argues for migrating their REST API to GraphQL. As an architect, what questions would you ask?

**Answer:**

1. **Problem Definition**
   - "What problems are we solving?" (over-fetching? multiple endpoints? complexity?)
   - "How would GraphQL help?" (be specific, not hype)

2. **Cost-Benefit**
   - How many development hours to migrate? (usually 3-6 months)
   - ROI? (reduced bandwidth? faster client development?)
   - Is the pain of REST worse than the pain of migration?

3. **Team Readiness**
   - Do we have GraphQL expertise?
   - How steep is the learning curve?
   - Client-side changes needed? (Apollo, Relay setup)

4. **Operations**
   - How do we cache GraphQL? (persisted queries? Redis?)
   - Monitoring & debugging (tools? expertise?)
   - Breaking change policy? (GraphQL allows evolution, but needs discipline)

5. **Hybrid Strategy**
   - Keep REST for simple, high-volume, cacheable endpoints
   - Add GraphQL for complex, internal, flexible queries
   - Run both in parallel initially

**My recommendation:** "REST is simpler and proven at scale. GraphQL solves real problems, but has real trade-offs. Use REST unless you hit its limits. Then add GraphQL layer for specific pain points."

### 38. Design an API for a real-time chat application.

**Answer:**

```
Requirements:
- Messages appear instantly
- Users see "typing" indicator
- Notifications for new messages
- Message search (complex query)
```

**Solution: Hybrid**

**REST Layer:**
- `GET /api/conversations` — List conversations
- `GET /api/conversations/{id}/messages?before=2025-01-06T10:00:00Z` — Pagination
- `POST /api/conversations/{id}/messages` — Send message
- `PATCH /api/messages/{id}` — Edit message
- `DELETE /api/messages/{id}` — Delete message

**GraphQL Layer:**
```graphql
# Real-time subscriptions
subscription {
  messageAdded(conversationId: "conv-101") {
    id
    text
    author { name avatar }
    createdAt
  }
  
  typingIndicator(conversationId: "conv-101") {
    userId
    isTyping
  }
}

# Complex search (GraphQL advantage)
query {
  searchMessages(
    text: "hello",
    conversationId: "conv-101",
    first: 20
  ) {
    edges {
      node {
        id
        text
        author { name }
        createdAt
      }
    }
  }
}
```

**Why hybrid:**
- REST for simple CRUD, high volume
- GraphQL for real-time (subscriptions), complex search

---

## 🏆 Bonus: Expert-Level Insights

### 39. What is "The GraphQL Way" vs "The REST Way"?

**REST Way:** "Think in resources. Use HTTP methods. Leverage caching."

```
Mindset: What are the nouns in my domain?
          User, Order, Product → Create endpoints for each
```

**GraphQL Way:** "Think in graphs and queries. Let clients ask for what they need."

```
Mindset: What are the questions clients ask?
         "Give me user + their orders + reviews"
         → Design schema to answer these efficiently
```

### 40. When do you know REST has become the wrong choice?

**Answer:** When you see these signs:

1. **Multiple GET requests for single operation**
   ```
   GET /api/users/101 → GET /api/users/101/orders → GET /api/orders/201/items
   Waterfall of requests indicating client needs joined data
   ```

2. **Proliferation of specialized endpoints**
   ```
   GET /api/users/101/summary
   GET /api/users/101/full
   GET /api/users/101/with-orders
   GET /api/users/101/with-orders-and-items
   → Signal that REST can't express clients' needs flexibly
   ```

3. **Significant over-fetching**
   ```
   Client needs: id, name
   Server returns: id, name, email, phone, address, metadata, ...
   Mobile users complaining about bandwidth
   ```

4. **Version explosion**
   ```
   /api/v1/, /api/v2/, /api/v3/
   Multiple versions hard to maintain
   ```

That's when GraphQL makes sense.

---

## Summary Checklist

- ✅ Understand REST philosophy (resources, HTTP semantics, caching)
- ✅ Understand GraphQL philosophy (queries, schema, introspection)
- ✅ Know when each is appropriate
- ✅ Understand N+1 problems (network and database level)
- ✅ Understand caching differences
- ✅ Know security considerations (DoS, introspection, BOLA)
- ✅ Can design APIs for different clients (web, mobile, IoT)
- ✅ Can migrate between them without disruption
