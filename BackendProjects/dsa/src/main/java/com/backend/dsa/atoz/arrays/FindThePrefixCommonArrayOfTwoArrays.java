package com.backend.dsa.atoz.arrays;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FindThePrefixCommonArrayOfTwoArrays {

    public static void main(String[] args) {
        int[] a = { 1, 3, 2, 4 };
        int[] b = { 3, 1, 2, 4 };
        System.out.println(Arrays.toString(findThePrefixCommonArray(a, b)));
    }

    public static int[] findThePrefixCommonArray(int[] A, int[] B) {
        int n = A.length;
        int[] result = new int[n];
        Set<Integer> setA = new HashSet<>();
        Set<Integer> setB = new HashSet<>();

        for (int i = 0; i < n; i++) {
            setA.add(A[i]);  // Add current element from A
            setB.add(B[i]);  // Add current element from B

            // Count intersection
            int count = 0;
            for (int num : setA) {
                if (setB.contains(num)) {
                    count++;
                }
            }
            result[i] = count;
        }

        return result;
    }
}
