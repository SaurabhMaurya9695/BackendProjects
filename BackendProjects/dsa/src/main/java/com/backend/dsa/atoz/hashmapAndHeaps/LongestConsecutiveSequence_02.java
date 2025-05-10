package com.backend.dsa.atoz.hashmapAndHeaps;

import java.util.Arrays;
import java.util.HashMap;

public class LongestConsecutiveSequence_02 {

    public LongestConsecutiveSequence_02(int[] arr) {
        // longestConsecutiveSequenceMethod1(arr);
        longestConsecutiveSequenceMethod2(arr);
    }

    private void longestConsecutiveSequenceMethod2(int[] arr) {

        HashMap<Integer, Boolean> map = new HashMap<>();
        for (int x : arr) {
            map.put(x, true);
        }

        for (Integer x : map.keySet()) {
            if (map.containsKey(x - 1)) {
                map.put(x, false);
            }
        }

        // so which of them are false, it means we can't start a sequence from there
        // sequence will start from which of them remain true

        int longestLength = 0;

        for (Integer x : map.keySet()) {
            if (map.get(x)) {
                int diff = 1;
                int val = x;
                while (map.containsKey(val + diff)) {
                    diff++;
                }
                longestLength = Math.max(longestLength, diff);
            }
        }
        System.out.println("Longest Consecutive Sequence: " + longestLength
                + " with TimeLimit of [ O(N) ] and SpaceComplexity of [ O(N) ]");
    }

    private void longestConsecutiveSequenceMethod1(int[] arr) {
        Arrays.sort(arr);

        int longestLength = 1;
        int ans = 1;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] - arr[i - 1] == 1) {
                longestLength++;
                ans = Math.max(ans, longestLength);
            } else {
                longestLength = 1;
            }
        }

        System.out.println("Longest length of consecutive subsequence is : " + ans + " with Time Limit of "
                + "[ O(nlog(n) + O(N) ]");
    }
}
