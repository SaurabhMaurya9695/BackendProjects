package com.backend.dsa.atoz.dynamicProgramming.climbStairs;

import java.util.Arrays;

/**
 * There is only one character 'A' on the screen of a notepad. You can perform one of two operations on this notepad
 * for each step:
 * <p>
 * Copy All: You can copy all the characters present on the screen (a partial copy is not allowed).
 * Paste: You can paste the characters which are copied last time.
 * Given an integer n, return the minimum number of operations to get the character 'A' exactly n times on the screen.
 */
public class TwoKeysKeyboard {

    public TwoKeysKeyboard(int n) {
        System.out.println(TwoKeysKeyboard(n));
    }

    public int TwoKeysKeyboard(int n) {
        if (n == 1) {
            return 0;
        }

        int[][] dp = new int[1001][1001];
        for (int i = 0; i < dp.length; i++) {
            Arrays.fill(dp[i], -1);
        }
        String screen = "A";
        return 1 + solve(n, screen, screen, dp);
    }

    private int solve(int n, String screen, String copy, int[][] dp) {

        if (screen.length() > n) {
            return 100000;
        }

        if (screen.length() == n) {
            return 0;
        }

        if (dp[screen.length()][copy.length()] != -1) {
            return dp[screen.length()][copy.length()];
        }

        // it means you have something in copy
        // now you have 2 options -> copy & paste
        int cp = 2 + solve(n, screen + screen, copy, dp);
        int paste = 1 + solve(n, screen + copy, copy, dp);
        return dp[screen.length()][copy.length()] = Math.min(cp, paste);
    }
}
