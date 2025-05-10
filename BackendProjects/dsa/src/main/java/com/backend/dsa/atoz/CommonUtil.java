package com.backend.dsa.atoz;

public class CommonUtil {

    public static void printArray(int[] array) {
        for (int value : array) {
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
}
