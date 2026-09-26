package com.backend.dsa.atoz.graphs.mst.prims;

import java.io.*;
import java.util.*;

public class DShichikujiandPowerGrid {

    static final FastScanner fs = new FastScanner();
    static final PrintWriter out = new PrintWriter(System.out);

    public static void main(String[] args) throws Exception {

        int n = fs.nextInt();

        int[][] points = new int[n][2];

        for (int i = 0; i < n; i++) {
            points[i][0] = fs.nextInt();
            points[i][1] = fs.nextInt();
        }

        long[] c = new long[n];

        for (int i = 0; i < n; i++) {
            c[i] = fs.nextLong();
        }

        long[] k = new long[n];

        for (int i = 0; i < n; i++) {
            k[i] = fs.nextLong();
        }

        solve(n, points, c, k);

        out.flush();
    }

    private static void solve(int n, int[][] points, long[] c, long[] k) {

        boolean[] visited = new boolean[n];

        // {cost, node}
        PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(a -> a[0]));

        /*
         * We can think of building a power station
         * as connecting the city to a virtual source.
         *
         * For Prim, initially we can choose any city.
         *
         * Its cost of being connected to the source
         * is simply c[i].
         *
         * So we push every city with its power station cost.
         */
        for (int i = 0; i < n; i++) {
            pq.add(new long[] { c[i], i });
        }

        long totalCost = 0;

        List<Integer> powerStations = new ArrayList<>();
        List<int[]> connections = new ArrayList<>();

        while (!pq.isEmpty()) {

            long[] top = pq.poll();

            long cost = top[0];
            int node = (int) top[1];

            if (visited[node]) {
                continue;
            }

            visited[node] = true;

            totalCost += cost;

            /*
             * If cost == c[node], we can interpret
             * this as building a power station.
             *
             * Otherwise, this city was connected
             * through another city.
             */
            if (cost == c[node]) {
                powerStations.add(node + 1);
            }

            /*
             * Try connecting this city to every
             * unvisited city.
             */
            for (int next = 0; next < n; next++) {

                if (visited[next]) {
                    continue;
                }

                long distance = Math.abs(points[node][0] - points[next][0]) + Math.abs(
                        points[node][1] - points[next][1]);

                long wireCost = distance * (k[node] + k[next]);

                pq.add(new long[] { wireCost, next });
            }
        }

        out.println(totalCost);

        out.println(powerStations.size());

        for (int city : powerStations) {
            out.print(city + " ");
        }

        out.println();

        out.println(connections.size());

        for (int[] connection : connections) {
            out.println(connection[0] + " " + connection[1]);
        }
    }

    static class FastScanner {

        private final BufferedInputStream in = new BufferedInputStream(System.in);

        private final byte[] buffer = new byte[1 << 16];

        private int ptr = 0;
        private int len = 0;

        private int read() throws IOException {

            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;

                if (len <= 0) {
                    return -1;
                }
            }

            return buffer[ptr++];
        }

        int nextInt() throws IOException {

            int c;

            while ((c = read()) <= ' ') {
                if (c == -1) {
                    return -1;
                }
            }

            int sign = 1;

            if (c == '-') {
                sign = -1;
                c = read();
            }

            int value = 0;

            while (c > ' ') {
                value = value * 10 + (c - '0');
                c = read();
            }

            return value * sign;
        }

        long nextLong() throws IOException {

            int c;

            while ((c = read()) <= ' ') {
                if (c == -1) {
                    return -1;
                }
            }

            int sign = 1;

            if (c == '-') {
                sign = -1;
                c = read();
            }

            long value = 0;

            while (c > ' ') {
                value = value * 10 + (c - '0');
                c = read();
            }

            return value * sign;
        }
    }
}