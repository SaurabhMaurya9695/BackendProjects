package com.backend.dsa.atoz.graphs.connectedComponent;

import java.util.LinkedList;
import java.util.Queue;

public class AsFarFromLandAsPossible {

    private static int[][] dirs = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

    public static void main(String[] args) {
        int[][] arr = { { 1, 0, 1 }, { 0, 0, 0 }, { 1, 0, 1 } };
        System.out.println(maxDistance(arr));
    }

    // This code is wrong because we are just checking the nbrs, not checking the all the distanced 0
    // it means we should think for bfs instead of dfs
    //    public static int maxDistance(int[][] grid) {
    //        int n = grid.length;
    //        int m = grid[0].length;
    //
    //        boolean[][] vis = new boolean[n + 1][m + 1];
    //        int ans = Integer.MIN_VALUE;
    //        // choose every 1 and try to find the maximum distance
    //        for (int i = 0; i < n; i++) {
    //            for (int j = 0; j < m; j++) {
    //                if (grid[i][j] == 1 && !vis[i][j]) {
    //                    ans = Math.max(dfs(i, j, n, m, vis, grid), ans);
    //                }
    //            }
    //        }
    //        return ans;
    //    }
    //
    //    private static int dfs(int i, int j, int n, int m, boolean[][] vis, int[][] grid) {
    //        int res = 0;
    //        vis[i][j] = true;
    //        // go in all four directions
    //        for (int k = 0; k < 4; k++) {
    //            int newI = i + dirs[k][0];
    //            int newJ = j + dirs[k][1];
    //            if (newI >= 0 && newI < n && newJ >= 0 && newJ < m && grid[newI][newJ] == 0) {
    //                res = Math.max(Math.abs(newI - i) + Math.abs(newJ - j), res);
    //            }
    //        }
    //
    //        return res;
    //    }

    public static int maxDistance(int[][] grid) {
        int n = grid.length;
        int m = grid[0].length;
        Queue<int[]> queue = new LinkedList<>();
        boolean[][] vis = new boolean[n][m];

        // Add all land cells to the queue
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == 1 && !vis[i][j]) {
                    queue.offer(new int[] { i, j }); // add all the possible location of 1
                    vis[i][j] = true;
                }
            }
        }

        // All water or all land
        if (queue.isEmpty() || queue.size() == n * m) {
            return -1;
        }

        int distance = -1;
        while (!queue.isEmpty()) {
            int size = queue.size(); // expand by radius of size
            distance++;
            for (int k = 0; k < size; k++) {
                int[] current = queue.poll();
                int i = current[0];
                int j = current[1];
                for (int[] dir : dirs) {
                    int newI = i + dir[0];
                    int newJ = j + dir[1];
                    if (newI >= 0 && newI < n && newJ >= 0 && newJ < m && !vis[newI][newJ] && grid[newI][newJ] == 0) {
                        vis[newI][newJ] = true;
                        queue.offer(new int[] { newI, newJ });
                    }
                }
            }
        }
        return distance;
    }
}
