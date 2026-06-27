package com.backend.dsa.atoz.graphs.dijkstra;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class ShortestPathInWeight_01 {

    public static void main(String[] args) {

        ArrayList<Edge>[] graph = buildGraph();

        boolean[] visited = new boolean[graph.length];
        PriorityQueue<Pair> pq = new PriorityQueue<>();

        pq.add(new Pair(0, 0));

        solveDijkstra(graph, visited, pq);
    }

    private static void solveDijkstra(ArrayList<Edge>[] graph, boolean[] visited, PriorityQueue<Pair> pq) {

        while (!pq.isEmpty()) {

            Pair curr = pq.poll();

            if (visited[curr.vertex]) {
                continue;
            }

            visited[curr.vertex] = true;

            System.out.println("Vertex : " + curr.vertex + " Cost : " + curr.cost);

            for (Edge edge : graph[curr.vertex]) {

                if (!visited[edge.nbr]) {
                    pq.add(new Pair(edge.nbr, curr.cost + edge.wt));
                }
            }
        }
    }

    public static ArrayList<Edge>[] buildGraph() {

        int vertices = 7;

        @SuppressWarnings("unchecked") ArrayList<Edge>[] graph = new ArrayList[vertices];

        for (int i = 0; i < vertices; i++) {
            graph[i] = new ArrayList<>();
        }

        graph[0].add(new Edge(0, 3, 40));
        graph[0].add(new Edge(0, 1, 10));

        graph[1].add(new Edge(1, 0, 10));
        graph[1].add(new Edge(1, 2, 10));

        graph[2].add(new Edge(2, 3, 10));
        graph[2].add(new Edge(2, 1, 10));

        graph[3].add(new Edge(3, 0, 40));
        graph[3].add(new Edge(3, 2, 10));
        graph[3].add(new Edge(3, 4, 2));

        graph[4].add(new Edge(4, 3, 2));
        graph[4].add(new Edge(4, 5, 3));
        graph[4].add(new Edge(4, 6, 8));

        graph[5].add(new Edge(5, 4, 3));
        graph[5].add(new Edge(5, 6, 3));

        graph[6].add(new Edge(6, 5, 3));
        graph[6].add(new Edge(6, 4, 8));

        return graph;
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

        int vertex;
        int cost;

        Pair(int vertex, int cost) {
            this.vertex = vertex;
            this.cost = cost;
        }

        @Override
        public int compareTo(Pair o) {
            return Integer.compare(this.cost, o.cost);
        }
    }
}