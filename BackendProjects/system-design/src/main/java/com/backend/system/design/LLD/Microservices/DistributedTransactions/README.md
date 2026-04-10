# Distributed Transactions

## The Problem First

In a monolith with a single database, transactions are easy:

```java
@Transactional
public void placeOrder(Order order) {
    inventoryService.deductStock(order);   // step 1
    paymentService.chargeCustomer(order);  // step 2
    orderService.saveOrder(order);         // step 3
    // if anything fails → everything rolls back automatically ✅
}
```

One DB → one transaction → ACID guarantees.

---

In microservices, each service **owns its own database**:

```
Order Service    → orders_db
Payment Service  → payments_db
Inventory Service → inventory_db
```

Now "place order" touches 3 different databases across 3 different services.
A single `@Transactional` cannot span multiple databases.

```
Order Service:     deduct stock  ✅
Payment Service:   charge card   ✅
Inventory Service: save order    ❌ FAILS

Now what?
- Stock is deducted ❌ (but order failed)
- Card is charged   ❌ (but order failed)
- No automatic rollback across services
```

This is the **distributed transaction problem**.

---

## Why Traditional ACID Doesn't Work Here

**ACID** properties (Atomicity, Consistency, Isolation, Durability) are guaranteed
by a single DB engine. The moment you cross service boundaries:

- No shared transaction coordinator
- Network failures can leave things in a half-done state
- You can't hold locks across services (performance killer)
- CAP theorem: in a distributed system, you can't have both Consistency and Availability during a partition

So distributed systems move from **ACID** to **BASE**:

```
ACID                          BASE
────────────────────────────────────────────────────
Atomic                        Basically Available
Consistent                    Soft state
Isolated                      Eventually consistent
Durable
```

**Eventual consistency** — the system will become consistent, but not instantly.

---

## Solution 1 — SAGA Pattern (Most Common)

### What it is

Instead of one big transaction, break it into a **sequence of local transactions**,
each in its own service. If one step fails, run **compensating transactions** to undo
the previous steps.

```
Step 1: Order Service     → creates order (PENDING)
Step 2: Inventory Service → deducts stock
Step 3: Payment Service   → charges customer
Step 4: Order Service     → marks order (CONFIRMED)

If Step 3 fails:
  Compensate Step 2: Inventory Service → restores stock
  Compensate Step 1: Order Service     → marks order (CANCELLED)
```

Each step has a **compensating action** — the undo operation.

---

### Two ways to implement SAGA

---

#### A) Choreography-based SAGA (Event-driven)

Services communicate via **events**. No central coordinator.
Each service listens for events and reacts.

```
Order Service        Inventory Service       Payment Service
     │                      │                      │
     │── OrderCreated ──────►│                      │
     │                      │── StockDeducted ─────►│
     │                      │                      │── PaymentProcessed ──►
     │◄─────────────────────────────────────── OrderConfirmed ────────────
     │
     │   (if payment fails)
     │◄──────────────── PaymentFailed ──────────────│
     │                      │◄── StockRestored ─────│  (compensating event)
     │── OrderCancelled ───►│
```

**Implementation with Spring + Kafka:**

```java
// Order Service — publishes event
@Service
public class OrderService {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @Transactional
    public Order createOrder(OrderRequest request) {
        Order order = new Order(request);
        order.setStatus(OrderStatus.PENDING);
        orderRepository.save(order);

        // Publish event to trigger next step
        kafkaTemplate.send("order-created", new OrderCreatedEvent(order.getId(), order.getItems()));
        return order;
    }

    // Listens for failure — runs compensation
    @KafkaListener(topics = "payment-failed")
    @Transactional
    public void onPaymentFailed(PaymentFailedEvent event) {
        Order order = orderRepository.findById(event.getOrderId()).orElseThrow();
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
}
```

```java
// Inventory Service — listens and reacts
@Service
public class InventoryService {

    @KafkaListener(topics = "order-created")
    @Transactional
    public void onOrderCreated(OrderCreatedEvent event) {
        try {
            deductStock(event.getItems());
            kafkaTemplate.send("stock-deducted", new StockDeductedEvent(event.getOrderId()));
        } catch (InsufficientStockException e) {
            kafkaTemplate.send("stock-failed", new StockFailedEvent(event.getOrderId()));
        }
    }

    // Compensation — restore stock if payment fails
    @KafkaListener(topics = "payment-failed")
    @Transactional
    public void onPaymentFailed(PaymentFailedEvent event) {
        restoreStock(event.getOrderId());
        kafkaTemplate.send("stock-restored", new StockRestoredEvent(event.getOrderId()));
    }
}
```

**Pros:**
- Loose coupling — services don't know about each other, only events
- No single point of failure
- Scales well

