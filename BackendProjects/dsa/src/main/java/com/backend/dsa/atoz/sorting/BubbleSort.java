package com.backend.dsa.atoz.sorting;

import com.backend.dsa.atoz.CommonUtil;

public class BubbleSort {

    public BubbleSort(int[] arr) {
        bubbleSort(arr);
    }

    private void bubbleSort(int[] arr) {
        int n = arr.length;
        for(int iteration = 1; iteration < n; iteration++) {
            for(int j = 0; j < n-iteration; j++) {
                if(arr[j] > arr[j+1]) {
                    CommonUtil.swap(arr, j, j+1);
                }
            }
        }

    }
}
