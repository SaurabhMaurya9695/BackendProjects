package com.backend.dsa.atoz.subarrays;

import java.util.HashMap;

public class MakeSumDivisiblebyP_02 {

    public static void main(String[] args) {
        int[] nums = { 3, 1, 4, 2 };
        int p = 6;
        System.out.println(minSubarray(nums, p));

        int[] nums1 = { 6, 3, 5, 2 };
        int p1 = 9;
        System.out.println(minSubarray(nums1, p1));

        int[] nums2 = { 1, 2, 3 };
        int p3 = 3;
        System.out.println(minSubarray(nums2, p3));

        int[] nums3 = { 26, 19, 11, 14, 18, 4, 7, 1, 30, 23, 19, 8, 10, 6, 26, 3 };
        int p2 = 26;
        System.out.println(minSubarray(nums3, p2));

        // nums = [26,19,11,14,18,4,7,1,30,23,19,8,10,6,26,3]
        // p = 26

        // sum = 225
        // target = 225 % 26 = 17
        // so We need to remove subarray whose sum of subarray is 17

        // step 1 :
        // map = {0: -1}
        // prefix = 0

    }

    public static int minSubarray(int[] nums, int p) {

        // nums = [26,19,11,14,18,4,7,1,30,23,19,8,10,6,26,3]
        // p = 26

        // sum = 225
        // target = 225 % 26 = 17
        // so We need to remove subarray whose sum of subarray is 17

        // step 1 :
        // map = {0: -1}
        // prefix = 0

        long sum = 0;

        for (int num : nums) {
            sum += num;
        }

        long target = sum % p;

        // already divisible
        if (target == 0) {
            return 0;
        }

        // curr = nums[0 ....j]
        // prev = nums[0 ....i]
        // sum[i .. j] = curr - prev
        // curr - prev = target
        // prev =  curr - target
        // for handling negative numbers we should have add p
        // prev = (curr - target + p ) % p

        return minSubArrayLen(target, nums, p);
    }

    public static int minSubArrayLen(long target, int[] nums, int p) {

        HashMap<Long, Integer> mp = new HashMap<>();

        long prefix = 0;
        int ans = nums.length;

        mp.put(0L, -1);

        for (int i = 0; i < nums.length; i++) {
            prefix += nums[i];

            long currMod = prefix % p;
            long needed = (currMod - target + p) % p;

            if (mp.containsKey(needed)) {
                ans = Math.min(ans, i - mp.get(needed));
            }
            mp.put(currMod, i);
        }

        return ans == nums.length ? -1 : ans;
    }
}
