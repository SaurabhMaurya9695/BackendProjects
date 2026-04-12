# Load Balancer Policies

## The Problem First

You have 3 instances of Order Service running:

```
Instance-1  →  192.168.1.1:8080
Instance-2  →  192.168.1.2:8080
Instance-3  →  192.168.1.3:8080
```

A client sends a request. Questions:
1. Which instance gets the request?
2. What if Instance-2 is overloaded?
3. What if Instance-3 is down?
4. How does the load balancer even know an instance is alive?

These are the questions load balancing policies answer.

---

## What is a Load Balancer?

A load balancer sits in front of your instances and **distributes incoming
requests across them**. Its job:

```
Client
  │
  ▼
Load Balancer  ←── knows which instances are alive and their load
  │
  ├──► Instance-1  (handles request)
  ├──► Instance-2  (handles request)
  └──► Instance-3  (handles request)
```

Two types:
- **Server-side load balancer** — a dedicated component (Nginx, AWS ALB, HAProxy)
- **Client-side load balancer** — logic lives in the client (Spring Cloud LoadBalancer, Ribbon)

---

## How Does the Load Balancer Know Which Instance is Available?

This is the core question. Three mechanisms work together:

---

### Mechanism 1 — Health Checks

The load balancer **periodically pings each instance** to verify it's alive.

```
Load Balancer
    │
    ├── GET http://instance-1:8080/actuator/health  → 200 OK ✅ (alive)
    ├── GET http://instance-2:8080/actuator/health  → 200 OK ✅ (alive)
    └── GET http://instance-3:8080/actuator/health  → timeout  ❌ (dead)
```

If an instance fails the health check → it's removed from the pool.
When it recovers → it's added back.

**Spring Boot Actuator health endpoint:**
```java
// application.yml
management:
  endpoints:
    web:
      exposure:
        include: health, info
  endpoint:
    health:
      show-details: always
```

```json
// GET /actuator/health response
{
  "status": "UP",
  "components": {
    "db": { "status": "UP" },
    "diskSpace": { "status": "UP" },
    "redis": { "status": "UP" }
  }
}
```

**Health check types:**
- **Active (Pull)** — load balancer polls instances every N seconds
- **Passive** — load balancer marks instance as down after N consecutive failures on real requests

---

### Mechanism 2 — Service Registry (Eureka / Consul)

In microservices, instances **register themselves** with a service registry on startup.
They send heartbeats every 30 seconds to prove they're alive.

```
Instance-1 starts → registers with Eureka → sends heartbeat every 30s
Instance-2 starts → registers with Eureka → sends heartbeat every 30s
Instance-3 crashes → stops sending heartbeat → Eureka removes it after 90s

Client asks Eureka: "give me instances of order-service"
Eureka returns: [Instance-1, Instance-2]  ← Instance-3 removed ✅
```

```java
// Instance registers itself
@SpringBootApplication
@EnableEurekaClient
public class OrderServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApp.class, args);
    }
}
```

```yaml
# application.yml — each instance
eureka:
  client:
    service-url:
      defaultZone: http://eureka-server:8761/eureka/
  instance:
    lease-renewal-interval-in-seconds: 30    # heartbeat every 30s
    lease-expiration-duration-in-seconds: 90 # removed if no heartbeat for 90s
```

---

### Mechanism 3 — Circuit Breaker (Stops Sending to Struggling Instances)

Even if an instance is technically "alive", it might be slow or throwing errors.
A circuit breaker **stops sending requests to a struggling instance** temporarily.

```
States:
  CLOSED   → instance is healthy, requests flow normally
  OPEN     → too many failures, requests blocked for cooldown period
  HALF-OPEN → after cooldown, let a few requests through to test recovery
```

```
Normal:     request → Instance-2 → 200 OK  (CLOSED)
Failures:   request → Instance-2 → 500     (5 times)
Opens:      Circuit OPEN → requests go to Instance-1 and Instance-3 instead
Recovery:   after 10s → HALF-OPEN → send 1 test request to Instance-2
Success:    back to CLOSED ✅
```

---

## Load Balancing Policies (Algorithms)

Once the load balancer knows which instances are alive, it uses a **policy** to
decide which one gets the next request.

---

### Policy 1 — Round Robin (Default)

Distribute requests **sequentially** across all instances, cycling through them.

```
Request-1  →  Instance-1
Request-2  →  Instance-2
Request-3  →  Instance-3
Request-4  →  Instance-1  ← wraps around
Request-5  →  Instance-2
Request-6  →  Instance-3
```

