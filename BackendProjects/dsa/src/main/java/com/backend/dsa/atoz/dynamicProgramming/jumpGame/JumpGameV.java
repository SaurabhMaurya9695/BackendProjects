package com.backend.dsa.atoz.dynamicProgramming.jumpGame;

public class JumpGameV {

    public static void main(String[] args) {
        int[] arr = { 6, 4, 14, 6, 8, 13, 9, 7, 10, 6, 12 };
        int d = 2;
        System.out.println(maxJumps(arr, d));
    }

    public static int maxJumps(int[] arr, int d) {
        int n = arr.length;
        int[] memo = new int[n];
        int maxJumps = 0;

        // Try starting from each index
        for (int i = 0; i < n; i++) {
            maxJumps = Math.max(maxJumps, dfs(arr, i, d, memo));
        }

        return maxJumps;
    }

    private static int dfs(int[] arr, int index, int d, int[] memo) {
        // If already calculated, return cached result
        if (memo[index] != 0) {
            return memo[index];
        }

        int maxLength = 1; // At minimum, we visit the current index

        // Try jumping left
        for (int i = index - 1; i >= 0 && i >= index - d; i--) {
            if (arr[i] >= arr[index]) {
                break; // Can't jump if intermediate value is >= current
            }
            maxLength = Math.max(maxLength, 1 + dfs(arr, i, d, memo));
        }

        // Try jumping right
        for (int i = index + 1; i < arr.length && i <= index + d; i++) {
            if (arr[i] >= arr[index]) {
                break; // Can't jump if intermediate value is >= current
            }
            maxLength = Math.max(maxLength, 1 + dfs(arr, i, d, memo));
        }

        memo[index] = maxLength;
        return maxLength;
    }
}
