package com.backend.dsa.atoz.graphs.floydWarshall;

import java.util.Arrays;

public class FindtheCityWiththeSmallestNumberofNeighborsataThresholdDistance {

    public static void main(String[] args) {
        int n = 4;
        int[][] edges = { { 0, 1, 3 }, { 1, 2, 1 }, { 1, 3, 4 }, { 2, 3, 1 } };
        int distanceThreshold = 4;
        System.out.println(findTheCity(n, edges, distanceThreshold));
    }

    private static int findTheCity(int V, int[][] edges, int distanceThreshold) {

        // Floyd-Warshall:
        // arr[i][j] = shortest distance from i -> j
        int INF = Integer.MAX_VALUE;
        int[][] arr = new int[V][V];
        // Initialize
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (i == j) {
                    arr[i][j] = 0;
                } else {
                    arr[i][j] = INF;
                }
            }
        }

        // Build undirected graph
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int wt = edge[2];
            arr[u][v] = Math.min(arr[u][v], wt);
            arr[v][u] = Math.min(arr[v][u], wt);
        }

        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i++) {
                // No path from i -> k
                if (arr[i][k] == INF) {
                    continue;
                }

                for (int j = 0; j < V; j++) {
                    // No path from k -> j
                    if (arr[k][j] == INF) {
                        continue;
                    }
                    arr[i][j] = Math.min(arr[i][j], arr[i][k] + arr[k][j]);
                }
            }
        }

        // count nbr
        int answer = -1;
        int minCount = Integer.MAX_VALUE;
        for (int i = 0; i < V; i++) {
            int count = 0;
            for (int j = 0; j < V; j++) {
                if (i != j && arr[i][j] <= distanceThreshold) {
                    count++;
                }
            }

            // If same count, choose larger city
            if (count <= minCount) {
                minCount = count;
                answer = i;
            }
        }

        return answer;
    }
}