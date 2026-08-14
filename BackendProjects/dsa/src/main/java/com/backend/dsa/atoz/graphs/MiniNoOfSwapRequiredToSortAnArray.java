package com.backend.dsa.atoz.graphs;

import java.util.Arrays;
import java.util.Comparator;

public class MiniNoOfSwapRequiredToSortAnArray {

    public static void main(String[] args) {
        int[] arr = { 1, 4, 3, 2 };
        System.out.println(minSwaps(arr));
    }

    static class Pair {

        int value;
        int index;

        Pair(int value, int index) {
            this.value = value;
            this.index = index;
        }
    }

    static int minSwaps(int[] arr) {

        int n = arr.length;

        Pair[] p = new Pair[n];

        for (int i = 0; i < n; i++) {
            p[i] = new Pair(arr[i], i);
        }

        Arrays.sort(p, Comparator.comparingInt(a -> a.value));

        boolean[] vis = new boolean[n];

        int swaps = 0;

        for (int i = 0; i < n; i++) {

            if (vis[i] || p[i].index == i) {
                continue;
            }

            int cycle = 0;
            int j = i;
            while (!vis[j]) {
                vis[j] = true;
                j = p[j].index;
                cycle++;
            }

            swaps += (cycle - 1);
        }

        return swaps;
    }
}
