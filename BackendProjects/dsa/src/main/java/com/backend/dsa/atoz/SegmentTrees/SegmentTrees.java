package com.backend.dsa.atoz.SegmentTrees;

import com.backend.dsa.atoz.CommonUtil;

public class SegmentTrees {

    // efficiently calculated the ranges of all possibilities
    private static int[] segmentTree = new int[2 * 4];

    public static void main(String[] args) {
        int[] nums = { 3, 1, 2, 7 };

        buildSegTree(0, 0, 3, nums);

        CommonUtil.display(segmentTree);

        // update 2nd idx with 4
        int indexToUpdate = 2;
        int valueToUpdate = 4;

        updateSegTree(indexToUpdate, valueToUpdate, nums, 0, 3, 0);

        CommonUtil.display(segmentTree);
    }

    private static void updateSegTree(int indexToUpdate, int valueToUpdate, int[] nums, int l, int r, int nodeIdx) {

        if (l == r) {
            nums[indexToUpdate] = valueToUpdate;
            segmentTree[nodeIdx] = valueToUpdate;
            return;
        }

        int mid = (l + r) / 2;

        // choose which side your value is present
        if (indexToUpdate <= mid) {
            updateSegTree(indexToUpdate, valueToUpdate, nums, l, mid, 2 * nodeIdx + 1);
        } else {
            updateSegTree(indexToUpdate, valueToUpdate, nums, mid + 1, r, 2 * nodeIdx + 2);
        }

        // we have put a hypothesis on the recursion saying that it would have updated my desire index
        segmentTree[nodeIdx] = segmentTree[2 * nodeIdx + 1] + segmentTree[2 * nodeIdx + 2];
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
