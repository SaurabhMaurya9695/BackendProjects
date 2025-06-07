package com.backend.dsa.atoz.dynamicProgramming.buyAndSell;

import java.util.Arrays;

public class BestTimeToBuyAndSellStockWithCooldown_04 {

    public BestTimeToBuyAndSellStockWithCooldown_04(int[] price) {
        System.out.println("Best Time to Buy and Sell Stock with Cooldown using recursion : " + maxProfit(price));
        System.out.println("Best Time to Buy and Sell Stock with Cooldown using tabulation : " + tabulation(price));
    }

    private int tabulation(int[] price) {
        int n = price.length;
        int[] buy = new int[n + 1];
        int[] sell = new int[n + 1];

        buy[0] = -price[0];
        sell[0] = 0;
        for (int i = 1; i < n; i++) {
            if (i == 1) {
                buy[i] = Math.max(buy[i - 1], sell[i - 1] - price[i]);
                sell[i] = Math.max(sell[i - 1], buy[i - 1] + price[i]);
            } else {
                buy[i] = Math.max(buy[i - 1], sell[i - 2] - price[i]);
                sell[i] = Math.max(sell[i - 1], buy[i - 1] + price[i]);
            }
        }

        return Math.max(buy[n - 1], sell[n - 1]);
    }

    private int maxProfit(int[] price) {
        int[][] dp = new int[5001][2];
        for (int i = 0; i < dp.length; i++) {
            Arrays.fill(dp[i], -1);
        }
        return solve(price, 0, price.length, 0, dp);
    }

    private int solve(int[] price, int idx, int n, int buy, int[][] dp) {

        if (idx >= n) {
            return 0;
        }

        if (dp[idx][buy] != -1) {
            return dp[idx][buy];
        }
        int ans = 0;
        if (buy == 1) {
            int sell = price[idx] + solve(price, idx + 2, n, 0, dp);
            int skip = solve(price, idx + 1, n, 1, dp);
            ans = Math.max(sell, skip);
        } else {
            // we can buy one
            int buying = -price[idx] + solve(price, idx + 1, n, 1, dp);
            int skip = solve(price, idx + 1, n, 0, dp);
            ans = Math.max(buying, skip);
        }

        return dp[idx][buy] = ans;
    }
}
