package com.backend.dsa.atoz.graphs.kosaraju;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CoinCollector {

    private static int ans = 0;

    public static void main(String[] args) throws IOException {
        FastScanner fs = new FastScanner();
        int n = fs.nextInt();
        int m = fs.nextInt();
        int[] coins = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            coins[i] = fs.nextInt();
        }

        List<Integer>[] graph = new List[n + 1];
        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int i = 0; i < m; i++) {
            int u = fs.nextInt();
            int v = fs.nextInt();
            graph[u].add(v);
        }

        boolean[] vis = new boolean[n + 1];
        int maxCoins = 0;
        for (int i = 1; i <= n; i++) {
            int ans = dfs(i, graph, vis, coins);
            maxCoins = Math.max(ans, maxCoins);
            vis = new boolean[n + 1];
            Arrays.fill(vis, false);
        }

        System.out.println(maxCoins);
    }

    public static int dfs(int node, List<Integer>[] graph, boolean[] vis, int[] coins) {
        vis[node] = true;
        ans = coins[node];
        for (Integer nbr : graph[node]) {
            if (!vis[nbr]) {
                ans += dfs(nbr, graph, vis, coins);
            }
        }

        return ans;
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
