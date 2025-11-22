package com.backend.system.design.LLD.BloomFilters.demo;

import com.backend.system.design.LLD.BloomFilters.*;
import com.backend.system.design.LLD.BloomFilters.config.BloomFilterStats;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Demonstration of Bloom Filter implementations
 */
public class BloomFilterDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Bloom Filter Demonstration ===\n");
        
        // Demo 1: Basic Bloom Filter
        demonstrateBasicBloomFilter();
        
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        // Demo 2: Counting Bloom Filter
        demonstrateCountingBloomFilter();
        
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        // Demo 3: Scalable Bloom Filter
        demonstrateScalableBloomFilter();
        
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        // Demo 4: Performance Comparison
        demonstratePerformanceComparison();
        
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        // Demo 5: Real-world Use Case - URL Checking
        demonstrateUrlChecking();
    }
    
    /**
     * Demonstrates basic Bloom Filter functionality
     */
    private static void demonstrateBasicBloomFilter() {
        System.out.println("1. BASIC BLOOM FILTER DEMONSTRATION");
        System.out.println("-".repeat(60));
        
        // Create a Bloom Filter expecting 1000 elements with 1% false positive rate
        BloomFilter<String> bloomFilter = new BloomFilter<>(1000, 0.01);
        
        System.out.println("Created Bloom Filter with:");
        System.out.println("  Expected elements: 1000");
        System.out.println("  Target false positive rate: 1%");
        System.out.println("  Calculated bit size: " + bloomFilter.getBitSetSize());
        System.out.println("  Number of hash functions: " + bloomFilter.getNumberOfHashFunctions());
        System.out.println();
        
        // Add some elements
        String[] words = {"apple", "banana", "cherry", "date", "elderberry", 
                         "fig", "grape", "honeydew", "kiwi", "lemon"};
        
        System.out.println("Adding elements to Bloom Filter:");
        for (String word : words) {
            bloomFilter.add(word);
            System.out.println("  Added: " + word);
        }
        System.out.println();
        
        // Test membership
        System.out.println("Testing membership:");
        String[] testWords = {"apple", "banana", "mango", "orange", "cherry", "pear"};
        
        for (String word : testWords) {
            boolean exists = bloomFilter.mightContain(word);
            System.out.printf("  %-12s : %s\n", word, exists ? "MIGHT EXIST" : "DEFINITELY NOT");
        }
        System.out.println();
        
        // Display statistics
        System.out.println("Statistics:");
        System.out.println(bloomFilter.getStats());
    }
    
    /**
     * Demonstrates Counting Bloom Filter with deletion
     */
    private static void demonstrateCountingBloomFilter() {
        System.out.println("2. COUNTING BLOOM FILTER DEMONSTRATION");
        System.out.println("-".repeat(60));
        
        CountingBloomFilter<String> countingFilter = new CountingBloomFilter<>(1000, 0.01);
        
        System.out.println("Adding elements:");
        String[] items = {"item1", "item2", "item3", "item4", "item5"};
        for (String item : items) {
            countingFilter.add(item);
            System.out.println("  Added: " + item);
        }
        System.out.println();
        
        System.out.println("Before deletion:");
        for (String item : items) {
            System.out.printf("  %-10s : %s\n", item, 
                countingFilter.mightContain(item) ? "EXISTS" : "NOT FOUND");
        }
        System.out.println();
        
        // Remove some elements
        System.out.println("Removing 'item2' and 'item4':");
        countingFilter.remove("item2");
        countingFilter.remove("item4");
        System.out.println();
        
        System.out.println("After deletion:");
        for (String item : items) {
            System.out.printf("  %-10s : %s\n", item, 
                countingFilter.mightContain(item) ? "EXISTS" : "NOT FOUND");
        }
        System.out.println();
        
        System.out.println("Counter Statistics:");
        System.out.println("  Non-zero counters: " + countingFilter.getNonZeroCounters());
        System.out.println("  Max counter value: " + countingFilter.getMaxCounter());
        System.out.println("  Average counter value: " + String.format("%.2f", 
            countingFilter.getAverageCounterValue()));
    }
    
    /**
     * Demonstrates Scalable Bloom Filter
     */
    private static void demonstrateScalableBloomFilter() {
        System.out.println("3. SCALABLE BLOOM FILTER DEMONSTRATION");
        System.out.println("-".repeat(60));
        
        ScalableBloomFilter<Integer> scalableFilter = 
            new ScalableBloomFilter<>(100, 0.01);
        
        System.out.println("Adding 500 elements to a filter with initial capacity of 100");
        System.out.println();
        
        for (int i = 0; i < 500; i++) {
            scalableFilter.add(i);
            
            // Print status at certain intervals
            if (i == 99 || i == 199 || i == 299 || i == 399 || i == 499) {
                System.out.printf("After adding %d elements:\n", i + 1);
                System.out.printf("  Number of filters: %d\n", scalableFilter.getFilterCount());
                System.out.printf("  Total bit size: %d bits\n", scalableFilter.getTotalBitSize());
                System.out.println();
            }
        }
        
        // Test some lookups
        System.out.println("Testing lookups:");
        int[] testNumbers = {50, 250, 450, 550, 750};
        for (int num : testNumbers) {
            boolean exists = scalableFilter.mightContain(num);
            System.out.printf("  %d : %s\n", num, exists ? "MIGHT EXIST" : "DEFINITELY NOT");
        }
        System.out.println();
        
        System.out.println("Final Statistics:");
        System.out.println(scalableFilter.getStatistics());
    }
    
    /**
     * Demonstrates performance comparison between Bloom Filter and HashSet
     */
    private static void demonstratePerformanceComparison() {
        System.out.println("4. PERFORMANCE COMPARISON: Bloom Filter vs HashSet");
        System.out.println("-".repeat(60));
        
        int numElements = 100000;
        int numLookups = 10000;
        
        // Bloom Filter
        BloomFilter<Integer> bloomFilter = new BloomFilter<>(numElements, 0.01);
        long bloomInsertStart = System.currentTimeMillis();
        for (int i = 0; i < numElements; i++) {
            bloomFilter.add(i);
        }
        long bloomInsertEnd = System.currentTimeMillis();
        
        // HashSet
        Set<Integer> hashSet = new HashSet<>();
        long hashInsertStart = System.currentTimeMillis();
        for (int i = 0; i < numElements; i++) {
            hashSet.add(i);
        }
        long hashInsertEnd = System.currentTimeMillis();
        
        System.out.println("Insertion of " + numElements + " elements:");
        System.out.println("  Bloom Filter: " + (bloomInsertEnd - bloomInsertStart) + " ms");
        System.out.println("  HashSet:      " + (hashInsertEnd - hashInsertStart) + " ms");
        System.out.println();
        
        // Lookups
        Random random = new Random(42);
        
        long bloomLookupStart = System.currentTimeMillis();
        int bloomPositives = 0;
        for (int i = 0; i < numLookups; i++) {
            int num = random.nextInt(numElements * 2);
            if (bloomFilter.mightContain(num)) {
                bloomPositives++;
            }
        }
        long bloomLookupEnd = System.currentTimeMillis();
        
        random = new Random(42); // Reset for fair comparison
        long hashLookupStart = System.currentTimeMillis();
        int hashPositives = 0;
        for (int i = 0; i < numLookups; i++) {
            int num = random.nextInt(numElements * 2);
            if (hashSet.contains(num)) {
                hashPositives++;
            }
        }
        long hashLookupEnd = System.currentTimeMillis();
        
        System.out.println("Lookup of " + numLookups + " elements:");
        System.out.println("  Bloom Filter: " + (bloomLookupEnd - bloomLookupStart) + " ms " +
                          "(Found: " + bloomPositives + ")");
        System.out.println("  HashSet:      " + (hashLookupEnd - hashLookupStart) + " ms " +
                          "(Found: " + hashPositives + ")");
        System.out.println();
        
        // Memory comparison
        System.out.println("Memory Usage (approximate):");
        int bloomMemory = bloomFilter.getBitSetSize() / 8; // Convert bits to bytes
        int hashMemory = numElements * 32; // Approximate: 32 bytes per Integer in HashSet
        
        System.out.println("  Bloom Filter: " + bloomMemory + " bytes (" + 
                          (bloomMemory / 1024) + " KB)");
        System.out.println("  HashSet:      " + hashMemory + " bytes (" + 
                          (hashMemory / 1024) + " KB)");
        System.out.println("  Memory Savings: " + 
                          String.format("%.1f%%", (1 - (double) bloomMemory / hashMemory) * 100));
        System.out.println();
        
        System.out.println("False Positive Analysis:");
        int falsePositives = bloomPositives - hashPositives;
        System.out.println("  False Positives: " + falsePositives + " out of " + 
                          (numLookups - hashPositives) + " negative lookups");
        System.out.println("  Actual FP Rate: " + 
                          String.format("%.4f%%", (double) falsePositives / (numLookups - hashPositives) * 100));
        System.out.println("  Expected FP Rate: 1.00%");
    }
    
    /**
     * Demonstrates real-world use case: URL checking for web crawler
     */
    private static void demonstrateUrlChecking() {
        System.out.println("5. REAL-WORLD USE CASE: Web Crawler URL Deduplication");
        System.out.println("-".repeat(60));
        
        // Simulate a web crawler that checks visited URLs
        BloomFilter<String> visitedUrls = new BloomFilter<>(10000, 0.001); // 0.1% FP rate
        
        String[] crawledUrls = {
            "https://example.com",
            "https://example.com/about",
            "https://example.com/contact",
            "https://example.com/products",
            "https://example.com/blog",
            "https://test.com",
            "https://test.com/api",
            "https://sample.org",
            "https://sample.org/docs"
        };
        
        System.out.println("Simulating web crawler...\n");
        System.out.println("Crawling URLs:");
        for (String url : crawledUrls) {
            if (!visitedUrls.mightContain(url)) {
                System.out.println("  NEW URL: " + url + " (crawling...)");
                visitedUrls.add(url);
            } else {
                System.out.println("  SKIP: " + url + " (already visited)");
            }
        }
        System.out.println();
        
        // Try to crawl some URLs again
        System.out.println("Attempting to re-crawl some URLs:");
        String[] recrawlUrls = {
            "https://example.com",
            "https://new-site.com",
            "https://test.com/api",
            "https://another-new.com"
        };
        
        for (String url : recrawlUrls) {
            if (!visitedUrls.mightContain(url)) {
                System.out.println("  NEW URL: " + url + " (crawling...)");
                visitedUrls.add(url);
            } else {
                System.out.println("  SKIP: " + url + " (already visited)");
            }
        }
        System.out.println();
        
        System.out.println("Crawler Statistics:");
        BloomFilterStats stats = visitedUrls.getStats();
        System.out.println("  URLs tracked: " + stats.getElementCount());
        System.out.println("  Memory used: " + (stats.getBitSetSize() / 8) + " bytes");
        System.out.println("  Bit utilization: " + String.format("%.2f%%", 
            stats.getBitUtilization() * 100));
        System.out.println("  False positive rate: " + String.format("%.4f%%", 
            stats.getFalsePositiveProbability() * 100));
    }
}

