package com.backend.dsa.atoz.arrays;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class MinimumSwapsToSortByDigitSum {

    public static void main(String[] args) {
        int[] nums = { 34, 56, 32, 19 };
        System.out.println(minSwaps(nums));
    }

    public static int minSwaps(int[] nums) {
        PriorityQueue<Pair> pq = new PriorityQueue<>((a, b) -> {
            if (a.sum == b.sum) {
                return a.val - b.val;
            }
            return a.sum - b.sum;
        });

        for (int i = 0; i < nums.length; i++) {
            int sum = getSum(nums[i]);
            pq.add(new Pair(sum, nums[i]));
        }

        int[] expected = new int[nums.length];
        int i = 0;
        while (!pq.isEmpty()) {
            Pair pair = pq.poll();
            expected[i] = pair.val;
            i++;
        }

        // now we have real value and expected values
        // 18 43 34 16
        // 16 34 43 18

        // 34, 56, 32, 19
        // 32, 56, 34, 19
        // 32, 34, 56, 19
        // 32, 34, 19, 56

        // 32, 34, 19, 56
        i = 0;
        Map<Integer, Integer> targetIndex = new HashMap<>();

        for (i = 0; i < expected.length; i++) {
            targetIndex.put(expected[i], i);
        }

        boolean[] visited = new boolean[nums.length];
        int swaps = 0;

        i = 0;
        for (i = 0; i < nums.length; i++) {
            if (visited[i] || targetIndex.get(nums[i]) == i) {
                continue;
            }

            int cycleSize = 0;
            int j = i;

            while (!visited[j]) {
                visited[j] = true;
                j = targetIndex.get(nums[j]);
                cycleSize++;
            }

            swaps += cycleSize - 1;
        }

        return swaps;
    }

    private static int getSum(int num) {
        int sum = 0;
        while (num != 0) {
            sum += num % 10;
            num /= 10;
        }
        return sum;
    }

    static class Pair {

        int sum;
        int val;

        Pair(int sum, int val) {
            this.sum = sum;
            this.val = val;
        }
    }
}
