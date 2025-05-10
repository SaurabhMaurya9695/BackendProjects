package com.backend.dsa.atoz.sorting;

// In this quick sort , we have one pivot element and based on that we have to shift the area, > element should be in
// in left side and <= should be in right side

// we have to define the region
// 0 - i => unknown region
// j - i-1 => greater element (> pivot)
// 0 - j-1 => smaller element (<= pivot)

// draw above and do a dry run, you'll get to know by dry run only

import com.backend.dsa.atoz.CommonUtil;

public class QuickSort {

    public QuickSort(int[] arr) {
        quickSort(arr, 0, arr.length - 1);
    }

    private void quickSort(int[] arr, int low, int high) {

        if (low >= high) {
            return;
        }

        int pivotElement = arr[high];
        int pivotIdx = partition(arr, pivotElement, low, high);
        quickSort(arr, low, pivotIdx - 1);
        quickSort(arr, pivotIdx + 1, high);
    }

    private int partition(int[] arr, int pivot, int low, int high) {
        int i = low, j = low;
        while (i <= high) { // unknown area
            if (arr[i] <= pivot) {
                CommonUtil.swap(arr, i, j);
                i++;
                j++;
            } else if (arr[i] > pivot) {
                i++;
            }
        }
        // System.out.println("pivot index is :" + (j - 1));
        return (j - 1);
    }
}
