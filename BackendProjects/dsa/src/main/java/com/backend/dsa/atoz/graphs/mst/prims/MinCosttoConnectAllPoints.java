package com.backend.dsa.atoz.graphs.mst.prims;

import java.util.Comparator;
import java.util.PriorityQueue;

public class MinCosttoConnectAllPoints {

    public static void main(String[] args) {
        int[][] points = { { 0, 0 }, { 2, 2 }, { 3, 10 }, { 5, 2 }, { 7, 0 } };
        System.out.println(minCostConnectPoints(points));
    }

    public static int minCostConnectPoints(int[][] points) {
        int n = points.length;
        boolean[] vis = new boolean[n];
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));

        // {cost, node (generally a index of an array)}
        pq.add(new int[] { 0, 0 });
        int totalCost = 0;

        while (!pq.isEmpty()) {
            int[] tp = pq.poll();
            int distance = tp[0];
            int node = tp[1];

            // Already part of MST
            if (vis[node]) {
                continue;
            }

            // Add node to MST
            vis[node] = true;

            // Add the selected edge cost
            totalCost += distance;
            // Connect current node to every unvisited node
            for (int next = 0; next < n; next++) {
                if (vis[next]) {
                    continue;
                }
                int newDistance = Math.abs(points[node][0] - points[next][0]) + Math.abs(
                        points[node][1] - points[next][1]);
                pq.add(new int[] { newDistance, next });
            }
        }

        return totalCost;
    }
}
