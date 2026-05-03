package com.backend.dsa.atoz.dynamicProgramming.digitDp;

/**
 * Pure Recursion - Rotated Digits
 * <p>
 * Problem: Count numbers in [1, n] where:
 * - No digits 3, 4, 7 (banned)
 * - Has at least one digit from {2, 5, 6, 9} (special)
 */
public class RotatedDigitsWithoutDP {

    private String n;

    public int rotatedDigits(int num) {
        n = String.valueOf(num);
        return solve(0, 1, 0);
    }

    // idx: current position (digit index)
    // bounded: 1 if we're still limited by n, 0 if we can use any digit
    // hasSpecial: 1 if we've used a special digit (2,5,6,9), 0 otherwise
    private int solve(int idx, int bounded, int hasSpecial) {
        // BASE CASE: Reached end of number
        if (idx == n.length()) {
            return hasSpecial;
        }

        // What's the max digit we can pick at this position?
        int limit = bounded == 1 ? (n.charAt(idx) - '0') : 9;

        int count = 0;

        // Try each digit from 0 to limit
        for (int digit = 0; digit <= limit; digit++) {
            // Skip banned digits (3, 4, 7)
            if (digit == 3 || digit == 4 || digit == 7) {
                continue;
            }

            // Update bounded: stay bounded only if we pick max digit
            int newBounded = (bounded == 1 && digit == limit) ? 1 : 0;

            // Update hasSpecial: set to 1 if digit is 2,5,6,9
            int newHasSpecial = hasSpecial;
            if (digit == 2 || digit == 5 || digit == 6 || digit == 9) {
                newHasSpecial = 1;
            }

            // Recurse to next position
            count += solve(idx + 1, newBounded, newHasSpecial);
        }

        return count;
    }

    // Test
    public static void main(String[] args) {
        RotatedDigitsWithoutDP sol = new RotatedDigitsWithoutDP();

        System.out.println("n = 10: " + sol.rotatedDigits(10));    // Expected: 4
        System.out.println("n = 13: " + sol.rotatedDigits(13));    // Expected: 5
        System.out.println("n = 100: " + sol.rotatedDigits(100));  // Expected: 20
    }
}
