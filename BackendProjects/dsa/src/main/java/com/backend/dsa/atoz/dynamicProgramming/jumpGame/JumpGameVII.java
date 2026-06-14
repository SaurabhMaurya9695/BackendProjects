package com.backend.dsa.atoz.dynamicProgramming.jumpGame;

import java.util.Arrays;

public class JumpGameVII {

    public static void main(String[] args) {
        String s = "01101110";
        int minJump = 2;
        int maxJump = 3;

        System.out.println(canReach(s, minJump, maxJump));
    }

    private static boolean canReach(String s, int minJump, int maxJump) {
        // constrain says to solve this in 10^5
        int n = s.length();
        boolean[] dp = new boolean[n];
        dp[0] = true;
        int reachable = 0; // Count of reachable positions
        for (int i = 1; i < n; i++) {
            // Add position that just entered the "can jump from" range
            if (i - minJump >= 0) {
                reachable += dp[i - minJump] ? 1 : 0;
            }

            // Remove position that just left the "can jump from" range
            if (i - maxJump - 1 >= 0) {
                reachable -= dp[i - maxJump - 1] ? 1 : 0;
            }

            // Can we reach position i?
            if (reachable > 0 && s.charAt(i) == '0') {
                dp[i] = true;
            }
        }

        return dp[n - 1]; // if we have something in dp > 1 it means we can reach to the end
    }

    // WORKING BUT GIVING TLE WHEN "0000000000000000000000000000000000000000000001" etc..
    public static boolean canReach1(String s, int minJump, int maxJump) {
        int n = s.length();
        Boolean[] dp = new Boolean[n];
        Arrays.fill(dp, false);
        return canReach(0, s, minJump, maxJump, dp);
    }

    private static boolean canReach(int idx, String s, int minJump, int maxJump, Boolean[] dp) {

        if (idx == s.length() - 1) {
            return true;
        }

        if (dp[idx] != false) {
            return dp[idx];
        }

        // from here I can move to i + minJump
        for (int j = idx + minJump; j <= Math.min(idx + maxJump, s.length() - 1); j++) {
            if (s.charAt(j) == '0') {
                // now we can jump from here
                if (canReach(j, s, minJump, maxJump, dp)) {
                    dp[j] = true;
                    return true;
                }
            }
        }
        return false;
    }
}
