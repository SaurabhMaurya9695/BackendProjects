package com.backend.dsa.atoz.recursion;

public class TargetSumSubset {

    public TargetSumSubset(int[] arr, int target) {
        System.out.print("[ ");
        tss(arr, 0, arr.length, target, "", 0);
        System.out.println("]");
    }

    private void tss(int[] arr, int idx, int n, int target, String s, int sum) {

        if (idx == n) {
            if (sum == target) {
                System.out.print(s + " ] ,");
                return;
            } else {
                return;
            }
        }

        // now we have two choices at every idx either to take or not

        tss(arr, idx + 1, n, target, s + arr[idx] + ", ", sum + arr[idx]);
        tss(arr, idx + 1, n, target, s, sum);
    }
}
