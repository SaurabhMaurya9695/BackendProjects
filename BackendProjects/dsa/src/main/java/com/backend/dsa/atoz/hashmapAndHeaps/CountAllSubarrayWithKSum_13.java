package com.backend.dsa.atoz.hashmapAndHeaps;

import java.util.HashMap;

public class CountAllSubarrayWithKSum_13 {

    public CountAllSubarrayWithKSum_13(int[] arr20, int k) {
        solve(arr20, k);
        solveByBruteForce(arr20, k);
    }

    private void solveByBruteForce(int[] arr20, int k) {
        int cnt = 0;
        for (int i = 0; i < arr20.length; i++) {
            int sum = 0;
            for (int j = i; j < arr20.length; j++) {
                sum += arr20[j];
                if (sum == k) {
                    cnt++;
                }
            }
        }
        System.out.println("Total subarray of all subarray with k sum with BruteForce sol is  : " + cnt);
    }

    private void solve(int[] arr20, int k) {
        int n = arr20.length;
        HashMap<Integer, Integer> freq = new HashMap<>();
        int subarray = 0;
        int i = -1;
        int sum = 0;
        freq.put(sum, 1); // we looked one time so freq should be 1
        while (i < n - 1) {
            i++; // we started from  -1 and i want to skip this -1 so increase +1 ;
            sum += arr20[i];
            if (freq.containsKey(sum - k)) {
                subarray += freq.get(sum - k);
                freq.put(sum, freq.getOrDefault(sum, 0) + 1);
            } else {
                freq.put(sum, 1);
            }
        }

        System.out.println("Total subarray of all subarray with k sum with O(N) is : " + subarray);
    }
}
