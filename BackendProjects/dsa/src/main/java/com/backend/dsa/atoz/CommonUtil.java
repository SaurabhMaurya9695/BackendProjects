package com.backend.dsa.atoz;

import java.util.List;

public class CommonUtil {

    public static void printArray(int[] array) {
        for (int value : array) {
            System.out.print(value + " ");
        }
        System.out.println();
    }

    public static void printArray(List<Integer> list) {
        for (int value : list) {
            System.out.print(value + " ");
        }
        System.out.println();
    }

    public static void swap(int[] arr, int l, int r) {
        int temp = arr[l];
        arr[l] = arr[r];
        arr[r] = temp;
    }

    public static int compare(int[] arr, int l, int r) {
        if (arr[l] > arr[r]) {
            return -1; // swap req
        } else if (arr[l] < arr[r]) {
            return +1; // swap not req
        } else {
            return 0; // nothing req
        }
    }

    public static void reverse(int[] arr, int i, int j) {
        int li = i;
        int ri = j;

        while (li < ri) {
            swap(arr, li, ri);
            li++;
            ri--;
        }
    }

    public static int back(int[] arr) {
        if (arr.length > 0) {
            return arr[arr.length - 1];
        }
        return 0;
    }

    public static int back(List<Integer> list) {
        if (!list.isEmpty()) {
            return list.get(list.size() - 1);
        }
        return 0;
    }
}