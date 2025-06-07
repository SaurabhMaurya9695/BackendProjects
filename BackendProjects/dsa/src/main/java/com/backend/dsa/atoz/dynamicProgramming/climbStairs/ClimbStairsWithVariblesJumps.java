package com.backend.dsa.atoz.dynamicProgramming.climbStairs;

import java.util.Arrays;

public class ClimbStairsWithVariblesJumps {

    public ClimbStairsWithVariblesJumps(int n) {
        System.out.println("Number of path to go till " + n + " is via jump of 1, 2 & 3 is ");
        System.out.println("Using recursion : " + solve(n));
        int[] dp = new int[n + 1];
        Arrays.fill(dp, -1);
        System.out.println("Using memo : " + memo(n, dp));
        System.out.println("Using tabu : " + tabulation(n));
        System.out.println("Using space Optimized : " + spaceOptimize(n));
    }

    private int solve(int n) {
        return 0;
    }

    private int memo(int n, int[] dp) {
        return 0;
    }

    private int tabulation(int n) {
        // think for storage
        // smaller to larger solve
        return 0;
    }

    private int spaceOptimize(int n) {
        return 0;
    }
}