**Implementation:**
```java
public class RoundRobinLoadBalancer {
    private final List<String> instances;
    private final AtomicInteger counter = new AtomicInteger(0);

    public String next() {
        int index = counter.getAndIncrement() % instances.size();
        return instances.get(index);
    }
}
```

**Pros:** Simple, even distribution, no overhead
**Cons:** Ignores instance load — a slow instance gets the same traffic as a fast one
**Best for:** Homogeneous instances with similar capacity and response time

---

### Policy 2 — Weighted Round Robin

Each instance gets a **weight** proportional to its capacity.
Higher weight = more requests.

```
Instance-1  weight=3  (powerful server)
Instance-2  weight=2
Instance-3  weight=1  (weaker server)

Distribution for every 6 requests:
Request-1 → Instance-1
Request-2 → Instance-1
Request-3 → Instance-1
Request-4 → Instance-2
Request-5 → Instance-2
Request-6 → Instance-3
```

**Implementation:**
```java
public class WeightedRoundRobinLoadBalancer {
    private final List<String> pool;  // pre-expanded pool based on weights

    public WeightedRoundRobinLoadBalancer(Map<String, Integer> weights) {
        pool = new ArrayList<>();
        weights.forEach((instance, weight) -> {
            for (int i = 0; i < weight; i++) {
                pool.add(instance);  // Instance-1 appears 3 times, etc.
            }
        });
    }

    private final AtomicInteger counter = new AtomicInteger(0);

    public String next() {
        int index = counter.getAndIncrement() % pool.size();
        return pool.get(index);
    }
}
```

**Best for:** Instances with different CPU/memory capacity

---

### Policy 3 — Least Connections

Send the next request to the instance with the **fewest active connections**.

```
Instance-1  →  5 active connections
Instance-2  →  2 active connections  ← next request goes here ✅
Instance-3  →  8 active connections
```

**Implementation:**
```java
public class LeastConnectionsLoadBalancer {
    private final Map<String, AtomicInteger> connections = new ConcurrentHashMap<>();

    public String next() {
        return connections.entrySet().stream()
            .min(Map.Entry.comparingByValue(
                Comparator.comparingInt(AtomicInteger::get)))
            .map(Map.Entry::getKey)
            .orElseThrow();
    }

    public void onRequestStart(String instance) {
        connections.get(instance).incrementAndGet();
    }

    public void onRequestEnd(String instance) {
        connections.get(instance).decrementAndGet();
    }
}
```

**Pros:** Automatically routes away from overloaded instances
**Cons:** Requires tracking active connections — more overhead
**Best for:** Requests with **variable duration** (some quick, some slow)

---

### Policy 4 — Least Response Time

Send the next request to the instance with the **lowest average response time**.

```
Instance-1  →  avg response: 120ms
Instance-2  →  avg response: 45ms   ← next request goes here ✅
Instance-3  →  avg response: 200ms
```

**Implementation:**
```java
public class LeastResponseTimeLoadBalancer {
    private final Map<String, Long> avgResponseTimes = new ConcurrentHashMap<>();

    public String next() {
        return avgResponseTimes.entrySet().stream()
            .min(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElseThrow();
    }

    public void recordResponseTime(String instance, long responseTimeMs) {
        avgResponseTimes.merge(instance, responseTimeMs,
            (oldAvg, newTime) -> (oldAvg + newTime) / 2);  // rolling average
    }
}
```

**Best for:** When instances have different performance characteristics

---

### Policy 5 — IP Hash (Sticky Sessions)

Hash the **client's IP address** to always send the same client to the same instance.

```
Client IP: 192.168.10.5  →  hash("192.168.10.5") % 3 = 1  →  always Instance-1
Client IP: 10.0.0.22     →  hash("10.0.0.22") % 3 = 0     →  always Instance-3
```

**Implementation:**
```java
public class IPHashLoadBalancer {
    private final List<String> instances;

    public String next(String clientIp) {
        int index = Math.abs(clientIp.hashCode()) % instances.size();
        return instances.get(index);
    }
}
```

**Pros:** Same client always hits the same instance — good for stateful sessions
**Cons:** If instance goes down, that client's session is lost. Uneven distribution if few clients.
**Best for:** Apps with **server-side session state** (shopping carts, login sessions)

---

### Policy 6 — Random

Pick a random instance for each request.

```java
public class RandomLoadBalancer {
    private final List<String> instances;
    private final Random random = new Random();

    public String next() {
        return instances.get(random.nextInt(instances.size()));
    }
}
```

**Pros:** Simple, no state to maintain
**Cons:** Can accidentally overload one instance by chance
**Best for:** Large number of instances where statistical distribution evens out

