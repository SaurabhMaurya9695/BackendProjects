package com.backend.dsa.atoz.dynamicProgramming.buyAndSell;

public class BestTimeToBuyAndSellStockIV_06 {

    public BestTimeToBuyAndSellStockIV_06(int[] price2, int k) {
        System.out.println("Best Time to Buy and Sell Stock IV : " + maxProfit(k, price2));
    }

    private int maxProfit(int k, int[] arr) {
        int n = arr.length;
        int buyingAtMost = 0;
        int sellingAtMost = 0;
        int[][][] dp = new int[n + 1][2][k + 1];

        for (int i = 0; i <= n; i++) {
            for (int j = 0; j < 2; j++) {
                for (int kk = 0; kk <= k; kk++) {
                    dp[i][j][kk] = -1;
                }
            }
        }

        // we can buy or sell at-most k times
        return solve(arr, 0, n, 0, k, dp, buyingAtMost, sellingAtMost, 0);
    }

    private int solve(int[] price, int idx, int n, int buy, int k, int[][][] dp, int buyingAtMost, int sellingAtMost,
            int transactions) {

        if (idx >= n || buyingAtMost > k || sellingAtMost > k || transactions > k) {
            return 0;
        }

        if (dp[idx][buy][k] != -1) {
            return dp[idx][buy][k];
        }

        int ans;
        if (buy == 1) {
            int sell = price[idx] + solve(price, idx + 1, n, 0, k - 1, dp, buyingAtMost, sellingAtMost + 1,
                    transactions + 1);
            int skip = solve(price, idx + 1, n, 1, k, dp, buyingAtMost, sellingAtMost, transactions);
            ans = Math.max(sell, skip);
        } else {
            // we can buy one
            int buying = -price[idx] + solve(price, idx + 1, n, 1, k, dp, buyingAtMost + 1, sellingAtMost,
                    transactions + 1);
            int skip = solve(price, idx + 1, n, 0, k, dp, buyingAtMost, sellingAtMost, transactions);
            ans = Math.max(buying, skip);
        }

        return ans;
    }
}
