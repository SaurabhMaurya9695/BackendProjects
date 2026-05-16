# REST vs GraphQL — Best Practices

Production-ready guidance for implementing both API styles safely and efficiently.

---

## REST API Best Practices

### 1. Use Meaningful HTTP Status Codes

```java
// Good
GET /api/orders/999    → 404 NOT FOUND (resource doesn't exist)
GET /api/orders/123    → 401 UNAUTHORIZED (auth required)
GET /api/orders        → 403 FORBIDDEN (auth ok, but not authorized)
POST /api/orders       → 201 CREATED (new resource)
DELETE /api/orders/123 → 204 NO CONTENT (deleted, empty response)

// Bad
GET /api/orders/999    → 200 OK { error: "Not found" }  ❌ Wrong status
POST /api/orders       → 500 { error: "..." }           ❌ Status mismatch
```

**Status code reference:**
- `2xx` — Success
- `3xx` — Redirect
- `4xx` — Client error (bad request, auth, not found)
- `5xx` — Server error (unhandled exception)

### 2. Implement Proper Pagination

```java
// Good: Cursor-based
GET /api/orders?cursor=eyJpZCI6MjAwfQ==&limit=20

// Response
{
  "data": [
    { "id": 200, "status": "PENDING" },
    { "id": 199, "status": "SHIPPED" }
  ],
  "pagination": {
    "nextCursor": "eyJpZCI6MTgwfQ==",
    "hasMore": true
  }
}

// Bad: Unbounded
GET /api/orders        → Returns all 10M orders ❌
```

### 3. Use Request/Response Envelopes

```java
// Good: Consistent structure
{
  "status": "success",
  "data": { /* actual data */ },
  "meta": {
    "requestId": "req-abc-123",
    "timestamp": "2025-01-06T10:30:00Z"
  }
}

// Bad: Inconsistent
{
  "order": { /* data */ }    // Sometimes 'order'
  "orders": [ ... ]          // Sometimes 'orders'
  "error": "..."             // Sometimes error property
}
```

### 4. Implement Proper Error Handling

```java
// Good
{
  "status": "error",
  "error": {
    "code": "ORDER_NOT_FOUND",
    "message": "Order with id 999 does not exist",
    "statusCode": 404,
    "requestId": "req-abc-123",
    "timestamp": "2025-01-06T10:30:00Z",
    "details": {
      "orderId": "999"
    }
  }
}

// Bad
{
  "error": "500 Internal Server Error"
}

// Bad (leaks internals)
{
  "error": "NullPointerException at UserService.java:42"
}
```

### 5. Use Proper HTTP Caching Headers

```java
// Cache for public resources
@GetMapping("/api/products/{id}")
public ResponseEntity<Product> getProduct(@PathVariable Long id) {
  Product product = productRepository.findById(id);
  
  return ResponseEntity.ok()
    .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic())
    .eTag(generateETag(product))
    .lastModified(product.getUpdatedAt())
    .body(product);
}

// Cache for user-specific resources
@GetMapping("/api/me/orders")
public ResponseEntity<List<Order>> getMyOrders() {
  List<Order> orders = orderRepository.findByCurrentUser();
  
  return ResponseEntity.ok()
    .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES).cachePrivate())
    .body(orders);
}
```

### 6. Validate Input on All Endpoints

```java
// Good
@PostMapping("/api/orders")
public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderRequest req) {
  // @Valid triggers constraint validation
  return ResponseEntity.status(201).body(orderService.create(req));
}

@Data
class OrderRequest {
  @NotNull
  @Size(min = 1)
  private List<OrderItem> items;
  
  @Email
  private String customerEmail;
  
  @Min(0) @Max(100)
  private Integer discountPercent;
}

// Bad
@PostMapping("/api/orders")
public Order createOrder(OrderRequest req) {
  // No validation ❌
  return orderService.create(req);  // Crashes if invalid
}
```

### 7. Use Proper Authentication & Authorization

```java
// Good
@GetMapping("/api/orders")
public ResponseEntity<List<Order>> getOrders(
    @RequestHeader("Authorization") String token) {
  
  User user = jwtTokenProvider.validateAndGetUser(token);
  
  List<Order> orders = orderRepository.findByUserId(user.getId());
  return ResponseEntity.ok(orders);
}

// Middleware/filter approach (cleaner)
@Component
public class AuthFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(HttpServletRequest req, ...) {
    String token = extractToken(req);
    User user = jwtTokenProvider.validateAndGetUser(token);
    SecurityContextHolder.getContext().setAuthentication(
      new UsernamePasswordAuthenticationToken(user, null, ...)
    );
  }
}

@GetMapping("/api/orders")
public ResponseEntity<List<Order>> getOrders() {
  User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  return ResponseEntity.ok(orderRepository.findByUserId(user.getId()));
}
```

