package com.backend.dsa.atoz.dynamicProgramming.buyAndSell;

public class BestTimeToBuyAndSellStock_01 {

    /**
     * Finds the maximum profit that can be obtained by buying and selling a stock once.
     * <p>
     * Problem: Stock Buy and Sell
     * Time Complexity: O(n) where n is the length of the prices array
     * Space Complexity: O(1) as we only use constant extra space
     *
     * @param prices An array where prices[i] represents the stock price on day i
     * @return The maximum profit possible from one buy-sell transaction, or 0 if no profit is possible
     * <p>
     * Example:
     * Input: prices = [7,1,5,3,6,4]
     * Output: 5
     * Explanation:
     * - Buy on day 2 when price = 1
     * - Sell on day 5 when price = 6
     * - Maximum profit = 6 - 1 = 5
     * <p>
     * Note:
     * 1. You must buy before selling (i.e., you can't sell first and buy later)
     * 2. Only one transaction is allowed
     * 3. If no profit is possible, return 0
     */
    public BestTimeToBuyAndSellStock_01(int[] prices) {
        System.out.println("Best Time to Buy and Sell Stock " + bttbs(prices));
    }

    private int bttbs(int[] arr) {
        int n = arr.length;
        int[] pre = new int[n];
        pre[0] = arr[0];
        for (int i = 1; i < n; i++) {
            pre[i] = Math.min(pre[i - 1], arr[i]);
        }

        int[] suf = new int[n];
        suf[n - 1] = arr[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            suf[i] = Math.max(suf[i], arr[i]);
        }

        int ans = 0;
        for (int i = 0; i < n; i++) {
            int val = Math.abs(pre[i] - suf[i]);
            ans = Math.max(val, ans);
        }

        return ans;
    }
}
