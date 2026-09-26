package com.backend.dsa.atoz.graphs.mst.prims;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class RoadReparation {

    static final FastScanner fs = new FastScanner();
    static final PrintWriter out = new PrintWriter(System.out);

    public static void main(String[] args) throws Exception {

        int t = 1;
        // t = fs.nextInt();

        while (t-- > 0) {
            solve();
        }

        out.flush();
    }

    static void solve() throws Exception {

        int n = fs.nextInt();
        int m = fs.nextInt();
        List<int[]>[] graph = new ArrayList[n + 1];
        for (int i = 0; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }

        int i = 0;
        HashSet<Integer> mst = new HashSet<>();
        while (m-- > 0) {

            int u, v;
            long wt;

            u = fs.nextInt();
            v = fs.nextInt();
            wt = fs.nextLong();

            graph[u].add(new int[] { v, (int) wt });
            graph[v].add(new int[] { u, (int) wt });
        }

        mstFun(graph, mst, n);
    }

    private static void mstFun(List<int[]>[] graph, HashSet<Integer> mst, int V) {
        // {weight, node}
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.add(new int[] { 0, 1 });
        long mstCost = 0;
        while (!pq.isEmpty()) {
            int[] top = pq.poll();
            int distance = top[0];
            int nodeIdx = top[1];

            if (mst.contains(nodeIdx)) {
                continue;
            }

            mst.add(nodeIdx);

            mstCost += distance;

            // now from current nodeIdx go to all neighbours
            // and try to add the minimum cost edge

            for (int[] nbr : graph[nodeIdx]) {
                int nextNode = nbr[0];
                int nextWt = nbr[1];
                if (mst.contains(nextNode)) {
                    continue;
                }

                pq.add(new int[] { nextWt, nextNode });
            }
        }

        if (mst.size() != V) {
            out.println("IMPOSSIBLE");
        } else {
            out.println(mstCost);
        }
    }

    static class FastScanner {

        private final BufferedInputStream in = new BufferedInputStream(System.in);

        private final byte[] buffer = new byte[1 << 16];

        private int ptr = 0, len = 0;

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

        String next() throws IOException {

            int c;

            while ((c = read()) <= ' ') {

                if (c == -1) {
                    return null;
                }
            }

            StringBuilder sb = new StringBuilder();

            while (c > ' ') {

                sb.append((char) c);
                c = read();
            }

            return sb.toString();
        }

        int nextInt() throws IOException {

            int c;

            while ((c = read()) <= ' ')
                ;

            int sign = 1;

            if (c == '-') {
                sign = -1;
                c = read();
            }

            int val = 0;

            while (c > ' ') {

                val = val * 10 + (c - '0');
                c = read();
            }

            return val * sign;
        }

        long nextLong() throws IOException {

            int c;

            while ((c = read()) <= ' ')
                ;

            int sign = 1;

            if (c == '-') {
                sign = -1;
                c = read();
            }

            long val = 0;

            while (c > ' ') {

                val = val * 10 + (c - '0');
                c = read();
            }

            return val * sign;
        }

        double nextDouble() throws IOException {
            return Double.parseDouble(next());
        }
    }
}