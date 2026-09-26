package com.backend.dsa.atoz.graphs.cycleDetection;

import java.io.*;
import java.util.*;

public class RoundTable {

    private static void solve(ArrayList<Integer>[] arr, int n, int m) {
        // now we have undirected graph ready
        boolean[] vis = new boolean[n + 1];
        int[] parent = new int[n + 1];
        Arrays.fill(parent, -1);
        ArrayList<Integer> ans = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            if (!vis[i]) {
                Queue<Integer> q = new ArrayDeque<>();
                q.add(i);
                vis[i] = true;
                while (!q.isEmpty()) {
                    int top = q.poll();
                    for (int nbr : arr[top]) {
                        if (!vis[nbr]) {
                            q.add(nbr);
                            parent[nbr] = top;
                            vis[nbr] = true;
                        } else if (parent[top] != nbr) {
                            // cycle found between top and nbr
                            ArrayList<Integer> path1 = new ArrayList<>();
                            ArrayList<Integer> path2 = new ArrayList<>();
                            int x = top;
                            int y = nbr;
                            while (x != -1) {
                                path1.add(x);
                                x = parent[x];
                            }

                            while (y != -1) {
                                path2.add(y);
                                y = parent[y];
                            }

                            // Find common ancestor
                            int p = path1.size() - 1;
                            int r = path2.size() - 1;
                            while (p >= 0 && r >= 0 && path1.get(p).equals(path2.get(r))) {
                                p--;
                                r--;
                            }

                            // Path from top to common ancestor
                            for (int k = 0; k <= p + 1; k++) {
                                ans.add(path1.get(k));
                            }

                            // Path from common ancestor to nbr

                            for (int k = r; k >= 0; k--) {
                                ans.add(path2.get(k));
                            }

                            // Close the cycle
                            ans.add(top);
                            System.out.println(ans.size());
                            for (int node : ans) {
                                System.out.print(node + " ");
                            }

                            System.out.println();
                            return;
                        }
                    }
                }
            }
        }

        System.out.println("IMPOSSIBLE");
    }

    public static void main(String[] args) throws Exception {
        FastScanner scanner = new FastScanner();
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<Integer>[] arr = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) {
            arr[i] = new ArrayList<>();
        }

        // Build directed graph
        for (int i = 0; i < m; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            arr[u].add(v);
            arr[v].add(u);
        }

        solve(arr, n, m);
    }

    static class FastScanner {

        private final InputStream in = System.in;
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0;
        private int len = 0;

        private int read() throws IOException {

            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;

                if (len == -1) {
                    return -1;
                }
            }

            return buffer[ptr++];
        }

        long nextLong() throws IOException {

            int c;

            do {
                c = read();
            } while (c <= ' ');

            boolean negative = false;

            if (c == '-') {
                negative = true;
                c = read();
            }

            long result = 0;

            while (c > ' ') {
                result = result * 10 + (c - '0');
                c = read();
            }

            return negative ? -result : result;
        }

        int nextInt() throws IOException {
            return (int) nextLong();
        }
    }
}