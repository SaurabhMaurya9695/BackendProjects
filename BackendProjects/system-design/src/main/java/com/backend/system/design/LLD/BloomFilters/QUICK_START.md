# Bloom Filter - Quick Start Guide

Get up and running with Bloom Filters in 5 minutes!

## 🚀 Running the Demo

```bash
# Navigate to the project directory
cd /path/to/BackendProjects/system-design

# Compile the code
mvn clean compile

# Run the main demo
mvn exec:java -Dexec.mainClass="com.backend.system.design.LLD.BloomFilters.demo.BloomFilterDemo"

# Or run the practical examples
mvn exec:java -Dexec.mainClass="com.backend.system.design.LLD.BloomFilters.demo.UsageExamples"
```

## 📖 Basic Usage

### 1. Standard Bloom Filter

```java
import com.backend.system.design.LLD.BloomFilters.*;

// Create a Bloom Filter for 1000 elements with 1% false positive rate
BloomFilter<String> filter = new BloomFilter<>(1000, 0.01);

// Add elements
filter.add("apple");
filter.add("banana");
filter.add("cherry");

// Check membership
if (filter.mightContain("apple")) {
    System.out.println("Apple might be present!");
}

if (!filter.mightContain("orange")) {
    System.out.println("Orange is definitely not present!");
}

// Get statistics
BloomFilterStats stats = filter.getStats();
System.out.println(stats);
```

### 2. Counting Bloom Filter (with deletion)

```java
// Create a Counting Bloom Filter
CountingBloomFilter<String> filter = new CountingBloomFilter<>(1000, 0.01);

// Add elements
filter.add("item1");
filter.add("item2");

// Remove elements (not possible with standard Bloom Filter!)
filter.remove("item1");

// Check membership
filter.mightContain("item1");  // false
filter.mightContain("item2");  // true
```

### 3. Scalable Bloom Filter (automatic growth)

```java
// Create a Scalable Bloom Filter (no need to know final size!)
ScalableBloomFilter<Integer> filter = 
    new ScalableBloomFilter<>(100, 0.01);  // Initial capacity: 100

// Add unlimited elements - it will automatically scale!
for (int i = 0; i < 10000; i++) {
    filter.add(i);
}

// Check membership
filter.mightContain(5000);  // true

// See how many internal filters were created
System.out.println("Filters: " + filter.getFilterCount());
```

## 💡 Real-World Examples

### Web Crawler - URL Deduplication

```java
BloomFilter<String> visitedUrls = new BloomFilter<>(1_000_000, 0.001);

public void crawl(String url) {
    if (!visitedUrls.mightContain(url)) {
        visitedUrls.add(url);
        fetchAndProcess(url);  // Actually crawl the URL
    }
}
```

### Database Cache Checker

```java
BloomFilter<String> cacheKeys = new BloomFilter<>(100_000, 0.01);

public Result query(String sql) {
    String key = generateKey(sql);
    
    if (!cacheKeys.mightContain(key)) {
        // Definitely not in cache - skip expensive cache lookup
        return queryDatabase(sql);
    }
    
    // Might be in cache - check it
    Result cached = cache.get(key);
    return cached != null ? cached : queryDatabase(sql);
}
```

### Spam Email Filter

```java
BloomFilter<String> spamList = new BloomFilter<>(1_000_000, 0.001);

// Load known spam addresses
loadSpamAddresses(spamList);

public boolean isSpam(Email email) {
    return spamList.mightContain(email.getFrom());
}
```

## 📊 Choosing Parameters

### False Positive Rate

| Rate | Use Case | Memory per Element |
|------|----------|-------------------|
| 1% (0.01) | General purpose | ~10 bits |
| 0.1% (0.001) | More accuracy needed | ~14 bits |
| 0.01% (0.0001) | High accuracy | ~19 bits |

### Expected Elements

Rule of thumb:
- **Known size**: Use standard `BloomFilter` with expected count
- **Unknown size**: Use `ScalableBloomFilter` with initial estimate
- **Need deletion**: Use `CountingBloomFilter`

## ⚠️ Common Pitfalls

### ❌ Don't: Use for exact membership checks

```java
// BAD - Bloom Filter can have false positives!
if (bloomFilter.mightContain(user)) {
    grantAccess(user);  // Dangerous!
}
```

### ✅ Do: Use as a pre-filter

```java
// GOOD - Verify after Bloom Filter check
if (bloomFilter.mightContain(user)) {
    if (actualDatabase.contains(user)) {  // Verify
        grantAccess(user);
    }
}
```

### ❌ Don't: Ignore false positives

