package com.backend.dsa.atoz.subarrays;

import java.util.HashMap;

public class MinimumSizeSubarraySum_01 {

    public static void main(String[] args) {
        int[] nums = { 2, 3, 1, 4, 3 };
        int target = 7;
        System.out.println(minSubArrayLenWithtarget(target, nums));

        int[] nums1 = { 26, 19, 11, 14, 18, 4, 7, 1, 30, 23, 19, 8, 10, 6, 26, 3 };
        int target2 = 26;
        System.out.println(minSubArrayLenWithtarget(target2, nums1));
    }

    public static int minSubArrayLenWithtarget(int target, int[] nums) {

        // Because nums[i] are positive, we can use two approaches: sliding window or prefix sum with a HashMap
        HashMap<Integer, Integer> mp = new HashMap<>();

        int pref = 0;
        int ans = Integer.MAX_VALUE;

        mp.put(0, -1);

        for (int i = 0; i < nums.length; i++) {
            pref += nums[i];

            // check if (pref - target) exists
            if (mp.containsKey(pref - target)) {
                ans = Math.min(ans, i - mp.get(pref - target));
            }

            // store prefix pref
            mp.put(pref, i);
        }

        return ans == Integer.MAX_VALUE ? 0 : ans;
    }

    public static int minSubArrayLen(int target, int[] nums) {

        // Because nums[i] are positive, we can use two approaches: sliding window or prefix sum with a HashMap
        // sliding windows fits perfect here because we want sum >= target and find a min window for subarray
        int left = 0;
        int sum = 0;
        int ans = Integer.MAX_VALUE;

        for (int right = 0; right < nums.length; right++) {
            sum += nums[right];

            // shrink window while valid
            while (sum >= target) {
                ans = Math.min(ans, right - left + 1);
                sum -= nums[left];
                left++;
            }
        }

        return ans == Integer.MAX_VALUE ? 0 : ans;
    }
}