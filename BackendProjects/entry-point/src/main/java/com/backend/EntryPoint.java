package com.backend;

import com.backend.dsa.atoz.CommonUtil;
import com.backend.dsa.atoz.arrays.MaxConsecutiveOnes;
import com.backend.dsa.atoz.arrays.MissingNumber;
import com.backend.dsa.atoz.arrays.MoveZerosToEnd;
import com.backend.dsa.atoz.arrays.RemoveDuplicatesFromSortedArray;
import com.backend.dsa.atoz.arrays.RotateArrayByD;
import com.backend.dsa.atoz.arrays.SecondLargetsElement;
import com.backend.dsa.atoz.arrays.UnionOfTwoSortedArrays;
import com.backend.dsa.atoz.hashmapAndHeaps.CheckEveryPairIsDivisibleByK_10;
import com.backend.dsa.atoz.hashmapAndHeaps.DistinctElementInWindowK_11;
import com.backend.dsa.atoz.hashmapAndHeaps.FindCEO_08;
import com.backend.dsa.atoz.hashmapAndHeaps.FindItineraryFromTickets_09;
import com.backend.dsa.atoz.hashmapAndHeaps.FoundHighOccuringChar_01;
import com.backend.dsa.atoz.hashmapAndHeaps.KLargestElements_03;
import com.backend.dsa.atoz.hashmapAndHeaps.LongestConsecutiveSequence_02;
import com.backend.dsa.atoz.hashmapAndHeaps.MergeKSortedList_06;
import com.backend.dsa.atoz.hashmapAndHeaps.NearlySorted_04;
import com.backend.dsa.atoz.sorting.BubbleSort;
import com.backend.dsa.atoz.sorting.InsertionSort;
import com.backend.dsa.atoz.sorting.MergeSort;
import com.backend.dsa.atoz.sorting.QuickSort;
import com.backend.dsa.atoz.sorting.SelectionSort;

import java.util.HashMap;

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

        String[] s = { "a", "b", "c", "d", "a", "a" };
        new FoundHighOccuringChar_01(s);

        int[] arr11 = { 10, 5, 9, 1, 11, 8, 6, 15, 3, 12, 2 };
        new LongestConsecutiveSequence_02(arr11);

        int[] arr12 = { 10, 5, 9, 1, 11, 8, 6, 15, 3, 12, 2 };
        new KLargestElements_03(arr12, 3);

        int[] arr13 = { 6, 5, 3, 2, 8, 10, 9 };
        int k = 3;
        new NearlySorted_04(arr13, k);

        /* ImplementMedianFinder_05 imf = new ImplementMedianFinder_05();
            imf.add(10);
            imf.add(20);
            imf.add(30);
            imf.add(40);
            imf.add(5);
            imf.add(50);
            System.out.println("removed value from pq : " + imf.remove());
            System.out.println("removed value from pq : " + imf.remove());
         */

        int[][] lst = { { 1, 2, 6 }, { 3, 4, 5, 10 }, { 8, 19 } };
        new MergeKSortedList_06(lst);

        /*
          for (int i = 0; i < 3; i++) {
             for (int j = 0; j < 3; j++) {
                 for (int x = 0; x < 3; x++) {
                    System.out.println("for {i,j,k} : " + i + " " + j + " " + x);
                    new GuideWireOA(i, j, x); // wrong solution
                }
            }
        }*/

        HashMap<String, String> listOfEmployee = new HashMap<>();
        listOfEmployee.put("A", "C");
        listOfEmployee.put("B", "C");
        listOfEmployee.put("C", "F");
        listOfEmployee.put("D", "E");
        listOfEmployee.put("E", "F");
        listOfEmployee.put("F", "F");
        new FindCEO_08(listOfEmployee);

        HashMap<String, String> itinerary = new HashMap<>();
        itinerary.put("chennai", "Bangalore");
        itinerary.put("Bombay", "Delhi");
        itinerary.put("Goa", "chennai");
        itinerary.put("Delhi", "Goa");
        new FindItineraryFromTickets_09(itinerary);

        int[] arr14 = { 9, 7, 5, 3 };
        int divisibleBY = 6;
        new CheckEveryPairIsDivisibleByK_10(arr14, divisibleBY);

        int[] arr15 = { 77, 22, 56, 11, 45, 35, 78, 29, 23, 55 };
        int divisibleBYK = 10;
        new CheckEveryPairIsDivisibleByK_10(arr15, divisibleBYK);

        int[] arr16 = { 2, 5, 5, 6, 3, 2, 3, 2, 4, 5, 2, 2, 2, 2, 3, 6 };
        int windowSize = 4;
        new DistinctElementInWindowK_11(arr16, windowSize);
    }
}
