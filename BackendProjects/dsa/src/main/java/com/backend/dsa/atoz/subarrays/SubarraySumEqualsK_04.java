package com.backend.dsa.atoz.subarrays;

import java.util.HashMap;

public class SubarraySumEqualsK_04 {

    public static void main(String[] args) {
        //        int[] nums = { 1, 1, 1 };
        //        int k = 2;
        //        // SINCE IT IS SHOWING IN CONSTRAIN nums[i] >= -1000 && <= 1000
        //        // so here we shouldn't think of sliding window
        //        // only one option is here - prefix sum with hashMap
        //        System.out.println(subarraySum(nums, k));

        int[] nums1 = { 1, 2, 3 };
        int k1 = 3;
        System.out.println(subarraySum(nums1, k1));
    }

    private static int subarraySum(int[] nums, int k) {
        int n = nums.length;
        int pref = 0;
        int ans = 0;
        HashMap<Integer, Integer> mp = new HashMap<>();
        mp.put(0, 1);
        for (int i = 0; i < n; i++) {
            pref += nums[i];
            int target = pref - k;
            if (mp.containsKey(target)) {
                ans += mp.get(target);
            }

            mp.put(pref, mp.getOrDefault(pref, 0) + 1);
        }
        return ans;
    }
}
