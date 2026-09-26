package com.backend.dsa.atoz.graphs.floydWarshall;

import java.util.*;

public class ShortesRoutesII {

    private final static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        long n = scanner.nextLong();
        long m = scanner.nextLong();
        long q = scanner.nextLong();

        long INF = (long) 1e18;

        long[][] dist = new long[Math.toIntExact(n + 1)][Math.toIntExact(n + 1)];
        // Initialize distance matrix
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (i == j) {
                    dist[i][j] = 0; // distance of self is 0
                } else {
                    dist[i][j] = INF;
                }
            }
        }

        // Build graph
        for (long i = 0; i < m; i++) {
            long u = scanner.nextLong();
            long v = scanner.nextLong();
            long wt = scanner.nextLong();
            // Multiple edges can exist
            dist[(int) u][(int) v] = Math.min(dist[(int) u][(int) v], wt);
            dist[(int) v][(int) u] = Math.min(dist[(int) v][(int) u], wt);
        }

        // Floyd-Warshall
        for (long k = 1; k <= n; k++) {
            for (long i = 1; i <= n; i++) {
                for (long j = 1; j <= n; j++) {
                    if (dist[Math.toIntExact(i)][Math.toIntExact(k)] != INF && dist[Math.toIntExact(k)][Math.toIntExact(
                            j)] != INF) {
                        dist[Math.toIntExact(i)][Math.toIntExact(j)] = Math.min(
                                dist[Math.toIntExact(i)][Math.toIntExact(j)],
                                dist[Math.toIntExact(i)][Math.toIntExact(k)] + dist[Math.toIntExact(k)][Math.toIntExact(
                                        j)]);
                    }
                }
            }
        }

        // Answer queries
        for (int i = 0; i < q; i++) {
            long a = scanner.nextInt();
            long b = scanner.nextInt();
            if (dist[Math.toIntExact(a)][Math.toIntExact(b)] == INF) {
                System.out.println(-1);
            } else {
                System.out.println(dist[Math.toIntExact(a)][Math.toIntExact(b)]);
            }
        }

        scanner.close();
    }
}