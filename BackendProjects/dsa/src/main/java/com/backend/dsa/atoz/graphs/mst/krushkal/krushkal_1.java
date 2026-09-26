package com.backend.dsa.atoz.graphs.mst.krushkal;

import java.util.Arrays;
import java.util.Comparator;

public class krushkal_1 {

    private static int[] parent;

    public static void main(String[] args) {
        int n = 5;
        // {u, v, weight}
        int[][] arr = { { 1, 2, 4 }, { 2, 3, 2 }, { 3, 4, 7 }, { 4, 5, 1 }, { 5, 1, 3 } };
        KruskalFun(n, arr);
    }

    private static void KruskalFun(int n, int[][] arr) {

        // ------------------------------------------------
        // STEP 1: Initialize DSU
        // ------------------------------------------------

        parent = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            parent[i] = i;
        }

        /*
            Initially:

            1 → 1
            2 → 2
            3 → 3
            4 → 4
            5 → 5

            So we have:

            {1} {2} {3} {4} {5}
        */

        // ------------------------------------------------
        // STEP 2: Sort all edges by weight
        // ------------------------------------------------

        Arrays.sort(arr, Comparator.comparingInt(a -> a[2]));

        /*
            Before:

            {1,2,4}
            {2,3,2}
            {3,4,7}
            {4,5,1}
            {5,1,3}


            After sorting:

            {4,5,1}
            {2,3,2}
            {5,1,3}
            {1,2,4}
            {3,4,7}
        */

        // ------------------------------------------------
        // STEP 3: Process edges one by one
        // ------------------------------------------------

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

    // ------------------------------------------------
    // FIND
    // ------------------------------------------------

    private static int find(int x) {
        // x is the root of its group
        if (parent[x] == x) {
            return x;
        }

        // Path compression
        return parent[x] = find(parent[x]);
    }

    // ------------------------------------------------
    // UNION
    // ------------------------------------------------

    private static void union(int rootX, int rootY) {
        // Connect the root of Y's group
        // to the root of X's group
        parent[rootY] = rootX;
    }
}
