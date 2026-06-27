package com.backend.dsa.atoz.graphs;

import java.util.ArrayList;
import java.util.Arrays;

public class NumberOfWaysToAssignEdgeWeightsII {

    public static void main(String[] args) {

        int[][] edges = {
                { 1, 2 }, { 1, 3 }, { 3, 4 }, { 3, 5 } };
        int[][] queries = { { 1, 4 }, { 3, 4 }, { 2, 5 } };
        int[] ans = getDistanceFromSrcToDest(edges, queries);
        System.out.println(Arrays.toString(ans));
    }

    static final int MOD = 1_000_000_007;

    private static int[] getDistanceFromSrcToDest(int[][] edges, int[][] q) {

        int maxNode = 0;
        for (int[] edge : edges) {
            maxNode = Math.max(maxNode, Math.max(edge[0], edge[1]));
        }

        ArrayList<Edge>[] graph = new ArrayList[maxNode + 1];
        for (int i = 0; i <= maxNode; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];

            graph[u].add(new Edge(u, v));
            graph[v].add(new Edge(v, u));
        }

        int[] ans = new int[q.length];
        for (int i = 0; i < q.length; i++) {
            boolean[] vis = new boolean[maxNode + 1];
            int src = q[i][0];
            int dest = q[i][1];
            int distance = getDistance(graph, vis, src, dest);
            ans[i] = modPow(2, distance - 1);
        }
        return ans;
    }

    private static int getDistance(ArrayList<Edge>[] graph, boolean[] vis, int src, int dest) {
        if (src == dest) {
            return 0;
        }

        vis[src] = true;
        for (Edge edge : graph[src]) {
            int nbr = edge.nbr;
            if (!vis[nbr]) {
                int dist = getDistance(graph, vis, nbr, dest);
                // destination found
                if (dist != -1) {
                    return dist + 1;
                }
            }
        }
        // destination not found in this path
        return -1;
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

    private static class Edge {

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
}
