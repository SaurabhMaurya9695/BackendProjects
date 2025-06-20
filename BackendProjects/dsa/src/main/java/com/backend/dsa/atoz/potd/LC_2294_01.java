package com.backend.dsa.atoz.potd;

import com.backend.dsa.atoz.CommonUtil;


public class LC_2294_01 {

    public int partitionArray(int[] A, int k) {
        CommonUtil.sortArray(A);
        // 1 2 3 5 6
        int subsequence = 0;
        int mini = Integer.MAX_VALUE;
        int maxi = Integer.MIN_VALUE;
        for (int i = 0; i < A.length; i++) {
            mini = Math.min(mini, A[i]);
            maxi = Math.max(maxi, A[i]);
            if (maxi - mini > k) {
                subsequence++;
                mini = A[i];
                maxi = A[i];
            }
        }
        return subsequence + 1;
    }
}
