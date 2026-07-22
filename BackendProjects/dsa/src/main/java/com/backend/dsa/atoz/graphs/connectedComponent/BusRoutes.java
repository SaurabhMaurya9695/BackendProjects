package com.backend.dsa.atoz.graphs.connectedComponent;

import java.util.*;

public class BusRoutes {

    public static void main(String[] args) {

        int[][] routes = {
                { 1, 2, 7 }, { 3, 6, 7 } };

        int source = 1;
        int target = 6;

        System.out.println(numBusesToDestination(routes, source, target));
    }

    private static int numBusesToDestination(int[][] routes, int source, int target) {

        // Already at the target
        if (source == target) {
            return 0;
        }

        /*
         * Adjacency list:
         *
         * station -> buses passing through the station
         *
         * 1 -> [0]
         * 2 -> [0]
         * 3 -> [1]
         * 6 -> [1]
         * 7 -> [0, 1]
         */

        Map<Integer, List<Integer>> adj = new HashMap<>();

        // Create the adjacency list
        for (int bus = 0; bus < routes.length; bus++) {
            for (int station : routes[bus]) {
                adj.computeIfAbsent(station, key -> new ArrayList<>()).add(bus);
            }
        }

        // Source or target is not present
        if (!adj.containsKey(source) || !adj.containsKey(target)) {
            return -1;
        }

        // Mark visited buses
        boolean[] visited = new boolean[routes.length];

        // Queue stores bus numbers
        Queue<Integer> queue = new ArrayDeque<>();

        // Add all buses available at source
        for (int bus : adj.get(source)) {
            queue.offer(bus);
            visited[bus] = true;
        }

        int busesTaken = 1;
        // Start BFS
        while (!queue.isEmpty()) {
            int size = queue.size();
            // Process the current BFS level
            while (size-- > 0) {
                int currentBus = queue.poll();
                // Visit all stations of the current bus
                for (int station : routes[currentBus]) {
                    // Target station found
                    if (station == target) {
                        return busesTaken;
                    }

                    // Check buses connected to this station
                    for (int nextBus : adj.get(station)) {
                        // Add unvisited bus
                        if (!visited[nextBus]) {
                            visited[nextBus] = true;
                            queue.offer(nextBus);
                        }
                    }
                }
            }

            // One more bus is required
            busesTaken++;
        }

        return -1;
    }
}