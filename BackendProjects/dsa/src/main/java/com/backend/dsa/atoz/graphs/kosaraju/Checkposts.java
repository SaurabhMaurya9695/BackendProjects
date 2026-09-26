package com.backend.dsa.atoz.graphs.kosaraju;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Checkposts {

    static final long MOD = 1_000_000_007L;

    public static void main(String[] args) throws IOException {
        FastScanner scanner = new FastScanner();
        int n = scanner.nextInt();
        int[] cost = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            cost[i] = scanner.nextInt();
        }

        int m = scanner.nextInt();
        List<Integer>[] graph = new List[n + 1];
        List<Integer>[] reverseGraph = new List[n + 1];
        for (int i = 0; i <= n; i++) {
            graph[i] = new ArrayList<>();
            reverseGraph[i] = new ArrayList<>();
        }

        while (m-- > 0) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            // start building kosaraju now
            graph[u].add(v);
            reverseGraph[v].add(u);
        }

        // first we need to find the scc once we get the scc we can choose where we need to fit the junction
        // and how many ways it says
        boolean[] vis = new boolean[n + 1];
        Stack<Integer> stk = new Stack<>();
        for (int i = 1; i <= n; i++) {
            if (!vis[i]) {
                dfs1(i, graph, vis, stk);
            }
        }

        // we full the stack now here, now as per algo do the same in reverse order
        long totalCost = 0;
        long ways = 1;
        vis = new boolean[n + 1];
        while (!stk.isEmpty()) {
            int top = stk.pop();
            if (!vis[top]) {
                int[] result = dfs2(top, reverseGraph, vis, cost);
                int minCost = result[0];
                int count = result[1];
                totalCost += minCost;
                ways = (ways * count) % MOD;
            }
        }

        System.out.println(totalCost + " " + ways);
    }

    public static void dfs1(int node, List<Integer>[] graph, boolean[] vis, Stack<Integer> stk) {
        vis[node] = true;
        for (Integer nbr : graph[node]) {
            if (!vis[nbr]) {
                dfs1(nbr, graph, vis, stk);
            }
        }

        stk.add(node);
    }

    public static int[] dfs2(int node, List<Integer>[] reverseGraph, boolean[] vis, int[] cost) {
        vis[node] = true;
        int minCost = cost[node];
        int count = 1;
        for (Integer nbr : reverseGraph[node]) {
            if (!vis[nbr]) {
                int[] result = dfs2(nbr, reverseGraph, vis, cost);
                int childMinCost = result[0];
                int childCount = result[1];
                // Found a cheaper cost
                if (childMinCost < minCost) {
                    minCost = childMinCost;
                    count = childCount;
                }
                // Found another city with same minimum cost
                else if (childMinCost == minCost) {
                    count += childCount;
                }
            }
        }
        return new int[] { minCost, count };
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

