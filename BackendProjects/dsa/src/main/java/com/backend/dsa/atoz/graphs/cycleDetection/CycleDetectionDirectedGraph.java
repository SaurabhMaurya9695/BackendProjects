package com.backend.dsa.atoz.graphs.cycleDetection;

import java.util.*;

public class CycleDetectionDirectedGraph {

    public static void main(String[] args) {
        // Test Case 1: Graph with NO cycle
        int n1 = 4;
        int[][] edges1 = { { 0, 1 }, { 0, 2 }, { 1, 3 }, { 2, 3 } };
        System.out.println("Test 1 - No Cycle: " + hasCycle(n1, edges1));  // false

        // Test Case 2: Graph WITH cycle
        int n2 = 3;
        int[][] edges2 = { { 0, 1 }, { 1, 2 }, { 2, 0 } };
        System.out.println("Test 2 - Has Cycle: " + hasCycle(n2, edges2));  // true

        // Test Case 3: Self loop (node pointing to itself)
        int n3 = 2;
        int[][] edges3 = { { 0, 0 }, { 1, 0 } };
        System.out.println("Test 3 - Self Loop: " + hasCycle(n3, edges3));  // true

        // Test Case 4: Single node, no edges
        int n4 = 1;
        int[][] edges4 = {};
        System.out.println("Test 4 - Single Node: " + hasCycle(n4, edges4));  // false
    }

    public static boolean hasCycle(int n, int[][] edges) {
        // Step 1: Build adjacency list from edges
        List<Integer>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        // Add edges to graph
        for (int[] edge : edges) {
            int u = edge[0];  // source node
            int v = edge[1];  // destination node
            graph[u].add(v);  // directed edge: u → v
        }

        // Step 2: Create color array for all nodes
        // 0 = WHITE (not visited)
        // 1 = GRAY (currently visiting, in recursion stack)
        // 2 = BLACK (completely visited, done)
        int[] color = new int[n];

        // Step 3: Check each node
        for (int i = 0; i < n; i++) {
            // If node is not visited (WHITE), start DFS from it
            if (color[i] == 0) {
                // If cycle found in this DFS tree, return true
                if (dfs(i, graph, color)) {
                    return true;  // Cycle detected!
                }
            }
        }

        // Step 4: If no cycle found in any DFS, return false
        return false;
    }

    private static boolean dfs(int node, List<Integer>[] graph, int[] color) {
        // Step 1: Mark current node as GRAY (visiting)
        color[node] = 1;

        // Step 2: Explore all neighbors of this node
        for (int neighbor : graph[node]) {
            // Case 1: Neighbor is GRAY means already visited
            if (color[neighbor] == 1) {
                // We found an edge to a node that's still in the current recursion stack
                // This means: node → ... → neighbor → ... → node (CYCLE!)
                return true;
            }

            // Case 2: Neighbor is WHITE (not visited)
            if (color[neighbor] == 0) {
                // Recursively explore this neighbor
                if (dfs(neighbor, graph, color)) {
                    return true;  // Cycle found in subtree
                }
            }

            // Case 3: Neighbor is BLACK (already completely visited)
            // Do nothing, no cycle through this path
        }

        // Step 3: Mark current node as BLACK (done visiting)
        // All neighbors have been explored, done with this node
        color[node] = 2;
        // No cycle found in this DFS path
        return false;
    }

    // Finds and prints the cycle path
    public static void findAndPrintCycle(int n, int[][] edges) {
        List<Integer>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int[] edge : edges) {
            graph[edge[0]].add(edge[1]);
        }

        int[] color = new int[n];
        List<Integer> path = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            if (color[i] == 0) {
                if (dfsFindCycle(i, graph, color, path)) {
                    System.out.println("Cycle found: " + path);
                    return;
                }
                path.clear();
            }
        }

        System.out.println("No cycle found");
    }

    private static boolean dfsFindCycle(int node, List<Integer>[] graph, int[] color, List<Integer> path) {
        color[node] = 1;
        path.add(node);

        for (int neighbor : graph[node]) {
            if (color[neighbor] == 1) {
                // Found cycle, print from neighbor to current
                int idx = path.indexOf(neighbor);
                System.out.print("Cycle: ");
                for (int i = idx; i < path.size(); i++) {
                    System.out.print(path.get(i) + " → ");
                }
                System.out.println(neighbor);
                return true;
            }

            if (color[neighbor] == 0) {
                if (dfsFindCycle(neighbor, graph, color, path)) {
                    return true;
                }
            }
        }

        path.remove(path.size() - 1);
        color[node] = 2;
        return false;
    }
}
