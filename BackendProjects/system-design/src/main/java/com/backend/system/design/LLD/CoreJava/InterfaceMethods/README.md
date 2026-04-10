# Interface Methods in Java

## What is an Interface? (Quick Recap)

An interface is a **contract** — it defines what a class must do, not how it does it.

```java
public interface Payment {
    void pay(double amount);  // implementor MUST provide this
}
```

When Java was first released (Java 1.0), interfaces could only have **one type of method** — abstract methods.
Over the years, Java added more types. Today (Java 9+), there are **5 types of methods** you can write in an interface.

---

## All 5 Types of Interface Methods

---

### Type 1 — Abstract Method (Java 1.0+)

The original, classic interface method.
- No body — just a signature
- Implicitly `public` and `abstract` (even if you don't write those keywords)
- **Every implementing class MUST override it**

```java
public interface Payment {
    void pay(double amount);                    // implicitly public abstract
    public abstract void refund(double amount); // same thing, explicit keywords
}

public class CreditCardPayment implements Payment {
    @Override
    public void pay(double amount) {
        System.out.println("Paying " + amount + " via Credit Card");
    }

    @Override
    public void refund(double amount) {
        System.out.println("Refunding " + amount + " to Credit Card");
    }
}
```

**Rule:** If a class implements an interface but doesn't override all abstract methods
→ the class must be declared `abstract`.

---

### Type 2 — Default Method (Java 8+)

A method with a **body** inside the interface.
- Keyword `default` is required
- Implementing classes **inherit it automatically** — no need to override
- Classes CAN override it if they want custom behaviour

**Why was it added?**
Before Java 8, adding a new method to an interface broke all existing implementations.
`default` methods let Java evolve interfaces without breaking existing code.

```java
public interface Payment {
    void pay(double amount);  // abstract — must implement

    // Default method — free implementation for all implementors
    default void printReceipt(double amount) {
        System.out.println("Receipt: paid " + amount + " on " + LocalDate.now());
    }
}

public class UpiPayment implements Payment {
    @Override
    public void pay(double amount) {
        System.out.println("Paying via UPI");
    }
    // printReceipt() is inherited automatically — no need to write it ✅
}

public class CryptoPayment implements Payment {
    @Override
    public void pay(double amount) {
        System.out.println("Paying via Crypto");
    }

    @Override
    public void printReceipt(double amount) {
        // Custom override — different receipt format
        System.out.println("Blockchain receipt: " + amount + " BTC");
    }
}
```

**Diamond Problem with default methods:**
If a class implements two interfaces with the same default method, it MUST override:

```java
interface A {
    default void greet() { System.out.println("Hello from A"); }
}

interface B {
    default void greet() { System.out.println("Hello from B"); }
}

class C implements A, B {
    @Override
    public void greet() {
        A.super.greet(); // explicitly choose which one, or write your own
    }
}
```

---

### Type 3 — Static Method (Java 8+)

A utility method that belongs to the **interface itself**, not to instances.
- Keyword `static` required
- Cannot be overridden by implementing classes
- Called on the interface name directly: `Payment.validate()`

```java
public interface Payment {
    void pay(double amount);

    // Static utility method — belongs to the interface, not instances
    static boolean isValidAmount(double amount) {
        return amount > 0 && amount <= 1_000_000;
    }
}

// Usage — called on interface name, not on an instance
boolean valid = Payment.isValidAmount(500.0);  // ✅
// upiPayment.isValidAmount(500.0);            // ❌ won't compile
```

**Common use:** Factory methods, validation helpers, utility functions related to the interface contract.

```java
public interface Comparators {
    static Comparator<String> byLength() {
        return Comparator.comparingInt(String::length);
    }
}
```

---

### Type 4 — Private Method (Java 9+)

A method with a body that is **only accessible within the interface itself**.
- Keyword `private` required
- Cannot be called from implementing classes or outside the interface
- Purpose: **avoid code duplication** between default or static methods inside the interface

**Why was it added?**
Before Java 9, if two `default` methods shared common logic, you had to either:
- Duplicate the code, or
- Move it to a utility class outside the interface (breaks encapsulation)

Java 9 solved this with private methods.

```java
public interface Payment {

    void pay(double amount);

    default void payWithLogging(double amount) {
        logTransaction("PAY", amount);    // reuses private method ✅
        pay(amount);
    }

    default void refundWithLogging(double amount) {
        logTransaction("REFUND", amount); // reuses private method ✅
        refund(amount);
    }

    void refund(double amount);

    // Private — only visible inside this interface
    // Removes duplication between the two default methods above
    private void logTransaction(String type, double amount) {
        System.out.println("[LOG] " + type + " of " + amount + " at " + LocalDateTime.now());
    }
}
```

Without the private method, `logTransaction` logic would be duplicated in both
`payWithLogging` and `refundWithLogging`. Bad.

---

### Type 5 — Private Static Method (Java 9+)

Same as private method, but `static` — used to avoid duplication between
**static** methods inside the interface.

```java
public interface Payment {

    static boolean isValidAmount(double amount) {
        return isPositive(amount) && isWithinLimit(amount); // reuses private static ✅
    }

    static boolean isRefundable(double amount) {
        return isPositive(amount) && amount <= 10_000;      // reuses private static ✅
    }

    // Private static — shared logic for static methods above
    private static boolean isPositive(double amount) {
        return amount > 0;
    }

    private static boolean isWithinLimit(double amount) {
        return amount <= 1_000_000;
    }
}
```

---

## Can we write private methods in an interface?

**Yes — since Java 9.**

```java
public interface MyInterface {
    private void privateMethod() {        // ✅ Java 9+
        System.out.println("private");
    }

    private static void privateStatic() { // ✅ Java 9+
        System.out.println("private static");
    }
}
```

**Rules for private methods in interfaces:**
- Must have a body (cannot be abstract)
- Cannot be `default` + `private` together (redundant — `default` implies non-private)
- Cannot be accessed from implementing classes
- Can only be called from within the same interface (by default or static methods)

---

## All 5 Types — Side by Side

| Type | Since | Keyword | Has Body | Inherited by class | Called via |
|------|-------|---------|----------|--------------------|------------|
| Abstract | Java 1.0 | (none) | No | Must override | instance |
| Default | Java 8 | `default` | Yes | Yes (auto) | instance |
| Static | Java 8 | `static` | Yes | No | Interface name |
| Private | Java 9 | `private` | Yes | No | Inside interface only |
| Private Static | Java 9 | `private static` | Yes | No | Inside interface only |

---

## What is NOT allowed in an interface

```java
public interface Bad {
    // ❌ instance fields (state) — interfaces cannot hold state
    int count = 0;  // this is actually treated as public static final — a CONSTANT

    // ❌ constructors
    Bad() { }

    // ❌ protected methods
    protected void doSomething();

    // ❌ abstract + default together
    abstract default void method();

    // ❌ private abstract (private methods must have a body)
    private void noBody();
}
```

**Constants in interfaces:**
Every field in an interface is implicitly `public static final`:
```java
public interface Config {
    int MAX_RETRIES = 3;  // → public static final int MAX_RETRIES = 3
}
```

---

## Interface vs Abstract Class — Key Difference

A common follow-up question:

| | Interface | Abstract Class |
|--|-----------|---------------|
| Methods | Abstract, Default, Static, Private | Abstract + Concrete |
| Fields | Constants only (`public static final`) | Instance fields allowed |
| Constructor | Not allowed | Allowed |
| Inheritance | A class can implement **multiple** interfaces | A class can extend only **one** abstract class |
| State | Cannot hold state | Can hold state |
| Use when | Defining a **contract** (what to do) | Sharing **common behaviour** (how to do it partially) |

---

## Real-World Example — All 5 Types Together

```java
public interface DataProcessor {

    // Type 1 — Abstract: every processor must implement this
    void process(String data);

    // Type 2 — Default: common pre-processing all processors can use
    default String preProcess(String data) {
        String cleaned = sanitize(data);   // calls private method
        log("PRE", cleaned);               // calls private method
        return cleaned;
    }

    // Type 2 — Default: common post-processing
    default void postProcess(String result) {
        log("POST", result);               // calls private method
    }

    // Type 3 — Static: utility for callers
    static boolean isEmpty(String data) {
        return data == null || data.isBlank();
    }

    // Type 4 — Private: shared by default methods (no duplication)
    private void log(String stage, String data) {
        System.out.println("[" + stage + "] " + data + " at " + LocalDateTime.now());
    }

    // Type 4 — Private: shared sanitization logic
    private String sanitize(String data) {
        return data.trim().toLowerCase();
    }

    // Type 5 — Private Static: shared by static methods
    private static String normalize(String data) {
        return data.replaceAll("\\s+", " ").trim();
    }
}
```

---

## Summary

```
Q: Is it possible to write private methods in an interface?
A: YES — since Java 9. Both private and private static.
   They exist to avoid code duplication inside the interface.
   They are NOT accessible from implementing classes.

Q: How many types of methods can we write in an interface?
A: 5 types (Java 9+)

   1. Abstract       → contract, no body, must be overridden     (Java 1.0)
   2. Default        → has body, inherited automatically          (Java 8)
   3. Static         → utility, called on interface name          (Java 8)
   4. Private        → helper for default methods, no leakage     (Java 9)
   5. Private Static → helper for static methods, no leakage      (Java 9)

Evolution:
  Java 1.0 → only abstract methods
  Java 8   → added default + static  (to evolve interfaces without breaking implementations)
  Java 9   → added private + private static  (to avoid duplication inside interfaces)
```
