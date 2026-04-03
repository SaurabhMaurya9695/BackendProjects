# N+1 Query Problem

## What is the N+1 Query Problem?

The N+1 query problem occurs when your code executes **1 query** to fetch a list of N records,
and then **N additional queries** to fetch related data for each record — resulting in **N+1 total queries**.

### Classic Example (Bad)
```
1 query  → SELECT * FROM authors           (fetches 100 authors)
100 queries → SELECT * FROM books WHERE author_id = ?   (one per author)
Total = 101 queries ❌
```

### Why It's a Problem
- **Performance killer**: 100 authors = 101 DB round trips instead of 2
- **Scales terribly**: 1000 authors = 1001 queries
- **Latency multiplies**: each query has network + DB overhead
- **Common in ORMs**: Hibernate, JPA lazy loading silently causes this

---

## Root Cause

Lazy loading. When you access a relationship (e.g., `author.getBooks()`),
the ORM fires a new SQL query on the spot — inside a loop = disaster.

```java
// Triggers N+1
List<Author> authors = authorRepo.findAll();         // 1 query
for (Author author : authors) {
    author.getBooks().size();  // 1 query per author ❌
}
```

---

## Solutions

### 1. Eager Loading / JOIN FETCH (Most Common)
Fetch everything in a single JOIN query.

```sql
-- Instead of:
SELECT * FROM authors;
SELECT * FROM books WHERE author_id = 1;
SELECT * FROM books WHERE author_id = 2;
...

-- Do this:
SELECT a.*, b.* FROM authors a
LEFT JOIN books b ON b.author_id = a.id;
```

In JPA/Hibernate:
```java
@Query("SELECT a FROM Author a LEFT JOIN FETCH a.books")
List<Author> findAllWithBooks();
```

**Pros:** Single query, simple  
**Cons:** Can cause Cartesian product if multiple collections are join-fetched simultaneously

---

### 2. Batch Fetching
Fetch related records in batches using IN clause instead of one-by-one.

```sql
-- Instead of N queries:
SELECT * FROM books WHERE author_id = 1;
SELECT * FROM books WHERE author_id = 2;
...

-- Do this (1 query):
SELECT * FROM books WHERE author_id IN (1, 2, 3, ..., 100);
```

In Hibernate:
```java
@BatchSize(size = 50)
@OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
private List<Book> books;
```

**Pros:** Works well with lazy loading, no Cartesian product  
**Cons:** Still 2 queries (1 + 1 batch), slightly more complex

---

### 3. DTO Projection
Select only what you need — avoid loading full entities.

```java
@Query("SELECT new com.example.AuthorBookDTO(a.name, b.title) " +
       "FROM Author a JOIN a.books b")
List<AuthorBookDTO> findAuthorBookProjections();
```

**Pros:** Minimal data transfer, fastest  
**Cons:** Need to define DTO classes

---

### 4. Manual Grouping (No ORM)
Fetch authors, fetch all books in one query, then group in memory.

```java
List<Author> authors = fetchAllAuthors();          // 1 query
List<Book> allBooks = fetchBooksByAuthorIds(ids);  // 1 query with IN clause
Map<Long, List<Book>> booksByAuthor = allBooks.stream()
    .collect(Collectors.groupingBy(Book::getAuthorId));

authors.forEach(a -> a.setBooks(booksByAuthor.get(a.getId())));
```

**Pros:** Full control, works without ORM  
**Cons:** More code, manual mapping

---

## Detection

### How to Spot It
1. **Logging:** Enable SQL logging and count queries in a loop
2. **APM tools:** Datadog, New Relic — look for repeated identical queries
3. **Hibernate stats:** `hibernate.generate_statistics=true`
4. **Code review:** any `getRelation()` call inside a loop is suspicious

### In Logs (Hibernate)
```
Hibernate: select * from authors
Hibernate: select * from books where author_id=?    ← repeated 100 times ❌
Hibernate: select * from books where author_id=?
...
```

---

## Decision Guide

| Situation | Solution |
|-----------|----------|
| Single collection, always needed | JOIN FETCH |
| Multiple collections | Batch Fetching |
| Read-only API response | DTO Projection |
| No ORM / raw JDBC | Manual grouping with IN clause |
| Conditional loading | Batch Fetching with @BatchSize |

---

## Key Takeaway

> Always ask: **"How many SQL queries does this code produce?"**  
> If the answer grows with N (the list size), you have an N+1 problem.

See `NPlusOneQueryDemo.java` for runnable examples simulating each approach.
