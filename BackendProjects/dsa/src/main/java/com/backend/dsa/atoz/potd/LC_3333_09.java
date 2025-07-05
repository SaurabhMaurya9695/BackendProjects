package com.backend.dsa.atoz.potd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LC_3333_09 {

    private final long M = (long) 1e9 + 7;

    public int possibleStringCount(String s, int k) {
        // Step 1: Group same consecutive characters
        int group = 1;
        List<Integer> allGroups = new ArrayList<>();

        for (int i = 0; i < s.length() - 1; i++) {
            if (s.charAt(i) == s.charAt(i + 1)) {
                group++;
            } else {
                allGroups.add(group);
                group = 1;
            }
        }
        allGroups.add(group); // Add last group

        // Step 2: Calculate total possible strings using multiplication of group sizes
        long totalPossibilities = 1;
        for (int count : allGroups) {
            totalPossibilities = (totalPossibilities * count) % M;
        }

        if (allGroups.size() >= k) {
            return (int) totalPossibilities;
        }

        // Create dp of size [groupCount][k]
        int n = allGroups.size();
        long[][] dp = new long[n][k];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dp[i], -1);
        }

        long invalid = invalidCountsWhichAreLessThanK(0, 0, allGroups, k, dp);
        return (int) ((totalPossibilities - invalid + M) % M);
    }

    private long invalidCountsWhichAreLessThanK(int i, int count, List<Integer> groups, int k, long[][] dp) {
        if (i >= groups.size()) {
            return count < k ? 1 : 0;
        }

        if (count < k && dp[i][count] != -1) {
            return dp[i][count];
        }

        long res = 0;
        int maxTake = groups.get(i);
        for (int take = 1; take <= maxTake; take++) {
            if (count + take < k) {
                res = (res + invalidCountsWhichAreLessThanK(i + 1, count + take, groups, k, dp)) % M;
            } else {
                break;
            }
        }

        if (count < k) {
            dp[i][count] = res;
        }

        return res;
    }
}
