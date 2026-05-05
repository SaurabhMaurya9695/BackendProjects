package com.backend.dsa.atoz.prefixSum;

public class FindTheHighestAltitude_01 {

    public static void main(String[] args) {
        int[] nums = { -5, 1, 5, 0, -7 };
        System.out.println(largestAltitude(nums));
    }

    private static int largestAltitude(int[] nums) {
        int ans = -1;
        int sum = 0;
        for (Integer x : nums) {
            sum += x;
            ans = Math.max(ans, sum);
        }
        return Math.max(ans, 0);
    }
}
