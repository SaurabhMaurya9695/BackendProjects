package com.backend.system.design.LLD.BloomFilters.demo;

import com.backend.system.design.LLD.BloomFilters.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Practical usage examples of Bloom Filters in real-world scenarios
 */
public class UsageExamples {
    
    /**
     * Example 1: Email Spam Filter
     * Quickly check if an email address is in a spam list
     */
    public static class SpamFilter {
        private final BloomFilter<String> spamAddresses;
        private final Set<String> confirmedSpam; // For verification
        
        public SpamFilter(int expectedSpammers) {
            this.spamAddresses = new BloomFilter<>(expectedSpammers, 0.001);
            this.confirmedSpam = new HashSet<>();
        }
        
        public void addSpammer(String email) {
            spamAddresses.add(email.toLowerCase());
            confirmedSpam.add(email.toLowerCase());
        }
        
        public boolean isLikelySpam(String email) {
            email = email.toLowerCase();
            
            // Quick check with Bloom Filter
            if (!spamAddresses.mightContain(email)) {
                return false; // Definitely not spam
            }
            
            // Verify with actual set (handle false positives)
            return confirmedSpam.contains(email);
        }
        
        public void printStats() {
            System.out.println("Spam Filter Statistics:");
            System.out.println("  Confirmed spammers: " + confirmedSpam.size());
            System.out.println(spamAddresses.getStats());
        }
    }
    
    /**
     * Example 2: Database Query Cache Checker
     * Avoid unnecessary cache lookups
     */
    public static class QueryCacheChecker {
        private final BloomFilter<String> cachedQueries;
        private final Map<String, Object> actualCache;
        private int bloomFilterSaves = 0;
        
        public QueryCacheChecker(int expectedQueries) {
            this.cachedQueries = new BloomFilter<>(expectedQueries, 0.01);
            this.actualCache = new ConcurrentHashMap<>();
        }
        
        public void cacheResult(String query, Object result) {
            String key = generateKey(query);
            cachedQueries.add(key);
            actualCache.put(key, result);
        }
        
        public Object getResult(String query) {
            String key = generateKey(query);
            
            // Quick Bloom Filter check
            if (!cachedQueries.mightContain(key)) {
                bloomFilterSaves++;
                return null; // Definitely not in cache, skip expensive lookup
            }
            
            // Might be in cache, check actual cache
            return actualCache.get(key);
        }
        
        private String generateKey(String query) {
            return String.valueOf(query.hashCode());
        }
        
        public void printStats() {
            System.out.println("Cache Checker Statistics:");
            System.out.println("  Cache entries: " + actualCache.size());
            System.out.println("  Bloom Filter saves: " + bloomFilterSaves);
            System.out.println("  (avoided " + bloomFilterSaves + " expensive cache lookups)");
        }
    }
    
    /**
     * Example 3: Unique Visitor Counter
     * Track unique visitors without storing all IPs
     */
    public static class UniqueVisitorTracker {
        private final BloomFilter<String> visitors;
        private final CountingBloomFilter<String> recentVisitors;
        private int approximateUniqueCount = 0;
        
        public UniqueVisitorTracker(int expectedVisitors) {
            this.visitors = new BloomFilter<>(expectedVisitors, 0.01);
            this.recentVisitors = new CountingBloomFilter<>(expectedVisitors / 10, 0.01);
        }
        
        public boolean recordVisit(String ipAddress) {
            boolean isNewVisitor = !visitors.mightContain(ipAddress);
            
            if (isNewVisitor) {
                visitors.add(ipAddress);
                approximateUniqueCount++;
            }
            
            // Track recent visitors (last window)
            if (!recentVisitors.mightContain(ipAddress)) {
                recentVisitors.add(ipAddress);
            }
            
            return isNewVisitor;
        }
        
        public int getApproximateUniqueVisitors() {
            return approximateUniqueCount;
        }
        
        public void resetRecentVisitors() {
            recentVisitors.clear();
        }
    }
    
    /**
     * Example 4: Distributed System - Task Deduplication
     * Prevent multiple workers from processing the same task
     */
    public static class DistributedTaskProcessor {
        private final ScalableBloomFilter<String> processedTasks;
        private final String workerId;
        private int tasksProcessed = 0;
        private int duplicatesSkipped = 0;
        
        public DistributedTaskProcessor(String workerId) {
            this.workerId = workerId;
            this.processedTasks = new ScalableBloomFilter<>(1000, 0.001);
        }
        
        public boolean processTask(String taskId) {
            // Check if task was likely already processed
            if (processedTasks.mightContain(taskId)) {
                duplicatesSkipped++;
                System.out.println("[" + workerId + "] Task " + taskId + " already processed, skipping");
                return false;
            }
            
            // Mark as processed
            processedTasks.add(taskId);
            
            // Simulate task processing
            System.out.println("[" + workerId + "] Processing task: " + taskId);
            tasksProcessed++;
            
            return true;
        }
        
