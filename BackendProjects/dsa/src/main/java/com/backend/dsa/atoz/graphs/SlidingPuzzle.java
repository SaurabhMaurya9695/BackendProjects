package com.backend.dsa.atoz.graphs;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class SlidingPuzzle {

    public static void main(String[] args) {
        int[][] board = { { 1,2,3 }, { 5,4,0 } };
        System.out.println(slidingPuzzle(board));
    }

    public static int slidingPuzzle(int[][] board) {
        int n = board.length;
        int m = board[0].length;

        Queue<String> q = new ArrayDeque<>();
        Set<String> vis = new HashSet<>();
        String target = "123450";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                sb.append(board[i][j]);
            }
        }

        String s = sb.toString(); // "123405"
        // get the position where 0 can be swapped with indexes

        int[][] pos = { { 1, 3 }, { 0, 2, 4 }, { 1, 5 }, { 0, 4 }, { 1, 3, 5 }, { 2, 4 } };
        q.add(s);
        int ans = 0;
        while (!q.isEmpty()) {
            int size = q.size();
            while (size-- > 0) {
                // get the swapped string
                String ss = q.peek();
                q.poll();

                assert ss != null;

                if (ss.equals(target)) {
                    return ans;
                }

                int idx0 = ss.lastIndexOf('0');
                int[] hasToBeSwappedWithIdxes = pos[idx0];
                for (int i = 0; i < hasToBeSwappedWithIdxes.length; i++) {
                    String temp = ss;
                    String swappwsString = swap(temp, hasToBeSwappedWithIdxes[i], idx0);
                    if(!vis.contains(swappwsString)) {
                        q.add(swappwsString);
                        vis.add(swappwsString);
                    }
                }
            }
            ans++;
        }

        return -1 ;
    }

    static String swap(String s, int i, int j) {
        char[] arr = s.toCharArray();
        char temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        return new String(arr);
    }
}
