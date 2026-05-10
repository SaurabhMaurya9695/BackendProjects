package com.backend.dsa.atoz.dynamicProgramming.jumpGame;

import java.util.Arrays;

public class MaximumNumberOfJumpsToReachTheLastIndex {

    public static void main(String[] args) {
        int[] nums = { 1, 3, 6, 4, 1, 2 };
        int target = 2;
//        System.out.println(maximumJumps_TLE(nums, target));
        System.out.println(maximumJumps(nums, target));

        int[] nums1 = { 1, 3, 6, 4, 1, 2 };
        int target1 = 3;
//        System.out.println(maximumJumps_TLE(nums1, target1));
        System.out.println(maximumJumps(nums1, target1));

        int[] nums2 = { 1, 0, 2 };
        int target2 = 1;
//        System.out.println(maximumJumps_TLE(nums2, target2));
        System.out.println(maximumJumps(nums2, target2));
    }

    public static int maximumJumps_TLE(int[] nums, int target) {
        int n = nums.length;
        int idx = 0;
        int cnt = 0;
        for (int i = idx; i < n; i++) {
            for (int j = idx + 1; j < n; j++) {
                if (nums[j] - nums[i] <= target && nums[j] - nums[i] >= target * -1 && target != 0) {
                    idx = j;
                    cnt++;
                    break;
                }
            }

            if (idx == n - 1) {
                return cnt;
            }
        }

        return -1;
    }

    public static int maximumJumps(int[] nums, int target) {
        int n = nums.length;
        // get the max value from all index ;
        int [] dp = new int[n];
        // initalize with -2 instead of -1 to pass this ques
        Arrays.fill(dp, Integer.MIN_VALUE);


        return dfs(nums, target, 0,dp);
    }

    private static int dfs(int[] nums, int target, int i, int [] dp) {
        // Base case: reached the last i
        if (i == nums.length - 1) {
            return 0;
        }

        if(dp[i] != Integer.MIN_VALUE){
            return dp[i];
        }

        int maxJumps = -1;

        // Try jumping to all valid next indices
        for (int j = i + 1; j < nums.length; j++) {
            int diff = nums[j] - nums[i];

            // Check if this jump is valid
            if (diff >= -target && diff <= target) {
                int jumpsFromNext = dfs(nums, target, j,dp);

                // Only count if the next position can reach the end
                if (jumpsFromNext != -1) {
                    maxJumps = Math.max(maxJumps, jumpsFromNext + 1);
                }
            }
        }

        return dp[i] = maxJumps;
    }
}