        public void printStats() {
            System.out.println("\nWorker " + workerId + " Statistics:");
            System.out.println("  Tasks processed: " + tasksProcessed);
            System.out.println("  Duplicates skipped: " + duplicatesSkipped);
            System.out.println("  Filters created: " + processedTasks.getFilterCount());
        }
    }
    
    /**
     * Example 5: Password Strength Checker
     * Check against common/breached passwords
     */
    public static class PasswordStrengthChecker {
        private final BloomFilter<String> commonPasswords;
        private final BloomFilter<String> breachedPasswords;
        
        public PasswordStrengthChecker() {
            this.commonPasswords = new BloomFilter<>(100_000, 0.001);
            this.breachedPasswords = new BloomFilter<>(500_000_000, 0.0001);
            
            loadCommonPasswords();
        }
        
        private void loadCommonPasswords() {
            // Simulate loading common passwords
            String[] common = {
                "password", "123456", "password123", "admin", "letmein",
                "welcome", "monkey", "dragon", "master", "sunshine",
                "princess", "qwerty", "abc123", "Football1", "iloveyou"
            };
            
            for (String pwd : common) {
                commonPasswords.add(pwd.toLowerCase());
            }
        }
        
        public PasswordStrength checkPassword(String password) {
            String lowerPassword = password.toLowerCase();
            
            // Check against common passwords
            if (commonPasswords.mightContain(lowerPassword)) {
                return PasswordStrength.VERY_WEAK;
            }
            
            // Check against breached passwords
            if (breachedPasswords.mightContain(lowerPassword)) {
                return PasswordStrength.WEAK;
            }
            
            // Additional strength checks
            if (password.length() < 8) {
                return PasswordStrength.WEAK;
            } else if (password.length() < 12) {
                return PasswordStrength.MEDIUM;
            } else if (hasSpecialCharsAndNumbers(password)) {
                return PasswordStrength.STRONG;
            }
            
            return PasswordStrength.MEDIUM;
        }
        
        private boolean hasSpecialCharsAndNumbers(String password) {
            boolean hasNumber = password.matches(".*\\d.*");
            boolean hasSpecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");
            return hasNumber && hasSpecial;
        }
        
        public enum PasswordStrength {
            VERY_WEAK, WEAK, MEDIUM, STRONG
        }
    }
    
    /**
     * Example 6: Content Deduplication
     * Detect duplicate articles or content
     */
    public static class ContentDeduplicator {
        private final BloomFilter<String> contentHashes;
        private int uniqueContent = 0;
        private int duplicates = 0;
        
        public ContentDeduplicator(int expectedContent) {
            this.contentHashes = new BloomFilter<>(expectedContent, 0.01);
        }
        
        public boolean isUnique(String content) {
            String hash = generateContentHash(content);
            
            if (contentHashes.mightContain(hash)) {
                duplicates++;
                return false; // Likely duplicate
            }
            
            contentHashes.add(hash);
            uniqueContent++;
            return true;
        }
        
        private String generateContentHash(String content) {
            // Simple hash - in production, use proper hashing like SHA-256
            return String.valueOf(content.replaceAll("\\s+", "").toLowerCase().hashCode());
        }
        
        public void printStats() {
            System.out.println("Content Deduplication Statistics:");
            System.out.println("  Unique content: " + uniqueContent);
            System.out.println("  Duplicates detected: " + duplicates);
            System.out.println("  Deduplication rate: " + 
                String.format("%.2f%%", (duplicates * 100.0) / (uniqueContent + duplicates)));
        }
    }
    
    /**
     * Main method to run all examples
     */
    public static void main(String[] args) {
        System.out.println("=== Bloom Filter Practical Usage Examples ===\n");
        
        // Example 1: Spam Filter
        runSpamFilterExample();
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // Example 2: Cache Checker
        runCacheCheckerExample();
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // Example 3: Unique Visitors
        runUniqueVisitorExample();
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // Example 4: Distributed Tasks
        runDistributedTaskExample();
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // Example 5: Password Strength
        runPasswordStrengthExample();
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // Example 6: Content Deduplication
        runContentDeduplicationExample();
    }
    
    private static void runSpamFilterExample() {
        System.out.println("EXAMPLE 1: Email Spam Filter");
        System.out.println("-".repeat(70));
        
        SpamFilter filter = new SpamFilter(10000);
        
        // Add known spammers
        filter.addSpammer("spam@example.com");
        filter.addSpammer("malicious@test.com");
        filter.addSpammer("phishing@fake.org");
        
        // Test emails
        String[] emails = {
            "spam@example.com",
            "legitimate@company.com",
            "malicious@test.com",
            "user@domain.com"
        };
        
        System.out.println("\nChecking emails:");
        for (String email : emails) {
            boolean isSpam = filter.isLikelySpam(email);
            System.out.printf("  %-30s : %s\n", email, isSpam ? "SPAM ⚠️" : "SAFE ✓");
        }
        
        System.out.println();
        filter.printStats();
    }
    
