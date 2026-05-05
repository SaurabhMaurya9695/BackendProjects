package com.backend.dsa.atoz.subarrays;

import java.util.HashMap;

public class SubarraySumsDivisibleByK_03 {

    public static void main(String[] args) {
        int[] nums = { 4, 5, 0, -2, -3, 1 };
        int k = 5;
        System.out.println(subarraysDivByK(nums, k));

        int[] nums1 = { 2, -6, 3, 1, 2, 8, 2, 1 };
        int k1 = 7;
        System.out.println(subarraysDivByK(nums1, k1));
    }

    public static int subarraysDivByK(int[] nums, int k) {
        // so we need to find the SubArray sum divisible by k
        // if we are talking about subarray means subarray should be contiguous

        // TODO - SINCE AS PER THE CONSTRAINS IT SHOWS NUMS[I] CAN BE NEGATIVE AND POSITIVE SO HERE WE CAN'T USE
        //  SLIDING WINDOW, SLIDING WINDOW IS APPLICABLE WHEN NUMS[I] HAS ALL POSITIVE NUMBERS.
        HashMap<Integer, Integer> mp = new HashMap<>();
        mp.put(0, 1);

        int sum = 0;
        int ans = 0;
        for (int num : nums) {
            sum += num;
            int rem = sum % k;
            if (rem < 0) {
                rem = rem + k;
            }

            if (mp.containsKey(rem)) {
                ans += mp.get(rem);
                mp.put(rem, mp.get(rem) + 1);
            } else {
                mp.put(rem, 1);
            }
        }

        return ans;
    }
}
