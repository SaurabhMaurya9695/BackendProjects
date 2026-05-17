package com.backend.dsa.atoz.graphs;

import java.util.ArrayList;

public class GetConnectedComponent_04 {

    public static void main(String[] args) {
        int V = 5;
        int[][] edges = { { 0, 1 }, { 2, 1 }, { 3, 4 } };
        System.out.println(getComponents(V, edges));
    }

    /*
     * https://www.geeksforgeeks.org/problems/connected-components-in-an-undirected-graph/1
     * */
    public static ArrayList<ArrayList<Integer>> getComponents(int V, int[][] edges) {
        // Create graph
        ArrayList<Edge>[] graph = new ArrayList[V];

        // Initialize every array index
        for (int i = 0; i < V; i++) {
            graph[i] = new ArrayList<>();
        }

        // Build undirected graph
        for (int i = 0; i < edges.length; i++) {
            int src = edges[i][0];
            int dest = edges[i][1];
            graph[src].add(new Edge(src, dest));
            graph[dest].add(new Edge(dest, src));
        }

        // DFS traversal
        boolean[] visited = new boolean[V];
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        // Visit every vertex
        for (int i = 0; i < V; i++) {
            // If not visited,
            // it means new connected component found
            if (!visited[i]) {
                ArrayList<Integer> ans = new ArrayList<>();
                dfs(graph, i, visited, ans);
                result.add(ans);
            }
        }
        return result;
    }

    private static void dfs(ArrayList<Edge>[] graph, int src, boolean[] visited, ArrayList<Integer> ans) {
        visited[src] = true;
        ans.add(src);

        for (Edge edge : graph[src]) {
            if (!visited[edge.nbr]) {
                dfs(graph, edge.nbr, visited, ans);
            }
        }
    }
}
