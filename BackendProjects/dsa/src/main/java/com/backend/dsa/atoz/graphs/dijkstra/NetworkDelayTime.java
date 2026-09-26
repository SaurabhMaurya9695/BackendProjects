package com.backend.dsa.atoz.graphs.dijkstra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class NetworkDelayTime {

    public static void main(String[] args) {
        int[][] times = { { 2, 1, 1 }, { 2, 3, 1 }, { 3, 4, 1 } };
        int n = 4;
        int k = 2;
        System.out.println(networkDelayTime(times, n, k));
    }

    public static int networkDelayTime(int[][] times, int n, int k) {
        List<int[]>[] graph = new ArrayList[n + 1];
        for (int i = 0; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int i = 0; i < times.length; i++) {
            int u = times[i][0];
            int v = times[i][1];
            int wt = times[i][2];

            graph[u].add(new int[] { v, wt });
        }

        // now we have a wt graph ready now.
        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MIN_VALUE);

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> {
            if (a[0] == b[0]) {
                return Integer.compare(a[1], b[1]);
            }
            return Integer.compare(a[0], b[0]);
        });

        pq.add(new int[] { 0, k });
        dist[k] = 0;

        while (!pq.isEmpty()) {
            // get the top element
            int[] top = pq.peek();
            pq.poll();
            int nodeWt = top[0];
            int node = top[1];

            if (!(dist[node] == Integer.MIN_VALUE)) {
                continue;
            }

            // if we come here it means visited
            // get the nbr
            for (int[] nbr : graph[node]) {
                if (dist[node] == Integer.MIN_VALUE) {
                    // means unvisited
                    // relax the nbr distance
                    int neighbor = nbr[0];
                    int weight = nbr[1];
                    int nbrDist = nodeWt + weight;

                    if (nbrDist < dist[neighbor]) {
                        dist[neighbor] = nbrDist;
                        pq.add(new int[] { nbrDist, neighbor });
                    }
                }
            }
        }

        // once we done with dejistra, check the distance
        for (int i = 0; i < dist.length; i++) {
            System.out.print(dist[i]);
        }
        return 1;
    }

    public int bellmonFord(int[][] times, int n, int k) {

        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);

        dist[k] = 0;

        // Bellman-Ford: V - 1 passes
        for (int i = 0; i < n - 1; i++) {
            for (int[] x : times) {
                int u = x[0];
                int v = x[1];
                int wt = x[2];

                if (dist[u] != Integer.MAX_VALUE &&
                        dist[u] + wt < dist[v]) {
                    dist[v] = dist[u] + wt;
                }
            }
        }

        int ans = 0;
        for (int i = 1; i <= n; i++) {
            // Node cannot be reached
            if (dist[i] == Integer.MAX_VALUE) {
                return -1;
            }

            ans = Math.max(ans, dist[i]);
        }

        return ans;
    }
}
