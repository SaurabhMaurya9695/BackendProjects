package com.backend.dsa.atoz.graphs;

import java.util.ArrayList;

public class FindIfPathExistsInGraph_02 {

    public boolean validPath(int n, int[][] edges, int source, int destination) {

        ArrayList<Edge>[] graph = new ArrayList[n];
        // initialize graph
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        // create graph
        for (int i = 0; i < edges.length; i++) {
            int src = edges[i][0];
            int dest = edges[i][1];
            graph[src].add(new Edge(src, dest));
            graph[dest].add(new Edge(dest, src));
        }

        boolean[] visited = new boolean[n];
        // now we have bidirectional graph ready
        return solve(graph, source, destination, visited);
    }

    public boolean solve(ArrayList<Edge>[] graph, int source, int destination, boolean[] visited) {
        // if src is a dest - means we have a path
        if (source == destination) {
            return true;
        }

        visited[source] = true;

        // if src is not a dest then check from its nbr, if its nbr has any path exist then
        // you have a path exist
        for (Edge edge : graph[source]) {
            int nbr = edge.nbr;
            if (!visited[nbr]) {
                boolean hasPathFromNbr = solve(graph, nbr, destination, visited);
                if (hasPathFromNbr) {
                    return true;
                }
            }
        }

        return false;
    }
}