    private static void runCacheCheckerExample() {
        System.out.println("EXAMPLE 2: Database Query Cache Checker");
        System.out.println("-".repeat(70));
        
        QueryCacheChecker checker = new QueryCacheChecker(1000);
        
        // Cache some query results
        checker.cacheResult("SELECT * FROM users WHERE id=1", "User1 Data");
        checker.cacheResult("SELECT * FROM users WHERE id=2", "User2 Data");
        checker.cacheResult("SELECT * FROM products", "Products Data");
        
        System.out.println("\nQuerying cache:");
        
        // These should be in cache
        System.out.println("Query 1 result: " + checker.getResult("SELECT * FROM users WHERE id=1"));
        System.out.println("Query 2 result: " + checker.getResult("SELECT * FROM users WHERE id=2"));
        
        // These should NOT be in cache (Bloom Filter saves expensive lookup)
        System.out.println("Query 3 result: " + checker.getResult("SELECT * FROM orders"));
        System.out.println("Query 4 result: " + checker.getResult("SELECT * FROM customers"));
        
        System.out.println();
        checker.printStats();
    }
    
    private static void runUniqueVisitorExample() {
        System.out.println("EXAMPLE 3: Unique Visitor Tracker");
        System.out.println("-".repeat(70));
        
        UniqueVisitorTracker tracker = new UniqueVisitorTracker(10000);
        
        // Simulate visitor traffic
        String[] visitors = {
            "192.168.1.1", "192.168.1.2", "192.168.1.1", // 1st visitor returns
            "192.168.1.3", "192.168.1.2", // 2nd visitor returns
            "192.168.1.4", "192.168.1.5", "192.168.1.1"  // 1st visitor returns again
        };
        
        System.out.println("\nTracking visitors:");
        for (String ip : visitors) {
            boolean isNew = tracker.recordVisit(ip);
            System.out.printf("  IP: %-15s -> %s\n", ip, isNew ? "NEW VISITOR" : "RETURNING");
        }
        
        System.out.println("\nApproximate unique visitors: " + tracker.getApproximateUniqueVisitors());
    }
    
    private static void runDistributedTaskExample() {
        System.out.println("EXAMPLE 4: Distributed Task Processing");
        System.out.println("-".repeat(70));
        
        // Simulate multiple workers
        DistributedTaskProcessor worker1 = new DistributedTaskProcessor("Worker-1");
        DistributedTaskProcessor worker2 = new DistributedTaskProcessor("Worker-2");
        
        // Simulate tasks (some duplicates)
        String[] tasks = {"task-1", "task-2", "task-3", "task-1", "task-4", "task-2", "task-5"};
        
        System.out.println("\nProcessing tasks across workers:");
        for (int i = 0; i < tasks.length; i++) {
            DistributedTaskProcessor worker = (i % 2 == 0) ? worker1 : worker2;
            worker.processTask(tasks[i]);
        }
        
        worker1.printStats();
        worker2.printStats();
    }
    
    private static void runPasswordStrengthExample() {
        System.out.println("EXAMPLE 5: Password Strength Checker");
        System.out.println("-".repeat(70));
        
        PasswordStrengthChecker checker = new PasswordStrengthChecker();
        
        String[] passwords = {
            "password",
            "MyP@ssw0rd",
            "short",
            "VeryStr0ng!P@ssw0rd123",
            "123456",
            "Welcome2024!"
        };
        
        System.out.println("\nChecking passwords:");
        for (String pwd : passwords) {
            PasswordStrengthChecker.PasswordStrength strength = checker.checkPassword(pwd);
            System.out.printf("  %-25s : %s\n", maskPassword(pwd), strength);
        }
    }
    
    private static void runContentDeduplicationExample() {
        System.out.println("EXAMPLE 6: Content Deduplication");
        System.out.println("-".repeat(70));
        
        ContentDeduplicator deduplicator = new ContentDeduplicator(1000);
        
        String[] articles = {
            "Breaking News: Technology advances rapidly",
            "Sports Update: Team wins championship",
            "Breaking News: Technology advances rapidly", // Duplicate
            "Weather Report: Sunny skies ahead",
            "Sports Update: Team wins championship", // Duplicate
            "Entertainment: New movie releases"
        };
        
        System.out.println("\nProcessing articles:");
        for (int i = 0; i < articles.length; i++) {
            boolean unique = deduplicator.isUnique(articles[i]);
            System.out.printf("  Article %d: %s\n", i + 1, unique ? "UNIQUE ✓" : "DUPLICATE ⚠️");
        }
        
        System.out.println();
        deduplicator.printStats();
    }
    
    private static String maskPassword(String password) {
        if (password.length() <= 4) {
            return "*".repeat(password.length());
        }
        return password.substring(0, 2) + "*".repeat(password.length() - 4) + 
               password.substring(password.length() - 2);
    }
}

