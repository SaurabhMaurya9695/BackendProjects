package com.backend.dsa.atoz.graphs.kosaraju;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Kosaraju {

    public static void main(String[] args) {
        int n = 5;
        int[][] edges = { { 0, 1 }, { 1, 2 }, { 2, 0 }, { 2, 3 }, { 3, 4 }, { 4, 3 } };
        System.out.println("Number of SCCs = " + kosaraju(n, edges));
    }

    private static int kosaraju(int n, int[][] edges) {

        List<Integer>[] graph = new ArrayList[n];
        List<Integer>[] reverseGraph = new ArrayList[n];

        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
            reverseGraph[i] = new ArrayList<>();
        }

        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];

            // Original graph
            graph[u].add(v);

            // Reverse graph
            reverseGraph[v].add(u);
        }

        boolean[] vis = new boolean[n];
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < n; i++) {
            if (!vis[i]) {
                dfs1(i, graph, vis, stack);
            }
        }

        vis = new boolean[n];
        int sccCount = 0;
        while (!stack.isEmpty()) {
            int node = stack.pop();
            // Already belongs to an SCC
            if (vis[node]) {
                continue;
            }
            dfs2(node, reverseGraph, vis);
            sccCount++;
        }

        return sccCount;
    }

    private static void dfs1(int node, List<Integer>[] graph, boolean[] vis, Stack<Integer> stack) {
        vis[node] = true;
        for (int nbr : graph[node]) {
            if (!vis[nbr]) {
                dfs1(nbr, graph, vis, stack);
            }
        }

        // Push AFTER visiting all neighbours
        stack.push(node);
    }

    private static void dfs2(int node, List<Integer>[] reverseGraph, boolean[] vis) {
        vis[node] = true;
        for (int nbr : reverseGraph[node]) {
            if (!vis[nbr]) {
                dfs2(nbr, reverseGraph, vis);
            }
        }
    }
}