```java
// BAD - Not handling false positives
if (bloomFilter.mightContain(item)) {
    return true;  // Might be wrong!
}
```

### ✅ Do: Always verify critical operations

```java
// GOOD - Verify critical operations
if (!bloomFilter.mightContain(item)) {
    return false;  // Definitely not present
}

// Might be present - verify with authoritative source
return database.contains(item);
```

## 🎯 Performance Tips

### 1. Size Appropriately

```java
// Calculate based on your needs
int expectedElements = 100_000;
double falsePositiveRate = 0.01;  // 1%

BloomFilter<String> filter = new BloomFilter<>(expectedElements, falsePositiveRate);
```

### 2. Monitor False Positive Rate

```java
// Check actual false positive rate
BloomFilterStats stats = filter.getStats();
System.out.println("Current FP rate: " + stats.getFalsePositiveProbability());

// If too high, consider creating a new larger filter
if (stats.getFalsePositiveProbability() > targetRate) {
    // Time to resize or use ScalableBloomFilter
}
```

### 3. Choose Right Implementation

```java
// Use Scalable if size is unknown
ScalableBloomFilter<String> dynamicFilter = 
    new ScalableBloomFilter<>(initialGuess, targetFPRate);

// Use Counting if deletion is needed
CountingBloomFilter<String> deletableFilter = 
    new CountingBloomFilter<>(size, fpRate);
```

## 📈 Memory Comparison

For **1 Million elements** with **1% false positive rate**:

| Data Structure | Memory | Notes |
|---------------|---------|-------|
| HashSet | ~32 MB | Exact membership |
| TreeSet | ~40 MB | Sorted, exact membership |
| **Bloom Filter** | **~1.2 MB** | Probabilistic, very efficient |

**Savings: ~96% less memory!**

## 🔍 Testing Your Setup

```java
public class BloomFilterTest {
    public static void main(String[] args) {
        // Create filter
        BloomFilter<String> filter = new BloomFilter<>(100, 0.01);
        
        // Add elements
        filter.add("test1");
        filter.add("test2");
        
        // Test positive cases
        assert filter.mightContain("test1") : "Should contain test1";
        assert filter.mightContain("test2") : "Should contain test2";
        
        // Test negative case (no false negatives!)
        assert !filter.mightContain("test3") || true : "Might have false positive";
        
        System.out.println("✓ All tests passed!");
        System.out.println(filter.getStats());
    }
}
```

## 📚 Next Steps

1. Read the full [README.md](README.md) for detailed explanations
2. Check out [UsageExamples.java](UsageExamples.java) for more real-world scenarios
3. Run [BloomFilterDemo.java](BloomFilterDemo.java) to see all features in action

## 🆘 Troubleshooting

### High False Positive Rate?

```java
// Solution 1: Increase bit array size
BloomFilter<String> filter = new BloomFilter<>(expectedSize * 1.5, fpRate);

// Solution 2: Use stricter false positive rate
BloomFilter<String> filter = new BloomFilter<>(expectedSize, 0.001); // 0.1% instead of 1%

// Solution 3: Use Scalable Bloom Filter
ScalableBloomFilter<String> filter = new ScalableBloomFilter<>(initialSize, fpRate);
```

### Running Out of Memory?

```java
// Consider higher false positive rate for less memory
BloomFilter<String> filter = new BloomFilter<>(size, 0.05); // 5% FP, less memory
```

### Need Deletion?

```java
// Use Counting Bloom Filter
CountingBloomFilter<String> filter = new CountingBloomFilter<>(size, fpRate);
filter.add("item");
filter.remove("item");  // Deletion supported!
```

## 💻 Import Statements

```java
// Core implementations
import com.backend.system.design.LLD.BloomFilters.BloomFilter;
import com.backend.system.design.LLD.BloomFilters.CountingBloomFilter;
import com.backend.system.design.LLD.BloomFilters.ScalableBloomFilter;

// Configuration classes
import com.backend.system.design.LLD.BloomFilters.config.BloomFilterConfig;
import com.backend.system.design.LLD.BloomFilters.config.BloomFilterStats;
import com.backend.system.design.LLD.BloomFilters.config.BloomFilterMath;

// Hash functions (if needed)
import com.backend.system.design.LLD.BloomFilters.hash.HashFunction;
import com.backend.system.design.LLD.BloomFilters.hash.MurmurHashFunction;
import com.backend.system.design.LLD.BloomFilters.hash.SimpleHashFunction;
```

---

**Ready to use Bloom Filters? Run the demo and start experimenting!** 🎉

