package com.backend.dsa.atoz.graphs;

import java.util.ArrayList;

public class PrintHamiltonianPath_06 {

    public static void main(String[] args) {
        int n = 5;
        int e = 7;

        int[][] arr = { { 0, 1 }, { 0, 3 }, { 1, 2 }, { 1, 3 }, { 1, 4 }, { 2, 4 }, { 3, 4 } };

        // Given an undirected graph, the task is to determine if it contains a Hamiltonian cycle. If found, print the
        // path; otherwise, print "Solution does not exist".

        // A Hamiltonian Cycle or Circuit in a graph G is a cycle that visits each vertex of G exactly once and
        // returns to the starting vertex.

        // convert into adj graph

        ArrayList<Edge>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int i = 0; i < arr.length; i++) {
            int src = arr[i][0];
            int dest = arr[i][1];
            graph[src].add(new Edge(src, dest));
            graph[dest].add(new Edge(dest, src));
        }

        // graph is ready now
        boolean[] vis = new boolean[n];
        printAllPath(n, graph, vis);
    }

    private static void printAllPath(int n, ArrayList<Edge>[] graph, boolean[] vis) {
        // lets track all path
        for (int i = 0; i < n; i++) {
            if (!vis[i]) {
                printAllPath(n, graph, vis, "" + i, i);
            }
        }
    }

    private static void printAllPath(int n, ArrayList<Edge>[] graph, boolean[] vis, String ans, int src) {
        vis[src] = true; // mark current idx as visited

        // we can assume our answere when all are visited
        boolean allVisited = true;
        for (int i = 0; i < vis.length; i++) {
            allVisited &= vis[i];
        }

        if (allVisited) {
            System.out.println(ans + ".");
            vis[src] = false;
            return;
        }

        for (Edge edge : graph[src]) {
            // now go to every nbr but before going check if it is not visited
            if (!vis[edge.nbr]) {
                printAllPath(n, graph, vis, ans + edge.nbr, edge.nbr);
            }
        }

        vis[src] = false; // give other option a chance
    }
}
