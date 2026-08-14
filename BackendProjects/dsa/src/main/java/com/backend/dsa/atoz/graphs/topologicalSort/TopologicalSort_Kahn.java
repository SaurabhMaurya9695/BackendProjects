package com.backend.dsa.atoz.graphs.topologicalSort;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class TopologicalSort_Kahn {

    public static void main(String[] args) {
        int vertices = 7;

        // Step 1: Build adjacency list (using List<Integer>, not Edge!)
        List<Integer>[] graph = new ArrayList[vertices];
        for (int i = 0; i < vertices; i++) {
            graph[i] = new ArrayList<>();
        }

        // Add edges
        addEdge(graph, 0, 1);
        addEdge(graph, 0, 3);
        addEdge(graph, 1, 2);
        addEdge(graph, 2, 3);
        addEdge(graph, 4, 3);
        addEdge(graph, 4, 5);
        addEdge(graph, 4, 6);
        addEdge(graph, 5, 6);

        // Step 2: Calculate indegree (CORRECT size!)
        int[] indegree = new int[vertices];
        for (int u = 0; u < vertices; u++) {
            for (int neighbor : graph[u]) {
                indegree[neighbor]++;
            }
        }

        // Step 3: Add all nodes with indegree 0 to queue
        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < vertices; i++) {
            if (indegree[i] == 0) {
                queue.offer(i);
            }
        }

        // Step 4: Process nodes (Kahn's Algorithm)
        List<Integer> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            int node = queue.poll();
            result.add(node);

            // For each neighbor, decrease indegree
            for (int neighbor : graph[node]) {
                indegree[neighbor]--;
                if (indegree[neighbor] == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        // Step 5: Check if valid topological sort (cycle detection)
        if (result.size() != vertices) {
            System.out.println("Cycle detected! No valid topological sort.");
            System.out.println(new ArrayList<>());
            return;
        }

        System.out.println("Topological Order: " + result);
    }

    static void addEdge(List<Integer>[] graph, int src, int dest) {
        graph[src].add(dest);
    }
}
