package com.backend.dsa.atoz.dynamicProgramming.buyAndSell;

import java.util.Arrays;

/**
 * You are given an array prices where prices[i] is the price of a given stock on the ith day.
 * <p>
 * Find the maximum profit you can achieve. You may complete at most two transactions.
 * <p>
 * Note: You may not engage in multiple transactions simultaneously (i.e., you must sell the stock before you buy
 * again).
 */
public class BestTimeToBuyAndSellStockIII_05 {

    public BestTimeToBuyAndSellStockIII_05(int[] prices1) {
        System.out.println("Best Time to Buy and Sell Stock III using recursion : " + maxProfit(prices1));
        System.out.println("Best Time to Buy and Sell Stock III using tabulation : " + tabulation(prices1));
    }

    private int tabulation(int[] prices) {
        int n = prices.length;
        int k = 2;
        int[][][] dp = new int[n + 1][2][k + 1];
        for (int idx = n - 1; idx >= 0; idx--) {
            for (int buy = 0; buy <= 1; buy++) {
                for (int cap = 1; cap <= 2; cap++) {
                    // write the recurrence relation of memorized code ;
                    if (buy == 1)  //state of buying
                    {
                        dp[idx][buy][cap] = Math.max(-prices[idx] + dp[idx + 1][0][cap], dp[idx + 1][1][cap]);
                    } else        //state of selling
                    {
                        dp[idx][buy][cap] = Math.max(+prices[idx] + dp[idx + 1][1][cap - 1], dp[idx + 1][0][cap]);
                    }
                }
            }
        }

        return dp[0][1][2];
    }

    private int maxProfit(int[] arr) {
        int n = arr.length;
        int k = 2;
        int cap = 0;
        int[][][] dp = new int[100001][2][k + 1];

        for (int i = 0; i < 100001; i++) {
            for (int j = 0; j < 2; j++) {
                for (int kk = 0; kk <= k; kk++) {
                    dp[i][j][kk] = -1;
                }
            }
        }

        return solve(arr, 0, n, 0, k, dp, cap);
    }

    private int solve(int[] price, int idx, int n, int buy, int k, int[][][] dp, int cap) {

        if (idx >= n || cap > k) {
            return 0;
        }

        if (dp[idx][buy][k] != -1) {
            return dp[idx][buy][k];
        }

        int ans;
        if (buy == 1) {
            int sell = price[idx] + solve(price, idx + 1, n, 0, k - 1, dp, cap + 1);
            int skip = solve(price, idx + 1, n, 1, k, dp, cap);
            ans = Math.max(sell, skip);
        } else {
            // we can buy one
            int buying = -price[idx] + solve(price, idx + 1, n, 1, k, dp, cap);
            int skip = solve(price, idx + 1, n, 0, k, dp, cap);
            ans = Math.max(buying, skip);
        }

        return ans;
    }
}