### 8. Implement Rate Limiting

```java
// Good
@Component
public class RateLimitFilter extends OncePerRequestFilter {
  private final Map<String, RateLimit> limits = new ConcurrentHashMap<>();
  
  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, ...) {
    String clientId = getClientId(req);  // User ID or IP
    RateLimit limit = limits.computeIfAbsent(clientId, k -> new RateLimit(100, 60000));
    
    if (!limit.allowRequest()) {
      res.setStatus(429);  // Too Many Requests
      res.addHeader("Retry-After", "60");
      res.getWriter().write("{\"error\": \"Rate limit exceeded\"}");
      return;
    }
    
    filterChain.doFilter(req, res);
  }
}

// Response headers
HTTP/1.1 429 Too Many Requests
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 0
X-RateLimit-Reset: 1704892860
Retry-After: 60
```

### 9. Use Idempotency Keys for Mutations

```java
@PostMapping("/api/orders")
public ResponseEntity<Order> createOrder(
    @RequestHeader("Idempotency-Key") String idempotencyKey,
    @Valid @RequestBody OrderRequest req) {
  
  // Check cache first
  Order cachedOrder = idempotencyCache.get(idempotencyKey);
  if (cachedOrder != null) {
    return ResponseEntity.status(201).body(cachedOrder);
  }
  
  // Create new order
  Order order = orderService.create(req);
  
  // Cache for 24 hours
  idempotencyCache.put(idempotencyKey, order);
  
  return ResponseEntity.status(201).body(order);
}
```

### 10. Use OpenAPI for Documentation

```yaml
openapi: 3.0.0
info:
  title: Orders API
  version: 1.0.0

paths:
  /api/orders:
    get:
      summary: List orders
      operationId: listOrders
      parameters:
        - name: status
          in: query
          schema:
            type: string
            enum: [PENDING, SHIPPED, DELIVERED]
        - name: limit
          in: query
          schema:
            type: integer
            default: 20
      responses:
        '200':
          description: Paginated list of orders
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/OrderList'
        '401':
          description: Unauthorized
        '429':
          description: Rate limit exceeded
          
    post:
      summary: Create order
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/OrderRequest'
      responses:
        '201':
          description: Order created
        '400':
          description: Invalid request
```

---

## GraphQL Best Practices

### 1. Design the Schema Carefully

```graphql
# Good: Clear, composable types
type Query {
  user(id: ID!): User
  users(first: Int!, after: String): UserConnection!
}

type User {
  id: ID!
  name: String!
  email: String!
  orders: [Order!]!
}

type Order {
  id: ID!
  totalAmount: Float!
  items: [OrderItem!]!
  customer: User!
}

# Bad: Inconsistent naming, unclear relationships
type Query {
  getUser(userId: Long): UserResponse
  listUsers(pageNumber: Int): [UserDTO]
}

type UserResponse {
  user: UserData
  errors: [String]
}
```

### 2. Protect Against Query Complexity

```java
@Component
public class QueryComplexityValidator implements GraphQLInstrumentation {
  
  private static final int MAX_DEPTH = 10;
  private static final int MAX_COMPLEXITY = 100;
  
  @Override
  public ExecutionInput instrumentExecutionInput(
      ExecutionInput executionInput,
      InstrumentationExecutionParameters params) {
    
    GraphQLQuery query = parseQuery(executionInput.getQuery());
    int depth = calculateDepth(query);
    int complexity = calculateComplexity(query);
    
    if (depth > MAX_DEPTH) {
      throw new QueryValidationException("Query too deep");
    }
    
    if (complexity > MAX_COMPLEXITY) {
      throw new QueryValidationException("Query too complex");
    }
    
    return executionInput;
  }
  
  private int calculateDepth(GraphQLQuery query) {
    // Implement depth calculation
    return 0;
  }
  
  private int calculateComplexity(GraphQLQuery query) {
    // Each field has a cost
    // Nested fields multiply costs
    // first: 1000 increases cost significantly
    return 0;
  }
}
```

### 3. Use DataLoader to Prevent N+1 Queries

