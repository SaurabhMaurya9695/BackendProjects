package com.backend.dsa.atoz.recursion;

import java.util.Arrays;

public class NumberOfZigZagArraysI {

    public static void main(String[] args) {
        int n = 3;
        int l = 1;
        int r = 3;

        System.out.println(zigZagArrays(n, l, r));
    }

    // Mod value given in the problem
    private static final long MOD = 1_000_000_007L;

    public static int zigZagArrays(int n, int l, int r) {
        // Since only relative ordering matters,
        // values from [l...r] can be compressed to [1...limit]
        int limit = r - l + 1;
        Long[][][] dp = new Long[n + 1][limit + 1][2];
        long ans = 0;

        // Try every possible starting value
        for (int start = 1; start <= limit; start++) {

            // Case 1:
            // First comparison should be increasing
            //
            // Example:
            // start = 2
            // Next value must be > 2
            ans = (ans + solve(1, start, 1, n, limit, dp)) % MOD;

            // Case 2:
            // First comparison should be decreasing
            //
            // Example:
            // start = 2
            // Next value must be < 2
            ans = (ans + solve(1, start, 0, n, limit, dp)) % MOD;
        }

        return (int) ans;
    }

    private static long solve(int idx, int prev, int needUp, int n, int limit, Long[][][] dp) {

        // Base case:
        // We have successfully formed an array of length n.
        if (idx == n) {
            return 1;
        }

        // Memoization hit
        if (dp[idx][prev][needUp] != null) {
            return dp[idx][prev][needUp];
        }

        long result = 0;

        if (needUp == 1) {

            // Next value must be strictly greater than prev.
            //
            // Example:
            // prev = 3
            // possible next values = 4,5,6...
            for (int next = prev + 1; next <= limit; next++) {

                // After taking a larger value,
                // next comparison must be decreasing.
                result = (result + solve(idx + 1, next, 0, n, limit, dp)) % MOD;
            }
        } else {

            // Next value must be strictly smaller than prev.
            //
            // Example:
            // prev = 5
            // possible next values = 1,2,3,4
            for (int next = 1; next < prev; next++) {

                // After taking a smaller value,
                // next comparison must be increasing.
                result = (result + solve(idx + 1, next, 1, n, limit, dp)) % MOD;
            }
        }

        // Store answer before returning
        return dp[idx][prev][needUp] = result;
    }
}