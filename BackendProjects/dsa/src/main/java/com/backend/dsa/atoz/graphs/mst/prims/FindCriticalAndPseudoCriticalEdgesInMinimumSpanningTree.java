package com.backend.dsa.atoz.graphs.mst.prims;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class FindCriticalAndPseudoCriticalEdgesInMinimumSpanningTree {

    public static void main(String[] args) {
        int n = 5;
        int[][] edges = { { 0, 1, 1 }, { 1, 2, 1 }, { 2, 3, 2 }, { 0, 3, 2 }, { 0, 4, 3 }, { 3, 4, 3 }, { 1, 4, 6 } };
        System.out.println(findCriticalAndPseudoCriticalEdges(n, edges));
    }

    public static List<List<Integer>> findCriticalAndPseudoCriticalEdges(int n, int[][] edges) {

        List<int[]>[] graph = new List[n + 1];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int wt = edge[2];
            graph[u].add(new int[] { v, wt });
            graph[v].add(new int[] { u, wt });
        }

        // 1st get the min mst path
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.add(new int[] { 0, 0 });

        int totalMstCost = 0;
        HashSet<Integer> mst = new HashSet<>();

        while (!pq.isEmpty()) {
            int[] top = pq.poll();
            int cost = top[0];
            int node = top[1];

            if (mst.contains(node)) {
                continue;
            }

            mst.add(node);
            totalMstCost += cost;
            for (int[] nbrs : graph[node]) {
                int nbr = nbrs[0];
                int nbrWt = nbrs[1];
                if (!mst.contains(nbr)) {
                    pq.add(new int[] { nbrWt, nbr });
                }
            }
        }

        if (mst.size() != n) {
            System.out.println("-1");
        } else {
            System.out.println("mst Cost is : " + totalMstCost);
        }

        // now I have a cost then I need to find all the possible mst which has the sum as 7 and all nodes as visited
        // or added in a mst means mst.size() == n
        return new ArrayList<>();
    }
}