```java
@Component
public class UserGraphQLResolver {
  
  private final OrderRepository orderRepository;
  
  // Bad: N+1 queries
  @SchemaMapping(typeName = "User", field = "orders")
  public List<Order> orders(User user) {
    // Called once per user → N queries total
    return orderRepository.findByUserId(user.getId());
  }
  
  // Good: Batched with DataLoader
  @SchemaMapping(typeName = "User", field = "orders")
  public CompletableFuture<List<Order>> orders(
      User user,
      @ContextValue OrdersDataLoader ordersLoader) {
    // Batch load all orders with a single query
    return ordersLoader.load(user.getId());
  }
}

@Component
public class OrdersDataLoader extends BatchLoader<Long, List<Order>> {
  
  private final OrderRepository orderRepository;
  
  @Override
  public CompletionStage<Map<Long, List<Order>>> load(List<Long> userIds) {
    // Single query instead of N
    Map<Long, List<Order>> result = orderRepository
      .findByUserIds(userIds)
      .stream()
      .collect(Collectors.groupingBy(Order::getUserId));
    
    return CompletableFuture.completedFuture(result);
  }
}

// Register DataLoader
@Configuration
public class GraphQLConfig {
  @Bean
  public DataLoaderRegistry dataLoaderRegistry(OrdersDataLoader ordersDataLoader) {
    DataLoaderRegistry registry = new DataLoaderRegistry();
    registry.register("orders", DataLoader.newDataLoader(ordersDataLoader));
    return registry;
  }
}
```

### 4. Implement Field-Level Authorization

```java
@Component
public class AuthorizedFieldFetcher implements DataFetcher<Object> {
  
  @Override
  public Object get(DataFetchingEnvironment env) {
    String fieldName = env.getField().getName();
    User currentUser = env.getGraphQLContext().get("user");
    
    // Check if user can access this field
    if (!hasPermission(currentUser, fieldName)) {
      throw new ForbiddenException("Cannot access field: " + fieldName);
    }
    
    return env.getSource();  // Proceed with normal fetching
  }
  
  private boolean hasPermission(User user, String fieldName) {
    // RBAC logic
    if ("email".equals(fieldName) && !user.hasRole("ADMIN")) {
      return false;
    }
    return true;
  }
}

// Apply to fields
@SchemaDirective(locations = DirectiveLocation.FIELD_DEFINITION)
public class AuthDirective implements SchemaDirectiveWiring {
  @Override
  public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> env) {
    GraphQLFieldDefinition field = env.getElement();
    return field.transform(builder ->
      builder.dataFetcher(new AuthorizedFieldFetcher())
    );
  }
}

# Usage in schema
type User {
  id: ID!
  name: String!
  email: String! @auth(requires: "ADMIN")
}
```

### 5. Disable Introspection in Production

```java
@Configuration
public class GraphQLSecurityConfig {
  
  @Bean
  public GraphQLInstrumentation introspectionInstrumentation(
      @Value("${graphql.introspection.enabled:false}") boolean introspectionEnabled) {
    
    return new InstrospectionDisablingInstrumentation(!introspectionEnabled);
  }
}

class IntrospectionDisablingInstrumentation extends SimpleInstrumentation {
  private final boolean introspectionEnabled;
  
  public IntrospectionDisablingInstrumentation(boolean introspectionEnabled) {
    this.introspectionEnabled = introspectionEnabled;
  }
  
  @Override
  public InstrumentationContext<ExecutionResult> beginExecuteGraphQLQuery(
      InstrumentationExecuteGraphQLQueryParameters parameters) {
    
    if (!introspectionEnabled && isIntrospectionQuery(parameters.getQuery())) {
      throw new GraphQLException("Introspection is disabled");
    }
    
    return super.beginExecuteGraphQLQuery(parameters);
  }
  
  private boolean isIntrospectionQuery(String query) {
    return query.contains("__schema") || query.contains("__type");
  }
}
```

### 6. Handle Errors Properly

```java
@Component
public class GraphQLErrorHandler implements DataFetcherExceptionHandler {
  
  @Override
  public DataFetcherExceptionHandlerResult onException(
      DataFetcherExceptionHandlerParameters handlerParameters) {
    
    Exception exception = handlerParameters.getException();
    GraphQLError error = GraphQLError.newError()
      .message(getSafeMessage(exception))
      .path(handlerParameters.getPath())
      .sourceLocation(handlerParameters.getSourceLocation())
      .errorType(getErrorType(exception))
      .build();
    
    return DataFetcherExceptionHandlerResult.newResult()
      .error(error)
      .build();
  }
  
  private String getSafeMessage(Exception ex) {
    // Never expose internal details in production
    if (ex instanceof ValidationException) {
      return ex.getMessage();
    }
    if (ex instanceof ResourceNotFoundException) {
      return ex.getMessage();
    }
    // Generic message for unexpected errors
    return "An internal error occurred";
  }
  
  private ErrorClassification getErrorType(Exception ex) {
    if (ex instanceof ValidationException) {
      return ErrorClassification.ValidationError;
    }
    if (ex instanceof ForbiddenException) {
      return ErrorClassification.PermissionError;
    }
    return ErrorClassification.InternalError;
  }
}
```

