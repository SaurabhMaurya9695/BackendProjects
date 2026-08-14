package com.backend.dsa.atoz.graphs;

import java.util.*;

public class MinimumNumberOfVerticesToReachAllNodes {

    public static void main(String[] args) {

        // int n = 6;
        // List<List<Integer>> edges = List.of(
        //         List.of(0, 1),
        //         List.of(0, 2),
        //         List.of(2, 5),
        //         List.of(3, 4),
        //         List.of(4, 2)
        // );

        int n = 5;
        List<List<Integer>> edges = List.of(List.of(0, 1), List.of(2, 1), List.of(3, 1), List.of(1, 4), List.of(2, 4));

        System.out.println(findSmallestSetOfVertices(n, edges));
    }

    private static List<Integer> findSmallestSetOfVertices(int n, List<List<Integer>> edges) {

        List<Integer> lst = new ArrayList<>();
        int[] indegree = new int[n];
        for (List<Integer> edge : edges) {
            int v = edge.get(1);
            indegree[v]++; // outgoing nodes
        }

        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) { // means
                lst.add(i);
            }
        }

        return lst;
    }
}