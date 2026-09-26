package com.backend.dsa.atoz.graphs.mst.krushkal;

import java.util.Arrays;
import java.util.Comparator;

public class MinCostToConnectAllPoints_krushkal {

    public static void main(String[] args) {
        int[][] points = { { 0, 0 }, { 2, 2 }, { 3, 10 }, { 5, 2 }, { 7, 0 } };
        System.out.println(minCostConnectPoints(points));
    }

    private static int minCostConnectPoints(int[][] points) {
        int n = points.length;
        dsu.init(n);

        // Number of possible edges between every pair of points
        int totalEdges = n * (n - 1) / 2;
        // {point1, point2, distance}
        int[][] adj = new int[totalEdges][3];

        int index = 0;
        // Generate all possible edges
        for (int i = 0; i < points.length; i++) {
            int xi = points[i][0];
            int yi = points[i][1];
            for (int j = i + 1; j < points.length; j++) {
                int xj = points[j][0];
                int yj = points[j][1];
                int manhattanDistance = Math.abs(xi - xj) + Math.abs(yi - yj);

                // Store POINT INDEXES, not coordinates
                adj[index++] = new int[] {
                        i, j, manhattanDistance };
            }
        }

        // Sort edges by weight
        Arrays.sort(adj, Comparator.comparingInt(a -> a[2]));

        int mstCost = 0;
        int edgesTaken = 0;

        // Kruskal
        for (int[] edge : adj) {

            int u = edge[0];
            int v = edge[1];
            int wt = edge[2];

            int rootU = dsu.find(u);
            int rootV = dsu.find(v);

            // Same group -> adding this edge creates a cycle
            if (rootU == rootV) {
                continue;
            }

            // Different groups -> take this edge
            dsu.union(u, v);

            mstCost += wt;
            edgesTaken++;

            // MST of n vertices has exactly n - 1 edges
            if (edgesTaken == n - 1) {
                break;
            }
        }

        return mstCost;
    }

    public static class dsu {

        private static int[] parent;
        private static int[] size;

        private static void init(int n) {

            parent = new int[n];
            size = new int[n];

            for (int i = 0; i < n; i++) {

                parent[i] = i;
                size[i] = 1;
            }
        }

        private static int find(int x) {

            if (parent[x] == x) {
                return x;
            }

            return parent[x] = find(parent[x]);
        }

        private static void union(int x, int y) {

            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) {
                return;
            }

            if (size[rootX] < size[rootY]) {

                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else {

                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            }
        }
    }
}