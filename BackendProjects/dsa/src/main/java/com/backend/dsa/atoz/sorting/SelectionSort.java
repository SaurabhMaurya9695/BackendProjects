package com.backend.dsa.atoz.sorting;

import com.backend.dsa.atoz.CommonUtil;

// name selection sort defined that , we have to select and then sort based on that
// so in first step select min and compare with the jth element
// once iteration is done of swapping then swap i and j
public class SelectionSort {

    public SelectionSort(int[] arr) {
        callFun(arr);
    }

    // if there is n length of array then  n - 1 swap / iteration required
    private void callFun(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            CommonUtil.swap(arr, i, minIndex);
        }
    }
}
