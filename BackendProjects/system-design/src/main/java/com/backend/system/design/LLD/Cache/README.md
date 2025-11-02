# Cache System Design

A comprehensive, modular, and extensible cache system implementation following SOLID principles and Low-Level Design (LLD) best practices.

## 📋 Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [SOLID Principles](#solid-principles)
- [Design Patterns](#design-patterns)
- [Components](#components)
- [Eviction Policies](#eviction-policies)
- [Usage](#usage)
- [Extension Guide](#extension-guide)
- [Performance](#performance)

## 🎯 Overview

This cache system provides a flexible, production-ready caching solution with multiple eviction policies. The design emphasizes:
- **Modularity**: Clear separation of concerns
- **Extensibility**: Easy to add new policies and storage implementations
- **Performance**: O(1) operations for most cache operations
- **Maintainability**: Clean code following best practices

## ✨ Features

- **Multiple Eviction Policies**: LRU, FIFO, LFU
- **Pluggable Storage**: Easy to swap storage implementations
- **Statistics Tracking**: Hit rate, miss rate, eviction count
- **Type-Safe**: Full generic support
- **Thread-Safe Ready**: Architecture supports concurrent implementations
- **Factory Pattern**: Simple cache creation
- **Comprehensive Documentation**: Well-documented code

## 🏗️ Architecture

```
Cache System
├── Cache (Interface)
│   └── CacheImpl (Implementation)
│
├── Storage (Interface)
│   └── HashMapStorage (Implementation)
│
├── EvictionPolicy (Interface)
│   ├── LRUEvictionPolicy
│   ├── FIFOEvictionPolicy
│   └── LFUEvictionPolicy
│
├── Algorithm (Data Structures)
│   ├── DoublyLinkedList
│   └── DLLNode
│
├── Exception (Custom Exceptions)
│   ├── CacheException
│   ├── KeyNotFoundException
│   └── InvalidCapacityException
│
└── Factory
    └── CacheFactory
```

### Package Structure

```
com.backend.system.design.LLD.Cache/
├── Cache.java                    # Main cache interface
├── CacheImpl.java                # Cache implementation
├── CacheStats.java               # Statistics tracking
├── CacheFactory.java             # Factory for cache creation
├── CacheDemo.java                # Demonstration examples
│
├── storage/
│   ├── Storage.java              # Storage abstraction
│   └── HashMapStorage.java       # HashMap-based storage
│
├── policy/
│   ├── EvictionPolicy.java       # Eviction policy interface
│   ├── LRUEvictionPolicy.java    # LRU implementation
│   ├── FIFOEvictionPolicy.java   # FIFO implementation
│   └── LFUEvictionPolicy.java    # LFU implementation
│
└── exception/
    ├── CacheException.java       # Base exception
    ├── KeyNotFoundException.java # Key not found
    └── InvalidCapacityException.java # Invalid capacity
```

## 🎓 SOLID Principles

### 1. Single Responsibility Principle (SRP)
- **CacheImpl**: Coordinates between storage and eviction policy
- **Storage**: Only handles data storage
- **EvictionPolicy**: Only handles eviction logic
- Each class has one clear responsibility

### 2. Open/Closed Principle (OCP)
- Open for extension: Add new eviction policies without modifying existing code
- Closed for modification: Core cache logic doesn't change
- Example: Adding a new policy requires only implementing `EvictionPolicy` interface

### 3. Liskov Substitution Principle (LSP)
- Any `Storage` implementation can replace another
- Any `EvictionPolicy` implementation can replace another
- All implementations are truly interchangeable

### 4. Interface Segregation Principle (ISP)
- Small, focused interfaces
- `Storage`: Only storage operations
- `EvictionPolicy`: Only eviction operations
- No client forced to depend on methods it doesn't use

### 5. Dependency Inversion Principle (DIP)
- High-level `CacheImpl` depends on abstractions (`Storage`, `EvictionPolicy`)
- Not dependent on concrete implementations
- Dependencies injected via constructor

## 🎨 Design Patterns

### 1. **Strategy Pattern**
- `EvictionPolicy` interface defines the strategy
- Different implementations (LRU, FIFO, LFU) provide different strategies
- Cache can switch strategies at runtime

### 2. **Factory Pattern**
- `CacheFactory` encapsulates cache creation logic
- Simplifies object creation
- Easy to create different cache configurations

### 3. **Dependency Injection**
- `CacheImpl` receives dependencies via constructor
- Improves testability and flexibility
- Follows Dependency Inversion Principle

## 🔧 Components

### Cache Interface
```java
public interface Cache<Key, Value> {
    void put(Key key, Value value);
    Optional<Value> get(Key key);
    Optional<Value> remove(Key key);
    boolean containsKey(Key key);
    int size();
    int capacity();
    boolean isEmpty();
    boolean isFull();
    void clear();
    CacheStats getStats();
}
```

### Storage Interface
```java
public interface Storage<Key, Value> {
    void put(Key key, Value value);
    Optional<Value> get(Key key);
    Optional<Value> remove(Key key);
    boolean containsKey(Key key);
    int size();
    int capacity();
    boolean isFull();
    void clear();
}
```

### EvictionPolicy Interface
```java
public interface EvictionPolicy<Key> {
    void keyAccessed(Key key);
    void keyAdded(Key key);
    void keyRemoved(Key key);
    Key evictKey();
    String getPolicyName();
}
```

## 📊 Eviction Policies

### 1. LRU (Least Recently Used)
- **Strategy**: Evicts the least recently accessed item
- **Use Case**: General-purpose caching, web applications
- **Time Complexity**: O(1) for all operations
- **Implementation**: Doubly Linked List + HashMap

**How it works:**
```
Initial: [A, B, C]  (capacity: 3)
Access B: [B, A, C]
Add D:    [D, B, A]  (C evicted)
```

### 2. FIFO (First In First Out)
- **Strategy**: Evicts the oldest item (first added)
- **Use Case**: Simple caching needs, predictable behavior
- **Time Complexity**: O(1) for add/evict, O(n) for remove
- **Implementation**: Queue

**How it works:**
```
Initial: [A, B, C]  (capacity: 3)
Access B: [A, B, C]  (no change)
Add D:    [B, C, D]  (A evicted - oldest)
```

### 3. LFU (Least Frequently Used)
- **Strategy**: Evicts the least frequently accessed item
- **Use Case**: Workloads with "hot" data, read-heavy applications
- **Time Complexity**: O(1) for all operations
- **Implementation**: Frequency map + LinkedHashSet

**How it works:**
```
Initial: A(freq=3), B(freq=2), C(freq=1)  (capacity: 3)
Add D:   A(freq=3), B(freq=2), D(freq=1)  (C evicted - least frequent)
```

## 💻 Usage

### Basic Usage

```java
// Create an LRU cache
Cache<String, Integer> cache = CacheFactory.createLRUCache(100);

// Add entries
cache.put("user:1", 42);
cache.put("user:2", 84);

// Retrieve entries
Optional<Integer> value = cache.get("user:1");
if (value.isPresent()) {
    System.out.println("Found: " + value.get());
}

// Remove entries
cache.remove("user:2");

// Check cache status
System.out.println("Size: " + cache.size());
System.out.println("Capacity: " + cache.capacity());
```

### Using Different Eviction Policies

```java
// LRU Cache
Cache<String, String> lruCache = CacheFactory.createLRUCache(50);

// FIFO Cache
Cache<String, String> fifoCache = CacheFactory.createFIFOCache(50);

// LFU Cache
Cache<String, String> lfuCache = CacheFactory.createLFUCache(50);

// Or use factory with enum
Cache<String, String> cache = CacheFactory.createCache(50, 
    CacheFactory.EvictionPolicyType.LRU);
```

### Advanced Usage - Custom Configuration

```java
// Create custom storage and policy
Storage<String, Product> storage = new HashMapStorage<>(1000);
EvictionPolicy<String> policy = new LRUEvictionPolicy<>(1000);

// Create cache with custom components
Cache<String, Product> cache = CacheFactory.createCache(storage, policy);
```

### Monitoring Cache Performance

```java
Cache<String, Data> cache = CacheFactory.createLRUCache(100);

// ... perform operations ...

// Get statistics
CacheStats stats = cache.getStats();
System.out.println("Hit Rate: " + stats.getHitRate());
System.out.println("Miss Rate: " + stats.getMissRate());
System.out.println("Evictions: " + stats.getEvictions());
System.out.println("Utilization: " + stats.getUtilization());
```

### Running the Demo

```java
// Run the comprehensive demo
public class Main {
    public static void main(String[] args) {
        CacheDemo.main(args);
    }
}
```

## 🔌 Extension Guide

### Adding a New Eviction Policy

1. **Implement the EvictionPolicy interface:**

```java
public class MRUEvictionPolicy<Key> implements EvictionPolicy<Key> {
    
    @Override
    public void keyAccessed(Key key) {
        // Track access
    }
    
    @Override
    public void keyAdded(Key key) {
        // Track addition
    }
    
    @Override
    public void keyRemoved(Key key) {
        // Remove from tracking
    }
    
    @Override
    public Key evictKey() {
        // Return most recently used key
    }
    
    @Override
    public String getPolicyName() {
        return "MRU (Most Recently Used)";
    }
}
```

2. **Add to CacheFactory:**

```java
public enum EvictionPolicyType {
    LRU, FIFO, LFU, MRU  // Add new type
}

// Add case in factory method
case MRU:
    return new MRUEvictionPolicy<>(capacity);
```

### Adding a New Storage Implementation

1. **Implement the Storage interface:**

```java
public class ConcurrentHashMapStorage<Key, Value> implements Storage<Key, Value> {
    private final ConcurrentHashMap<Key, Value> storage;
    
    // Implement all methods thread-safely
}
```

2. **Use in cache creation:**

```java
Storage<K, V> storage = new ConcurrentHashMapStorage<>(capacity);
Cache<K, V> cache = CacheFactory.createCache(storage, policy);
```

## ⚡ Performance

### Time Complexity

| Operation | LRU | FIFO | LFU |
|-----------|-----|------|-----|
| get()     | O(1)| O(1) | O(1)|
| put()     | O(1)| O(1) | O(1)|
| remove()  | O(1)| O(n) | O(1)|
| evict()   | O(1)| O(1) | O(1)|

### Space Complexity

All implementations: **O(n)** where n is the cache capacity

### Benchmarks (Approximate)

- **LRU**: Best for general-purpose caching
- **FIFO**: Lowest memory overhead, simplest
- **LFU**: Best for read-heavy workloads with hot data

## 🧪 Testing

Run the demo to see all policies in action:

```bash
# Compile
javac com/backend/system/design/LLD/Cache/*.java \
      com/backend/system/design/LLD/Cache/storage/*.java \
      com/backend/system/design/LLD/Cache/policy/*.java \
      com/backend/system/design/LLD/Cache/exception/*.java

# Run demo
java com.backend.system.design.LLD.Cache.CacheDemo
```

## 📝 Best Practices

1. **Choose the right eviction policy:**
   - **LRU**: General purpose, most common
   - **FIFO**: Simple, predictable
   - **LFU**: When you have "hot" frequently accessed data

2. **Set appropriate capacity:**
   - Consider your memory constraints
   - Monitor hit rate to optimize capacity

3. **Monitor statistics:**
   - Track hit/miss rates
   - Adjust capacity based on performance

4. **Thread safety:**
   - Use `ConcurrentHashMap` storage for concurrent access
   - Add synchronization in `CacheImpl` if needed

## 🚀 Future Enhancements

Potential extensions to consider:

1. **Time-based eviction (TTL)**
2. **Write-through/Write-back policies**
3. **Distributed caching support**
4. **Persistence layer integration**
5. **Asynchronous operations**
6. **Warm-up strategies**
7. **Multi-level caching**

## 📚 References

- **Design Patterns**: Gang of Four
- **SOLID Principles**: Robert C. Martin
- **Effective Java**: Joshua Bloch

---

**Author**: Backend Projects Team  
**License**: MIT  
**Version**: 1.0.0



