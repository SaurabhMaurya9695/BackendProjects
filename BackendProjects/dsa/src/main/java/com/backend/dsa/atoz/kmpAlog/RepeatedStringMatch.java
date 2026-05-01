package com.backend.dsa.atoz.kmpAlog;

/**
 * LeetCode: Repeated String Match
 * https://leetcode.com/problems/repeated-string-match/description/
 * <p>
 * Problem: Find minimum repetitions of string 'a' needed to contain 'b' as substring
 * <p>
 * Example 1:
 * Input: a = "abcd", b = "cdabcdab"
 * Output: 3  (because "abcdabcdabcd" contains "cdabcdab")
 * <p>
 * Example 2:
 * Input: a = "a", b = "aa"
 * Output: 2
 * <p>
 * Example 3:
 * Input: a = "a", b = "b"
 * Output: -1
 */
public class RepeatedStringMatch {

    // ============ APPROACH 1: KMP PATTERN MATCHING (Optimal) ============
    public static int repeatedStringMatch_KMP(String a, String b) {
        // Step 1: Check if all characters in b exist in a
        if (!allCharsExist(a, b)) {
            return -1;
        }

        // Step 2: Calculate minimum possible repetitions needed
        // We need at least ceil(b.length() / a.length()) repetitions
        int minReps = (b.length() + a.length() - 1) / a.length();

        // Step 3: Try with minReps and minReps + 1
        // We add +1 because pattern might start near the end
        // Example: a="ab", b="bab" needs pattern starting from position 1 of first "ab"
        // So we need "ab" + "ab" = "abab" to find "bab"

        for (int reps = minReps; reps <= minReps + 1; reps++) {
            StringBuilder repeated = new StringBuilder();
            for (int i = 0; i < reps; i++) {
                repeated.append(a);
            }

            // Use KMP to find if b is substring of repeated a
            if (findPattern(repeated.toString(), b)) {
                return reps;
            }
        }

        return -1;
    }

    // Helper: Use KMP to find if pattern p exists in text s
    private static boolean findPattern(String s, String p) {
        int[] lps = buildLPS(p);
        int iIdx = 0;  // text pointer
        int jIdx = 0;  // pattern pointer

        while (iIdx < s.length()) {
            if (s.charAt(iIdx) == p.charAt(jIdx)) {
                iIdx++;
                jIdx++;
            }

            // Pattern found!
            if (jIdx == p.length()) {
                return true;
            }
            // Mismatch
            else if (iIdx < s.length() && s.charAt(iIdx) != p.charAt(jIdx)) {
                if (jIdx != 0) {
                    jIdx = lps[jIdx - 1];
                } else {
                    iIdx++;
                }
            }
        }

        return false;
    }

    // ============ APPROACH 2: Simple Brute Force (Also Correct) ============
    public static int repeatedStringMatch_BruteForce(String a, String b) {
        // Check if all characters in b exist in a
        if (!allCharsExist(a, b)) {
            return -1;
        }

        // Maximum repetitions we could need
        int maxReps = (b.length() / a.length()) + 2;

        StringBuilder repeated = new StringBuilder();

        for (int reps = 1; reps <= maxReps; reps++) {
            repeated.append(a);

            // Use built-in contains (which uses optimized matching internally)
            if (repeated.toString().contains(b)) {
                return reps;
            }
        }

        return -1;
    }

    // ============ APPROACH 3: KMP + Smart Iteration (Most Efficient) ============
    public static int repeatedStringMatch_SmartKMP(String a, String b) {
        // Check if all characters in b exist in a
        if (!allCharsExist(a, b)) {
            return -1;
        }

        // Build LPS array for pattern b
        int[] lps = buildLPS(b);

        // Calculate minimum repetitions needed
        int minReps = (b.length() + a.length() - 1) / a.length();

        // Try minReps and minReps + 1
        for (int reps = minReps; reps <= minReps + 1; reps++) {
            // Use KMP to match b in repeated a
            if (kmpMatch(a, b, reps, lps)) {
                return reps;
            }
        }

        return -1;
    }

    // KMP matching with repeated string (without actually building it)
    private static boolean kmpMatch(String a, String b, int reps, int[] lps) {
        int jIdx = 0;  // pattern pointer

        for (int rep = 0; rep < reps; rep++) {
            for (int i = 0; i < a.length(); i++) {
                while (jIdx > 0 && a.charAt(i) != b.charAt(jIdx)) {
                    jIdx = lps[jIdx - 1];
                }

                if (a.charAt(i) == b.charAt(jIdx)) {
                    jIdx++;
                }

                // Pattern found
                if (jIdx == b.length()) {
                    return true;
                }
            }
        }

        return false;
    }

    // ============ HELPER FUNCTIONS ============

    // Check if all characters in b exist in a
    private static boolean allCharsExist(String a, String b) {
        boolean[] chars = new boolean[26];

        for (char c : a.toCharArray()) {
            chars[c - 'a'] = true;
        }

        for (char c : b.toCharArray()) {
            if (!chars[c - 'a']) {
                return false;
            }
        }

        return true;
    }

