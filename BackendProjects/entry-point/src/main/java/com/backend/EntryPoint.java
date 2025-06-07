package com.backend;

import com.backend.dsa.atoz.dynamicProgramming.buyAndSell.BestTimeToBuyAndSellStockIII_05;
import com.backend.dsa.atoz.dynamicProgramming.buyAndSell.BestTimeToBuyAndSellStockIV_06;
import com.backend.dsa.atoz.dynamicProgramming.buyAndSell.BestTimeToBuyAndSellStockWithCooldown_04;
import com.backend.dsa.atoz.dynamicProgramming.buyAndSell.BestTimeToBuyAndSellStockWithTransactionFee_03;
import com.backend.dsa.atoz.dynamicProgramming.buyAndSell.BestTimeToBuyAndSellStock_01;
import com.backend.dsa.atoz.dynamicProgramming.buyAndSell.BestTimeToBuyAndSellStock_02;
import com.backend.dsa.atoz.dynamicProgramming.climbStairs.ClimbStairs;
import com.backend.dsa.atoz.dynamicProgramming.finobacci.Fibonacci;

public class EntryPoint {

    public static void main(String[] args) {
        int n = 7;
        new Fibonacci(n);

        new ClimbStairs(6);

        // -------------------------- DP ON BUY AND SELL --------------------------------------------------------
        int[] arr = { 7, 1, 5, 3, 6, 4 };
        new BestTimeToBuyAndSellStock_01(arr);
        new BestTimeToBuyAndSellStock_02(arr);

        int[] prices = { 1, 3, 2, 8, 4, 9 };
        int fee = 2;
        new BestTimeToBuyAndSellStockWithTransactionFee_03(prices, fee);

        int[] price = { 1, 2, 3, 0, 2 };
        new BestTimeToBuyAndSellStockWithCooldown_04(price);

        int[] prices1 = { 3, 3, 5, 0, 0, 3, 1, 4 };
        new BestTimeToBuyAndSellStockIII_05(prices1);

        int[] price2 = { 2, 4, 1 };
        int k = 2;
        new BestTimeToBuyAndSellStockIV_06(price2, k);

        int[] price3 = { 3, 2, 6, 5, 0, 3 };
        int k1 = 2;
        new BestTimeToBuyAndSellStockIV_06(price3, k1);

        // ----------------------------------------------------------------------------------------------------
    }
}
