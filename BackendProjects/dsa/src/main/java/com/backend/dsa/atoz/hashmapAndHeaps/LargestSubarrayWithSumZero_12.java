package com.backend.dsa.atoz.hashmapAndHeaps;

import java.util.HashMap;

public class LargestSubarrayWithSumZero_12 {

    public LargestSubarrayWithSumZero_12(int[] arr17) {
        solve(arr17);
    }

    private void solve(int[] arr17) {
        int n = arr17.length;
        HashMap<Integer, Integer> map = new HashMap<>();
        int ans = 0;
        int i = -1;
        int sum = 0;  // At index -1 sum was zero
        map.put(sum, -1);
        while (i < n - 1) {
            i++;
            sum += arr17[i];
            if (!map.containsKey(sum)) {
                map.put(sum, i);
            } else {
                ans = Math.max(ans, i - map.get(sum));
            }
        }

        System.out.println("Largest length of subarray with sum zero : " + ans);
    }
}
