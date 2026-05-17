package com.backend.dsa.atoz.dynamicProgramming.jumpGame;

public class JumpGameIII {

    public static void main(String[] args) {
        int startIdx = 0;
        int[] arr = { 4, 2, 3, 0, 3, 2, 1 };
        System.out.println(canReach(arr, startIdx));

        int startIdx1 = 2;
        int[] arr1 = { 3, 0, 2, 1, 2 };
        System.out.println(canReach(arr1, startIdx1));
    }

    private static boolean canReach(int[] arr, int startIdx) {
        boolean[] vis = new boolean[arr.length];
        return canReach(startIdx, arr, arr.length, vis);
    }

    private static boolean canReach(int startIdx, int[] arr, int n, boolean[] vis) {
        if (arr[startIdx] == 0) {
            return true;
        }

        vis[startIdx] = true;
        boolean canReachByX = false;
        if (startIdx + arr[startIdx] < n && !vis[startIdx + arr[startIdx]]) {
            canReachByX = canReach(startIdx + arr[startIdx], arr, n, vis);
        }

        boolean canReachByY = false;
        if (startIdx - arr[startIdx] >= 0 && !vis[startIdx - arr[startIdx]]) {
            canReachByY = canReach(startIdx - arr[startIdx], arr, n, vis);
        }

        return canReachByY || canReachByX;
    }
}
