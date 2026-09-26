package com.backend.dsa.atoz.graphs.bellmanFord;

import java.util.Arrays;

public class NegativeWeightCycle_03 {

    //https://www.geeksforgeeks.org/problems/negative-weight-cycle3504/1?utm_source=chatgpt.com
    public static void main(String[] args) {
        int V = 4;
        int E = 4;
        int[][] edges = { { 1, 0, 4 }, { 3, 1, -2 }, { 1, 2, -6 }, { 2, 3, 5 } };
        System.out.println(isNegativeWeightCycle(V, edges));
    }

    // determine whether the graph contains a negative weight cycle or not.
    public static boolean isNegativeWeightCycle(int V, int[][] edges) {
        int[] dist = new int[V];
        // Every vertex is reachable from the conceptual super-source
        Arrays.fill(dist, 0);

        // V - 1 passes
        for (int i = 0; i < V - 1; i++) {
            for (int[] x : edges) {
                int u = x[0];
                int v = x[1];
                int wt = x[2];

                if (dist[u] + wt < dist[v]) {
                    dist[v] = dist[u] + wt;
                }
            }
        }

        // Extra pass to detect negative cycle
        for (int[] x : edges) {
            int u = x[0];
            int v = x[1];
            int wt = x[2];

            if (dist[u] + wt < dist[v]) {
                return true;
            }
        }

        return false;
    }
}
