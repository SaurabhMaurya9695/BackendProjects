package com.backend.dsa.atoz.graphs.zeroOneBfs;

import java.util.ArrayDeque;
import java.util.Deque;

public class MinimumCostToMakeAtLeastOneValidPathInAGrid {

    public static void main(String[] args) {

        int[][] grid = {
                { 1, 1, 1, 1 },
                { 2, 2, 2, 2 },
                { 1, 1, 1, 1 },
                { 2, 2, 2, 2 }
        };

        System.out.println(minCost(grid));
    }

    private static final int[] X4 = { 1, -1, 0, 0 };
    private static final int[] Y4 = { 0, 0, -1, 1 };

    private static int minCost(int[][] grid) {
        int n = grid.length;
        int m = grid[0].length;

        Deque<int[]> dq = new ArrayDeque<>();

        // {cost, row, column}
        dq.addFirst(new int[] { 0, 0, 0 });
        boolean[][] vis = new boolean[n][m];
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

            // Try all 4 directions
            for (int dir = 0; dir < 4; dir++) {

                int newI = u + X4[dir];
                int newJ = v + Y4[dir];

                // Boundary check
                if (newI < 0 || newI >= n || newJ < 0 || newJ >= m) {
                    continue;
                }

                int moveDirection;
                if (dir == 3) {
                    moveDirection = 1; // right
                } else if (dir == 2) {
                    moveDirection = 2; // left
                } else if (dir == 0) {
                    moveDirection = 3; // down
                } else {
                    moveDirection = 4; // up
                }

                int movementCost = (grid[u][v] == moveDirection) ? 0 : 1;

                int newCost = wt + movementCost;

                if (movementCost == 0) {
                    dq.addFirst(new int[] { newCost, newI, newJ });
                } else {
                    dq.addLast(new int[] { newCost, newI, newJ });
                }
            }
        }

        return -1;
    }
}