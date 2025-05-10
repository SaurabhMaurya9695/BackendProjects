package com.backend.dsa.atoz.arrays;

public class RemoveDuplicatesFromSortedArray {

    public RemoveDuplicatesFromSortedArray(int[] arr) {
        solve(arr);
    }

    private void solve(int[] arr) {
        int i = 0;
        int j = 0;
        while (j < arr.length) {
            if (arr[i] == arr[j]) {
                j++;
            } else if (arr[i] != arr[j]) {
                arr[i + 1] = arr[j];
                i++;
                j++;
            }
        }
        System.out.println("No of Unique element in an array is : " + (i + 1));
    }
}
