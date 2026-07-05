package com.backend.dsa.atoz.graphs.topologicalSort;

import com.backend.dsa.atoz.graphs.Edge;

import java.util.ArrayList;
import java.util.Stack;

public class TopologicalSort {

    // Topological Sort
    // Topological Sort is a linear ordering of the vertices of a Directed Acyclic Graph (DAG) such that
    // for every directed edge: u→v
    // vertex u always appears before v in the ordering.
    // In simple words:
    // If A depends on B, then B must come before A.
    public static void main(String[] args) {
        int vertices = 7;

        ArrayList<Edge>[] graph = new ArrayList[vertices];

        for (int i = 0; i < vertices; i++) {
            graph[i] = new ArrayList<>();
        }

        addEdge(graph, 0, 1);
        addEdge(graph, 0, 3);
        addEdge(graph, 1, 2);
        addEdge(graph, 2, 3);
        addEdge(graph, 4, 3);
        addEdge(graph, 4, 5);
        addEdge(graph, 4, 6);
        addEdge(graph, 5, 6);

        boolean[] visited = new boolean[vertices];
        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < vertices; i++) {
            if (!visited[i]) {
                topologicalSort(graph, visited, i, stack);
            }
        }

        while (!stack.isEmpty()) {
            System.out.print(stack.pop() + " ");
        }
    }

    private static void topologicalSort(ArrayList<Edge>[] graph, boolean[] visited, int src, Stack<Integer> stk) {
        visited[src] = true;
        for (Edge edge : graph[src]) {
            if (!visited[edge.nbr]) {
                topologicalSort(graph, visited, edge.nbr, stk);
            }
        }

        stk.push(src);
    }

    static void addEdge(ArrayList<Edge>[] graph, int src, int dest) {
        graph[src].add(new Edge(src, dest, 0));
    }
}
