package com.backend.dsa.atoz.graphs.kosaraju;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ReachabilityFromTheCapital {

    public static void main(String[] args) throws IOException {

        FastScanner fs = new FastScanner();

        int n = fs.nextInt();
        int m = fs.nextInt();
        int src = fs.nextInt();

        List<Integer>[] graph = new List[n + 1];
        List<Integer>[] reverseGraph = new List[n + 1];

        for (int i = 0; i <= n; i++) {
            graph[i] = new ArrayList<>();
            reverseGraph[i] = new ArrayList<>();
        }

        while (m-- > 0) {
            int u = fs.nextInt();
            int v = fs.nextInt();

            graph[u].add(v);
            reverseGraph[v].add(u);
        }

        // Step 1: Find all nodes reachable from source
        boolean[] reachable = new boolean[n + 1];
        dfs(src, graph, reachable);

        // If every city is already reachable
        boolean allReachable = true;
        for (int i = 1; i <= n; i++) {
            if (!reachable[i]) {
                allReachable = false;
                break;
            }
        }

        if (allReachable) {
            // if all reachable then return 0
            System.out.println(0);
            return;
        }

        // Step 2: Kosaraju - first DFS on all nodes
        boolean[] vis = new boolean[n + 1];
        Stack<Integer> stk = new Stack<>();
        for (int i = 1; i <= n; i++) {
            if (!vis[i]) {
                dfs1(i, graph, vis, stk);
            }
        }

        // Step 3: Find SCCs using reverse graph
        int[] component = new int[n + 1];
        int sccCount = 0;
        while (!stk.isEmpty()) {
            int node = stk.pop();
            if (!vis[node]) {
                sccCount++;
                dfs2(node, reverseGraph, vis, component, sccCount);
            }
        }

        // Step 4: Find SCCs which are not reachable from source
        boolean[] hasIncoming = new boolean[sccCount + 1];
        for (int u = 1; u <= n; u++) {
            for (int v : graph[u]) {
                if (component[u] != component[v]) {
                    hasIncoming[component[v]] = true;
                }
            }
        }

        /*
         * We need the number of SCCs which:
         * 1. Are not reachable from source
         * 2. Have no incoming edge from another SCC
         *
         * Each such SCC needs one new road.
         */

        int answer = 0;
        for (int i = 1; i <= sccCount; i++) {
            boolean reachableComponent = false;
            for (int node = 1; node <= n; node++) {
                if (component[node] == i && reachable[node]) {
                    reachableComponent = true;
                    break;
                }
            }

            if (!reachableComponent && !hasIncoming[i]) {
                answer++;
            }
        }

        System.out.println(answer);
    }

    private static void dfs(int node, List<Integer>[] graph, boolean[] reachable) {

        reachable[node] = true;

        for (int nbr : graph[node]) {
            if (!reachable[nbr]) {
                dfs(nbr, graph, reachable);
            }
        }
    }

    private static void dfs1(int node, List<Integer>[] graph, boolean[] vis, Stack<Integer> stk) {
        vis[node] = true;
        for (int nbr : graph[node]) {
            if (!vis[nbr]) {
                dfs1(nbr, graph, vis, stk);
            }
        }

        stk.push(node);
    }

    private static void dfs2(int node, List<Integer>[] reverseGraph, boolean[] vis, int[] component, int sccId) {
        vis[node] = true;
        component[node] = sccId;
        for (int nbr : reverseGraph[node]) {
            if (!vis[nbr]) {
                dfs2(nbr, reverseGraph, vis, component, sccId);
            }
        }
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