---

## All Policies — Side by Side

| Policy | How it picks | Tracks state | Best for |
|--------|-------------|--------------|----------|
| Round Robin | Sequential cycle | Counter only | Uniform instances, stateless |
| Weighted Round Robin | By capacity weight | Counter + weights | Different capacity instances |
| Least Connections | Fewest active connections | Connection count | Variable request duration |
| Least Response Time | Fastest avg response | Response time metrics | Mixed performance instances |
| IP Hash | Client IP hash | None | Sticky sessions |
| Random | Random pick | None | Large, uniform instance pools |

---

## Spring Cloud LoadBalancer (Client-Side)

Spring Cloud LoadBalancer implements client-side load balancing.
The client itself decides which instance to call.

```yaml
# pom.xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-loadbalancer</artifactId>
</dependency>
```

```java
// Default is Round Robin — change to Random
@Configuration
public class LoadBalancerConfig {

    @Bean
    ReactorLoadBalancer<ServiceInstance> randomLoadBalancer(
            Environment env,
            LoadBalancerClientFactory factory) {

        String name = env.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new RandomLoadBalancer(
            factory.getLazyProvider(name, ServiceInstanceListSupplier.class), name);
    }
}
```

```java
// Using @LoadBalanced RestTemplate — automatically load balanced
@Configuration
public class RestTemplateConfig {

    @Bean
    @LoadBalanced  // adds load balancing to all calls made with this template
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

@Service
public class OrderService {

    @Autowired
    private RestTemplate restTemplate;

    public Product getProduct(Long id) {
        // "product-service" resolved to an actual instance by load balancer
        return restTemplate.getForObject(
            "http://product-service/api/products/" + id,
            Product.class
        );
    }
}
```

---

## Full Scenario — 3 Instances, One Goes Down

```
Setup:
  Instance-1: 192.168.1.1:8080  HEALTHY
  Instance-2: 192.168.1.2:8080  HEALTHY
  Instance-3: 192.168.1.3:8080  HEALTHY
  Policy: Round Robin

Timeline:

t=0s   Request-1 → Instance-1  ✅
t=1s   Request-2 → Instance-2  ✅
t=2s   Request-3 → Instance-3  ✅
t=3s   Instance-3 crashes
t=4s   Request-4 → Instance-1  ✅
t=5s   Request-5 → Instance-2  ✅
t=6s   Request-6 → Instance-3  ❌ (fails — circuit opens)
t=7s   Health check detects Instance-3 is down → removed from pool
t=8s   Request-7 → Instance-1  ✅ (only 2 in pool now)
t=9s   Request-8 → Instance-2  ✅
t=90s  Instance-3 recovers → passes health check → added back to pool
t=91s  Request-N → Instance-3  ✅ (back in rotation)
```

---

## Health Check Configuration (Spring Boot)

```yaml
# application.yml
management:
  endpoint:
    health:
      show-details: always
  health:
    livenessstate:
      enabled: true     # is the app running?
    readinessstate:
      enabled: true     # is the app ready to accept traffic?

# Kubernetes probes
# livenessProbe  → restart pod if fails (app crashed)
# readinessProbe → remove from load balancer if fails (app not ready)
```

Custom health indicator:
```java
@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    @Autowired
    private DataSource dataSource;

    @Override
    public Health health() {
        try (Connection conn = dataSource.getConnection()) {
            conn.prepareStatement("SELECT 1").execute();
            return Health.up()
                .withDetail("database", "reachable")
                .build();
        } catch (Exception e) {
            return Health.down()
                .withDetail("database", "unreachable")
                .withException(e)
                .build();
        }
    }
}
```

---

## Summary

```
Question: 3 instances running — how does the load balancer know which is available?

Answer: Three mechanisms work together:
  1. Health Checks    → periodically ping /actuator/health
                        remove instance if it fails, add back when it recovers
  2. Service Registry → instances self-register (Eureka/Consul)
                        heartbeat every 30s — removed if heartbeat stops
  3. Circuit Breaker  → stop sending to struggling instances automatically

Policies (how to pick which available instance to send to):

  Round Robin          → default, even distribution, no overhead
  Weighted Round Robin → different capacity servers
  Least Connections    → variable duration requests
  Least Response Time  → mixed performance instances
  IP Hash              → sticky sessions (same client → same instance)
  Random               → large uniform pools

In Spring Boot:
  @LoadBalanced RestTemplate / WebClient → client-side load balancing
  Default policy: Round Robin
  Registry: Eureka (Spring Cloud Netflix) or Consul
```
