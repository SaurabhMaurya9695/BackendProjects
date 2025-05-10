package com.backend.dsa.atoz.arrays;

public class MaxConsecutiveOnes {

    public MaxConsecutiveOnes(int[] arr) {
        maxConsecutiveOnes(arr);
    }

    private void maxConsecutiveOnes(int[] arr) {
        // 1 , 1, 0 ,0 ,0 ,2 ,2 ,1 , 1, 1, 1
        int cnt = 0;
        int ans = 1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 1) {
                cnt++;
                ans = Math.max(ans, cnt);
            } else {
                cnt = 0;
            }
        }
        System.out.println("Max no of ones in an array is : " + cnt);
    }
}
