package com.backend.dsa.atoz.dynamicProgramming.climbStairs;

import java.util.Arrays;

public class ClimbStairs {

    public ClimbStairs(int n) {
        System.out.println("Number of path to go till " + n + " is via jump of 1, 2 & 3 is ");
        System.out.println("Using recursion : " + solve(n));
        int[] dp = new int[n + 1];
        Arrays.fill(dp, -1);
        System.out.println("Using memo : " + memo(n, dp));
        System.out.println("Using tabu : " + tabulation(n));
        System.out.println("Using space Optimized : " + spaceOptimize(n));
    }

    private int solve(int n) {
        if (n < 0) {
            return 0;
        }
        if (n == 0) {
            return 1;
        }
        int x = solve(n - 1);
        int y = solve(n - 2);
        int z = solve(n - 3);
        return x + y + z;
    }

    private int memo(int n, int[] dp) {
        if (n < 0) {
            return 0;
        }
        if (n == 0) {
            return 1;
        }
        if (dp[n] != -1) {
            return dp[n];
        }
        int x = solve(n - 1);
        int y = solve(n - 2);
        int z = solve(n - 3);
        return dp[n] = x + y + z;
    }

    private int tabulation(int n) {
        // think for storage
        // smaller to larger solve
        int[] dp = new int[n + 1];
        dp[0] = 1;
        for (int i = 1; i <= n; i++) {
            if (i == 1) {
                dp[i] = dp[i - 1];
            } else if (i == 2) {
                dp[i] = dp[i - 1] + dp[i - 2];
            } else {
                dp[i] = dp[i - 1] + dp[i - 2] + dp[i - 3];
            }
        }

        return dp[n];
    }

    private int spaceOptimize(int n) {
        int a = 1;
        int b = 1;
        int c = 2;
        int d = 0;
        for (int i = 3; i <= n; i++) {
            d = a + b + c;
            a = b;
            b = c;
            c = d;
        }
        return d;
    }
}
