package com.backend.dsa.atoz.graphs.mst.krushkal;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;

public class RoadReparation_krushakal {

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

        int[][] arr = new int[m][3];
        for (int i = 0; i < m; i++) {
            int u = fs.nextInt();
            int v = fs.nextInt();
            long wt = fs.nextLong();
            arr[i] = new int[] { u, v, (int) wt };
        }

        krushkal(arr, n);
    }

    private static void krushkal(int[][] graph, int n) {
        dsu.init(n);
        // sort the array based on the wt I guess
        Arrays.sort(graph, Comparator.comparingInt(a -> a[2]));

        long mstCost = 0;
        long edgetaken = 0;
        for (int[] x : graph) {
            int u = x[0];
            int v = x[1];
            int wt = x[2];

            // we have two edges now get the parent of those edges
            long rootU = dsu.find(u);
            long rootV = dsu.find(v);

            // if they both has the same parent means they belong to same parent
            if (rootU == rootV) {
                continue;
            } else {
                mstCost += wt;
                edgetaken++;
                dsu.union(u, v); // add a edges from u -> v
            }
        }

        if (edgetaken != n - 1) {
            System.out.println("IMPOSSIBLE");
        } else {
            System.out.println(mstCost);
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

    public static class dsu {

        private static long[] parent;
        private static long[] size;

        private static void init(int n) {
            parent = new long[n + 1];
            size = new long[n + 1];
            for (int i = 1; i <= n; i++) {
                parent[i] = i; // every parent has its own parent
                size[i] = 1; // size means every group has size one initially
            }
        }

        private static long find(long x) {
            if (parent[(int) x] == x) {
                return (long) x;
            }
            return parent[(int) x] = find(parent[(int) x]);
        }

        private static void union(int x, int y) {
            long rootX = find(x);
            long rootY = find(y);

            if (rootX == rootY) {
                // if they both values has the same root
                // it means they can create cycle
                return;
            }

            // add smaller element (node) into a large element (root which has multiple nodes)
            if (size[(int) rootX] < size[(int) rootY]) {
                parent[(int) rootX] = rootY;
                size[(int) rootY] += size[(int) rootX];
            } else {
                parent[(int) rootY] = rootX;
                size[(int) rootX] += size[(int) rootY];
            }
        }
    }
}
