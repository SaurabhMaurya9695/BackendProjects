package com.backend.dsa.atoz;

import java.util.Arrays;

public class MinimumInitialEnergyToFinishTasks {

    public static void main(String[] args) {
        int[][] nums = { { 1, 2 }, { 2, 4 }, { 4, 8 } };
        System.out.println(minimumEffort(nums));

        int[][] nums1 = { { 1, 3 }, { 2, 4 }, { 10, 11 }, { 10, 12 }, { 8, 9 } };
        System.out.println(minimumEffort(nums1));
    }

    public static int minimumEffort(int[][] tasks) {
        Arrays.sort(tasks, (a, b) -> {
            if (b[0] == a[0]) {
                return Integer.compare(b[1], a[1]);
            }
            return Integer.compare(b[1], a[1]);
        });

        int n = tasks.length;

        int left = 0;
        int right = 34;
        int ans = right;
        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (isPossible(mid, tasks)) {
                // this would be our potential ans
                ans = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return ans;
    }

    private static boolean isPossible(int mid, int[][] tasks) {
        int energy = mid;
        for (int i = 0 ; i< tasks.length ; i++) {
            int actual = tasks[i][0];
            int minimum = tasks[i][1];
            if (energy < minimum) {
                return false;
            }
            energy -= actual;
        }
        return energy >= 0;
    }
}
