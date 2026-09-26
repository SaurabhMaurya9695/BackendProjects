package com.backend.dsa.atoz.graphs.mst.krushkal;

import java.util.Arrays;
import java.util.Comparator;

public class RemoveMaxNumberOfEdgesToKeepGraphFullyTraversable {

    public static void main(String[] args) {
        int n = 4;
        int[][] arr = { { 3, 1, 2 }, { 3, 2, 3 }, { 1, 1, 3 }, { 1, 2, 4 }, { 1, 1, 2 }, { 2, 3, 4 } };

        System.out.println(maxNumEdgesToRemove(n, arr));
    }

    private static int maxNumEdgesToRemove(int n, int[][] arr) {

        // Type 3 should come first
        Arrays.sort(arr, Comparator.comparingInt((int[] a) -> a[0]).reversed());

        DSU alice = new DSU(n);
        DSU bob = new DSU(n);

        int edgesUsed = 0;
        for (int[] x : arr) {
            int type = x[0];
            int u = x[1];
            int v = x[2];
            if (type == 3) {
                // If both Alice and Bob are already connected,
                // this edge is useless and can be removed.
                if (alice.find(u) == alice.find(v) && bob.find(u) == bob.find(v)) {
                    continue;
                }
                alice.union(u, v);
                bob.union(u, v);
                edgesUsed++;
            }
        }

        for (int[] x : arr) {
            int type = x[0];
            int u = x[1];
            int v = x[2];

            if (type == 1) {
                // Alice only
                if (alice.find(u) != alice.find(v)) {
                    alice.union(u, v);
                    edgesUsed++;
                }
            } else if (type == 2) {
                // Bob only
                if (bob.find(u) != bob.find(v)) {
                    bob.union(u, v);
                    edgesUsed++;
                }
            }
        }

        // Both Alice and Bob must be fully connected
        if (alice.components != 1 || bob.components != 1) {
            return -1;
        }

        // Total edges - edges actually required
        return arr.length - edgesUsed;
    }

    public static class DSU {

        private int[] parent;
        private int[] size;
        private int components;

        DSU(int n) {

            parent = new int[n + 1];
            size = new int[n + 1];

            components = n;

            for (int i = 1; i <= n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        private int find(int x) {

            if (parent[x] == x) {
                return x;
            }

            return parent[x] = find(parent[x]);
        }

        private void union(int x, int y) {

            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) {
                return;
            }

            if (size[rootX] < size[rootY]) {

                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else {

                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            }

            // Two components became one
            components--;
        }
    }
}