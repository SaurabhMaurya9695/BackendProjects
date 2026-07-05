package com.backend.dsa.atoz.graphs.connectedComponent;

public class NumberOfEnclaves {

    public static void main(String[] args) {
        int[][] grid = { { 0, 0, 0, 0 }, { 1, 0, 1, 0 }, { 0, 1, 1, 0 }, { 0, 0, 0, 0 } };
        System.out.println(numEnclaves(grid));
    }

    private static final int[][] dirs = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

    private static void dfs(int i, int j, int n, int m, int[][] grid, boolean[][] visited) {
        visited[i][j] = true;
        grid[i][j] = 0;
        for (int k = 0; k < 4; k++) {
            int row = i + dirs[k][0];
            int col = j + dirs[k][1];
            if (row >= 0 && row < n && col >= 0 && col < m && grid[row][col] == 1 && !visited[row][col]) {
                dfs(row, col, n, m, grid, visited);
            }
        }
    }

    public static int numEnclaves(int[][] grid) {
        int n = grid.length;
        int m = grid[0].length;

        boolean[][] visited = new boolean[n + 1][m + 1];
        int cnt = 0;
        // lets start from boundary and make all the ones to zero which are connected from boundary
        // first row
        for (int j = 0; j < m; j++) {
            if (grid[0][j] == 1 && !visited[0][j]) {
                dfs(0, j, n, m, grid, visited);
            }
        }

        // first column
        for (int i = 0; i < n; i++) {
            if (grid[i][0] == 1 && !visited[i][0]) {
                dfs(i, 0, n, m, grid, visited);
            }
        }

        // last row
        for (int j = 0; j < m; j++) {
            if (grid[n - 1][j] == 1 && !visited[n - 1][j]) {
                dfs(n - 1, j, n, m, grid, visited);
            }
        }

        // last column
        for (int i = 0; i < n; i++) {
            if (grid[i][m - 1] == 1 && !visited[i][m - 1]) {
                dfs(i, m - 1, n, m, grid, visited);
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }

        // now we remove all the component which starts from boundary
        // now find the first one and get the connected component
        int ans = 0;

        // all boundary-connected land is already removed
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == 1) {
                    ans += countComponent(i, j, n, m, grid);
                }
            }
        }

        return ans;
    }

    private static int countComponent(int i, int j, int n, int m, int[][] grid) {
        if (i < 0 || i >= n || j < 0 || j >= m || grid[i][j] == 0) {
            return 0;
        }

        grid[i][j] = 0;
        int count = 1;
        for (int k = 0; k < 4; k++) {
            int row = i + dirs[k][0];
            int col = j + dirs[k][1];
            count += countComponent(row, col, n, m, grid);
        }

        return count;
    }
}
