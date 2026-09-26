package com.backend.dsa.atoz.graphs.mst.prims;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class ConnectingCitiesWithMinimumCost {

    public static void main(String[] args) {
        int n = 4;
        int[][] arr = { { 1, 2, 5 }, { 1, 3, 6 }, { 2, 3, 1 }, { 2, 4, 2 }, { 3, 4, 4 } };
        System.out.println(minimumCost(n, arr));
    }

    private static int minimumCost(int n, int[][] arr) {
        List<int[]>[] graph = new List[n + 1];
        for (int i = 0; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int i = 0; i < arr.length; i++) {
            int u = arr[i][0];
            int v = arr[i][1];
            int wt = arr[i][2];
            graph[u].add(new int[] { v, wt });
            graph[v].add(new int[] { u, wt });
        }

        // we have a graph ready
        int mstCost = 0;
        HashSet<Integer> mst = new HashSet<>();
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.add(new int[] { 0, 1 });

        while (!pq.isEmpty()) {
            int[] poll = pq.poll();
            int cost = poll[0];
            int node = poll[1];

            if (mst.contains(node)) {
                continue;
            }

            mst.add(node);
            mstCost += cost;

            for (int[] nbr : graph[node]) {
                // these are nbrs
                int v = nbr[0];
                int wt = nbr[1];
                if (mst.contains(v)) {
                    continue;
                }
                pq.add(new int[] { wt, v });
            }
        }

        if (mst.size() != n) {
            return -1;
        }
        return mstCost;
    }
}
