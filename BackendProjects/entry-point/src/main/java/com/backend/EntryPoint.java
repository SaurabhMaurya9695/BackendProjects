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
import com.backend.dsa.atoz.hashmapAndHeaps.CountAllSubarrayWithKSum_13;
import com.backend.dsa.atoz.hashmapAndHeaps.CountNumberOfNiceSubarrays_21;
import com.backend.dsa.atoz.hashmapAndHeaps.CountOfEquivalentSubarrays_20;
import com.backend.dsa.atoz.hashmapAndHeaps.CountSubstringsWithKDistinctCharacters_19;
import com.backend.dsa.atoz.hashmapAndHeaps.DistinctElementInWindowK_11;
import com.backend.dsa.atoz.hashmapAndHeaps.FIndAllAnagaramInString_25;
import com.backend.dsa.atoz.hashmapAndHeaps.FindCEO_08;
import com.backend.dsa.atoz.hashmapAndHeaps.FindItineraryFromTickets_09;
import com.backend.dsa.atoz.hashmapAndHeaps.FoundHighOccuringChar_01;
import com.backend.dsa.atoz.hashmapAndHeaps.GroupAnagrams_26;
import com.backend.dsa.atoz.hashmapAndHeaps.KAnagram_24;
import com.backend.dsa.atoz.hashmapAndHeaps.KLargestElements_03;
import com.backend.dsa.atoz.hashmapAndHeaps.LargestSubarrayWithSumZero_12;
import com.backend.dsa.atoz.hashmapAndHeaps.LengthOfLargestSubarrayWithContiguousElements_14;
import com.backend.dsa.atoz.hashmapAndHeaps.LongestConsecutiveSequence_02;
import com.backend.dsa.atoz.hashmapAndHeaps.LongestSubstringWithKUniques_18;
import com.backend.dsa.atoz.hashmapAndHeaps.LongestSubstringWithoutRepeatingCharacters_16;
import com.backend.dsa.atoz.hashmapAndHeaps.MaxConsecutiveOnes_2_23;
import com.backend.dsa.atoz.hashmapAndHeaps.MaximumConsecutiveOnes_1_22;
import com.backend.dsa.atoz.hashmapAndHeaps.MergeKSortedList_06;
import com.backend.dsa.atoz.hashmapAndHeaps.MinimumWindowSubstring01_15;
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

        int[] arr17 = { 15, -2, 2, -8, 1, 7, 10, 23 };
        int[] arr18 = { 2, 10, 4, -14, -2 };
        int[] arr19 = { -42, 12, 20, 15, 31, -4, 0, 15 };
        new LargestSubarrayWithSumZero_12(arr17);
        new LargestSubarrayWithSumZero_12(arr18);
        new LargestSubarrayWithSumZero_12(arr19);

        int[] arr20 = { 2, 8, -3, -5, 2, 4, 6, 1, 2, 1, -3, 4, -1, 3 };
        int target = 0;
        new CountAllSubarrayWithKSum_13(arr20, target);

        int[] arr21 = { 1, 4, 3, 1, 1, 2 };
        int target1 = 5;
        new CountAllSubarrayWithKSum_13(arr21, target1);

        int[] arr22 = { 9, 2, 7, 5, 6, 23, 24, 22, 23, 19, 17, 16, 18, 39 };
        new LengthOfLargestSubarrayWithContiguousElements_14(arr22);

        int[] arr23 = { 1, 56, 58, 57, 90, 92, 94, 93, 91, 45 };
        new LengthOfLargestSubarrayWithContiguousElements_14(arr23);

        String originalString1 = "dbaecbbabdcaafbddcabgba";
        String matchedString1 = "abbcdc";
        new MinimumWindowSubstring01_15(originalString1, matchedString1);

        String originalString2 = "adobecodebanc";
        String matchedString2 = "abc";
        new MinimumWindowSubstring01_15(originalString2, matchedString2);

        String originalString3 = "a";
        String matchedString3 = "aa";
        new MinimumWindowSubstring01_15(originalString3, matchedString3);

        String originalString4 = "abcabcbb";
        new LongestSubstringWithoutRepeatingCharacters_16(originalString4);

        String originalString5 = "pwwkew";
        new LongestSubstringWithoutRepeatingCharacters_16(originalString5);

        String originalString6 = "aabacbebebe";
        int uniqueCharWeWant = 3;
        new LongestSubstringWithKUniques_18(originalString6, uniqueCharWeWant);

        String originalString7 = "aabaaab";
        int uniqueCharWeWant1 = 2;
        new LongestSubstringWithKUniques_18(originalString7, uniqueCharWeWant1);

        String originalString8 = "aaaa";
        int uniqueCharWeWant2 = 2;
        new LongestSubstringWithKUniques_18(originalString8, uniqueCharWeWant2);

        String originalString9 = "aba";
        int uniqueCharWeWant3 = 2;
        new CountSubstringsWithKDistinctCharacters_19(originalString9, uniqueCharWeWant3);

        String originalString10 = "aa";
        int uniqueCharWeWant4 = 1;
        new CountSubstringsWithKDistinctCharacters_19(originalString10, uniqueCharWeWant4);

        int[] arr24 = { 2, 5, 3, 5, 2, 4, 1, 3, 1, 4 };
        new CountOfEquivalentSubarrays_20(arr24);

        int[] arr25 = { 1, 3, 1, 2, 2 };
        new CountOfEquivalentSubarrays_20(arr25);

        int[] arr26 = { 2, 2, 2, 1, 2, 2, 1, 2, 2, 2 };
        int k1 = 2;
        new CountNumberOfNiceSubarrays_21(arr26, k1);

        int[] arr27 = { 1, 1, 2, 1, 1 };
        int k2 = 3;
        new CountNumberOfNiceSubarrays_21(arr27, k2);

        int[] arr28 = { 1, 1, 0, 1, 0, 0, 1, 1, 0, 1, 0, 1, 1 };
        int maxFlipAllowed = 2;
        new MaximumConsecutiveOnes_1_22(arr28, maxFlipAllowed);

        int[] arr29 = { 1, 0, 1 };
        int maxFlipAllowed1 = 1;
        new MaximumConsecutiveOnes_1_22(arr29, maxFlipAllowed1);

        int[] arr30 = { 1, 0, 0, 1, 0, 1, 0, 1 };
        int maxFlipAllowed2 = 2;
        new MaximumConsecutiveOnes_1_22(arr30, maxFlipAllowed2);

        int[] arr31 = { 1, 1 };
        int maxFlipAllowed3 = 2;
        new MaximumConsecutiveOnes_1_22(arr31, maxFlipAllowed3);

        int[] arr32 = { 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1 };
        int AtMostFlipAllowed4 = 3;
        new MaxConsecutiveOnes_2_23(arr32, AtMostFlipAllowed4);

        String s1 = "geeks";
        String s2 = "eggkf";
        int swaps = 1;
        new KAnagram_24(s1, s2, swaps);

        String s11 = "fodr";
        String s22 = "gork";
        int swaps1 = 2;
        new KAnagram_24(s11, s22, swaps1);

        String sr = "cbaebabacd";
        String pattern = "abc";
        new FIndAllAnagaramInString_25(sr, pattern);

        String[] str = { "eat", "tea", "tan", "ate", "nat", "bat" };
        new GroupAnagrams_26(str);
    }
}
