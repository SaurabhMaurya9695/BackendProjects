package com.backend.dsa.atoz.graphs;

import java.util.ArrayList;

public class NumberOfWaysToAssignEdgeWeightsI {

    // Stores the maximum depth found during DFS
    static int maxDepth = 0;

    public static void main(String[] args) {

        int[][] edges = {
                { 1, 2 }, { 1, 3 }, { 3, 4 }, { 3, 5 } };
        int ans = assignEdgeWeights(edges);
        System.out.println(ans);
    }

    static final int MOD = 1_000_000_007;

    public static int assignEdgeWeights(int[][] edges) {
        int maxNode = 0;
        for (int[] edge : edges) {
            maxNode = Math.max(maxNode, Math.max(edge[0], edge[1]));
        }

        // Create adjacency list
        ArrayList<Edge>[] graph = new ArrayList[maxNode + 1];

        for (int i = 0; i <= maxNode; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int[] edge : edges) {
            int src = edge[0];
            int dest = edge[1];

            graph[src].add(new Edge(src, dest));
            graph[dest].add(new Edge(dest, src));
        }

        // Visited array to avoid revisiting nodes
        boolean[] vis = new boolean[maxNode + 1];

        // Start DFS from node 1 (root)
        vis[1] = true;

        // Current depth of root node is 0
        dfs(graph, vis, 1, 0);
        int d = maxDepth;
        maxDepth = 0;
        System.out.println(d);
        // now get the factorial of the noOfNodes ans
        return modPow(2, d - 1) % MOD;
    }

    private static void dfs(ArrayList<Edge>[] graph, boolean[] vis, int src, int depth) {

        // Update maximum depth if current depth is larger
        maxDepth = Math.max(maxDepth, depth);

        // Visit all neighbors of current node
        for (Edge edge : graph[src]) {

            int nbr = edge.nbr;

            // Go only to unvisited neighbors
            if (!vis[nbr]) {
                vis[nbr] = true;

                // Move one level deeper
                dfs(graph, vis, nbr, depth + 1);
            }
        }
    }

    public static class Edge {

        int src;
        int nbr;
        int wt;

        public Edge(int src, int nbr) {
            this.src = src;
            this.nbr = nbr;
        }

        public Edge(int src, int nbr, int wt) {
            this.src = src;
            this.nbr = nbr;
            this.wt = wt;
        }
    }

    private static int modPow(long base, long exp) {
        long ans = 1;

        while (exp > 0) {
            if ((exp & 1) == 1) {
                ans = (ans * base) % MOD;
            }

            base = (base * base) % MOD;
            exp >>= 1;
        }

        return (int) ans;
    }

}