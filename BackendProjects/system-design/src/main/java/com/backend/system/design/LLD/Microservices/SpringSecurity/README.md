# Spring Security in Distributed Microservices

## The Problem — Why Security is Hard in Microservices

In a monolith, security is simple:
```
User → Login → Session stored in memory → Every request checks the session
```

In microservices, this breaks:
```
User → API Gateway → Service-A → Service-B → Service-C
                         ↓
              Which service validates the token?
              How does Service-B trust Service-A?
              If Service-A already authenticated the user,
              does Service-B need to re-authenticate?
```

Three new problems appear:
1. **Who validates the token?** — Every service? Only the gateway?
2. **How do services trust each other?** — Service-to-service calls
3. **How does user identity travel?** — From gateway down to Service-C

---

## The Standard Architecture

```
                         ┌─────────────────────────────────────┐
                         │         Auth Service                 │
                         │  - issues JWT tokens on login        │
                         │  - manages users, roles, passwords   │
                         └──────────────┬──────────────────────┘
                                        │ issues JWT
                                        ▼
User ──── request + JWT ──► API Gateway (validates JWT here)
                                        │
                          ┌─────────────┼──────────────┐
                          ▼             ▼               ▼
                     Service-A      Service-B       Service-C
                   (trusts gateway, forwards user context via headers)
```

**Key idea:** JWT is validated **once** at the API Gateway.
Internal services trust the gateway and read user context from headers.

---

## Step 1 — Auth Service (Issues Tokens)

The Auth Service is the only service that handles login and issues JWTs.

```java
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtTokenProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        // 1. Authenticate username + password
        Authentication auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // 2. Issue JWT
        String token = jwtProvider.generateToken(auth);
        return ResponseEntity.ok(new TokenResponse(token));
    }
}
```

---

## Step 2 — JWT (JSON Web Token)

JWT is the standard token format for stateless authentication in microservices.

### Structure
```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsInJvbGVzIjpbIlJPTEVfVVNFUiJdfQ.signature
       ▲                              ▲                              ▲
    Header                         Payload                       Signature
  (algorithm)              (userId, roles, expiry)           (tamper-proof)
```

### Payload example (decoded)
```json
{
  "sub": "user123",
  "roles": ["ROLE_USER", "ROLE_ADMIN"],
  "tenantId": "acme-corp",
  "iat": 1700000000,
  "exp": 1700003600
}
```

### JwtTokenProvider
```java
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationMs;

    public String generateToken(Authentication auth) {
        UserDetails user = (UserDetails) auth.getPrincipal();

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", user.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
```

---

## Step 3 — API Gateway (Validates JWT Once)

The gateway is the **single entry point**. It validates the JWT on every incoming request
so internal services don't have to.

```java
@Component
public class JwtAuthenticationFilter implements GatewayFilter {

    private final JwtTokenProvider jwtProvider;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = extractToken(exchange.getRequest());

        if (token == null || !jwtProvider.validateToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Token is valid — extract user info and forward downstream
        String userId = jwtProvider.getUsernameFromToken(token);
        String roles  = jwtProvider.getRolesFromToken(token);

        // Add user context as headers for internal services
        ServerHttpRequest enrichedRequest = exchange.getRequest().mutate()
                .header("X-User-Id", userId)
                .header("X-User-Roles", roles)
                .build();

        return chain.filter(exchange.mutate().request(enrichedRequest).build());
    }

    private String extractToken(ServerHttpRequest request) {
        String header = request.getHeaders().getFirst("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
```

---

## Step 4 — Internal Services (Trust the Gateway)

Internal services do NOT re-validate the JWT. They simply read user context
from the headers forwarded by the gateway.

```java
@Configuration
@EnableWebSecurity
public class InternalServiceSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            // Add filter that reads X-User-Id and X-User-Roles headers
            .addFilterBefore(new InternalHeaderAuthFilter(), UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests()
                .requestMatchers("/internal/**").hasRole("INTERNAL")
                .anyRequest().authenticated();

        return http.build();
    }
}
```

```java
// Reads user identity from gateway-forwarded headers
public class InternalHeaderAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String userId = request.getHeader("X-User-Id");
        String roles  = request.getHeader("X-User-Roles");

        if (userId != null) {
            // Reconstruct security context from headers
            List<GrantedAuthority> authorities = Arrays.stream(roles.split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userId, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(request, response);
    }
}
```

Now inside any internal service, you can access the user:
```java
@GetMapping("/orders")
public List<Order> getOrders() {
    String userId = SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName();   // "user123" ✅
    return orderService.getOrdersForUser(userId);
}
```

---

## Step 5 — Service-to-Service Calls (mTLS or Internal JWT)

When Service-A calls Service-B directly (no user involved), two common approaches:

### Option A — Internal Service Token
Service-A gets its own JWT (a service account token) and sends it to Service-B.

```java
@Component
public class InternalFeignConfig implements RequestInterceptor {

    @Value("${internal.service.token}")
    private String serviceToken;

    @Override
    public void apply(RequestTemplate template) {
        // Add service identity to all outgoing Feign calls
        template.header("Authorization", "Bearer " + serviceToken);
        template.header("X-Service-Name", "order-service");
    }
}
```