**Cons:**
- Hard to track overall transaction state (where is this order right now?)
- Cyclic dependencies can appear as event chains grow
- Debugging is hard — events are scattered across services

---

#### B) Orchestration-based SAGA (Central Coordinator)

A dedicated **Saga Orchestrator** (or Order Saga) tells each service what to do next.
It knows the full flow and handles failures centrally.

```
                    ┌─────────────────────┐
                    │   Order Saga        │
                    │   Orchestrator      │
                    └──────────┬──────────┘
                               │
          ┌────────────────────┼────────────────────┐
          ▼                    ▼                     ▼
   Inventory Service     Payment Service       Order Service
   deductStock()         chargeCustomer()      confirmOrder()

   (if any fails, orchestrator calls compensations in reverse order)
```

```java
@Component
public class PlaceOrderSaga {

    private final InventoryClient inventoryClient;
    private final PaymentClient paymentClient;
    private final OrderRepository orderRepository;

    public void execute(Order order) {
        boolean stockDeducted = false;
        boolean paymentCharged = false;

        try {
            // Step 1
            inventoryClient.deductStock(order.getItems());
            stockDeducted = true;

            // Step 2
            paymentClient.charge(order.getCustomerId(), order.getAmount());
            paymentCharged = true;

            // Step 3
            order.setStatus(OrderStatus.CONFIRMED);
            orderRepository.save(order);

        } catch (Exception e) {
            // Run compensations in reverse order
            if (paymentCharged) {
                paymentClient.refund(order.getCustomerId(), order.getAmount());
            }
            if (stockDeducted) {
                inventoryClient.restoreStock(order.getItems());
            }
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
        }
    }
}
```

**Pros:**
- Easy to understand — the full flow is in one place
- Easy to debug and monitor
- Clear error handling and compensation logic

**Cons:**
- Orchestrator becomes a central dependency
- Can turn into a "God class" as flows grow complex
- Tighter coupling than choreography

---

### Choreography vs Orchestration — When to use which

| | Choreography | Orchestration |
|---|---|---|
| **Coupling** | Loose | Tighter |
| **Flow visibility** | Hard to see the full picture | Clear in one place |
| **Complexity** | Grows with event chains | Grows with orchestrator logic |
| **Debugging** | Hard (distributed events) | Easier (central log) |
| **Best for** | Simple, independent flows | Complex flows with many steps |
| **Failure handling** | Each service handles its own | Centralized |

---

## Solution 2 — Two-Phase Commit (2PC)

### What it is

A classic distributed transaction protocol. A central **coordinator** manages
a two-phase process across all participating services.

```
Phase 1 — PREPARE
Coordinator → "Can you commit this transaction?" → Service-A, Service-B, Service-C
Service-A   → "Yes, ready" (locks resources, writes to temp log)
Service-B   → "Yes, ready"
Service-C   → "Yes, ready"

Phase 2 — COMMIT
Coordinator → "All ready, commit!" → Service-A, Service-B, Service-C
All services commit and release locks

(If any service says "No" in Phase 1 → Coordinator sends ROLLBACK to all)
```

### Why it's rarely used in microservices

- **Blocking** — resources are locked during Phase 1. If coordinator crashes between phases, locks are held forever
- **Single point of failure** — coordinator going down blocks all participants
- **Latency** — two network round trips for every transaction
- **Tight coupling** — all services must be available simultaneously

2PC works well for: databases within the same infrastructure (e.g., XA transactions across two DBs in the same data center). Not suitable for internet-scale microservices.

---

## Solution 3 — Outbox Pattern (Reliable Event Publishing)

### The subtle bug in SAGA

Even with SAGA, there's a hidden problem:

```java
@Transactional
public Order createOrder(OrderRequest request) {
    orderRepository.save(order);           // saves to DB ✅
    kafkaTemplate.send("order-created");   // publishes event
    // What if Kafka is down here? ❌
    // Order is saved but event never published
    // Inventory service never deducts stock
    // System is now inconsistent
}
```

The DB write and the event publish are **two separate operations** — they can't be atomic together.

### The Fix — Outbox Pattern

Instead of publishing to Kafka directly, write the event to an **outbox table**
in the same DB transaction. A separate poller reads the outbox and publishes to Kafka.

```
┌─────────────────────────────────────────────────────┐
│                 Order Service DB                     │
│                                                      │
│  ┌──────────────┐        ┌──────────────────────┐   │
│  │ orders table │        │  outbox table        │   │
│  │              │        │  id | event | status │   │
│  │ order saved  │───────►│  1  | OrderCreated   │   │
│  └──────────────┘  same  └──────────────────────┘   │
│                  transaction                         │
└─────────────────────────────────────────────────────┘
                                   │
                                   │ poller reads and publishes
                                   ▼
                              Kafka Topic
                         "order-created" event
```

