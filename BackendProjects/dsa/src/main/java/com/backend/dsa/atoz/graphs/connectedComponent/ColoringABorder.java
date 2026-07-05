package com.backend.dsa.atoz.graphs.connectedComponent;

public class ColoringABorder {

    private int m, n, origColor;
    private int[][] grid;
    private int[][] dirs = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

    public int[][] colorBorder(int[][] grid, int row, int col, int color) {
        this.grid = grid;
        this.m = grid.length;
        this.n = grid[0].length;
        this.origColor = grid[row][col];

        // Phase 1: DFS — mark all component cells as -origColor
        dfs(row, col);

        // Phase 2: Classify — determine borders WITHOUT modifying the grid yet
        boolean[][] isBorder = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == -origColor) {
                    isBorder[i][j] = checkBorder(i, j);
                }
            }
        }

        // Phase 3: Apply — now safe to modify since classification is done
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == -origColor) {
                    grid[i][j] = isBorder[i][j] ? color : origColor;
                }
            }
        }

        return grid;
    }

    private void dfs(int r, int c) {
        grid[r][c] = -origColor;
        for (int[] d : dirs) {
            int nr = r + d[0], nc = c + d[1];
            if (nr >= 0 && nr < m && nc >= 0 && nc < n && grid[nr][nc] == origColor) {
                dfs(nr, nc);
            }
        }
    }

    private boolean checkBorder(int r, int c) {
        if (r == 0 || r == m - 1 || c == 0 || c == n - 1) {
            return true;
        }
        for (int[] d : dirs) {
            int nr = r + d[0], nc = c + d[1];
            // At this point, grid still has only -origColor (component) or original values
            // So any neighbor that isn't -origColor is outside the component
            if (grid[nr][nc] != -origColor) {
                return true;
            }
        }
        return false;
    }
}