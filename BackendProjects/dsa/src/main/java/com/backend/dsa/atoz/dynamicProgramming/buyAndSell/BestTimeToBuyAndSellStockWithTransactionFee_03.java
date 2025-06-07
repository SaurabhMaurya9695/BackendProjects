package com.backend.dsa.atoz.dynamicProgramming.buyAndSell;

public class BestTimeToBuyAndSellStockWithTransactionFee_03 {

    public BestTimeToBuyAndSellStockWithTransactionFee_03(int[] prices, int fee) {
        System.out.println(
                "Best Time to Buy and Sell Stock with Transaction Fee using recursion: " + maxProfit(prices, fee));
        System.out.println(
                "Best Time to Buy and Sell Stock with Transaction Fee using tabulation : " + tabulation(prices, fee));
    }

    private int tabulation(int[] prices, int fee) {
        int n = prices.length;
        int[] buy = new int[n + 1];
        int[] sell = new int[n + 1];
        buy[0] = -prices[0];
        sell[0] = 0;
        for (int i = 1; i < n; i++) {
            buy[i] = Math.max(buy[i - 1], sell[i - 1] - prices[i]);
            sell[i] = Math.max(sell[i - 1], buy[i - 1] + prices[i] - fee);
        }
        return Math.max(buy[n - 1], sell[n - 1]);
    }

    int[][] dp;

    public int maxProfit(int[] prices, int fee) {
        int n = prices.length;
        dp = new int[n + 1][2];

        for (int i = 0; i <= n; i++) {
            dp[i][0] = -1;
            dp[i][1] = -1;
        }

        return solve(prices, fee, 0, 0);
    }

    private int solve(int[] prices, int fee, int idx, int buy) {
        if (idx >= prices.length) {
            return 0;
        }

        if (dp[idx][buy] != -1) {
            return dp[idx][buy];
        }

        int ans;
        if (buy == 1) {
            // We already bought, now either sell or skip
            int sell = (prices[idx] - fee) + solve(prices, fee, idx + 1, 0);
            int skip = solve(prices, fee, idx + 1, 1);
            ans = Math.max(sell, skip);
        } else {
            // We can buy now, either buy or skip
            int buyNow = -prices[idx] + solve(prices, fee, idx + 1, 1);
            int skip = solve(prices, fee, idx + 1, 0);
            ans = Math.max(buyNow, skip);
        }

        return dp[idx][buy] = ans;
    }
}
