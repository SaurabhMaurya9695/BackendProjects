package com.backend.dsa.atoz.graphs.bellmanFord;

import java.util.Arrays;

public class BellmanFord_02 {

    public static void main(String[] args) {

        int[][] flights = {
                { 1, 2, 3 }, { 0, 1, 5 }, { 2, 3, 4 }, { 0, 3, 20 } };
        int n = 4;
        int src = 0;
        int dest = 3;
        solve(flights, src, dest, n);
    }

    private static void solve(int[][] flights, int src, int dest, int n) {
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);

        dist[src] = 0;

        // v - 1 passes
        for (int i = 0; i < n - 1; i++) {
            for (int[] x : flights) {
                int u = x[0];
                int v = x[1];
                int wt = x[2];
                if (dist[u] != Integer.MAX_VALUE && dist[u] + wt < dist[v]) {
                    dist[v] = Math.min(dist[v], dist[u] + wt);
                }
            }
        }

        System.out.println("Shortest distance from " + src + " to " + dest + " = " + dist[dest]);
        for (int x : dist) {
            System.out.print(x + " ");
        }

    }
}