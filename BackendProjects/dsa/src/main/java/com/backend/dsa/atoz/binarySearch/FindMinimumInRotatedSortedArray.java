package com.backend.dsa.atoz.binarySearch;

public class FindMinimumInRotatedSortedArray {

    public static void main(String[] args) {
        int[] nums = { 3, 4, 5, 1, 2 };
        System.out.println(findMin(nums));
    }

    public static int findMin(int[] nums) {
        int left = 0;
        int right = nums.length - 1;

        // Binary search for the minimum element
        while (left < right) {
            int mid = left + (right - left) / 2;

            // Compare mid with right to determine which half is sorted
            if (nums[mid] < nums[right]) {
                // Right half is sorted, minimum is in left half (including mid)
                right = mid;
            } else if (nums[mid] > nums[right]) {
                // Left half is sorted, minimum is in right half (after mid)
                left = mid + 1;
            } else {
                // nums[mid] == nums[right]: ambiguous, shrink search space
                // We can safely ignore nums[right] since nums[mid] has the same value
                right--;
            }
        }

        // When left == right, we've found the minimum
        return nums[left];
    }
}
