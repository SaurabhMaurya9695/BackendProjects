package com.backend.dsa.atoz.hashmapAndHeaps;

import java.util.HashMap;

/**
 * There is a forest with an unknown number of rabbits. We asked n rabbits, "How many rabbits have the same color as
 * you?" and collected the answers in an integer array answers where answers[i] is the answer of the ith rabbit.
 * <p>
 * Given the array answers, return the minimum number of rabbits that could be in the forest.
 */
public class RabbitsInForest_26 {

    public RabbitsInForest_26(int[] rabbits) {
        solve(rabbits);
    }

    private void solve(int[] arr) {
        int n = arr.length;
        HashMap<Integer, Integer> mp = new HashMap<>();
        int ans = 0;
        for (Integer x : arr) {
            mp.put(x, mp.getOrDefault(x, 0) + 1);
        }

        for (int x : mp.keySet()) {
            int group = x;
            int reportees = mp.get(x);
            int numberOfGroups = (int) Math.ceil((reportees * 1.0) / (group * 1.0));
            ans += numberOfGroups * group;
        }

        System.out.println("Minimum no of rabbits would be : " + ans);
    }
}
