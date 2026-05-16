# API Design Patterns — REST vs GraphQL

Comprehensive comparison, trade-offs, and implementation patterns for REST and GraphQL APIs.

## 📚 Table of Contents

1. [Module Overview](#module-overview)
2. [Quick Comparison](#quick-comparison)
3. [Contents](#contents)
4. [When to Use What](#when-to-use-what)
5. [Getting Started](#getting-started)

---

## Module Overview

This module contains **detailed comparisons, best practices, and Q&A** for the two dominant API architectural styles:

- **REST** — Resource-oriented, HTTP-centric, stateless, cacheable
- **GraphQL** — Query-oriented, strongly-typed, flexible field selection, single endpoint

Both have use cases. This module helps you **choose correctly** and **implement each well**.

---

## Quick Comparison

| Aspect | REST | GraphQL |
|--------|------|---------|
| **Architecture** | Resource-oriented | Query-oriented |
| **Endpoints** | Multiple (`/users`, `/orders`) | Single endpoint (`/graphql`) |
| **Field Selection** | Fixed response shape (over/under-fetching) | Client specifies exact fields (precise) |
| **Versioning** | Explicit (`/v1/`, `/v2/`) | None (schema evolution) |
| **Caching** | Native HTTP caching (ETags, Cache-Control) | Harder (mostly client-side, Apollo) |
| **Learning Curve** | Easier, familiar to most devs | Steeper, new mental model |
| **Query Complexity** | Simple | Can become complex; needs protection |
| **Real-time** | Polling or WebSockets | Subscriptions built-in |
| **Mobile-friendly** | Good (with pagination) | Excellent (precise field selection) |
| **Enterprise Adoption** | Standard | Growing (AWS AppSync, Shopify, GitHub) |

---

## Contents

### 📄 Documentation Files

1. **[REST_vs_GraphQL.md](REST_vs_GraphQL.md)** — Deep architectural comparison
   - Design philosophy
   - Protocol differences
   - Strengths & weaknesses
   - Real-world trade-offs

2. **[QUESTIONS_AND_ANSWERS.md](QUESTIONS_AND_ANSWERS.md)** — Interview & conceptual Q&A
   - 50+ questions across all difficulty levels
   - Beginner, Intermediate, Advanced sections
   - Scenario-based questions
   - Design decision questions

3. **[BEST_PRACTICES.md](BEST_PRACTICES.md)** — Production implementation guidance
   - REST best practices
   - GraphQL best practices
   - Security considerations
   - Performance optimization
   - Common pitfalls

4. **[DECISION_MATRIX.md](DECISION_MATRIX.md)** — How to choose
   - Scoring rubric
   - Decision tree
   - Organization-specific factors
   - Hybrid approaches

### 💻 Code Examples

**REST API Examples** (`com.backend.system.design.LLD.APIDesignPatterns.rest.*`)
- `UserRestController.java` — Pagination, filtering, sorting
- `OrderRestService.java` — Caching, N+1 solutions
- `RestPaginationStrategy.java` — Offset vs cursor patterns
- `RestCachingStrategy.java` — ETags, Cache-Control headers

**GraphQL Examples** (`com.backend.system.design.LLD.APIDesignPatterns.graphql.*`)
- `UserGraphQLResolver.java` — Query resolvers, N+1 problems & solutions
- `OrderGraphQLSubscription.java` — Real-time subscriptions
- `GraphQLComplexityValidator.java` — Query depth limiting, cost analysis
- `GraphQLErrorHandler.java` — Error handling patterns

---

## When to Use What

### ✅ **Choose REST when:**
- Simple, resource-centric data (CRUD operations)
- Heavy caching is important (CDN, browser cache, ETags)
- Your team is more familiar with REST
- Public APIs (stateless, standard HTTP semantics)
- High-volume, low-complexity queries
- Integration with older systems expecting REST

**Example:** E-commerce product catalog, public APIs (GitHub REST v3, AWS API Gateway)

### ✅ **Choose GraphQL when:**
- Complex, interconnected data models
- Clients have diverse field requirements (web, mobile, IoT)
- Over/under-fetching is a significant problem
- Real-time data critical (subscriptions)
- Internal APIs (controlled clients)
- Need for powerful developer tools (introspection, auto-completion)

**Example:** Mobile app with bandwidth constraints, internal APIs (Shopify, GitHub GraphQL, AWS AppSync)

### 🔀 **Hybrid Approach:**
- Expose REST for simple, high-volume endpoints
- Use GraphQL for complex, flexible queries
- Migrate gradually (run both in parallel)

---

## Getting Started

### 1. Read the Conceptual Docs
Start with `REST_vs_GraphQL.md` to understand the fundamental differences.

### 2. Study Q&A
Review `QUESTIONS_AND_ANSWERS.md`:
- Beginner section if new to either paradigm
- Intermediate if experienced with one but not the other
- Advanced for system design interviews

### 3. Examine Code Examples
Look at the Java implementation examples to see patterns in action.

### 4. Use Decision Matrix
Run through `DECISION_MATRIX.md` for your specific use case.

### 5. Reference Best Practices
Return to `BEST_PRACTICES.md` when implementing.

---

## Key Insights

### REST
> REST is not about the endpoints — it's about **treating data as resources, using HTTP semantics correctly, and leveraging the cacheable nature of GET requests**.

### GraphQL
> GraphQL is not just "fancy queries" — it's about **moving query flexibility to the client, eliminating over/under-fetching, and building introspectable, self-documenting APIs**.

---

## Common Misconceptions Debunked

### ❌ "REST requires multiple endpoints"
✅ REST = architectural constraints (stateless, cacheable, uniform interface). Endpoints follow naturally from resources, but are not the definition.

### ❌ "GraphQL is always faster"
✅ GraphQL can enable precise fetching (faster for clients), but query complexity & N+1 problems can kill performance if not managed.

### ❌ "GraphQL replaces REST"
✅ Both coexist. Many platforms run both (GitHub REST v3 + GraphQL v4, Shopify REST + GraphQL).

### ❌ "REST has no way to prevent over-fetching"
✅ Sparse fieldsets (`?fields=id,name`) exist in REST, but are not standardized. GraphQL makes it the default behavior.

---

## Quick Navigation

- **Architecture Comparison:** [REST_vs_GraphQL.md](REST_vs_GraphQL.md)
- **Interview Prep:** [QUESTIONS_AND_ANSWERS.md](QUESTIONS_AND_ANSWERS.md) → Intermediate section
- **Choosing for Your Project:** [DECISION_MATRIX.md](DECISION_MATRIX.md)
- **Implementing Safely:** [BEST_PRACTICES.md](BEST_PRACTICES.md)

---

## Further Learning

### REST
- RFC 7231 — HTTP Semantics and Content
- REST in Practice — Jim Webber et al.
- Designing Web APIs — Arnaud Lauret

### GraphQL
- [graphql.org/learn](https://graphql.org/learn/)
- Apollo GraphQL Documentation
- GraphQL in Action — Samer Buna

---

⭐ **Pro Tip:** Many senior engineers default to REST for simplicity, then add GraphQL layer later when complexity demands it. Start simple; refactor when you hit limits.
