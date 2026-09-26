package com.backend.dsa.atoz.graphs.topologicalSort;

import java.util.Scanner;

public class B_FoxAndTwoDots {

    private static final Scanner _scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int n = _scanner.nextInt();
        int m = _scanner.nextInt();
        Character[][] arr = new Character[n][m];
        for (int i = 0; i < n; i++) {
            String row = _scanner.next();
            for (int j = 0; j < m; j++) {
                arr[i][j] = row.charAt(j);
            }
        }

        // we need to explore from all locations (i , j) and find the cycle
        // graph is undirected
        boolean[][] vis = new boolean[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (!vis[i][j]) {
                    if (dfs(i, j, arr, vis, -1, -1)) {
                        System.out.println("Yes");
                        return;
                    }
                }
            }
        }

        System.out.println("No");
    }

    private static boolean dfs(int i, int j, Character[][] arr, boolean[][] vis, int pari, int parj) {
        vis[i][j] = true;

        // now from this pos we can go to all four directions
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        for (int[] dir : directions) {
            int nextI = i + dir[0];
            int nextJ = j + dir[1];

            // Bounds checking
            if (nextI < 0 || nextI >= arr.length || nextJ < 0 || nextJ >= arr[0].length) {
                continue;
            }

            // Check if same letter
            if (arr[i][j] != arr[nextI][nextJ]) {
                continue;
            }

            // If not visited, recurse with current cell as parent
            if (!vis[nextI][nextJ]) {
                if (dfs(nextI, nextJ, arr, vis, i, j)) {
                    return true;
                }
            }
            // If visited and not parent, cycle found!
            else if (nextI != pari || nextJ != parj) {
                return true;
            }
        }

        return false;
    }
}
