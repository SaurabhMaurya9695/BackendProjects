package com.backend.dsa.atoz;

public class MinimumMovesToMakeArrayComplementary {

    public static void main(String[] args) {
        int[] nums = { 1, 2, 2, 1 };
        int limit = 2;
        System.out.println(minMoves(nums, limit));
    }

    public static int minMoves(int[] nums, int limit) {

        int left = 0;
        int right = nums.length - 1;
        int desireSum = nums[left] + nums[right];

        left++;
        right--;

        int cnt = 0;
        while (left < right) {
            int sum = nums[left] + nums[right];
            if (sum == desireSum) {
                continue;
            } else {
                int needed = Math.abs(desireSum - sum);
                if (needed >= 1 && needed <= limit) {
                    cnt++;
                }
            }
            left++;
            right--;
        }

        return cnt;
    }
}
