package com.backend.dsa.atoz.graphs.topologicalSort;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
//codeforces
public class C_FoxAndNames {

    private static final Scanner _scanner = new Scanner(System.in);

    public static void main(String[] args) {

        int t = 1;
        while (t-- > 0) {
            dfs();
        }

        _scanner.close();
    }

    private static void dfs() {
        int n = _scanner.nextInt();
        _scanner.nextLine();

        String[] arr = new String[n];
        for (int i = 0; i < n; i++) {
            arr[i] = _scanner.nextLine();
        }

        List<Integer>[] graph = new ArrayList[26];
        for (int j = 0; j < 26; j++) {
            graph[j] = new ArrayList<>();
        }

        boolean[][] hasEdge = new boolean[26][26];
        int[] indegree = new int[26];

        for (int j = 0; j < arr.length - 1; j++) {
            String curr = arr[j];
            String next = arr[j + 1];
            int x = 0;

            while (x < curr.length() && x < next.length()) {
                if (curr.charAt(x) != next.charAt(x)) {
                    int u = curr.charAt(x) - 'a';
                    int v = next.charAt(x) - 'a';

                    if (!hasEdge[u][v]) {
                        graph[u].add(v);
                        indegree[v]++;
                        hasEdge[u][v] = true;
                    }
                    break;
                }
                x++;
            }

            // ✅ Fix: Check if curr is longer than next
            if (x == next.length() && x < curr.length()) {
                System.out.println("Impossible");
                return;
            }
        }

        List<Integer> topoOrder = kahnSort(26, graph, indegree);

        if (topoOrder.size() != 26) {
            System.out.println("Impossible");
            return;
        }

        StringBuilder result = new StringBuilder();
        for (int node : topoOrder) {
            result.append((char) ('a' + node));
        }
        System.out.println(result);
    }

    private static List<Integer> kahnSort(int n, List<Integer>[] graph, int[] indegree) {
        Queue<Integer> dq = new ArrayDeque<>();
        for (int i = 0; i < indegree.length; i++) {
            if (indegree[i] == 0) {
                dq.offer(i);
            }
        }
        List<Integer> ans = new ArrayList<>();
        while (!dq.isEmpty()) {
            Integer poll = dq.poll();
            ans.add(poll);

            // get the nbrs
            for (Integer node : graph[poll]) {
                indegree[node]--;
                if (indegree[node] == 0) {
                    dq.offer(node);
                }
            }
        }

        if (ans.size() != n) {
            return new ArrayList<>();
        }
        return ans;
    }
}
