package com.backend.dsa.atoz.dynamicProgramming.kadane;

public class MaxSumSubarray_01 {

    public MaxSumSubarray_01(int[] nums) {
        System.out.println("Max Sum Subarray : " + maxSubArray(nums));
    }

    public int maxSubArray(int[] nums) {
        // every number has two choice either be include with me or start a new seq
        int overall_sum = nums[0], best_sum = nums[0];
        for (int i = 1; i < nums.length; i++) {
            int currElement = nums[i];
            if (overall_sum + currElement >= currElement) {
                // now we are choosing we currElem be a part of new group or not
                overall_sum = overall_sum + currElement;
            } else {
                overall_sum = currElement;
            }
            if (overall_sum > best_sum) {
                best_sum = overall_sum;
            }
        }

        return best_sum;
    }
}
