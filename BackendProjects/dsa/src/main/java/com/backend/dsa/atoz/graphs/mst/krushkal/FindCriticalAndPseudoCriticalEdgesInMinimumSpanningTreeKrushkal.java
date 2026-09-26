package com.backend.dsa.atoz.graphs.mst.krushkal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FindCriticalAndPseudoCriticalEdgesInMinimumSpanningTreeKrushkal {

    public static void main(String[] args) {
        int n = 5;
        int[][] edges = { { 0, 1, 1 }, { 1, 2, 1 }, { 2, 3, 2 }, { 0, 3, 2 }, { 0, 4, 3 }, { 3, 4, 3 }, { 1, 4, 6 } };
        System.out.println(findCriticalAndPseudoCriticalEdges(n, edges));
    }

/*
               Edge E
                 |
       ┌─────────┴─────────┐
       ↓                   ↓
    SKIP E              FORCE E
       ↓                   ↓
   Build MST            Build MST
       ↓                   ↓
   cost > original?    cost == original?
   YES → Critical      YES → Pseudo-critical

*/

    public static List<List<Integer>> findCriticalAndPseudoCriticalEdges(int n, int[][] edges) {
        int[][] newEdges = new int[edges.length][4];
        for (int i = 0; i < edges.length; i++) {
            newEdges[i][0] = edges[i][0];
            newEdges[i][1] = edges[i][1];
            newEdges[i][2] = edges[i][2];

            // remember original position
            newEdges[i][3] = i;
        }

        // Kruskal -> sort by weight
        Arrays.sort(newEdges, Comparator.comparingInt(a -> a[2]));
        int originalMstCost = kruskal(n, newEdges, -1, -1);

        List<Integer> critical = new ArrayList<>();
        List<Integer> pseudoCritical = new ArrayList<>();

        for (int i = 0; i < newEdges.length; i++) {
            int originalIndex = newEdges[i][3];
            int costWithoutEdge = kruskal(n, newEdges, i, -1); // skip this edge

            /*
             * If:
             *
             * costWithoutEdge > originalMstCost
             *
             * OR no MST can be formed
             *
             * then this edge is CRITICAL.
             */

            if (costWithoutEdge > originalMstCost) {
                critical.add(originalIndex);
            } else {
                int costWithEdge = kruskal(n, newEdges, -1, i);

                /*
                 * If forcing this edge still gives
                 * the original MST cost,
                 *
                 * then this edge can belong to
                 * an MST.
                 */
                if (costWithEdge == originalMstCost) {
                    pseudoCritical.add(originalIndex);
                }
            }
        }

        List<List<Integer>> answer = new ArrayList<>();
        answer.add(critical);
        answer.add(pseudoCritical);
        return answer;
    }

    private static int kruskal(int n, int[][] edges, int skipEdge, int forceEdge) {
        DSU.init(n);
        int mstCost = 0;
        int edgesTaken = 0;

        if (forceEdge != -1) {
            int[] edge = edges[forceEdge];
            int u = edge[0];
            int v = edge[1];
            int wt = edge[2];
            DSU.union(u, v);
            mstCost += wt;
            edgesTaken++;
        }

        for (int i = 0; i < edges.length; i++) {
            // Don't use the skipped edge
            if (i == skipEdge) {
                continue;
            }

            // Don't add forced edge twice
            if (i == forceEdge) {
                continue;
            }

            int[] edge = edges[i];
            int u = edge[0];
            int v = edge[1];
            int wt = edge[2];
            int rootU = DSU.find(u);
            int rootV = DSU.find(v);
            // Same group -> cycle
            if (rootU == rootV) {
                continue;
            }

            // Different groups -> safely connect
            DSU.union(u, v);
            mstCost += wt;
            edgesTaken++;

            // MST has exactly n - 1 edges
            if (edgesTaken == n - 1) {
                break;
            }
        }

        // Could not connect all vertices
        if (edgesTaken != n - 1) {
            return Integer.MAX_VALUE;
        }

        return mstCost;
    }

    public static class DSU {

        private static int[] parent;
        private static int[] size;

        private static void init(int n) {
            parent = new int[n];
            size = new int[n];

            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        private static int find(int x) {
            if (parent[x] == x) {
                return x;
            }

            return parent[x] = find(parent[x]);
        }

        private static void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) {
                return;
            }

            // Union by size
            if (size[rootX] < size[rootY]) {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            }
        }
    }
}