### 7. Implement Subscriptions Safely

```java
@Component
public class OrderSubscriptionResolver {
  
  private final ApplicationContext context;
  
  @SubscriptionMapping
  public Flux<Order> orderStatusChanged(@Argument Long orderId) {
    // Create a topic stream for this order
    return Flux.create(sink -> {
      // Subscribe to domain events
      context.getBean(OrderEventBus.class)
        .subscribe(orderId, event -> {
          Order order = event.getOrder();
          sink.next(order);
        });
      
      // Handle cancellation
      sink.onCancel(() -> {
        context.getBean(OrderEventBus.class).unsubscribe(orderId);
      });
    });
  }
}
```

### 8. Use Persisted Queries

```javascript
// Client: Register query
POST /graphql/register
{
  "query": "query GetOrder($id: ID!) { order(id: $id) { id status } }"
}

Response: { "queryId": "query-abc123" }

// Later: Use query ID instead of full query
POST /graphql
{
  "queryId": "query-abc123",
  "variables": { "id": 101 }
}
```

**Benefits:**
- ✅ Enables HTTP caching (GET /graphql?queryId=abc123)
- ✅ Reduces bandwidth (send query ID instead of full query)
- ✅ Prevents ad-hoc attacks (only allow whitelisted queries)

### 9. Monitor Query Metrics

```java
@Component
public class GraphQLMetricsInstrumentation extends SimpleInstrumentation {
  
  private final MeterRegistry meterRegistry;
  
  @Override
  public ExecutionResult instrumentExecutionResult(
      ExecutionResult result,
      InstrumentationExecuteParameters parameters) {
    
    long duration = System.currentTimeMillis() - parameters.getStartTime();
    String operationName = parameters.getOperation().getName();
    
    meterRegistry.timer("graphql.query.duration")
      .tag("operation", operationName)
      .record(duration, TimeUnit.MILLISECONDS);
    
    if (!result.getErrors().isEmpty()) {
      meterRegistry.counter("graphql.query.errors")
        .tag("operation", operationName)
        .increment();
    }
    
    return result;
  }
}
```

---

## Security Best Practices (Both)

### 1. Input Validation

```java
// Always validate at API boundary
@PostMapping("/api/orders")
public ResponseEntity<Order> createOrder(
    @Valid @RequestBody OrderRequest req) {  // ✅ Validates
  return ...;
}

// GraphQL
@SchemaMapping(typeName = "Mutation")
public Order createOrder(
    @Argument @Valid OrderInput input) {  // ✅ Validates
  return ...;
}
```

### 2. Prevent SQL Injection

```java
// Bad
Query q = em.createQuery("SELECT o FROM Order o WHERE id = " + orderId);

// Good: Parameterized
Query q = em.createQuery("SELECT o FROM Order o WHERE id = :id")
  .setParameter("id", orderId);

// JPA (automatic)
orderRepository.findById(orderId);
```

### 3. Use HTTPS Always

```yaml
# application.yml
server:
  ssl:
    enabled: true
    key-store: classpath:keystore.jks
    key-store-password: ${KEY_STORE_PASSWORD}
  
  http:
    # Redirect HTTP to HTTPS
    redirect: true
```

### 4. Implement CORS Properly

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
  
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")
      .allowedOrigins("https://app.example.com")  // ✅ Specific
      .allowedMethods("GET", "POST", "PUT", "DELETE")
      .allowedHeaders("Content-Type", "Authorization")
      .allowCredentials(true)
      .maxAge(3600);
    
    // Bad
    // .allowedOrigins("*")  ❌ Too permissive
  }
}
```

---

## Summary

| Practice | REST | GraphQL |
|----------|------|---------|
| Input validation | Always | Always |
| Error handling | Envelope pattern | Query error handling |
| Caching | HTTP headers | DataLoader + App cache |
| N+1 prevention | Multiple endpoints | DataLoader, batch loading |
| Security | RBAC, auth headers | Field-level auth, complexity limits |
| Monitoring | Request logs | Query metrics, complexity analysis |

Use these as a checklist before deploying to production.
