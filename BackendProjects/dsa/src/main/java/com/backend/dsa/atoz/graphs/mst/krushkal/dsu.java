package com.backend.dsa.atoz.graphs.mst.krushkal;

public class dsu {

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
