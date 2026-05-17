package com.backend.dsa.atoz.graphs;

import java.util.ArrayList;

public class PrintAllPathsInGraph_03 {

    public static void main(String[] args) {
        // Build graph
        ArrayList<Edge>[] graph = Graph_01.buildGraph();

        // visited array to avoid cycles
        boolean[] visited = new boolean[graph.length];

        // print all paths from 0 to 6
        printAllPath(graph, 0, 6, visited, "");
    }

    private static void printAllPath(ArrayList<Edge>[] graph, int src, int dest, boolean[] visited, String path) {
        // Base case:
        // if current source becomes destination,
        // print the complete path
        if (src == dest) {
            System.out.println(path + dest);
            return;
        }

        // Mark current node visited
        // so we don't revisit it in same path
        visited[src] = true;

        // Explore all neighbors of current node
        for (Edge edge : graph[src]) {
            int nbr = edge.nbr;
            // Visit only unvisited neighbors
            // to avoid infinite cycles
            if (!visited[nbr]) {
                // Add current node into path
                // and recursively go to neighbor
                printAllPath(graph, nbr, dest, visited, path + src + " -> ");
            }
        }

        // Backtracking step:
        // unmark node so it can be used
        // in another valid path
        visited[src] = false;
    }
}
