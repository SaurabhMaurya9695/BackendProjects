package com.backend.dsa.atoz.dynamicProgramming.digitDp;

/**
 * LeetCode: Rotated Digits
 * <a href="https://leetcode.com/problems/rotated-digits/description/">...</a>
 * <p>
 * Problem: Count numbers in [1, n] where:
 * 1. All digits must be from {0, 1, 2, 5, 6, 8, 9} (no 3, 4, 7)
 * 2. Must have at least one digit from {2, 5, 6, 9} (special digits)
 * <p>
 * Example 1:
 * Input: n = 10
 * Output: 4
 * Explanation: Numbers are 2, 5, 6, 9
 * <p>
 * Example 2:
 * Input: n = 13
 * Output: 5
 * Explanation: Numbers are 2, 5, 6, 9, 12
 * <p>
 * Complexity:
 * Time: O(log n) - Only build digits, not check all numbers
 * Space: O(log n) - For memo and recursion stack
 */
public class RotatedDigits {

    // ============ APPROACH 1: DIGIT DP (Optimal) ============
    public static int rotatedDigits(int num) {
        String n = String.valueOf(num);

        // memo[position][isBoundedByN][hasSpecialDigit] = count of valid numbers
        int[][][] memo = new int[n.length() + 1][2][2];

        // Initialize memo with -1 (not computed yet)
        for (int i = 0; i <= n.length(); i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    memo[i][j][k] = -1;
                }
            }
        }

        return buildNumber(n, 0, 1, 0, memo);
    }

    // Build numbers digit by digit using Digit DP
    private static int buildNumber(String n, int currentPos, int isBoundedByN, int hasSpecialDigit, int[][][] memo) {
        // ===== BASE CASE =====
        // We've built the complete number
        if (currentPos == n.length()) {
            // Only count if we used at least one special digit (2, 5, 6, 9)
            return hasSpecialDigit;
        }

        // If we've already solved this state, return cached answer
        if (memo[currentPos][isBoundedByN][hasSpecialDigit] != -1) {
            return memo[currentPos][isBoundedByN][hasSpecialDigit];
        }

        // limit for current pos
        int maxDigitAtThisPos = 9; // By default, we can pick 0-9

        if (isBoundedByN == 1) {
            // We're still bounded by n, so check what digit n has at this position
            maxDigitAtThisPos = n.charAt(currentPos) - '0';
            // Example: if n="258" and currentPos=1, then maxDigitAtThisPos=5
        }

        int totalCount = 0;
        for (int digit = 0; digit <= maxDigitAtThisPos; digit++) {
            // Skip if this digit is banned (3, 4, 7 cannot be rotated)
            if (isBannedDigit(digit)) {
                continue;
            }

            int newBoundedStatus = 0;
            if (isBoundedByN == 1 && digit == maxDigitAtThisPos) {
                // We picked the maximum allowed digit, still bounded by n
                newBoundedStatus = 1;
            }
            // If we picked less than max, we're no longer bounded (newBoundedStatus = 0)
            int newHasSpecialDigit = hasSpecialDigit;
            if (isSpecialDigit(digit)) {
                // We found a special digit from {2, 5, 6, 9}!
                newHasSpecialDigit = 1;
            }

            int countFromThisDigit = buildNumber(n, currentPos + 1, newBoundedStatus, newHasSpecialDigit, memo);
            totalCount += countFromThisDigit;
        }

        // ===== CACHE AND RETURN =====
        memo[currentPos][isBoundedByN][hasSpecialDigit] = totalCount;
        return totalCount;
    }

    // Check if digit is banned (cannot be rotated)
    private static boolean isBannedDigit(int digit) {
        return digit == 3 || digit == 4 || digit == 7;
    }

    // Check if digit is special (0, 1, 8 stay same; 2, 5, 6, 9 rotate to different)
    private static boolean isSpecialDigit(int digit) {
        return digit == 2 || digit == 5 || digit == 6 || digit == 9;
    }

    // ============ APPROACH 2: BRUTE FORCE (For Comparison) ============
    public static int rotatedDigits_BruteForce(int n) {
        int count = 0;
        for (int i = 1; i <= n; i++) {
            if (isValidNumber(i)) {
                count++;
            }
        }
        return count;
    }

    private static boolean isValidNumber(int num) {
        boolean hasSpecial = false;

        while (num > 0) {
            int digit = num % 10;

            // Check if digit is banned
            if (digit == 3 || digit == 4 || digit == 7) {
                return false;
            }

            // Check if digit is special
            if (digit == 2 || digit == 5 || digit == 6 || digit == 9) {
                hasSpecial = true;
            }

            num /= 10;
        }

        return hasSpecial;
    }

    // ============ TEST CASES ============
    public static void main(String[] args) {
        System.out.println("=== Rotated Digits - Digit DP Solution ===\n");

        // Test Case 1: Basic example
        int n1 = 10;
        System.out.println("Test 1: n = " + n1);
        System.out.println("Digit DP: " + rotatedDigits(n1));  // Expected: 4
        //        System.out.println("Brute Force: " + rotatedDigits_BruteForce(n1));  // Expected: 4
        System.out.println("Valid numbers: 2, 5, 6, 9\n");

        // Test Case 2: With banned digits
        int n2 = 13;
        System.out.println("Test 2: n = " + n2);
        System.out.println("Digit DP: " + rotatedDigits(n2));  // Expected: 5
        System.out.println("Brute Force: " + rotatedDigits_BruteForce(n2));  // Expected: 5
        System.out.println("Valid numbers: 2, 5, 6, 9, 12\n");

        // Test Case 3: Single digit
        int n3 = 9;
        System.out.println("Test 3: n = " + n3);
        System.out.println("Digit DP: " + rotatedDigits(n3));  // Expected: 4
        System.out.println("Brute Force: " + rotatedDigits_BruteForce(n3));  // Expected: 4
        System.out.println("Valid numbers: 2, 5, 6, 9\n");

        // Test Case 4: No valid numbers
        int n4 = 1;
        System.out.println("Test 4: n = " + n4);
        System.out.println("Digit DP: " + rotatedDigits(n4));  // Expected: 0
        System.out.println("Brute Force: " + rotatedDigits_BruteForce(n4));  // Expected: 0
        System.out.println("Valid numbers: none\n");

        // Test Case 5: Contains all valid digits
        int n5 = 100;
        System.out.println("Test 5: n = " + n5);
        System.out.println("Digit DP: " + rotatedDigits(n5));  // Expected: 20
        System.out.println("Brute Force: " + rotatedDigits_BruteForce(n5));  // Expected: 20\n");

        // Test Case 6: Larger number
        int n6 = 500;
        System.out.println("Test 6: n = " + n6);
        System.out.println("Digit DP: " + rotatedDigits(n6));  // Expected: 92
        System.out.println("Brute Force: " + rotatedDigits_BruteForce(n6));  // Expected: 92\n");

        // Performance comparison
        System.out.println("=== Performance Comparison ===");
        int largeN = 100000;

        long start = System.currentTimeMillis();
        int result1 = rotatedDigits(largeN);
        long digitDpTime = System.currentTimeMillis() - start;

        start = System.currentTimeMillis();
        int result2 = rotatedDigits_BruteForce(largeN);
        long bruteForceTime = System.currentTimeMillis() - start;

        System.out.println("n = " + largeN);
        System.out.println("Digit DP Result: " + result1 + " (Time: " + digitDpTime + "ms)");
        System.out.println("Brute Force Result: " + result2 + " (Time: " + bruteForceTime + "ms)");
        System.out.println("Speed improvement: " + (bruteForceTime / (double) digitDpTime) + "x faster");
    }
}
