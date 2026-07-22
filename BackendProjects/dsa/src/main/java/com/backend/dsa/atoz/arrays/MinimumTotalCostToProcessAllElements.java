package com.backend.dsa.atoz.arrays;

public class MinimumTotalCostToProcessAllElements {

    public static void main(String[] args) {
        int[] nums = { 1,2,3,4 };
        int k = 4;
        System.out.println(minimumCost(nums, k));
    }

    public static int minimumCost(int[] nums, int k) {
        int n = nums.length;

        long resource = k;
        long ans = 0;
        long op = 0;

        long mod = 10_000_007L;

        for (int i = 0; i < n; i++) {
            if (nums[i] <= resource) {
                resource -= nums[i];
            } else {
                long required = nums[i] - resource;
                long needed = (required + k - 1L) / k;
                long firstOperation = op + 1;
                long lastOperation = op + needed;
                long operationCost =
                        needed * (firstOperation + lastOperation) / 2;
                ans = (ans + operationCost) % mod;
                op += needed;
                resource += needed * (long) k;
                resource -= nums[i];
            }
        }

        return (int)( ans % mod);
    }
}
