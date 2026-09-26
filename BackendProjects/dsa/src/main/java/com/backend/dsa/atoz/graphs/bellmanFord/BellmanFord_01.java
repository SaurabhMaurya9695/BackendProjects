package com.backend.dsa.atoz.graphs.bellmanFord;

import java.util.Arrays;

public class BellmanFord_01 {

    public static void main(String[] args) {

        int[][] flights = {
                { 0, 1, 5 }, { 1, 2, -2 }, { 0, 2, 10 } };
        int n = 3;
        int src = 0;
        int dest = 2;
        solve(flights, src, dest, n);
    }

    private static void solve(int[][] flights, int src, int dest, int n) {
        // dist[i] = shortest distance from src to vertex i
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        // Distance from source to itself is 0
        dist[src] = 0;

        /*
         * Bellman-Ford:
         * Relax every edge V - 1 times.
         */
        for (int i = 0; i < n - 1; i++) {
            for (int[] edge : flights) {
                int u = edge[0];
                int v = edge[1];
                int wt = edge[2];

                // Only relax if u is reachable
                if (dist[u] != Integer.MAX_VALUE && dist[u] + wt < dist[v]) {
                    dist[v] = dist[u] + wt;
                }
            }
        }

        System.out.println("Shortest distance from " + src + " to " + dest + " = " + dist[dest]);
        System.out.println("All distances:");
        for (int i = 0; i < n; i++) {
            System.out.println(src + " -> " + i + " = " + dist[i]);
        }
    }
}