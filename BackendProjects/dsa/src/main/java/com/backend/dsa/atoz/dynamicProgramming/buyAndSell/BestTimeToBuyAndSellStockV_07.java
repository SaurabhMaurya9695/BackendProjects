package com.backend.dsa.atoz.dynamicProgramming.buyAndSell;

/**
 * Given an integer array {@code prices}, where {@code prices[i]} represents the price of a stock in dollars on the
 * i-th day,
 * and an integer {@code k} representing the maximum number of transactions allowed.
 * <p>
 * You are permitted to make at most {@code k} transactions. Each transaction can be one of the following:
 *
 * <ul>
 *   <li><b>Normal Transaction:</b> Buy on day <i>i</i>, then sell on a later day <i>j</i> (i &lt; j). Profit is
 *   {@code prices[j] - prices[i]}.</li>
 *   <li><b>Short Selling Transaction:</b> Sell on day <i>i</i>, then buy back on a later day <i>j</i> (i &lt; j).
 *   Profit is {@code prices[i] - prices[j]}.</li>
 * </ul>
 *
 * <p>
 * Constraints:
 * <ul>
 *   <li>You must complete one transaction before starting another.</li>
 *   <li>You cannot buy/sell on the same day as the completion of a previous transaction.</li>
 * </ul>
 *
 * <p>
 * <p>
 * /**
 * Solution :
 * Solution Approach
 * We use a 3D DP with memoization (top‑down).
 * <p>
 * State Definition: dp[i][t][s] = maximum profit from day i with t transactions remaining and state s:
 * <p>
 * s = 0 → No position (free to buy or short-sell).
 * s = 1 → Holding long (we have bought and waiting to sell).
 * s = 2 → Holding short (we have sold first, waiting to buy back).
 * Transitions:
 * <p>
 * If s = 0 (flat):
 * <p>
 * Buy long: -prices[i] + solve(i+1, t, 1)
 * Short sell: +prices[i] + solve(i+1, t, 2)
 * Skip: solve(i+1, t, 0)
 * If s = 1 (long):
 * <p>
 * Sell: +prices[i] + solve(i+1, t-1, 0)
 * Hold: solve(i+1, t, 1)
 * If s = 2 (short):
 * <p>
 * Buy back: -prices[i] + solve(i+1, t-1, 0)
 * Hold: solve(i+1, t, 2)
 * Memoization:
 * <p>
 * Use a 3D array Long dp[n][k+1][3] to store intermediate results and avoid recomputation.
 * Base Case:
 * <p>
 * If i == n (past last day):
 * <p>
 * If s == 0 → 0 profit; else -∞ (invalid).
 */
public class BestTimeToBuyAndSellStockV_07 {

    public BestTimeToBuyAndSellStockV_07() {
        int[] prices = { 1, 7, 9, 8, 2 };
        int k = 2;
        System.out.println("BestTimeToBuyAndSellStockV_07 " + maximumProfit(prices, k));
    }

    private long maximumProfit(int[] prices, int k) {
        long ans = 0;
        int n = prices.length;
        Long[][][] dp = new Long[n][k + 1][3];
        long res = solve(0, k, 0, n, prices, dp);
        return res;
    }

    static long solve(int i, int k, int decider, int n, int[] prices, Long[][][] dp) {
        if (i == n) {
            if (k >= 0 && decider == 0) {
                return 0;
            }
            return Integer.MIN_VALUE;
        }

        if (dp[i][k][decider] != null) {
            return dp[i][k][decider];
        }

        long take = Integer.MIN_VALUE, dontTake = Integer.MIN_VALUE;
        if (k > 0) {
            if (decider == 1) {
                take = prices[i] + solve(i + 1, k - 1, 0, n, prices, dp);
            } else if (decider == 2) {
                take = -prices[i] + solve(i + 1, k - 1, 0, n, prices, dp);
            } else {
                take = Math.max(prices[i] + solve(i + 1, k, 2, n, prices, dp),
                        -prices[i] + solve(i + 1, k, 1, n, prices, dp));
            }
        }

        dontTake = solve(i + 1, k, decider, n, prices, dp);
        return dp[i][k][decider] = Math.max(take, dontTake);
    }
}
