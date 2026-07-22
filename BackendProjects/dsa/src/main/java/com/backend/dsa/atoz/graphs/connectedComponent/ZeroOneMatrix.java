package com.backend.dsa.atoz.graphs.connectedComponent;

import java.util.ArrayDeque;
import java.util.Deque;

public class ZeroOneMatrix {

    public static void main(String[] args) {
        int[][] mat = { { 0, 0, 0 }, { 0, 1, 0 }, { 1, 1, 1 } };
        int[][] ans = updateMatrix(mat);
        for (int i = 0; i < ans.length; i++) {
            for (int j = 0; j < ans[0].length; j++) {
                System.out.print(ans[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static final int[][] dirs = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

    public static int[][] updateMatrix(int[][] mat) {
        int n = mat.length;
        int m = mat[0].length;
        int[][] dist = new int[m][n];

        // we need to find the one's and start a bfs from there to capture the nearest 0 and update in
        // same
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (mat[i][j] == 1) {
                    // start a bfs from here
                    boolean[][] visited = new boolean[m][n];
                    Deque<int[]> dq = new ArrayDeque<>();
                    dq.add(new int[] { i, j });
                    visited[i][j] = true;
                    int d = 0;
                    boolean isFoundNearestZero = false;
                    while (!dq.isEmpty() && !isFoundNearestZero) {
                        // lets start in radius
                        int sz = dq.size();
                        d++;
                        while (sz-- > 0) {
                            int[] cell = dq.poll();
                            // get the nearest cells and try to find the nearest zero
                            for (int k = 0; k < 4; i++) {
                                int row = cell[0] + dirs[k][0];
                                int col = cell[1] + dirs[k][1];
                                if (row < 0 || row >= m || col < 0 || col >= n || visited[row][col]) {
                                    continue;
                                }

                                if (mat[row][col] == 0) {
                                    dist[i][j] = d;
                                    isFoundNearestZero = true;
                                    break;
                                }

                                visited[row][col] = true;
                                dq.add(new int[] { row, col });
                            }

                            // found the zero im this same radius
                            if (isFoundNearestZero) {
                                break;
                            }
                        }
                    }
                }
            }
        }
        return dist;
    }

    /* SECOND METHOD
    * public int[][] updateMatrix(int[][] mat) {
        int m = mat.length, n = mat[0].length;
        int[][] dist = new int[m][n];
        int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
        Queue<int[]> queue = new LinkedList<>();

        // Initialize: 0-cells get distance 0 and are seeded into queue
        //             1-cells get MAX_VALUE (unknown distance)
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j] == 0) {
                    dist[i][j] = 0;
                    queue.offer(new int[]{i, j});
                } else {
                    dist[i][j] = Integer.MAX_VALUE;
                }
            }
        }

        // Multi-source BFS: wave expands from all 0s simultaneously
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            for (int[] d : dirs) {
                int nr = cell[0] + d[0], nc = cell[1] + d[1];
                if (nr < 0 || nr >= m || nc < 0 || nc >= n) continue;

                // Only update if we found a shorter path
                if (dist[nr][nc] > dist[cell[0]][cell[1]] + 1) {
                    dist[nr][nc] = dist[cell[0]][cell[1]] + 1;
                    queue.offer(new int[]{nr, nc});
                }
            }
        }

        return dist;
    }
    *
    * */
}
