package com.backend.dsa.atoz.dynamicProgramming.buyAndSell;

import java.util.Arrays;

public class BestTimeToBuyAndSellStock_02 {

    /**
     * You are given an integer array prices where prices[i] is the price of a given stock on the ith day.
     * <p>
     * On each day, you may decide to buy and/or sell the stock. You can only hold at most one share of the stock at
     * any time. However, you can buy it then immediately sell it on the same day.
     * <p>
     * Find and return the maximum profit you can achieve.
     * <p>
     * <p>
     * <p>
     * Example 1:
     * <p>
     * Input: prices = [7,1,5,3,6,4]
     * <p>Output: 7 </p>
     * Explanation: Buy on day 2 (price = 1) and sell on day 3 (price = 5), profit = 5-1 = 4.
     * Then buy on day 4 (price = 3) and sell on day 5 (price = 6), profit = 6-3 = 3.
     * Total profit is 4 + 3 = 7.
     */
    public BestTimeToBuyAndSellStock_02(int[] prices) {
        System.out.println("Best Time to Buy and Sell Stock ii using recursion : " + bttbs(prices));
        System.out.println("Best Time to Buy and Sell Stock ii using tabulation : " + tabulation(prices));
    }

    private int tabulation(int[] arr) {
        int n = arr.length;
        int[] buy = new int[n + 1];
        int[] sell = new int[n + 1];

        if (n == 1) {
            return 0;
        }

        buy[0] = -arr[0]; // buy on day 1
        sell[0] = 0;
        for (int i = 1; i < n; i++) {
            // go with current buy or sell with the previous val
            buy[i] = Math.max(buy[i - 1], sell[i - 1] - arr[i]);
            sell[i] = Math.max(sell[i - 1], buy[i - 1] + arr[i]);
        }
        return Math.max(buy[n - 1], sell[n - 1]);
    }

    private int bttbs(int[] arr) {
        int n = arr.length;
        int[][] dp = new int[n + 1][2];
        for (int i = 0; i < dp.length; i++) {
            Arrays.fill(dp[i], -1);
        }
        return solve(0, n, arr, 0, dp);
    }

    private int solve(int idx, int n, int[] arr, int buy, int[][] dp) {

        if (idx == n - 1) {
            // we have no other option we need to buy, but the profile would be zero since we are not selling
            return 0;
        }

        if (idx >= n) {
            return 0;
        }
        if (dp[idx][buy] != -1) {
            return dp[idx][buy];
        }
        // treat every day like buy and sell
        int ans;
        if (buy == 1) {
            // we already buy somewhere in a past, so now time to sell
            int sl = arr[idx] + solve(idx + 1, n, arr, 0, dp); // selling
            int by = solve(idx + 1, n, arr, 1, dp);
            // no selling a
            ans = Math.max(sl, by);
        } else {
            int by = -arr[idx] + solve(idx + 1, n, arr, 1, dp);
            int sl = solve(idx + 1, n, arr, 0, dp);
            ans = Math.max(sl, by);
        }
        return dp[idx][buy] = ans;
    }
}
