package com.backend.dsa.atoz.arrays;

public class SecondLargetsElement {

    public SecondLargetsElement(int[] arr) {
        secondLargetsElement(arr);
    }

    private void secondLargetsElement(int[] arr) {
        int maxi = Integer.MIN_VALUE;
        int ans = Integer.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > maxi) {
                ans = maxi;
                maxi = arr[i];
            } else if (arr[i] > ans && arr[i] != maxi) {
                ans = arr[i];
            }
        }

        System.out.println("Second largest element is : " + ans);
    }
}
