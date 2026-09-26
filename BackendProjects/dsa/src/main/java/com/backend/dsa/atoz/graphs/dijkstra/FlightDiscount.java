package com.backend.dsa.atoz.graphs.dijkstra;

import java.io.*;
import java.util.*;

public class FlightDiscount {

    static class FastScanner {

        private final InputStream in = System.in;
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0;
        private int len = 0;

        private int read() throws IOException {

            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;

                if (len == -1) {
                    return -1;
                }
            }

            return buffer[ptr++];
        }

        long nextLong() throws IOException {

            int c;

            do {
                c = read();
            } while (c <= ' ');

            boolean negative = false;

            if (c == '-') {
                negative = true;
                c = read();
            }

            long result = 0;

            while (c > ' ') {
                result = result * 10 + (c - '0');
                c = read();
            }

            return negative ? -result : result;
        }

        int nextInt() throws IOException {
            return (int) nextLong();
        }
    }

    public static void main(String[] args) throws Exception {
        FastScanner scanner = new FastScanner();
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        List<int[]>[] graph = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }

        // Build directed graph
        for (int i = 0; i < m; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            int wt = scanner.nextInt();
            graph[u].add(new int[] { v, wt });
        }

        // dist[node][0] = coupon NOT used
        // dist[node][1] = coupon USED
        long[][] dist = new long[n + 1][2];
        long INF = Long.MAX_VALUE / 4;
        for (int i = 1; i <= n; i++) {
            dist[i][0] = INF;
            dist[i][1] = INF;
        }

        // {distance, node, couponState}
        PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(a -> a[0]));
        dist[1][0] = 0;
        // Start at node 1, coupon not used
        pq.add(new long[] { 0, 1, 0 });
        while (!pq.isEmpty()) {
            long[] current = pq.poll();
            long currentWt = current[0];
            int node = (int) current[1];
            int couponState = (int) current[2];

            // Remove stale entry
            if (currentWt > dist[node][couponState]) {
                continue;
            }

            // If destination reached WITH coupon
            if (node == n && couponState == 1) {
                System.out.println(currentWt);
                return;
            }

            for (int[] nbr : graph[node]) {
                int nbrNode = nbr[0];
                int nbrWt = nbr[1];

                // --------------------------------
                // Coupon already used
                // --------------------------------
                if (couponState == 1) {
                    long newDist = currentWt + nbrWt;
                    if (newDist < dist[nbrNode][1]) {
                        dist[nbrNode][1] = newDist;
                        pq.add(new long[] {
                                newDist, nbrNode, 1 });
                    }
                }

                // --------------------------------
                // Coupon NOT used yet
                // --------------------------------
                else {

                    // Choice 1:
                    // Don't use coupon
                    long normalDist = currentWt + nbrWt;
                    if (normalDist < dist[nbrNode][0]) {
                        dist[nbrNode][0] = normalDist;
                        pq.add(new long[] { normalDist, nbrNode, 0 });
                    }

                    // Choice 2:
                    // Use coupon on this flight
                    long discountedDist = currentWt + nbrWt / 2;
                    if (discountedDist < dist[nbrNode][1]) {
                        dist[nbrNode][1] = discountedDist;
                        pq.add(new long[] { discountedDist, nbrNode, 1 });
                    }
                }
            }
        }

        System.out.println("-1");
    }
}