package com.backend.dsa.atoz.graphs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;

public class isGraphBipartite_10 {

    public static void main(String[] args) {
        /*
                Graph Representation:

                        0 ----- 1
                        |       |
                        |       |
                        3 ----- 2

                This graph is Bipartite
         */

        int[][] graph = {
                { 1, 3 }, // neighbours of 0
                { 0, 2 }, // neighbours of 1
                { 1, 3 }, // neighbours of 2
                { 0, 2 }  // neighbours of 3
        };

        System.out.println(isBipartite(graph));
    }

    public static boolean isBipartite(int[][] graph) {
        int V = graph.length;
        // adjacency list
        ArrayList<Edge>[] adj = new ArrayList[V];
        // initialize adjacency list
        for (int i = 0; i < V; i++) {
            adj[i] = new ArrayList<>();
        }

        /* Convert input graph into adjacency list graph[i] contains all neighbors of node i
         */
        for (int i = 0; i < V; i++) {
            for (int nbr : graph[i]) {
                adj[i].add(new Edge(i, nbr));
            }
        }

        /* visited[] stores level/color
            -1 -> not visited
             0 -> even level
             1 -> odd level
         */
        int[] visited = new int[V];
        Arrays.fill(visited, -1);
        /*   Graph may contain multiple disconnected components. So we must start BFS from every unvisited node.
         */
        for (int i = 0; i < V; i++) {
            if (visited[i] == -1) {
                if (!bfs(adj, i, visited)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * BFS traversal to check Bipartite property.
     */
    private static boolean bfs(ArrayList<Edge>[] adj, int src, int[] visited) {
        ArrayDeque<Pair> q = new ArrayDeque<>();
        // source node starts at level 0
        q.add(new Pair(src, 0));
        while (!q.isEmpty()) {
            Pair pair = q.removeFirst();
            /* If node already visited, check whether parity(level) matches.
             */
            if (visited[pair.src] != -1) {
                /* Different parity means conflict.
                        Example:
                                previously node was at even level
                                now reached again at odd level
                        => odd cycle exists
                        => graph is NOT bipartite
                 */
                if (visited[pair.src] % 2 != pair.level % 2) {
                    return false;
                }
                continue;
            }

            //Mark node visited with current level.
            visited[pair.src] = pair.level;

            for (Edge edge : adj[pair.src]) {
                // only add unvisited neighbours
                if (visited[edge.nbr] == -1) {
                    q.add(new Pair(edge.nbr, pair.level + 1));
                }
            }
        }
        return true;
    }

    /**
     * Pair class used in BFS queue.
     * <p>
     * src   -> current node
     * level -> BFS level
     */
    private static class Pair {

        int src;
        int level;

        Pair(int src, int level) {
            this.src = src;
            this.level = level;
        }
    }

    /**
     * Edge representation.
     */
    private static class Edge {

        int src;
        int nbr;

        Edge(int src, int nbr) {
            this.src = src;
            this.nbr = nbr;
        }
    }
}