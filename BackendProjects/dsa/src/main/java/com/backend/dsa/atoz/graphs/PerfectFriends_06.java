package com.backend.dsa.atoz.graphs;

import java.util.ArrayList;

public class PerfectFriends_06 {

    // You have to find in how many ways can we select a pair of students such that both students can belong to
    // different clubs

    // given n = number of students
    // k = number of clubs

    public static void main(String[] args) {
        int n = 7;
        int k = 5;
        int[][] arr = { { 0, 1 }, { 2, 3 }, { 4, 5 }, { 5, 6 }, { 4, 6 } };

        System.out.println(countNoOfWaysOfClub(n, k, arr));
    }

    private static int countNoOfWaysOfClub(int n, int k, int[][] arr) {
        // first prepare a graph
        ArrayList<Edge>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int i = 0; i < arr.length; i++) {
            int src = arr[i][0];
            int dest = arr[i][1];
            graph[src].add(new Edge(src, dest));
            graph[dest].add(new Edge(dest, src));
        }

        // graph is ready, now get all connected component list

        // we need a visited list as well
        boolean[] vis = new boolean[n];
        ArrayList<ArrayList<Integer>> connectedComponentList = getConnectedComponentList(graph, n, vis);

        int pairs = 0;
        // now we have connected list here
        for (int i = 0; i < connectedComponentList.size(); i++) {
            for (int j = i + 1; j < connectedComponentList.size(); j++) {
                ArrayList<Integer> l1 = connectedComponentList.get(i);
                ArrayList<Integer> l2 = connectedComponentList.get(j);
                pairs += (l1.size() * l2.size());
            }
        }
        return pairs;
    }

    private static ArrayList<ArrayList<Integer>> getConnectedComponentList(ArrayList<Edge>[] graph, int n,
            boolean[] vis) {
        ArrayList<ArrayList<Integer>> res = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            // try to get it from all possible list
            if (!vis[i]) {
                ArrayList<Integer> lst = new ArrayList<>();
                dfs(graph, n, vis, i, lst);
                res.add(lst);
            }
        }
        return res;
    }

    private static void dfs(ArrayList<Edge>[] graph, int n, boolean[] vis, int src, ArrayList<Integer> lst) {
        lst.add(src);
        vis[src] = true;
        for (Edge edge : graph[src]) {
            int nbr = edge.nbr;
            // before going towards nbr check for vis
            if (!vis[nbr]) {
                dfs(graph, n, vis, nbr, lst);
            }
        }
    }
}
