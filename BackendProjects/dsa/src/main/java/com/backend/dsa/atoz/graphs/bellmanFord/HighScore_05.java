package com.backend.dsa.atoz.graphs.bellmanFord;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

//https://cses.fi/problemset/task/1673/
public class HighScore_05 {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int rooms = scanner.nextInt();
        int tunnels = scanner.nextInt();
        long[][] graph = new long[tunnels][3];
        for (int i = 0; i < tunnels; i++) {
            long u = scanner.nextLong();
            long v = scanner.nextLong();
            long wt = scanner.nextLong();
            graph[i] = new long[] { u, v, wt };
        }

        maxScore(rooms, tunnels, graph);
        scanner.close();
    }

    private static void maxScore(int n, int m, long[][] graph) {
        long NEG_INF = Long.MIN_VALUE / 4;
        long[] dist = new long[n + 1];
        Arrays.fill(dist, NEG_INF);
        dist[1] = 0;

        /*
         * Reverse graph
         *
         * Original:
         * u -> v
         *
         * Reverse:
         * v -> u
         *
         * We will start BFS from n to find
         * which nodes can eventually reach n.
         */
        ArrayList<Integer>[] reverseGraph = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) {
            reverseGraph[i] = new ArrayList<>();
        }

        for (long[] x : graph) {
            int u = (int) x[0];
            int v = (int) x[1];
            reverseGraph[v].add(u);
        }

        /*
         * BFS from n in the reverse graph.
         *
         * canReachEnd[x] == true means:
         * In the original graph, x can reach n.
         */
        boolean[] canReachEnd = new boolean[n + 1];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(n);
        canReachEnd[n] = true;
        while (!queue.isEmpty()) {
            int current = queue.poll();
            for (int next : reverseGraph[current]) {
                if (!canReachEnd[next]) {
                    canReachEnd[next] = true;
                    queue.add(next);
                }
            }
        }

        /*
         * V - 1 passes
         */
        for (int i = 0; i < n - 1; i++) {
            for (long[] x : graph) {
                int u = (int) x[0];
                int v = (int) x[1];
                long wt = x[2];
                if (dist[u] != NEG_INF) {
                    dist[v] = Math.max(dist[v], dist[u] + wt);
                }
            }
        }

        /*
         * Extra pass to detect a positive cycle.
         *
         * If we can still increase dist[v], then
         * there is a positive cycle reachable from 1.
         *
         * But the cycle only matters if v can eventually
         * reach n.
         */
        for (long[] x : graph) {
            int u = (int) x[0];
            int v = (int) x[1];
            long wt = x[2];
            if (dist[u] != NEG_INF && dist[u] + wt > dist[v] && canReachEnd[v]) {
                System.out.println(-1);
                return;
            }
        }

        System.out.println(dist[n]);
    }
}