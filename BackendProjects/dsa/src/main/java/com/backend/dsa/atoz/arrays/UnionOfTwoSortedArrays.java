package com.backend.dsa.atoz.arrays;

import com.backend.dsa.atoz.CommonUtil;

import java.util.ArrayList;

public class UnionOfTwoSortedArrays {

    public UnionOfTwoSortedArrays(int[] a, int[] b) {
        unionOfTwoSortedArrays(a, b, a.length, b.length);
    }

    private void unionOfTwoSortedArrays(int[] arr1, int[] arr2, int n, int m) {
        int i = 0, j = 0;
        ArrayList<Integer> ans = new ArrayList<>();
        while (i < n && j < m) {
            if (arr1[i] <= arr2[j]) {
                if (ans.isEmpty() || CommonUtil.back(ans) != arr1[i]) {
                    ans.add(arr1[i]);
                }
                i++;
            } else {
                if (ans.isEmpty() || CommonUtil.back(ans) != arr2[j]) {
                    ans.add(arr2[j]);
                }
                j++;
            }
        }
        while (i < n) {
            if (ans.get(ans.size() - 1) != arr1[i]) {
                ans.add(arr1[i]);
            }
            i++;
        }
        while (j < m) {
            if (ans.get(ans.size() - 1) != arr2[j]) {
                ans.add(arr2[j]);
            }
            j++;
        }
        System.out.print("Union of Two Sorted Array is : ");
        CommonUtil.printArray(ans);
    }
}
