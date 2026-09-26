package com.backend.dsa.atoz.graphs.mst.krushkal;

import java.util.Arrays;
import java.util.Comparator;

public class krushkal_2_optimized {

    public static void main(String[] args) {
        int n = 5;
        int[][] edges = {
                { 1, 2, 3 }, { 1, 3, 8 }, { 2, 3, 2 }, { 2, 4, 5 }, { 3, 4, 1 }, { 4, 5, 7 }, { 3, 5, 9 } };
        KruskalFun(n, edges);
    }

    private static int[] parent;
    private static int[] size;

    private static void init(int n) {
        parent = new int[n + 1];
        size = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            parent[i] = i; // every parent has its own parent
            size[i] = 1; // size means every group has size one initially
        }
    }

    private static void KruskalFun(int n, int[][] arr) {
        init(n);
        Arrays.sort(arr, Comparator.comparingInt(a -> a[2]));
        int mstCost = 0;
        int edgesTaken = 0;

        for (int[] edge : arr) {
            int u = edge[0];
            int v = edge[1];
            int wt = edge[2];

            // Find which group u belongs to
            int rootU = find(u);
            // Find which group v belongs to
            int rootV = find(v);

            // ------------------------------------------------
            // If both belong to same group:
            // adding this edge creates a cycle
            // ------------------------------------------------

            if (rootU == rootV) {
                continue;
            }

            // ------------------------------------------------
            // Different groups:
            // safely connect them
            // ------------------------------------------------

            union(rootU, rootV);
            mstCost += wt;
            edgesTaken++;
            System.out.println("Taking edge: " + u + " - " + v + " with weight " + wt);
            // MST of n vertices always has n - 1 edges
            if (edgesTaken == n - 1) {
                break;
            }
        }

        // ------------------------------------------------
        // STEP 4: Check if MST was possible
        // ------------------------------------------------

        if (edgesTaken != n - 1) {
            System.out.println("IMPOSSIBLE");
        } else {
            System.out.println("MST Cost = " + mstCost);
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
            // if they both values has the same root
            // it means they can create cycle
            return;
        }

        // add smaller element (node) into a large element (root which has multiple nodes)
        if (size[rootX] < size[rootY]) {
            parent[rootX] = rootY;
            size[rootY] += size[rootX];
        } else {
            parent[rootY] = rootX;
            size[rootX] += size[rootY];
        }
    }
}
