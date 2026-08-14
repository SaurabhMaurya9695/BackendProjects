package com.backend.dsa.atoz.graphs;

import java.util.ArrayList;
import java.util.List;

public class NumberOfOperationsToMakeNetworkConnected {

    public static void main(String[] args) {
        int n = 4;
        int[][] connections = { { 0, 1 }, { 0, 2 }, { 1, 2 } };
        System.out.println(makeConnected(n, connections));
    }

    private static int makeConnected(int n, int[][] connections) {
        // Edge case: not enough edges to connect all
        if (connections.length < n - 1) {
            return -1;
        }

        // Build adjacency list
        List<Integer>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int[] conn : connections) {
            graph[conn[0]].add(conn[1]);
            graph[conn[1]].add(conn[0]);
        }

        // Count connected components using DFS
        boolean[] visited = new boolean[n];
        int components = 0;

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                dfs(i, graph, visited);
                components++;
            }
        }

        // Answer = cables to remove = edges - (n - components)
        return components - 1;
    }

    private static void dfs(int node, List<Integer>[] graph, boolean[] visited) {
        visited[node] = true;

        for (int neighbor : graph[node]) {
            if (!visited[neighbor]) {
                dfs(neighbor, graph, visited);
            }
        }
    }
}
