package com.backend.dsa.atoz.dynamicProgramming.jumpGame;

public class JumpGameIV {

    public static int minJumps(int[] arr) {
        int n = arr.length;
        int src = 0;
        int dest = n - 1;
        boolean[] visited = new boolean[n];
        return solve(arr, n, src, dest, visited);
    }

    private static int solve(int[] arr, int n, int src, int dest, boolean[] visited) {

        if (src == dest) {
            return 0; // no jump required
        }

        visited[src] = true;

        // now from here I can go to i + 1, i - 1 and i != j wher arr[i] == arr[j]

        int ans = Integer.MAX_VALUE;
        // i + 1;
        if (src + 1 < n) {
            if (!visited[src + 1]) {
                int RightSide = solve(arr, n, src + 1, dest, visited);
                if (RightSide != Integer.MAX_VALUE) {
                    ans = Math.min(ans, 1 + RightSide);
                }
            }
        }

        if (src - 1 >= 0) {
            if (!visited[src - 1]) {
                int LeftSide = solve(arr, n, src - 1, dest, visited);
                if (LeftSide != Integer.MAX_VALUE) {
                    ans = Math.min(ans, 1 + LeftSide);
                }
            }
        }

        for (int j = 0; j < n; j++) {
            if (arr[src] == arr[j] && j != src && !visited[j]) {
                int LoopSide = solve(arr, n, j, dest, visited);
                if (LoopSide != Integer.MAX_VALUE) {
                    ans = Math.min(ans, 1 + LoopSide);
                }
            }
        }

        visited[src] = false;

        return ans;
    }
}
