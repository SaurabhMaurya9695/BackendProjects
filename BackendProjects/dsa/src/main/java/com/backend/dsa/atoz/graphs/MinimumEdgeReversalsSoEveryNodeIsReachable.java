package com.backend.dsa.atoz.graphs;

import java.util.*;

public class MinimumEdgeReversalsSoEveryNodeIsReachable {
    public static void main(String[] args) {
        int n = 4;
        int[][] edges = { { 2, 0 }, { 2, 1 }, { 1, 3 } };
        System.out.println(Arrays.toString(minEdgeReversals(n, edges)));
    }

    public static int[] minEdgeReversals(int n, int[][] edges) {
        List<Edge>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        // Build weighted graph
        for (int[] e : edges) {
            int u = e[0];
            int v = e[1];
            graph[u].add(new Edge(v, 0)); // original direction
            graph[v].add(new Edge(u, 1)); // reverse direction
        }

        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            ans[i] = dijkstra(i, graph, n);
        }
        return ans;
    }

    private static int dijkstra(int src, List<Edge>[] graph, int n) {
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);

        PriorityQueue<Pair> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a.dist));
        dist[src] = 0;
        pq.offer(new Pair(src, 0));
        while (!pq.isEmpty()) {
            Pair cur = pq.poll();
            if (cur.dist != dist[cur.node]) {
                continue;
            }

            for (Edge edge : graph[cur.node]) {
                if (dist[edge.to] > cur.dist + edge.cost) {
                    dist[edge.to] = cur.dist + edge.cost;
                    pq.offer(new Pair(edge.to, dist[edge.to]));
                }
            }
        }

        int total = 0;
        for (int d : dist) {
            total += d;
        }

        return total;
    }

    static class Edge {
        int to;
        int cost;

        Edge(int to, int cost) {
            this.to = to;
            this.cost = cost;
        }
    }

    static class Pair {
        int node;
        int dist;
        Pair(int node, int dist) {
            this.node = node;
            this.dist = dist;
        }
    }
}