### Option B — mTLS (Mutual TLS)
Both services present certificates to prove identity at the network level.
No tokens needed — the TLS handshake itself authenticates both sides.
Common in Kubernetes with service meshes (Istio, Linkerd).

```
Service-A ──── presents cert ────► Service-B
Service-A ◄─── presents cert ───── Service-B
         (both verified at TLS layer)
```

---

## Step 6 — Role-Based Authorization in Services

Once the user context is available via `SecurityContextHolder`, authorization is the same
as in a monolith:

```java
// Method-level security
@PreAuthorize("hasRole('ADMIN')")
@DeleteMapping("/users/{id}")
public ResponseEntity<Void> deleteUser(@PathVariable String id) {
    userService.delete(id);
    return ResponseEntity.noContent().build();
}

@PreAuthorize("hasRole('USER') and #userId == authentication.name")
@GetMapping("/users/{userId}/profile")
public UserProfile getProfile(@PathVariable String userId) {
    return userService.getProfile(userId);
}
```

Enable method security in config:
```java
@Configuration
@EnableMethodSecurity   // enables @PreAuthorize, @PostAuthorize
public class MethodSecurityConfig { }
```

---

## Step 7 — Token Refresh

JWTs expire. Clients need a way to get a new token without logging in again.

```
Client ──── POST /auth/refresh  { refreshToken: "..." } ────► Auth Service
       ◄─── { accessToken: "new JWT", refreshToken: "new refresh" } ────
```

```java
@PostMapping("/auth/refresh")
public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshRequest request) {
    // Validate refresh token (stored in DB or Redis, not JWT)
    String userId = refreshTokenService.validate(request.getRefreshToken());

    // Issue new access token
    String newAccessToken = jwtProvider.generateTokenForUser(userId);
    String newRefreshToken = refreshTokenService.rotate(request.getRefreshToken());

    return ResponseEntity.ok(new TokenResponse(newAccessToken, newRefreshToken));
}
```

**Access token** — short lived (15 min), stateless JWT
**Refresh token** — long lived (7 days), stored in DB/Redis, can be revoked

---

## The Complete Flow

```
1. User logs in
   POST /auth/login { username, password }
   ← JWT access token + refresh token

2. User makes a request
   GET /api/orders  Authorization: Bearer <JWT>
   → API Gateway validates JWT signature + expiry
   → Gateway extracts userId, roles from JWT payload
   → Gateway adds X-User-Id, X-User-Roles headers
   → Forwards request to Order Service

3. Order Service handles request
   → Reads X-User-Id from header (no JWT re-validation)
   → SecurityContextHolder has the user identity
   → @PreAuthorize checks role
   → Returns user's orders

4. Order Service calls Inventory Service internally
   → Feign client adds service token to request
   → Inventory Service verifies it's an internal call
   → Returns inventory data

5. JWT expires
   POST /auth/refresh { refreshToken }
   ← New JWT access token
```

---

## Security Checklist for Microservices

```
TOKEN DESIGN
[ ] Short-lived access tokens (15–60 min)
[ ] Refresh token rotation (invalidate old on use)
[ ] Include only necessary claims in JWT payload
[ ] Sign with strong algorithm (RS256 for multi-service, HS256 for single-secret)

API GATEWAY
[ ] Validate JWT at gateway — not in every service
[ ] Strip Authorization header before forwarding internally
[ ] Rate limiting at gateway level
[ ] HTTPS only — never HTTP

INTERNAL COMMUNICATION
[ ] Internal endpoints not exposed externally
[ ] Service-to-service auth (service tokens or mTLS)
[ ] Propagate user context via headers, not re-authentication

AUTHORIZATION
[ ] Role checks at method level with @PreAuthorize
[ ] Never trust client-provided role — always from the token
[ ] Row-level security: always filter by authenticated userId

SECRETS
[ ] JWT secret in environment variable, never in code
[ ] Rotate secrets without downtime (support multiple valid keys)
[ ] Use asymmetric keys (RSA) if multiple services validate tokens
```

---

## RS256 vs HS256 — Which to use?

| | HS256 (Symmetric) | RS256 (Asymmetric) |
|---|---|---|
| **Keys** | One shared secret | Private key (Auth Service) + Public key (all services) |
| **Who can verify** | Anyone with the secret | Anyone with the public key |
| **Who can issue** | Anyone with the secret | Only Auth Service (has private key) |
| **Best for** | Single service or tight control | Multiple services verifying tokens |
| **Secret exposure risk** | High — secret shared across services | Low — public key is safe to distribute |

For microservices: **RS256 is safer** — services only need the public key to verify tokens,
they cannot issue new tokens even if compromised.

---

## Summary

```
Problem:  In microservices, every service cannot independently manage auth.
          It creates duplication, inconsistency, and performance overhead.

Solution: Centralized Auth Service + JWT + API Gateway validation

Flow:
  Auth Service  →  issues JWT
  API Gateway   →  validates JWT once, forwards user context as headers
  Services      →  read headers, no re-validation, use @PreAuthorize
  Service-to-Service → service tokens or mTLS

Key rules:
  1. Validate token ONCE at the edge (gateway)
  2. Internal services trust the gateway — read headers
  3. Never re-authenticate inside the cluster
  4. Short-lived access tokens + rotatable refresh tokens
  5. RS256 for multi-service environments
```
