package com.backend.dsa.atoz.arrays;

import com.backend.dsa.atoz.CommonUtil;

public class RotateArrayByD {

    public RotateArrayByD(int[] arr, int d) {
        //        rotateArray(arr, d);
        rotateArrayByReversing(arr, d);
    }

    private void rotateArrayByReversing(int[] arr, int d) {
        d = d % arr.length;
        if (d < 0) {
            d += arr.length;
        }

        CommonUtil.reverse(arr, 0, arr.length - d - 1);
        CommonUtil.reverse(arr, arr.length - d, arr.length - 1);
        CommonUtil.reverse(arr, 0, arr.length - 1);
        System.out.print("Rotating array after " + d + " times : ");
    }

    public void rotateArray(int[] nums, int d) {
        d = d % nums.length;
        int[] temp = new int[d + 1];
        for (int i = 0; i <= d; i++) {
            temp[i] = nums[i];
        }

        int i;
        for (i = d + 1; i < nums.length; i++) {
            nums[i - d - 1] = nums[i];
        }

        int k = 0;
        int j = i - d - 1;
        while (j < nums.length) {
            nums[j] = temp[k];
            j++;
            k++;
        }
    }
}
