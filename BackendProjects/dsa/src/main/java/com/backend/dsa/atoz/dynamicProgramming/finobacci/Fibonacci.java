package com.backend.dsa.atoz.dynamicProgramming.finobacci;

import java.util.Arrays;

/**
 * The Fibonacci numbers, commonly denoted F(n) form a sequence, called the Fibonacci sequence, such that each number
 * is the sum of the two preceding ones, starting from 0 and 1. That is,
 * <p>
 * F(0) = 0, F(1) = 1
 * F(n) = F(n - 1) + F(n - 2), for n > 1.
 * Given n, calculate F(n).
 */
public class Fibonacci {

    public Fibonacci(int n) {
        System.out.println(recursion(n));

        int[] dp = new int[n + 1];
        Arrays.fill(dp, -1);
        System.out.println(memorization(n, dp));

        System.out.println(tabulation(n));

        System.out.println(spaceOptimized(n));
    }

    private int spaceOptimized(int n) {
        if (n == 0 || n == 1) {
            return n;
        }
        int b = 1, a = 0;
        int curr = 0;
        for (int i = 2; i <= n; i++) {
            curr = b + a;
            a = b;
            b = curr;
        }
        return b;
    }

    private int recursion(int n) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        return recursion(n - 2) + recursion(n - 1);
    }

    private int memorization(int n, int[] dp) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }

        if (dp[n] != -1) {
            return dp[n];
        }

        return dp[n] = memorization(n - 2, dp) + memorization(n - 1, dp);
    }

    private int tabulation(int n) {
        if (n == 0) {
            return 0;
        }

        if (n == 1) {
            return 1;
        }

        int[] arr = new int[n + 1];
        arr[0] = 0;
        arr[1] = 1;
        for (int i = 2; i <= n; i++) {
            arr[i] = arr[i - 1] + arr[i - 2];
        }
        return arr[n];
    }
}
