package com.backend.dsa.atoz.recursion;

import java.util.ArrayList;
import java.util.List;

public class MazePath {

    public MazePath(int[][] arr) {
        int n = arr.length - 1;
        int m = arr[0].length - 1;
        System.out.println("These are the ways to reach at end : " + findWayToReachAtEnd(arr, 0, 0, n, m));
    }

    private List<String> findWayToReachAtEnd(int[][] arr, int i, int j, int n, int m) {

        if (i == n && j == m) {
            List<String> list = new ArrayList<>();
            list.add("");
            return list;
        }

        List<String> rightCall = (i < n) ? findWayToReachAtEnd(arr, i + 1, j, n, m) : new ArrayList<>();
        List<String> leftCall = (j < m) ? findWayToReachAtEnd(arr, i, j + 1, n, m) : new ArrayList<>();

        List<String> ways = new ArrayList<>();
        for (int row = 0; row < rightCall.size(); row++) {
            ways.add("H" + rightCall.get(row));
        }

        for (int col = 0; col < leftCall.size(); col++) {
            ways.add("V" + leftCall.get(col));
        }

        return ways;
    }
}
