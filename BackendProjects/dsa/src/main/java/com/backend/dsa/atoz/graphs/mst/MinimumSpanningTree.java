package com.backend.dsa.atoz.graphs.mst;

import java.util.*;

public class MinimumSpanningTree {

    // Minimum Spanning Tree
    // Algorithms:
    // 1. Prim's Algorithm
    // 2. Kruskal's Algorithm

    public static void main(String[] args) {

        int vertices = 7;

        ArrayList<Edge>[] graph = new ArrayList[vertices];
        for (int i = 0; i < vertices; i++) {
            graph[i] = new ArrayList<>();
        }

        addEdge(graph, 0, 1, 10);
        addEdge(graph, 0, 3, 40);
        addEdge(graph, 1, 2, 10);
        addEdge(graph, 2, 3, 10);
        addEdge(graph, 3, 4, 2);
        addEdge(graph, 4, 5, 3);
        addEdge(graph, 5, 6, 3);
        addEdge(graph, 4, 6, 8);

        prims(graph);
    }

    static void prims(ArrayList<Edge>[] graph) {

        boolean[] visited = new boolean[graph.length];

        PriorityQueue<Pair> pq = new PriorityQueue<>();

        // source vertex, parent, weight - dummy vertices
        pq.add(new Pair(0, -1, 0));

        int totalCost = 0;

        while (!pq.isEmpty()) {

            Pair rem = pq.poll();

            // if current index is already visited
            if (visited[rem.currentVertex]) {
                continue;
            }

            // if current index is not visited - visit that
            visited[rem.currentVertex] = true;

            if (rem.acquiringVertex != -1) {
                System.out.println(rem.acquiringVertex + " -- " + rem.currentVertex + " @ " + rem.wt);
            }

            totalCost += rem.wt;

            for (Edge edge : graph[rem.currentVertex]) {

                if (!visited[edge.nbr]) {
                    pq.add(new Pair(edge.nbr, rem.currentVertex, edge.wt));
                }
            }
        }

        System.out.println("Minimum Cost = " + totalCost);
    }

    static void addEdge(ArrayList<Edge>[] graph, int src, int dest, int wt) {

        graph[src].add(new Edge(src, dest, wt));
        graph[dest].add(new Edge(dest, src, wt));
    }

    static class Edge {

        int src;
        int nbr;
        int wt;

        Edge(int src, int nbr, int wt) {
            this.src = src;
            this.nbr = nbr;
            this.wt = wt;
        }
    }

    static class Pair implements Comparable<Pair> {

        int currentVertex;
        int acquiringVertex;
        int wt;

        Pair() {
        }

        Pair(int currentVertex, int acquiringVertex, int wt) {
            this.currentVertex = currentVertex;
            this.acquiringVertex = acquiringVertex;
            this.wt = wt;
        }

        @Override
        public int compareTo(Pair other) {
            return this.wt - other.wt;
        }
    }
}