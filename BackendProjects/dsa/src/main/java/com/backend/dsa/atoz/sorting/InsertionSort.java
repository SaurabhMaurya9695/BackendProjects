package com.backend.dsa.atoz.sorting;

import com.backend.dsa.atoz.CommonUtil;

public class InsertionSort {

    public InsertionSort(int[] arr) {
        insertionSort(arr);
    }

    // 2,9,5,1,3
    // hum mante hai ki first element is sorted hai, will start from second element
    // no iteration yet : [2] [9,5,1,3]
    // 1st iteration with 9 : [2 ,9] [5,1,3]
    // 2nd iteration with 5 : [2,5,9] [1,3]
    // 3rd iteration with 1 : [1,2,5,9] [3]
    // 4th iteration with 3 : [1,2,3,5,9]

    public void insertionSort(int[] array) {
        for (int i = 1; i < array.length; i++) {
            for (int j = i - 1; j >= 0; j--) {
                if (CommonUtil.compare(array, j, j + 1) == -1) {
                    // comparing j with j + 1 , if j > j + 1 then swap
                    CommonUtil.swap(array, j, j + 1);
                } else {
                    break;
                }
            }
        }
    }
}
