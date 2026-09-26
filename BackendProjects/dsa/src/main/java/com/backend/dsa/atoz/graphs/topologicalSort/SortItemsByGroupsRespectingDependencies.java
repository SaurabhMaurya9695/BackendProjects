package com.backend.dsa.atoz.graphs.topologicalSort;

import java.util.*;

public class SortItemsByGroupsRespectingDependencies {

    public static void main(String[] args) {
        int n = 8;
        int m = 2;
        int[] group = { -1, -1, 1, 0, 0, 1, 0, -1 };
        List<List<Integer>> beforeItems = List.of(List.of(), List.of(6), List.of(5), List.of(6), List.of(3, 6),
                List.of(), List.of(), List.of());
        System.out.println(Arrays.toString(sortItems(n, m, group, beforeItems)));
    }

    public static int[] sortItems(int n, int m, int[] group, List<List<Integer>> beforeItems) {
        // Items with group = -1 don't belong to any real group
        // Assign each to a unique virtual group (treats them as separate groups)
        int virtualGroupId = m;
        for (int i = 0; i < n; i++) {
            if (group[i] == -1) {
                group[i] = virtualGroupId++;  // m, m+1, m+2, ...
            }
        }

        // 2 3 1 0 0 1 0 4
        int totalGroups = virtualGroupId;

        // create a graph
        List<Integer>[] itemGraph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            itemGraph[i] = new ArrayList<>();
        }

        // For each item, process its dependencies
        int[] itemIndegree = new int[n];
        for (int i = 0; i < n; i++) {
            for (int prevItem : beforeItems.get(i)) {
                // prevItem must come BEFORE item i
                itemGraph[prevItem].add(i);      // Edge: prevItem → i
                itemIndegree[i]++;               // i has 1 more dependency
            }
        }

        // create a dependency graph so that we know which item needs to be pick first
        List<Integer>[] groupGraph = new ArrayList[totalGroups];
        int[] groupIndegree = new int[totalGroups];
        for (int i = 0; i < totalGroups; i++) {
            groupGraph[i] = new ArrayList<>();
        }

        // Track which edges we've added (avoid duplicates)
        Set<String> groupEdgeExists = new HashSet<>();

        // If item A (in group X) depends on item B (in group Y)
        // Then group X depends on group Y
        for (int i = 0; i < n; i++) {
            for (int prevItem : beforeItems.get(i)) {
                int prevGroup = group[prevItem];
                int currGroup = group[i];

                // Create a unique key for this edge
                String edgeKey = prevGroup + "," + currGroup;

                if (prevGroup != currGroup && !groupEdgeExists.contains(edgeKey)) {
                    groupGraph[prevGroup].add(currGroup);
                    groupIndegree[currGroup]++;
                    groupEdgeExists.add(edgeKey);
                }
            }
        }

        // Find the order in which to process groups
        List<Integer> groupOrder = topologicalSort(totalGroups, groupGraph, groupIndegree.clone());
        // If we couldn't sort all groups, there's a cycle
        if (groupOrder.size() != totalGroups) {
            return new int[0];
        }

        // we get the order in which we need to processed the group
        List<Integer> result = new ArrayList<>();
        int[] processedIndegree = itemIndegree.clone();

        // Process groups in their topological order
        for (int groupId : groupOrder) {
            // Collect all items in this group
            List<Integer> itemsInGroup = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if (group[i] == groupId) {
                    itemsInGroup.add(i);
                }
            }

            // Topological sort items within this group - if we did the topo sort earlier without finding the
            // group order, that would've given a wrong ans
            Queue<Integer> q = new ArrayDeque<>();

            // Add items with indegree 0 (no dependencies)
            for (int item : itemsInGroup) {
                if (processedIndegree[item] == 0) {
                    q.offer(item);
                }
            }

            // Process items (Kahn's algorithm)
            while (!q.isEmpty()) {
                int item = q.poll();
                result.add(item);

                // For each item that depends on this one
                for (int nextItem : itemGraph[item]) {
                    processedIndegree[nextItem]--;

                    // Only add to queue if:
                    // 1. Its indegree becomes 0 (all dependencies done)
                    // 2. It's in the SAME group (we process groups separately)
                    if (processedIndegree[nextItem] == 0 && group[nextItem] == groupId) {
                        q.offer(nextItem);
                    }
                }
            }
        }

        // If we didn't process all items, there's a cycle
        if (result.size() != n) {
            return new int[0];  // Cycle detected
        }

        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            ans[i] = result.get(i);
        }
        return ans;
    }

    // Helper: Topological Sort using Kahn's Algorithm
    private static List<Integer> topologicalSort(int n, List<Integer>[] graph, int[] indegree) {
        Queue<Integer> q = new ArrayDeque<>();
        // Add all nodes with indegree 0
        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) {
                q.offer(i);
            }
        }

        List<Integer> result = new ArrayList<>();
        // Process nodes
        while (!q.isEmpty()) {
            int node = q.poll();
            result.add(node);

            // For each dependent node
            for (int neighbor : graph[node]) {
                indegree[neighbor]--;
                if (indegree[neighbor] == 0) {
                    q.offer(neighbor);
                }
            }
        }

        return result;
    }
}