```java
@Transactional
public Order createOrder(OrderRequest request) {
    // Step 1: save order
    Order order = orderRepository.save(new Order(request));

    // Step 2: write event to outbox (same transaction — atomic ✅)
    OutboxEvent event = new OutboxEvent(
        "order-created",
        objectMapper.writeValueAsString(new OrderCreatedEvent(order.getId()))
    );
    outboxRepository.save(event);

    return order;
    // both order + outbox event committed together or both rolled back
}
```

```java
// Separate poller — publishes outbox events to Kafka
@Scheduled(fixedDelay = 1000)
@Transactional
public void publishOutboxEvents() {
    List<OutboxEvent> pending = outboxRepository.findByStatus(PENDING);
    for (OutboxEvent event : pending) {
        kafkaTemplate.send(event.getTopic(), event.getPayload());
        event.setStatus(PUBLISHED);
        outboxRepository.save(event);
    }
}
```

**Guarantee:** The order and the event are always in sync — either both saved or neither.

---

## Solution 4 — Eventual Consistency with Idempotency

Accept that things may fail mid-way, but ensure:
1. Every operation can be **retried safely** (idempotent)
2. The system will **eventually reach a consistent state**

### Idempotency

An operation is idempotent if calling it multiple times gives the same result as calling it once.

```java
// NOT idempotent — calling twice charges twice ❌
paymentService.charge(customerId, amount);

// Idempotent — calling twice with same idempotency key charges once ✅
paymentService.charge(customerId, amount, idempotencyKey);

// Implementation
@Transactional
public PaymentResult charge(String customerId, BigDecimal amount, String idempotencyKey) {
    // Check if already processed
    Optional<Payment> existing = paymentRepository.findByIdempotencyKey(idempotencyKey);
    if (existing.isPresent()) {
        return existing.get().toResult();  // return same result, don't charge again ✅
    }

    // Process fresh payment
    Payment payment = processNewPayment(customerId, amount, idempotencyKey);
    return payment.toResult();
}
```

The `idempotencyKey` is usually a UUID generated by the caller and passed through all retries.

---

## Putting It All Together — Recommended Approach

For most production microservices systems:

```
SAGA (Orchestration or Choreography)
    +
Outbox Pattern (reliable event publishing)
    +
Idempotent operations (safe retries)
    +
Eventual consistency (accept it, design for it)
```

```
1. Use SAGA for multi-service business transactions
2. Use Outbox Pattern to ensure events are never lost
3. Make every service operation idempotent for safe retries
4. Monitor saga state — know which orders are stuck mid-flow
5. Have a reconciliation job — periodically find and fix inconsistent records
```

---

## Handling Stuck SAGAs

Sometimes a saga gets stuck — Service-B is down for 2 hours.
You need to handle this:

```java
// Track saga state in DB
public enum SagaStatus {
    STARTED,
    STOCK_DEDUCTED,
    PAYMENT_CHARGED,
    COMPLETED,
    COMPENSATING,
    CANCELLED
}

// Scheduled job — find sagas stuck for > 5 minutes
@Scheduled(fixedDelay = 60000)
public void recoverStuckSagas() {
    List<Saga> stuck = sagaRepository.findStuckSagas(Duration.ofMinutes(5));
    for (Saga saga : stuck) {
        sagaOrchestrator.retry(saga);  // or compensate, depending on business rules
    }
}
```

---

## Summary

```
Problem:   Microservices have separate DBs — no single ACID transaction.

Solutions:

1. SAGA Pattern          → Break into local transactions + compensations
   Choreography          → Event-driven, loose coupling
   Orchestration         → Central coordinator, clear flow

2. Two-Phase Commit (2PC) → Rarely used — blocking, not scalable
                            OK for same-infrastructure DB transactions

3. Outbox Pattern        → Solves "DB write + event publish" atomicity
                           Write to outbox table in same transaction
                           Poller publishes to Kafka reliably

4. Idempotency           → Make every operation safe to retry
                           Use idempotency keys to prevent double-processing

Golden rule:
  In distributed systems, don't fight eventual consistency — design for it.
  Make operations idempotent, track saga state, and have recovery jobs.
```

---

## Quick Comparison

| Approach | Consistency | Complexity | Use When |
|----------|-------------|------------|----------|
| SAGA Choreography | Eventual | Medium | Loosely coupled simple flows |
| SAGA Orchestration | Eventual | Medium-High | Complex flows, clear visibility needed |
| Two-Phase Commit | Strong | High | Same-DC, small number of participants |
| Outbox Pattern | Eventual | Low | Reliable event publishing (use with SAGA) |
| Idempotency | Eventual | Low | Always — pair with any above solution |
