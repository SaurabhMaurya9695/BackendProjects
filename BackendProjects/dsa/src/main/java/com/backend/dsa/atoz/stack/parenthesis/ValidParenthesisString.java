package com.backend.dsa.atoz.stack.parenthesis;

import java.util.Arrays;

public class ValidParenthesisString {

    public static void main(String[] args) {
        String s = "(((";
        System.out.println(checkValidString(s));
    }

    public static boolean checkValidString(String s) {
        int n = s.length();
        int open = 0, close = 0, star = 0;
        for (Character x : s.toCharArray()) {
            if (x == '(') {
                open++;
            } else if (x == ')') {
                close++;
            } else {
                star++;
            }
        }

        int[][] dp = new int[n + 1][n + 1];
        for (int[] row : dp) {
            Arrays.fill(row, -1);
        }

        return solve(open, close, star, s, 0, 0, dp);
    }

    private static boolean solve(int open, int close, int star, String s, int idx, int balance, int[][] dp) {

        // At the end, all parentheses must be balanced
        if (idx == s.length()) {
            return balance == 0;
        }

        // Balance cannot become negative
        if (balance < 0) {
            return false;
        }

        // Remaining characters must be sufficient to close open brackets
        if (balance > close + star) {
            return false;
        }

        // Already computed this state
        if (dp[idx][balance] != -1) {
            return dp[idx][balance] == 1;
        }

        boolean ans = false;
        if (s.charAt(idx) == '(') {
            ans = solve(open - 1, close, star, s, idx + 1, balance + 1, dp);
        } else if (s.charAt(idx) == ')') {
            ans = solve(open, close - 1, star, s, idx + 1, balance - 1, dp);
        } else if (s.charAt(idx) == '*') {
            // If chosen as ''
            ans = solve(open, close, star - 1, s, idx + 1, balance, dp);

            // If chosen as '('
            if (!ans) {
                ans = solve(open, close, star - 1, s, idx + 1, balance + 1, dp);
            }

            // If chosen as ')'
            if (!ans) {
                ans = solve(open, close, star - 1, s, idx + 1, balance - 1, dp);
            }
        }

        dp[idx][balance] = ans ? 1 : 0;
        return ans;
    }
}
