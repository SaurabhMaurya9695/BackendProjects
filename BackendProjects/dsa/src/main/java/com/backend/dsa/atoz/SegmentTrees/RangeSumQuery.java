package com.backend.dsa.atoz.SegmentTrees;

import com.backend.dsa.atoz.CommonUtil;

public class RangeSumQuery {

    private static int[] segmentTree;

    public static void main(String[] args) {
        int[] nums = { 3, 1, 2, 7, 2, 1, 2, 3 };
        segmentTree = new int[nums.length * 2];
        buildSegTree(0, 0, nums.length - 1, nums);
        CommonUtil.display(segmentTree);
        System.out.println(findSum(0, 2, 6, 0, nums.length - 1));
    }

    private static int findSum(int idx, int leftRange, int rightRange, int l, int r) {
        // No overlap - out of bound range
        if (r < leftRange || l > rightRange) {
            return 0;
        }

        // Complete overlap
        if (l >= leftRange && r <= rightRange) {
            return segmentTree[idx];
        }

        int mid = (l + r) / 2;

        // overlapping case
        return findSum(2 * idx + 1, leftRange, rightRange, l, mid) +
                findSum(2 * idx + 2, leftRange, rightRange, mid + 1, r);
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
