package com.backend.dsa.atoz.graphs.bellmanFord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class CycleFinding_04 {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        long[][] graph = new long[m][3];
        for (int i = 0; i < m; i++) {
            long u = scanner.nextLong();
            long v = scanner.nextLong();
            long wt = scanner.nextLong();
            graph[i] = new long[] { u, v, wt };
        }

        findCycle(n, m, graph);
        scanner.close();
    }

    private static void findCycle(int n, int m, long[][] graph) {
        long[] dist = new long[n + 1];
        // Super-source idea:
        // Every vertex starts with distance 0.
        Arrays.fill(dist, 0);
        long[] parent = new long[n + 1];
        Arrays.fill(parent, -1);

        long cycleNode = -1;
        // V - 1 passes
        for (int i = 0; i < n - 1; i++) {
            for (long[] x : graph) {
                long u = x[0];
                long v = x[1];
                long wt = x[2];
                if (dist[(int) u] + wt < dist[(int) v]) {
                    dist[(int) v] = dist[(int) u] + wt;
                    parent[(int) v] = u;
                }
            }
        }

        // Extra pass to detect negative cycle
        for (long[] x : graph) {
            long u = x[0];
            long v = x[1];
            long wt = x[2];
            if (dist[(int) u] + wt < dist[(int) v]) {
                // Negative cycle detected
                cycleNode = v;
                parent[(int) v] = u;
                break;
            }
        }

        // No negative cycle
        if (cycleNode == -1) {
            System.out.println("NO");
            return;
        }

        // Move inside the cycle
        for (int i = 0; i < n; i++) {
            cycleNode = parent[(int) cycleNode];
        }

        long start = cycleNode;
        List<Long> cycle = new ArrayList<>();
        cycle.add(start);
        cycleNode = parent[(int) start];
        while (cycleNode != start) {
            cycle.add(cycleNode);
            cycleNode = parent[(int) cycleNode];
        }

        // parent[] walks backwards, so reverse it
        Collections.reverse(cycle);
        cycle.add(cycle.get(0));
        System.out.println("YES");
        for (long node : cycle) {
            System.out.print(node + " ");
        }

        System.out.println();
    }
}