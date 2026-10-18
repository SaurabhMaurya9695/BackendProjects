package com.backend.dsa.atoz.arrays;

import java.util.Arrays;

public class MaximumAlternatingSumOfSquares {

    public static void main(String[] args) {
        int[] nums = { 1, -1, 2, -2, 3, -3 };
        System.out.println(maxAlternatingSum(nums));
    }

    private static long maxAlternatingSum(int[] nums) {
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            if (nums[i] < 0) {
                nums[i] = Math.abs(nums[i]);
            }
        }
        // 1 1 2 2 3 3
        // + (a0 + a2 + a4)
        // - (a1 + a3 + a5)

        // 9 + 9 + 4 - (4 + 1 + 1)
        // 22 - 6 => 16

        // 1,2,3
        // 3/2 => 1
        // 9 + 4 - 1 => 9 + 4 - 1 => 12

        Arrays.sort(nums);
        long choose = (n + 1) / 2;
        int i = n - 1;
        long sum1 = 0;
        while (choose-- > 0) {
            sum1 += ((long) nums[i] * nums[i]);
            i--;
        }

        long sum2 = 0;
        for (int j = 0; j <= i; j++) {
            sum2 += ((long) nums[j] * nums[j]);
        }

        return (long)(sum1 - sum2);
    }
}
