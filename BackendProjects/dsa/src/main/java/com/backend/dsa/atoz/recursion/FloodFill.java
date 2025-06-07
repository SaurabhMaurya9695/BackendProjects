package com.backend.dsa.atoz.recursion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FloodFill {

    public FloodFill(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        int[][] vis = new int[rows][cols];
        List<String> paths = floodFill(matrix, 0, 0, rows, cols, vis);

        System.out.print("[");
        for (String path : paths) {
            System.out.print(path + ",");
        }
        System.out.print("]\n");
    }

    private List<String> floodFill(int[][] matrix, int i, int j, int n, int m, int[][] vis) {
        // Boundary and visited check
        if (i < 0 || j < 0 || i >= n || j >= m || vis[i][j] == 1) {
            return new ArrayList<>();
        }

        // Destination check (bottom-right corner)
        if (i == n - 1 && j == m - 1) {
            List<String> base = new ArrayList<>();
            base.add("");
            return base;
        }

        vis[i][j] = 1;

        List<String> ans = new ArrayList<>();

        // Move Right
        List<String> right = floodFill(matrix, i, j + 1, n, m, vis);
        for (String path : right) {
            ans.add("R" + path);
        }

        // Move Down
        List<String> down = floodFill(matrix, i + 1, j, n, m, vis);
        for (String path : down) {
            ans.add("D" + path);
        }

        // Move Left
        List<String> left = floodFill(matrix, i, j - 1, n, m, vis);
        for (String path : left) {
            ans.add("L" + path);
        }

        // Move Up
        List<String> up = floodFill(matrix, i - 1, j, n, m, vis);
        for (String path : up) {
            ans.add("U" + path);
        }

        vis[i][j] = 0; // Backtrack

        return ans;
    }
}
