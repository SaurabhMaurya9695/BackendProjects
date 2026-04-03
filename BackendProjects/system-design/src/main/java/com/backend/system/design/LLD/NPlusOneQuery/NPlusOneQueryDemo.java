package com.backend.system.design.LLD.NPlusOneQuery;

import java.util.*;
import java.util.stream.Collectors;

/**
 * N+1 Query Problem - Demo
 * <p>
 * Simulates the N+1 problem and 4 common fixes using in-memory data
 * (no real DB needed). Each approach prints query counts to show the difference.
 */
public class NPlusOneQueryDemo {

    static class Author {

        long id;
        String name;
        List<Book> books; // populated lazily or eagerly depending on approach

        Author(long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    static class Book {

        long id;
        long authorId;
        String title;

        Book(long id, long authorId, String title) {
            this.id = id;
            this.authorId = authorId;
            this.title = title;
        }
    }

    static int queryCount = 0;
    static List<Author> DB_AUTHORS = List.of(new Author(1, "Joshua Bloch"), new Author(2, "Robert Martin"),
            new Author(3, "Martin Fowler"));

    static List<Book> DB_BOOKS = List.of(new Book(1, 1, "Effective Java"), new Book(2, 1, "Java Puzzlers"),
            new Book(3, 2, "Clean Code"), new Book(4, 2, "Clean Architecture"), new Book(5, 3, "Refactoring"),
            new Book(6, 3, "Patterns of Enterprise Application Architecture"));

    /**
     * Simulates: SELECT * FROM authors
     */
    static List<Author> fetchAllAuthors() {
        queryCount++;
        System.out.println("  [SQL] SELECT * FROM authors  →  " + DB_AUTHORS.size() + " rows");
        return new ArrayList<>(DB_AUTHORS);
    }

    /**
     * Simulates: SELECT * FROM books WHERE author_id = ?
     */
    static List<Book> fetchBooksByAuthorId(long authorId) {
        queryCount++;
        List<Book> result = DB_BOOKS.stream().filter(b -> b.authorId == authorId).collect(Collectors.toList());
        System.out.println(
                "  [SQL] SELECT * FROM books WHERE author_id = " + authorId + "  →  " + result.size() + " rows");
        return result;
    }

    /**
     * Simulates: SELECT a.*, b.* FROM authors a LEFT JOIN books b ON b.author_id = a.id
     */
    static List<Author> fetchAuthorsWithBooksJoin() {
        queryCount++;
        System.out.println("  [SQL] SELECT a.*, b.* FROM authors a LEFT JOIN books b ON b.author_id = a.id" + "  →  "
                + DB_BOOKS.size() + " rows");
        Map<Long, Author> authorMap = new LinkedHashMap<>();
        for (Author a : DB_AUTHORS) {
            Author copy = new Author(a.id, a.name);
            copy.books = new ArrayList<>();
            authorMap.put(a.id, copy);
        }
        for (Book b : DB_BOOKS) {
            authorMap.get(b.authorId).books.add(b);
        }
        return new ArrayList<>(authorMap.values());
    }

    /**
     * Simulates: SELECT * FROM books WHERE author_id IN (1, 2, 3)
     */
    static List<Book> fetchBooksByAuthorIds(List<Long> authorIds) {
        queryCount++;
        List<Book> result = DB_BOOKS.stream().filter(b -> authorIds.contains(b.authorId)).collect(Collectors.toList());
        System.out.println(
                "  [SQL] SELECT * FROM books WHERE author_id IN " + authorIds + "  →  " + result.size() + " rows");
        return result;
    }

    /**
     * APPROACH 1: N+1 Problem (Bad)
     * 1 query for authors + N queries for books (one per author)
     */
    static void demonstrateNPlusOne() {
        System.out.println("\n--- APPROACH 1: N+1 Problem (BAD) ---");
        queryCount = 0;

        List<Author> authors = fetchAllAuthors();
        for (Author author : authors) {
            // Each call fires a separate query — this is the N+1 problem
            List<Book> books = fetchBooksByAuthorId(author.id);
            author.books = books;
        }

        System.out.println("Total queries: " + queryCount + " (1 + " + DB_AUTHORS.size() + " = N+1) ❌");
        printAuthors(authors);
    }

    /**
     * APPROACH 2: JOIN FETCH (Best for single collection)
     * 1 query with JOIN — fetches everything at once
     */
    static void demonstrateJoinFetch() {
        System.out.println("\n--- APPROACH 2: JOIN FETCH (GOOD) ---");
        queryCount = 0;

        List<Author> authors = fetchAuthorsWithBooksJoin();

        System.out.println("Total queries: " + queryCount + " ✅");
        printAuthors(authors);
    }

    /**
     * APPROACH 3: Batch Fetching (Good for lazy loading scenarios)
     * 1 query for authors + 1 batch query using IN clause
     */
    static void demonstrateBatchFetch() {
        System.out.println("\n--- APPROACH 3: BATCH FETCHING (GOOD) ---");
        queryCount = 0;

        List<Author> authors = fetchAllAuthors();

        List<Long> authorIds = authors.stream().map(a -> a.id).collect(Collectors.toList());

        // Single IN query instead of N queries
        List<Book> allBooks = fetchBooksByAuthorIds(authorIds);

        // Group in memory
        Map<Long, List<Book>> booksByAuthor = allBooks.stream().collect(Collectors.groupingBy(b -> b.authorId));

        authors.forEach(a -> a.books = booksByAuthor.getOrDefault(a.id, Collections.emptyList()));

        System.out.println("Total queries: " + queryCount + " ✅");
        printAuthors(authors);
    }

    /**
     * APPROACH 4: DTO Projection (Best for read-only, minimal data)
     * Select only required fields — no full entity loading
     */
    static void demonstrateDtoProjection() {
        System.out.println("\n--- APPROACH 4: DTO PROJECTION (GOOD for read-only) ---");
        queryCount = 0;

        // Simulates: SELECT a.name, b.title FROM authors a JOIN books b ON b.author_id = a.id
        queryCount++;
        System.out.println("  [SQL] SELECT a.name, b.title FROM authors a JOIN books b ON b.author_id = a.id");

        record AuthorBookDTO(String authorName, String bookTitle) {

        }

        List<AuthorBookDTO> projections = new ArrayList<>();
        for (Author a : DB_AUTHORS) {
            for (Book b : DB_BOOKS) {
                if (b.authorId == a.id) {
                    projections.add(new AuthorBookDTO(a.name, b.title));
                }
            }
        }

        System.out.println("Total queries: " + queryCount + " ✅");
        projections.forEach(dto -> System.out.println("  " + dto.authorName() + " → " + dto.bookTitle()));
    }

    static void printAuthors(List<Author> authors) {
        authors.forEach(a -> {
            List<String> titles = a.books.stream().map(b -> b.title).collect(Collectors.toList());
            System.out.println("  " + a.name + " → " + titles);
        });
    }


    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("       N+1 QUERY PROBLEM DEMO");
        System.out.println("========================================");
        System.out.println("Dataset: " + DB_AUTHORS.size() + " authors, " + DB_BOOKS.size() + " books");

        demonstrateNPlusOne();
        demonstrateJoinFetch();
        demonstrateBatchFetch();
        demonstrateDtoProjection();

        System.out.println("\n========================================");
        System.out.println("SUMMARY");
        System.out.println("========================================");
        System.out.println("N+1 Problem  → " + (1 + DB_AUTHORS.size()) + " queries ❌");
        System.out.println("JOIN FETCH   → 1 query  ✅");
        System.out.println("Batch Fetch  → 2 queries ✅");
        System.out.println("DTO Project  → 1 query  ✅");
    }
}
