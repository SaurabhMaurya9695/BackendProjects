package com.backend.dsa.atoz.graphs;

import java.util.ArrayList;

public class PrintHamiltonianCycle_07 {

    public static void main(String[] args) {
        int n = 5;
        int[][] arr = {
                { 0, 1 }, { 0, 3 }, { 1, 2 }, { 1, 3 }, { 1, 4 }, { 2, 4 }, { 3, 4 } };

        ArrayList<Edge>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        // build graph
        for (int[] edge : arr) {
            int src = edge[0];
            int dest = edge[1];
            graph[src].add(new Edge(src, dest));
            graph[dest].add(new Edge(dest, src));
        }

        boolean[] vis = new boolean[n];
        // start from every vertex
        printHamiltonian(graph, vis, 0, 0, 1, 0 + "");
    }

    private static void printHamiltonian(ArrayList<Edge>[] graph, boolean[] vis, int src, int originalSource, int count,
            String path) {

        // all vertices visited
        if (count == graph.length) {
            boolean cycle = false;
            // check if last node connects to original source
            for (Edge edge : graph[src]) {
                if (edge.nbr == originalSource) {
                    cycle = true;
                    break;
                }
            }

            if (cycle) {
                System.out.println(path + "*");
            } else {
                System.out.println(path + ".");
            }

            return;
        }

        vis[src] = true;
        for (Edge edge : graph[src]) {
            if (!vis[edge.nbr]) {
                printHamiltonian(graph, vis, edge.nbr, originalSource, count + 1, path + edge.nbr);
            }
        }

        vis[src] = false;
    }
}
