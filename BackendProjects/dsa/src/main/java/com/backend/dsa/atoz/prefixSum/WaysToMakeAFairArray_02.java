package com.backend.dsa.atoz.prefixSum;

import java.util.ArrayList;
import java.util.List;

public class WaysToMakeAFairArray_02 {

    public static void main(String[] args) {
        int[] nums = { 2, 1, 6, 4 };
        System.out.println(waysToMakeFair(nums));
    }

    public static int waysToMakeFair(int[] nums) {
        int n = nums.length;

        // brute force - TLE
        List<Integer> lst = new ArrayList<>();

        // populate list from nums
        for (int num : nums) {
            lst.add(num);
        }

        int ans = 0;
        for (int i = 0; i < nums.length; i++) {
            List<Integer> temp = new ArrayList<>(lst);
            temp.remove(i);
            int evenSum = 0, oddSum = 0;
            for (int j = 0; j < temp.size(); j++) {
                if (j % 2 == 0) {
                    evenSum += temp.get(j);
                } else {
                    oddSum += temp.get(j);
                }
            }
            if (evenSum == oddSum) {
                ans++;
            }
        }
        return ans;
    }
}
