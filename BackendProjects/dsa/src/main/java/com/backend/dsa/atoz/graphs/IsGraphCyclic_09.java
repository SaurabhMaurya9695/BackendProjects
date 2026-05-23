package com.backend.dsa.atoz.graphs;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class IsGraphCyclic_09 {

    static class Edge {
        int src;
        int nbr;

        Edge(int src, int nbr) {
            this.src = src;
            this.nbr = nbr;
        }
    }

    // add edge
    static void addEdge(ArrayList<Edge>[] adj, int u, int v) {
        adj[u].add(new Edge(u, v));
        adj[v].add(new Edge(v, u));
    }

    public static void main(String[] args) {

        int V = 4;
        ArrayList<Edge>[] adj = new ArrayList[V];
        for (int i = 0; i < V; i++) {
            adj[i] = new ArrayList<>();
        }

        addEdge(adj, 0, 1);
        addEdge(adj, 0, 2);
        addEdge(adj, 1, 2);
        addEdge(adj, 2, 3);

        System.out.println(isCycle(adj));
    }

    static class Pair {
        int src;
        int parent;

        Pair(int src, int parent) {
            this.src = src;
            this.parent = parent;
        }
    }

    static boolean isCycle(ArrayList<Edge>[] adj) {
        boolean[] vis = new boolean[adj.length];
        for (int v = 0; v < adj.length; v++) {
            // if not visited start bfs from here
            if (!vis[v]) {
                if (bfs(v, adj, vis)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean bfs(int src, ArrayList<Edge>[] adj, boolean[] vis) {
        ArrayDeque<Pair> q = new ArrayDeque<>();
        q.add(new Pair(src, -1));
        while (!q.isEmpty()) {
            Pair rem = q.removeFirst();
            if (vis[rem.src]) {
                continue;
            }
            vis[rem.src] = true;

            for (Edge edge : adj[rem.src]) {

                // if neighbor not visited
                if (!vis[edge.nbr]) {
                    q.add(new Pair(edge.nbr, rem.src));
                }
                // visited and not parent => cycle
                else if (edge.nbr != rem.parent) {
                    return true;
                }
            }
        }
        return false;
    }
}