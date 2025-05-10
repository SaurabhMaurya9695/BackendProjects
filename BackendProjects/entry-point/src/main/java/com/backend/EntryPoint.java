package com.backend;

import com.backend.dsa.atoz.CommonUtil;
import com.backend.dsa.atoz.sorting.BubbleSort;
import com.backend.dsa.atoz.sorting.InsertionSort;
import com.backend.dsa.atoz.sorting.MergeSort;
import com.backend.dsa.atoz.sorting.QuickSort;
import com.backend.dsa.atoz.sorting.SelectionSort;

public class EntryPoint {

    public static void main(String[] args) {
        int N = 6;
        int[] arr = { 13, 46, 24, 52, 20, 9 };
        new InsertionSort(arr);
        System.out.print("Insertion sort result is : ");
        CommonUtil.printArray(arr);

        new SelectionSort(arr);
        System.out.print("Selection sort result is : ");
        CommonUtil.printArray(arr);

        new BubbleSort(arr);
        System.out.print("Bubble sort result is : ");
        CommonUtil.printArray(arr);

        new MergeSort(arr);
        System.out.print("Merge sort result is : ");
        CommonUtil.printArray(arr);

        int[] arr1 = { 7, 9, 4, 8, 3, 6, 2,1 };
        new QuickSort(arr1);
        System.out.print("Quick sort result is : ");
        CommonUtil.printArray(arr1);
    }
}
