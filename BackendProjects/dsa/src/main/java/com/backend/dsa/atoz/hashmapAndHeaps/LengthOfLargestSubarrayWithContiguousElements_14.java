package com.backend.dsa.atoz.hashmapAndHeaps;

import com.backend.dsa.atoz.CommonUtil;

import java.util.HashSet;

public class LengthOfLargestSubarrayWithContiguousElements_14 {

    public LengthOfLargestSubarrayWithContiguousElements_14(int[] arr22) {
        solveWithBruteForceWithoutDuplicate(arr22);
        solveWithBruteForceWithDuplicate(arr22);
    }

    private void solveWithBruteForceWithDuplicate(int[] arr22) {
        int n = arr22.length;
        int i = 0, j = 1;
        int length = 0;
        while (i < n) {
            int maxi = arr22[i];
            int mini = arr22[i];
            HashSet<Integer> set = new HashSet<>();
            set.add(arr22[i]);
            while (j < n) {
                if (set.contains(arr22[j])) {
                    break;
                }

                set.add(arr22[j]);
                maxi = CommonUtil.max(maxi, arr22[j]);
                mini = CommonUtil.min(mini, arr22[j]);

                if (maxi - mini > n) {
                    break;
                }

                if (maxi - mini == (j - i)) { // -> this rule is only valid for no duplicate , if duplicate occure then
                    // we will handle separately
                    length = Math.max(length, j - i + 1);
                }
                j++;
            }
            i++;
            j = i + 1;
        }
        System.out.println("Length of the longest contiguous subarray with duplicates allowed: " + length);
    }

    private void solveWithBruteForceWithoutDuplicate(int[] arr22) {
        int n = arr22.length;
        int i = 0, j = 0;
        int length = 0;
        while (i < n) {
            int maxi = Integer.MIN_VALUE;
            int mini = Integer.MAX_VALUE;
            while (j < n) {
                maxi = Math.max(maxi, arr22[j]);
                mini = Math.min(mini, arr22[j]);

                if (maxi - mini > n) {
                    break;
                }
                if (maxi - mini == (j - i)) { // -> this rule is only valid for no duplicate , if duplicate occure then
                    // we will handle separately
                    length = Math.max(length, j - i + 1);
                }
                j++;
            }
            i++;
            j = i;
        }
        System.out.println("Length of the longest contiguous subarray without duplicates: " + length);
    }
}
