package com.backend.dsa.atoz.arrays;

import com.backend.dsa.atoz.CommonUtil;

public class MoveZerosToEnd {

    public MoveZerosToEnd(int[] arr) {
        moveZerosToEnd(arr);
    }

    private void moveZerosToEnd(int[] arr) {
        // same as quick sort , here pivot element is zero
        int i = 0;
        int j = 0;
        while (i < arr.length) {
            if (arr[i] == 0) {
                i++;
            } else {
                CommonUtil.swap(arr, i, j);
                i++;
                j++;
            }
        }

        System.out.print("After Moving Zeros to End : ");
    }
}
