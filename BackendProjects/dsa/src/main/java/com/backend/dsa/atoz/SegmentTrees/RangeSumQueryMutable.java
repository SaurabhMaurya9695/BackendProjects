package com.backend.dsa.atoz.SegmentTrees;

import com.backend.dsa.atoz.CommonUtil;

public class RangeSumQueryMutable {

    public static void main(String[] args) {
        int[] nums = new int[] { 1, 3, 5 };
        NumArray ns = new NumArray(nums);
        System.out.println(ns.sumRange(0, 2));
    }

    public static class NumArray {

        private int[] segTree;
        private int n;
        private int[] arr;

        public NumArray(int[] nums) {
            // lets build seg tree here
            n = nums.length;
            arr = nums;
            segTree = new int[2 * n];
            buildSegTree(0, 0, n - 1, nums);
            CommonUtil.display(segTree);
        }

        private void buildSegTree(int rootIdx, int l, int r, int[] nums) {
            if (l == r) {
                segTree[rootIdx] = nums[r];
                return;
            }

            int mid = (l + r) / 2;
            buildSegTree(2 * rootIdx + 1, l, mid, nums);
            buildSegTree(2 * rootIdx + 2, mid + 1, r, nums);
            segTree[rootIdx] = segTree[2 * rootIdx + 1] + segTree[2 * rootIdx + 2];
        }

        public void update(int index, int val) {
            updateSegTree(index, val, arr, 0, n - 1, 0);
            CommonUtil.display(segTree);
        }

        private void updateSegTree(int index, int val, int[] nums, int l, int r, int nodeIdx) {

            if (l == r) {
                nums[index] = val;
                segTree[nodeIdx] = val;
                return;
            }

            int mid = (l + r) / 2;

            // choose which side your value is present
            if (index <= mid) {
                updateSegTree(index, val, nums, l, mid, 2 * nodeIdx + 1);
            } else {
                updateSegTree(index, val, nums, mid + 1, r, 2 * nodeIdx + 2);
            }

            // we have put a hypothesis on the recursion saying that it would have updated my desire index
            segTree[nodeIdx] = segTree[2 * nodeIdx + 1] + segTree[2 * nodeIdx + 2];
        }

        public int sumRange(int left, int right) {
            return findSum(0, left, right, 0, n - 1);
        }

        private int findSum(int idx, int leftRange, int rightRange, int l, int r) {
            // No overlap - out of bound range
            if (r < leftRange || l > rightRange) {
                return 0;
            }

            // Complete overlap
            if (l >= leftRange && r <= rightRange) {
                return segTree[idx];
            }

            int mid = (l + r) / 2;

            // overlapping case
            return findSum(2 * idx + 1, leftRange, rightRange, l, mid) + findSum(2 * idx + 2, leftRange, rightRange,
                    mid + 1, r);
        }
    }

    /**
     * Your NumArray object will be instantiated and called as such:
     * NumArray obj = new NumArray(nums);
     * obj.update(index,val);
     * int param_2 = obj.sumRange(left,right);
     */
}
