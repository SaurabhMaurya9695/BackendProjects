package com.backend.dsa.atoz.graphs.zeroOneBfs;

import java.util.ArrayDeque;
import java.util.Deque;

public class MinimumObstacleRemovalToReachCorner {

    public static void main(String[] args) {
        int[][] grid = { { 0, 1, 1 }, { 1, 1, 0 }, { 1, 1, 0 } };
        System.out.println(minimumObstacles(grid));
    }

    private static final int[] X4 = { 1, -1, 0, 0 };
    private static final int[] Y4 = { 0, 0, -1, 1 };

    public static int minimumObstacles(int[][] grid) {
        int n = grid.length;
        int m = grid[0].length;

        Deque<int[]> dq = new ArrayDeque<>();
        dq.addFirst(new int[] { 0, 0, 0 });
        boolean[][] vis = new boolean[n + 1][m + 1];
        int obst = 0;
        while (!dq.isEmpty()) {
            int[] top = dq.pollFirst();
            int wt = top[0];
            int u = top[1];
            int v = top[2];

            if (vis[u][v]) {
                continue;
            }
            vis[u][v] = true;

            if (u == n - 1 && v == m - 1) {
                return wt;
            }

            for (int i = 0; i < 4; i++) {
                int newU = u + X4[i];
                int newV = v + Y4[i];

                if (newU < 0 || newU >= n || newV < 0 || newV >= m) {
                    continue;
                }

                if (vis[newU][newV]) {
                    continue;
                }

                int newCost = wt + grid[newU][newV];
                if (grid[newU][newV] == 1) {
                    dq.addLast(new int[] { newCost, newU, newV });
                } else {
                    dq.addFirst(new int[] { newCost, newU, newV });
                }
            }
        }
        return obst;
    }
}
