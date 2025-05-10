package com.backend;

import com.backend.dsa.atoz.CommonUtil;
import com.backend.dsa.atoz.arrays.BasicOp;
import com.backend.dsa.atoz.arrays.MaxConsecutiveOnes;
import com.backend.dsa.atoz.arrays.MissingNumber;
import com.backend.dsa.atoz.arrays.MoveZerosToEnd;
import com.backend.dsa.atoz.arrays.RemoveDuplicatesFromSortedArray;
import com.backend.dsa.atoz.arrays.RotateArrayByD;
import com.backend.dsa.atoz.arrays.SecondLargetsElement;
import com.backend.dsa.atoz.arrays.UnionOfTwoSortedArrays;
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

        int[] arr1 = { 13, 46, 24, 52, 20, 9 };
        new SelectionSort(arr1);
        System.out.print("Selection sort result is : ");
        CommonUtil.printArray(arr1);

        int[] arr2 = { 13, 46, 24, 52, 20, 9 };
        new BubbleSort(arr2);
        System.out.print("Bubble sort result is : ");
        CommonUtil.printArray(arr2);

        int[] arr3 = { 13, 46, 24, 52, 20, 9 };
        new MergeSort(arr3);
        System.out.print("Merge sort result is : ");
        CommonUtil.printArray(arr3);

        int[] arr4 = { 7, 9, 4, 8, 3, 6, 2, 1 };
        new QuickSort(arr4);
        System.out.print("Quick sort result is : ");
        CommonUtil.printArray(arr4);

        // new BasicOp();

        int[] arr5 = { 7, 9, 4, 8, 3, 6, 2, 1 };
        new SecondLargetsElement(arr5);

        int[] arr6 = { 1, 1, 2, 3, 4 };
        new RemoveDuplicatesFromSortedArray(arr6);

        int[] arr7 = { 1, 2, 3, 4, 5, 6, 7 };
        new RotateArrayByD(arr7, 3);
        CommonUtil.printArray(arr7);

        int[] arr8 = { 1, 2, 0, 4, 0, 6, 0 };
        new MoveZerosToEnd(arr8);
        CommonUtil.printArray(arr8);

        int[] a = { 1, 2, 3, 4, 5, 6, 7 };
        int[] b = { 1, 1, 2, 2, 3, 3 };
        new UnionOfTwoSortedArrays(a, b);

        int[] arr9 = { 1, 2, 3, 4, 5, 6, 8 };
        int noOfElements = 8;
        new MissingNumber(arr9, noOfElements);

        int[] arr10 = { 1, 1, 0, 0, 0, 2, 2, 1, 1, 1, 1 };
        new MaxConsecutiveOnes(arr10);
    }
}
