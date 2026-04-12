# Java 17 Features

Java 17 is an **LTS (Long Term Support)** release — meaning it gets security and
bug fixes for years. It's the most widely adopted Java version in enterprise today.

This covers all major features from **Java 9 through Java 17** — everything
you'd be expected to know in an interview or production environment.

---

## Table of Contents

1. [Sealed Classes (Java 17)](#1-sealed-classes-java-17)
2. [Pattern Matching for switch (Java 17 Preview)](#2-pattern-matching-for-switch-java-17-preview)
3. [Records (Java 16)](#3-records-java-16)
4. [Pattern Matching for instanceof (Java 16)](#4-pattern-matching-for-instanceof-java-16)
5. [Text Blocks (Java 15)](#5-text-blocks-java-15)
6. [Helpful NullPointerExceptions (Java 14)](#6-helpful-nullpointerexceptions-java-14)
7. [Switch Expressions (Java 14)](#7-switch-expressions-java-14)
8. [var — Local Variable Type Inference (Java 10)](#8-var--local-variable-type-inference-java-10)
9. [Private Methods in Interfaces (Java 9)](#9-private-methods-in-interfaces-java-9)
10. [Stream API Enhancements (Java 9–16)](#10-stream-api-enhancements-java-916)
11. [Optional Enhancements (Java 9–11)](#11-optional-enhancements-java-911)
12. [String Enhancements (Java 11–15)](#12-string-enhancements-java-1115)
13. [Collection Factory Methods (Java 9)](#13-collection-factory-methods-java-9)

---

## 1. Sealed Classes (Java 17)

### What it is
A sealed class **restricts which classes can extend or implement it**.
You explicitly declare the permitted subclasses.

### Before sealed classes
```java
// Anyone could extend Shape — no control
public abstract class Shape { }
public class Circle extends Shape { }
public class Rectangle extends Shape { }
public class Triangle extends Shape { }
public class Hexagon extends Shape { }  // unexpected extension — no way to prevent
```

### With sealed classes
```java
// Only Circle, Rectangle, Triangle are allowed — nothing else
public sealed class Shape permits Circle, Rectangle, Triangle { }

public final class Circle extends Shape {
    double radius;
}

public final class Rectangle extends Shape {
    double width, height;
}

public non-sealed class Triangle extends Shape {
    // non-sealed = Triangle can be extended freely by anyone
    double base, height;
}
```

**Three keywords:**
- `sealed` — this class/interface restricts who can extend it
- `permits` — lists the allowed subclasses
- `final` — permitted subclass cannot be extended further
- `non-sealed` — permitted subclass opens itself for further extension

### Why it matters
Works beautifully with **pattern matching** — the compiler knows ALL possible subtypes:

```java
// Compiler knows Shape can only be Circle, Rectangle, or Triangle
// No default case needed — it's exhaustive ✅
double area = switch (shape) {
    case Circle c    -> Math.PI * c.radius * c.radius;
    case Rectangle r -> r.width * r.height;
    case Triangle t  -> 0.5 * t.base * t.height;
};
```

**Use when:** Modeling a fixed set of types — HTTP responses (Success/Error),
payment states (Pending/Approved/Rejected), AST nodes in a compiler.

---

## 2. Pattern Matching for switch (Java 17 Preview)

### What it is
Extends `switch` to match on **types**, not just values.
Eliminates the classic if-else instanceof chain.

### Before (verbose, error-prone)
```java
static String describe(Object obj) {
    if (obj instanceof Integer i) {
        return "Integer: " + i;
    } else if (obj instanceof String s) {
        return "String of length " + s.length();
    } else if (obj instanceof Double d) {
        return "Double: " + d;
    } else {
        return "Unknown";
    }
}
```

### With pattern matching switch
```java
static String describe(Object obj) {
    return switch (obj) {
        case Integer i -> "Integer: " + i;
        case String s  -> "String of length " + s.length();
        case Double d  -> "Double: " + d;
        case null      -> "null value";
        default        -> "Unknown: " + obj.getClass();
    };
}
```

### With guards (when clause)
```java
static String classify(Object obj) {
    return switch (obj) {
        case Integer i when i > 0  -> "Positive integer";
        case Integer i when i < 0  -> "Negative integer";
        case Integer i             -> "Zero";
        case String s when s.isEmpty() -> "Empty string";
        case String s              -> "String: " + s;
        default                    -> "Other";
    };
}
```

---

## 3. Records (Java 16)

### What it is
A `record` is a **concise way to declare immutable data classes**.
It auto-generates: constructor, getters, `equals()`, `hashCode()`, `toString()`.

### Before records — pure boilerplate
```java
public class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) { this.x = x; this.y = y; }
    public int x() { return x; }
    public int y() { return y; }

    @Override
    public boolean equals(Object o) { ... }   // 10 lines
    @Override
    public int hashCode() { ... }              // 5 lines
    @Override
    public String toString() { ... }           // 3 lines
}
```

### With records — same thing, 1 line
```java
public record Point(int x, int y) { }

// Usage
Point p = new Point(3, 4);
System.out.println(p.x());        // 3
System.out.println(p.y());        // 4
System.out.println(p);            // Point[x=3, y=4]
System.out.println(p.equals(new Point(3, 4))); // true
```

### Compact constructor (for validation)
```java
public record Range(int min, int max) {
    Range {  // compact constructor — no parameter list needed
        if (min > max) throw new IllegalArgumentException("min > max");
    }
}
```

### Records can have methods
```java
public record Circle(double radius) {
    public double area() {
        return Math.PI * radius * radius;
    }
}
```

### Limitations of records
- **Immutable** — fields are `final`, cannot be changed after creation
- Cannot extend another class (implicitly extends `Record`)
- Cannot declare instance fields outside the record components

**Use when:** DTOs, API request/response objects, value objects, coordinates, ranges.

---

## 4. Pattern Matching for instanceof (Java 16)

### Before — cast after check (redundant)
```java
if (obj instanceof String) {
    String s = (String) obj;  // cast again — redundant ❌
    System.out.println(s.length());
}
```

### After — binding variable inline
```java
if (obj instanceof String s) {  // declares 's' if check passes ✅
    System.out.println(s.length());
}
```

### In complex conditions
```java
// 's' is in scope for the entire && expression
if (obj instanceof String s && s.length() > 5) {
    System.out.println("Long string: " + s);
}
```

### Negation pattern
```java
if (!(obj instanceof String s)) {
    return; // early exit — s not in scope here
}
// s IS in scope here ✅
System.out.println(s.toUpperCase());
```

---

## 5. Text Blocks (Java 15)

### What it is
Multi-line strings with **no escape characters** — what you write is what you get.

### Before — painful escaping
```java
String json = "{\n" +
              "  \"name\": \"Saurabh\",\n" +
              "  \"age\": 30\n" +
              "}";

String html = "<html>\n" +
              "    <body>\n" +
              "        <p>Hello</p>\n" +
              "    </body>\n" +
              "</html>";
```

### After — text blocks
```java
String json = """
        {
          "name": "Saurabh",
          "age": 30
        }
        """;

String html = """
        <html>
            <body>
                <p>Hello</p>
            </body>
        </html>
        """;

String sql = """
        SELECT u.id, u.name, o.total
        FROM users u
        JOIN orders o ON o.user_id = u.id
        WHERE u.status = 'ACTIVE'
        ORDER BY o.created_at DESC
        """;
```

### Rules
- Opening `"""` must be followed by a newline
- Indentation is relative to the closing `"""`
- Trailing newline included if closing `"""` is on its own line

**Use when:** JSON, HTML, SQL, XML, multiline messages in tests.

---

## 6. Helpful NullPointerExceptions (Java 14)

### Before Java 14 — useless message
```java
user.getAddress().getCity().toUpperCase();

// NullPointerException at line 42
// (which one was null? user? address? city? you have no idea)
```

### Java 14+ — precise message
```java
// NullPointerException: Cannot invoke "Address.getCity()"
// because the return value of "User.getAddress()" is null
```

The JVM now tells you **exactly which variable/method returned null**.
No more adding debug logs just to find the null.

Enabled by default in Java 17. In Java 14/15: `-XX:+ShowCodeDetailsInExceptionMessages`

---

## 7. Switch Expressions (Java 14)

### What changed
`switch` can now be used as an **expression** (returns a value) not just a statement.

### Old switch statement (verbose, fall-through bugs)
```java
int numLetters;
switch (day) {
    case MONDAY:
    case FRIDAY:
    case SUNDAY:
        numLetters = 6;
        break;             // easy to forget break → fall-through bug
    case TUESDAY:
        numLetters = 7;
        break;
    default:
        numLetters = 8;
}
```

### New switch expression (concise, no fall-through)
```java
// Arrow syntax — no fall-through, returns value directly
int numLetters = switch (day) {
    case MONDAY, FRIDAY, SUNDAY -> 6;   // multiple labels
    case TUESDAY                -> 7;
    default                     -> 8;
};
```

### With yield (for multi-line cases)
```java
String result = switch (status) {
    case "PENDING" -> "Order is being processed";
    case "SHIPPED" -> {
        log.info("Order shipped");
        yield "Your order is on the way";  // yield returns from a block
    }
    case "DELIVERED" -> "Order delivered successfully";
    default -> "Unknown status: " + status;
};
```

---

## 8. var — Local Variable Type Inference (Java 10)

### What it is
`var` lets the compiler **infer the type** of a local variable — less verbosity.

```java
// Before
Map<String, List<Integer>> scores = new HashMap<String, List<Integer>>();

// After
var scores = new HashMap<String, List<Integer>>();  // type inferred ✅
```

### Common uses
```java
var name = "Saurabh";                        // String
var count = 42;                              // int
var list = new ArrayList<String>();          // ArrayList<String>
var entry = map.entrySet().iterator().next(); // Map.Entry<K,V>

// In for loops — cleaner
for (var entry : map.entrySet()) {
    System.out.println(entry.getKey() + " = " + entry.getValue());
}

// With streams
var result = list.stream()
                 .filter(s -> s.startsWith("A"))
                 .collect(Collectors.toList());
```

### Rules and limitations
```java
var x;               // ❌ must be initialized — compiler needs to infer type
var x = null;        // ❌ cannot infer from null alone
var x = (String) null; // ✅ explicit cast helps

// var is for LOCAL variables only
private var name;    // ❌ not for fields
public var method()  // ❌ not for method return types
```

### When NOT to use var
```java
// Bad — type is unclear from right side
var x = getValue();        // what type is returned? unclear ❌

// Good — type is obvious from right side
var list = new ArrayList<String>();   // clearly ArrayList<String> ✅
var user = new User("Saurabh");       // clearly User ✅
```

---

## 9. Private Methods in Interfaces (Java 9)

Covered in detail in `CoreJava/InterfaceMethods/README.md`.

Quick summary:
```java
public interface Validator {
    default boolean validateAge(int age) {
        return isPositive(age) && age <= 150;   // reuses private method
    }

    default boolean validateSalary(double salary) {
        return isPositive((int) salary);         // reuses private method
    }

    private boolean isPositive(int value) {     // shared helper — Java 9
        return value > 0;
    }
}
```

---

## 10. Stream API Enhancements (Java 9–16)

### takeWhile and dropWhile (Java 9)
```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7);

// takeWhile — take elements while condition is true, stop at first false
List<Integer> taken = numbers.stream()
    .takeWhile(n -> n < 5)
    .collect(Collectors.toList());
// [1, 2, 3, 4]

// dropWhile — skip elements while condition is true, keep rest
List<Integer> dropped = numbers.stream()
    .dropWhile(n -> n < 5)
    .collect(Collectors.toList());
// [5, 6, 7]
```

### Stream.iterate with predicate (Java 9)
```java
// Old — infinite stream, must limit manually
Stream.iterate(0, n -> n + 2).limit(5)   // 0, 2, 4, 6, 8

// New — built-in stop condition (like a for loop)
Stream.iterate(0, n -> n < 10, n -> n + 2)  // 0, 2, 4, 6, 8
```

### Stream.ofNullable (Java 9)
```java
// Avoids NullPointerException when value might be null
Stream<String> stream = Stream.ofNullable(getValue());  // empty stream if null
```

### Collectors.teeing (Java 12)
```java
// Process a stream with TWO collectors simultaneously, merge results
var stats = numbers.stream().collect(
    Collectors.teeing(
        Collectors.summingInt(Integer::intValue),   // collector 1: sum
        Collectors.counting(),                       // collector 2: count
        (sum, count) -> "Sum=" + sum + ", Count=" + count  // merge
    )
);
// "Sum=28, Count=7"
```

### toList() shorthand (Java 16)
```java
// Before
List<String> names = stream.collect(Collectors.toList());

// After — shorter, returns unmodifiable list
List<String> names = stream.toList();
```

---

## 11. Optional Enhancements (Java 9–11)

### ifPresentOrElse (Java 9)
```java
// Before — two separate checks
optional.ifPresent(v -> process(v));
if (!optional.isPresent()) handleEmpty();

// After — one call
optional.ifPresentOrElse(
    value -> process(value),       // if present
    () -> handleEmpty()            // if empty
);
```

### or() — chain optionals (Java 9)
```java
// Try cache first, then DB, then default
Optional<User> user = cache.findUser(id)
    .or(() -> database.findUser(id))    // if cache empty, try DB
    .or(() -> Optional.of(User.guest())); // if DB empty, use guest
```

### stream() — bridge Optional to Stream (Java 9)
```java
List<Optional<String>> optionals = List.of(
    Optional.of("Alice"),
    Optional.empty(),
    Optional.of("Bob")
);

List<String> names = optionals.stream()
    .flatMap(Optional::stream)   // converts each Optional to 0 or 1 element stream
    .collect(Collectors.toList());
// ["Alice", "Bob"]
```

### isEmpty() (Java 11)
```java
// Before
if (!optional.isPresent()) { }

// After — more readable
if (optional.isEmpty()) { }
```

---

## 12. String Enhancements (Java 11–15)

### isBlank() (Java 11)
```java
"".isBlank()      // true
"  ".isBlank()    // true  ← isEmpty() would return false for "  "
"hi".isBlank()    // false
```

### strip(), stripLeading(), stripTrailing() (Java 11)
```java
"  hello  ".strip()          // "hello"       (Unicode-aware, better than trim())
"  hello  ".stripLeading()   // "hello  "
"  hello  ".stripTrailing()  // "  hello"
```

### repeat() (Java 11)
```java
"ab".repeat(3)    // "ababab"
"-".repeat(20)    // "--------------------"
```

### lines() (Java 11)
```java
"line1\nline2\nline3".lines()
    .collect(Collectors.toList());
// ["line1", "line2", "line3"]
```

### indent() (Java 12)
```java
"Hello\nWorld".indent(4);
// "    Hello\n    World\n"
```

### formatted() (Java 15)
```java
// Before
String msg = String.format("Hello %s, you are %d years old", name, age);

// After — instance method, cleaner
String msg = "Hello %s, you are %d years old".formatted(name, age);
```

---

## 13. Collection Factory Methods (Java 9)

### Immutable collections with of()
```java
// Before — verbose
List<String> list = new ArrayList<>();
list.add("a"); list.add("b"); list.add("c");
list = Collections.unmodifiableList(list);

// After — one line, immutable ✅
List<String> list = List.of("a", "b", "c");
Set<String>  set  = Set.of("x", "y", "z");
Map<String, Integer> map = Map.of("one", 1, "two", 2, "three", 3);
```

### copyOf() — defensive copy
```java
List<String> original = new ArrayList<>(List.of("a", "b"));
List<String> copy = List.copyOf(original);  // immutable copy
original.add("c");
// copy still has ["a", "b"] — not affected ✅
```

### Map.entry() and Map.ofEntries()
```java
// For maps with more than 10 entries (Map.of() only supports up to 10)
Map<String, Integer> map = Map.ofEntries(
    Map.entry("one",   1),
    Map.entry("two",   2),
    Map.entry("three", 3),
    Map.entry("four",  4)
    // ... unlimited entries
);
```

**Important:** `List.of()`, `Set.of()`, `Map.of()` are **truly immutable** —
calling `add()`, `remove()` or `put()` throws `UnsupportedOperationException`.

---

## Summary — Feature Timeline

```
Java 9  (2017)
  ├── Private methods in interfaces
  ├── Collection factory methods  (List.of, Set.of, Map.of)
  ├── Stream enhancements         (takeWhile, dropWhile, ofNullable)
  ├── Optional enhancements       (ifPresentOrElse, or, stream)
  └── Module system (JPMS)        (not covered — enterprise rarely uses)

Java 10 (2018)
  └── var — local variable type inference

Java 11 (2018) ← LTS
  ├── String methods              (isBlank, strip, lines, repeat)
  └── Optional.isEmpty()

Java 12 (2019)
  └── Collectors.teeing

Java 14 (2020)
  ├── Switch expressions          (stable)
  ├── Records                     (preview)
  └── Helpful NullPointerExceptions

Java 15 (2020)
  ├── Text blocks                 (stable)
  └── Sealed classes              (preview)

Java 16 (2021)
  ├── Records                     (stable)
  ├── Pattern matching instanceof (stable)
  └── Stream.toList()

Java 17 (2021) ← LTS ★ most widely used
  ├── Sealed classes              (stable)
  └── Pattern matching for switch (preview)
```

---

## Most Asked in Interviews

```
1. Records          → immutable data classes, replaces Lombok @Data for simple cases
2. Sealed classes   → controlled type hierarchies, works with pattern matching
3. Text blocks      → multiline strings, no escape characters
4. var              → type inference for local variables
5. Pattern matching instanceof → no more redundant cast
6. Switch expressions → switch as expression with ->
7. List.of() etc    → immutable collections
```
