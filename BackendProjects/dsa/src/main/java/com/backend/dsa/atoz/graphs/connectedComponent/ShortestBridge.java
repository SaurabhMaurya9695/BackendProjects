package com.backend.dsa.atoz.graphs.connectedComponent;

import java.util.ArrayDeque;

public class ShortestBridge {

    private static final int[][] dirs = {
            { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

    public static void main(String[] args) {
        int[][] arr = {
                { 1, 1, 1, 1, 1 }, { 1, 0, 0, 0, 1 }, { 1, 0, 1, 0, 1 }, { 1, 0, 0, 0, 1 }, { 1, 1, 1, 1, 1 } };

        System.out.println(shortestBridge(arr)); // 1
    }

    public static int shortestBridge(int[][] grid) {
        int n = grid.length;
        int m = grid[0].length;
        boolean[][] vis = new boolean[n][m];
        ArrayDeque<int[]> dq = new ArrayDeque<>();

        boolean firstIslandFound = false;
        // Step 1:
        // Find the first island
        for (int i = 0; i < n; i++) {
            if (firstIslandFound) {
                break;
            }
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == 1) {
                    // Step 2:
                    // Find the complete connected component
                    // and add all its cells to the queue
                    dfs(i, j, n, m, grid, vis, dq);
                    firstIslandFound = true;
                    break;
                }
            }
        }

        // Step 3:
        // Run multi-source BFS from the first island
        int distance = 0;
        while (!dq.isEmpty()) {
            int size = dq.size();
            for (int i = 0; i < size; i++) {
                int[] current = dq.poll();
                int x = current[0];
                int y = current[1];
                for (int[] dir : dirs) {
                    int newX = x + dir[0];
                    int newY = y + dir[1];
                    if (newX >= 0 && newX < n && newY >= 0 && newY < m && !vis[newX][newY]) {
                        // We found the second island
                        if (grid[newX][newY] == 1) {
                            return distance;
                        }

                        // Water cell
                        // Expand BFS through it
                        vis[newX][newY] = true;
                        dq.offer(new int[] {
                                newX, newY });
                    }
                }
            }
            distance++;
        }
        return -1;
    }

    private static void dfs(int i, int j, int n, int m, int[][] grid, boolean[][] vis, ArrayDeque<int[]> dq) {
        vis[i][j] = true;
        // Add every cell of the first island
        // to the queue
        dq.offer(new int[] {
                i, j });
        for (int[] dir : dirs) {
            int newI = i + dir[0];
            int newJ = j + dir[1];
            if (newI >= 0 && newI < n && newJ >= 0 && newJ < m && !vis[newI][newJ] && grid[newI][newJ] == 1) {
                dfs(newI, newJ, n, m, grid, vis, dq);
            }
        }
    }
}