    // Build LPS array for KMP
    public static int[] buildLPS(String s) {
        int n = s.length();
        int[] lps = new int[n];
        int len = 0;
        int i = 1;

        while (i < n) {
            if (s.charAt(i) == s.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }

        return lps;
    }

    // ============ TEST CASES ============
    public static void main(String[] args) {
        System.out.println("=== Repeated String Match - KMP Solutions ===\n");

        // Test Case 1: Basic example
        String a1 = "abcd";
        String b1 = "cdabcdab";
        System.out.println("Test 1: a=\"" + a1 + "\", b=\"" + b1 + "\"");
        System.out.println("KMP Approach: " + repeatedStringMatch_KMP(a1, b1));  // Expected: 3
        System.out.println("Brute Force: " + repeatedStringMatch_BruteForce(a1, b1));  // Expected: 3
        System.out.println("Smart KMP: " + repeatedStringMatch_SmartKMP(a1, b1));  // Expected: 3
        System.out.println();

        // Test Case 2: Single character repeated
        String a2 = "a";
        String b2 = "aa";
        System.out.println("Test 2: a=\"" + a2 + "\", b=\"" + b2 + "\"");
        System.out.println("KMP Approach: " + repeatedStringMatch_KMP(a2, b2));  // Expected: 2
        System.out.println("Brute Force: " + repeatedStringMatch_BruteForce(a2, b2));  // Expected: 2
        System.out.println("Smart KMP: " + repeatedStringMatch_SmartKMP(a2, b2));  // Expected: 2
        System.out.println();

        // Test Case 3: Character not in a
        String a3 = "a";
        String b3 = "b";
        System.out.println("Test 3: a=\"" + a3 + "\", b=\"" + b3 + "\"");
        System.out.println("KMP Approach: " + repeatedStringMatch_KMP(a3, b3));  // Expected: -1
        System.out.println("Brute Force: " + repeatedStringMatch_BruteForce(a3, b3));  // Expected: -1
        System.out.println("Smart KMP: " + repeatedStringMatch_SmartKMP(a3, b3));  // Expected: -1
        System.out.println();

        // Test Case 4: Pattern starts at boundary
        String a4 = "ab";
        String b4 = "bab";
        System.out.println("Test 4: a=\"" + a4 + "\", b=\"" + b4 + "\"");
        System.out.println("KMP Approach: " + repeatedStringMatch_KMP(a4, b4));  // Expected: 2
        System.out.println("Brute Force: " + repeatedStringMatch_BruteForce(a4, b4));  // Expected: 2
        System.out.println("Smart KMP: " + repeatedStringMatch_SmartKMP(a4, b4));  // Expected: 2
        System.out.println();

        // Test Case 5: Long pattern in a
        String a5 = "abc";
        String b5 = "cab";
        System.out.println("Test 5: a=\"" + a5 + "\", b=\"" + b5 + "\"");
        System.out.println("KMP Approach: " + repeatedStringMatch_KMP(a5, b5));  // Expected: 2
        System.out.println("Brute Force: " + repeatedStringMatch_BruteForce(a5, b5));  // Expected: 2
        System.out.println("Smart KMP: " + repeatedStringMatch_SmartKMP(a5, b5));  // Expected: 2
        System.out.println();

        // Test Case 6: Larger repetitions needed
        String a6 = "a";
        String b6 = "aaaa";
        System.out.println("Test 6: a=\"" + a6 + "\", b=\"" + b6 + "\"");
        System.out.println("KMP Approach: " + repeatedStringMatch_KMP(a6, b6));  // Expected: 4
        System.out.println("Brute Force: " + repeatedStringMatch_BruteForce(a6, b6));  // Expected: 4
        System.out.println("Smart KMP: " + repeatedStringMatch_SmartKMP(a6, b6));  // Expected: 4
        System.out.println();

        // Test Case 7: Already contained
        String a7 = "abcd";
        String b7 = "ab";
        System.out.println("Test 7: a=\"" + a7 + "\", b=\"" + b7 + "\"");
        System.out.println("KMP Approach: " + repeatedStringMatch_KMP(a7, b7));  // Expected: 1
        System.out.println("Brute Force: " + repeatedStringMatch_BruteForce(a7, b7));  // Expected: 1
        System.out.println("Smart KMP: " + repeatedStringMatch_SmartKMP(a7, b7));  // Expected: 1
        System.out.println();

        // Test Case 8: Complex pattern match
        String a8 = "aba";
        String b8 = "abaaba";
        System.out.println("Test 8: a=\"" + a8 + "\", b=\"" + b8 + "\"");
        System.out.println("KMP Approach: " + repeatedStringMatch_KMP(a8, b8));  // Expected: 2
        System.out.println("Brute Force: " + repeatedStringMatch_BruteForce(a8, b8));  // Expected: 2
        System.out.println("Smart KMP: " + repeatedStringMatch_SmartKMP(a8, b8));  // Expected: 2
    }
}
