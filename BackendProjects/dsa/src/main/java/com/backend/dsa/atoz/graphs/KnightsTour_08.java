package com.backend.dsa.atoz.graphs;

public class KnightsTour_08 {

    public static void main(String[] args) {
        int[][] grid = {
                { 0, 11, 16, 5, 2 }, { 17, 4, 19, 10, 1 }, { 12, 1, 8, 21, }, { 3, 18, 23, 14, }, { 24, 13, 2, 7, 2 } };
        System.out.println(checkValidGrid(grid));
    }

    static int[][] moves = {
            { -2, +1 }, { -2, -1 }, { +2, +1 }, { +2, -1 }, { -1, +2 }, { +1, +2 }, { -1, -2 }, { +1, -2 } };

    private static boolean checkValidGrid(int[][] grid) {
        int n = grid.length;
        // Knight tour must start from 0,0 with value 0
        if (grid[0][0] != 0) {
            return false;
        }

        boolean[][] visited = new boolean[n][n];
        return solve(0, 0, n, grid, visited, 0);
    }

    private static boolean solve(int row, int col, int n, int[][] grid, boolean[][] visited, int cnt) {

        // boundary + visited check
        if (row < 0 || row >= n || col < 0 || col >= n || visited[row][col]) {
            return false;
        }

        // current cell should contain current move number
        if (grid[row][col] != cnt) {
            return false;
        }

        // all cells visited
        if (cnt == n * n - 1) {
            return true;
        }

        visited[row][col] = true;

        for (int[] move : moves) {

            int newRow = row + move[0];
            int newCol = col + move[1];

            if (solve(newRow, newCol, n, grid, visited, cnt + 1)) {
                return true;
            }
        }

        visited[row][col] = false;

        return false;
    }

    // horse can move from (i, j) ->
    // (i - 2, j + 1)
    // (i - 2, j - 1)
    //
    // (i + 2, j + 1)
    // (i + 2, j - 1)
    //
    // (i - 1, j + 2)
    // (i + 1, j + 2)
    //
    // (i - 1, j - 2)
    // (i + 1, j - 2)
}
