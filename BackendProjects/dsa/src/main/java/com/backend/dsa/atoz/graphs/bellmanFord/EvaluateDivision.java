package com.backend.dsa.atoz.graphs.bellmanFord;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;

public class EvaluateDivision {

    public static void main(String[] args) {
        List<List<String>> equations = new ArrayList<>();
        equations.add(List.of("a", "b"));
        equations.add(List.of("b", "c"));

        double[] values = { 2.0, 3.0 };

        List<List<String>> queries = new ArrayList<>();
        queries.add(List.of("a", "c"));
        queries.add(List.of("b", "a"));
        queries.add(List.of("a", "e"));
        queries.add(List.of("a", "a"));
        queries.add(List.of("x", "x"));

        double[] x = calcEquation(equations, values, queries);

        for (int i = 0; i < x.length; i++) {
            System.out.println(x[i] + " ");
        }
    }

    public static double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {

        // this ques doesn't ask about any shortest path
        // so it means normal dfs will work here

        HashMap<String, List<Pair>> adj = new HashMap<>();
        for (List<String> equation : equations) {
            String u = equation.get(0);
            String v = equation.get(1);
            adj.putIfAbsent(u, new ArrayList<>());
            adj.putIfAbsent(v, new ArrayList<>());
        }

        for (int i = 0; i < equations.size(); i++) {
            String u = equations.get(i).get(0);
            String v = equations.get(i).get(1);
            double wt = values[i];
            adj.get(u).add(new Pair(v, wt));
            adj.get(v).add(new Pair(u, 1.0 / wt));
        }

        double[] ans = new double[queries.size()];

        // now we have a graph ready then
        // {x,y,wt} in one direction and
        // opposite direction {x,y,1/wt}

        for (int i = 0; i < queries.size(); i++) {
            String src = queries.get(i).get(0);
            String dest = queries.get(i).get(1);
            HashSet<String> vis = new HashSet<>();
            if (!adj.containsKey(src)) {
                ans[i] = -1.0;
            } else {
                ans[i] = dfs(src, dest, adj, vis);
            }
        }

        return ans;
    }

    private static double dfs(String src, String dest, HashMap<String, List<Pair>> adj, HashSet<String> vis) {
        Queue<Pair> q = new ArrayDeque<>();
        q.add(new Pair(src, 1.0));
        vis.add(src);
        while (!q.isEmpty()) {
            Pair top = q.poll();
            String v = top.x;
            double wt = top.y;

            if (v.equals(dest)) {
                return wt;
            }

            for (Pair nbr : adj.get(v)) {
                if (!vis.contains(nbr.x)) {
                    vis.add(nbr.x);
                    q.add(new Pair(nbr.x, wt * nbr.y));
                }
            }
        }

        return -1.0;
    }

    private static class Pair {

        String x;
        double y;

        Pair() {
        }

        Pair(String x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}