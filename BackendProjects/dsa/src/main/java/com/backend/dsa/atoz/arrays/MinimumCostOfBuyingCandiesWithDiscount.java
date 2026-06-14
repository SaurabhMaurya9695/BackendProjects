package com.backend.dsa.atoz.arrays;

import java.util.Arrays;

public class MinimumCostOfBuyingCandiesWithDiscount {

    public static void main(String[] args) {
        int[] nums = {3,3,3,1};
        System.out.println(minimumCost(nums));
    }

    public static int minimumCost(int[] nums) {
        Arrays.sort(nums);
        if (nums.length == 1) {
            return nums[0];
        }

        int j = nums.length - 1;
        int sum = 0;
        while (j >= 0) {
            sum += nums[j];
            j--;
            if(j >=0 ){
                sum += nums[j];
                j--;
            }
            j--;
        }
        return sum;
    }
}
