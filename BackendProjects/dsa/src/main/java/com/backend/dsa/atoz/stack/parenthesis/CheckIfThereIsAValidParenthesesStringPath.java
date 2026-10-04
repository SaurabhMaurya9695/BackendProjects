package com.backend.dsa.atoz.stack.parenthesis;

import java.util.ArrayDeque;
import java.util.Queue;

public class CheckIfThereIsAValidParenthesesStringPath {

    public static void main(String[] args) {
        char[][] grid = { { '(', '(', '(' }, { ')', '(', ')' }, { '(', '(', ')' }, { '(', '(', ')' } };
        System.out.println(hasValidPath(grid));
    }

    private static final int[][] dir = { { 0, 1 }, { 1, 0 } };

    public static boolean hasValidPath(char[][] grid) {
        int n = grid.length;
        int m = grid[0].length;

        int open = 0;
        int close = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == '(') {
                    open++;
                } else {
                    close++;
                }
            }
        }

        if ((n + m - 1) % 2 != 0 || grid[0][0] != '(' || grid[n - 1][m - 1] != ')') {
            return false;
        }

        int needOpen = 1;
        int needClose = 0;

        Queue<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[] { needOpen, needClose, 0, 0 });

        boolean[][][] vis = new boolean[n][m][n + m];
        vis[0][0][needOpen] = true;

        while (!queue.isEmpty()) {
            int[] peek = queue.poll();

            int curOpen = peek[0];
            int curClose = peek[1];
            int src = peek[2];
            int col = peek[3];

            if (src == n - 1 && col == m - 1) {
                if (curOpen == curClose) {
                    return true;
                }
                continue;
            }

            for (int[] d : dir) {
                int nx = src + d[0];
                int ny = col + d[1];

                if (nx >= 0 && nx < n && ny >= 0 && ny < m) {
                    int nextOpen = curOpen;
                    int nextClose = curClose;

                    if (grid[nx][ny] == '(') {
                        nextOpen++;
                    } else {
                        nextClose++;
                    }

                    int balance = nextOpen - nextClose;
                    int remaining = (n - 1 - nx) + (m - 1 - ny);

                    if (balance < 0 || balance > remaining) {
                        continue;
                    }

                    if (!vis[nx][ny][balance]) {
                        vis[nx][ny][balance] = true;
                        queue.offer(new int[] { nextOpen, nextClose, nx, ny });
                    }
                }
            }
        }

        return false;
    }
}
