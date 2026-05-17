package com.backend.dsa.atoz.graphs;

public class CountNumberOfIsland_05 {

    int cnt = 0;
    // 4 directions
    int[] x4 = { 1, -1, 0, 0 };
    int[] y4 = { 0, 0, -1, 1 };

    public int numIslands(char[][] grid) {
        int n = grid.length;
        int m = grid[0].length;

        // visited array
        boolean[][] visited = new boolean[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                // if land cell and not visited
                if (grid[i][j] == '1' && !visited[i][j]) {
                    cnt++;
                    // start DFS
                    dfs(i, j, visited, grid, n, m);
                }
            }
        }
        return cnt;
    }

    private void dfs(int r, int c, boolean[][] visited, char[][] grid, int n, int m) {
        // invalid condition
        if (!shouldGo(r, c, visited, grid, n, m)) {
            return;
        }

        // mark visited
        visited[r][c] = true;

        // explore all 4 directions
        for (int i = 0; i < 4; i++) {
            int nr = r + x4[i];
            int nc = c + y4[i];
            dfs(nr, nc, visited, grid, n, m);
        }
    }

    private boolean shouldGo(int r, int c, boolean[][] visited, char[][] grid, int n, int m) {
        return r >= 0 && r < n && c >= 0 && c < m && grid[r][c] == '1' && !visited[r][c];
    }
}
