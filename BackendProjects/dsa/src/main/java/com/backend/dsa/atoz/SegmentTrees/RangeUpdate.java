package com.backend.dsa.atoz.SegmentTrees;

import java.util.ArrayList;
import java.util.Arrays;

public class RangeUpdate {

    public static void main(String[] args) {
        int[] nums = { 3, 4, 2, 5, 3, 6, 3, 4 };
        // sum = 30
        // {1,4,5} => {3,20,10,25,15,6,3,4} => sum = 86
        // {0,7,4} => multiply every element by 4 => sum = 344

        int[][] queries = { { 1, 4, 5 }, { 0, 7, 4 } };
        for (Integer ans : findSumQuery(nums, queries)) {
            System.out.println(ans);
        }
        System.out.println();
    }

    private static int[] segmentTree;
    private static int[] lazy;

    public static ArrayList<Integer> findSumQuery(int[] nums, int[][] queries) {
        int n = nums.length;
        segmentTree = new int[4 * n];
        lazy = new int[4 * n];
        Arrays.fill(lazy, 0);
        buildSegTree(0, 0, n - 1, nums);

        ArrayList<Integer> ans = new ArrayList<>();
        for (int i = 0; i < queries.length; i++) {
            int[] q = queries[i];
            int leftRange = q[0];
            int rightRange = q[1];
            int value = q[2];
            updateInRange(leftRange, rightRange, value, 0, n - 1, 0);
            ans.add(findSumInRange(leftRange, rightRange, 0, n - 1, 0));
        }
        return ans;
    }

    private static void updateInRange(int leftRange, int rightRange, int value, int l, int r, int nodeIdx) {

        // Apply any pending update
        if (lazy[nodeIdx] != 0) {
            segmentTree[nodeIdx] += (r - l + 1) * lazy[nodeIdx];

            if (l != r) {
                // update the lazy nodes to the upcoming updated values
                lazy[2 * nodeIdx + 1] += lazy[nodeIdx];
                lazy[2 * nodeIdx + 2] += lazy[nodeIdx];
            }

            // mark the current lazy value to 0
            lazy[nodeIdx] = 0;
        }

        // No overlap
        if (r < leftRange || l > rightRange) {
            return;
        }

        // Complete overlap
        if (l >= leftRange && r <= rightRange) {
            // don't go in depth and give the values to lazy nodes
            segmentTree[nodeIdx] += (r - l + 1) * value;

            if (l != r) {
                // update lazy nodes
                lazy[2 * nodeIdx + 1] += value;
                lazy[2 * nodeIdx + 2] += value;
            }
            return;
        }

        int mid = (l + r) / 2;

        updateInRange(leftRange, rightRange, value, l, mid, 2 * nodeIdx + 1);
        updateInRange(leftRange, rightRange, value, mid + 1, r, 2 * nodeIdx + 2);
        segmentTree[nodeIdx] = segmentTree[2 * nodeIdx + 1] + segmentTree[2 * nodeIdx + 2];
    }

    private static int findSumInRange(int leftRange, int rightRange, int l, int r, int nodeIdx) {
        // Apply any pending update
        if (lazy[nodeIdx] != 0) {
            segmentTree[nodeIdx] += (r - l + 1) * lazy[nodeIdx];

            if (l != r) {
                lazy[2 * nodeIdx + 1] += lazy[nodeIdx];
                lazy[2 * nodeIdx + 2] += lazy[nodeIdx];
            }

            lazy[nodeIdx] = 0;
        }

        // No overlap
        if (r < leftRange || l > rightRange) {
            return 0;
        }

        // Complete overlap
        if (l >= leftRange && r <= rightRange) {
            return segmentTree[nodeIdx];
        }

        int mid = (l + r) / 2;
        return findSumInRange(leftRange, rightRange, l, mid, 2 * nodeIdx + 1) + findSumInRange(leftRange, rightRange,
                mid + 1, r, 2 * nodeIdx + 2);
    }

    private static void buildSegTree(int nodeIdx, int l, int r, int[] nums) {
        if (l == r) {
            segmentTree[nodeIdx] = nums[l];
            return;
        }

        int mid = (l + r) / 2;

        buildSegTree(2 * nodeIdx + 1, l, mid, nums);
        buildSegTree(2 * nodeIdx + 2, mid + 1, r, nums);

        segmentTree[nodeIdx] = segmentTree[2 * nodeIdx + 1] + segmentTree[2 * nodeIdx + 2];
    }
}
