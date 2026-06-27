package com.backend.dsa.atoz.SegmentTrees;

import java.util.*;

public class BlockPlacementQueries {

    static class Solution {

        static class SegTreeNode {

            long rangeSum;
            long prefMaxSum;
            long suffMaxSum;
            long maxSubArrSum;

            SegTreeNode() {
            }

            SegTreeNode(long rs, long ps, long ss, long ms) {
                this.rangeSum = rs;
                this.prefMaxSum = ps;
                this.suffMaxSum = ss;
                this.maxSubArrSum = ms;
            }
        }

        private void buildSegTree(int ind, int l, int r, int[] arr, SegTreeNode[] segTree) {

            if (l == r) {
                segTree[ind].rangeSum = arr[l];
                segTree[ind].prefMaxSum = Math.max(0, arr[l]);
                segTree[ind].suffMaxSum = Math.max(0, arr[l]);
                segTree[ind].maxSubArrSum = Math.max(0, arr[l]);
                return;
            }

            int mid = l + (r - l) / 2;

            buildSegTree(2 * ind + 1, l, mid, arr, segTree);
            buildSegTree(2 * ind + 2, mid + 1, r, arr, segTree);

            pull(ind, segTree);
        }

        private void pointUpdate(int ind, int l, int r, int pos, int newVal, SegTreeNode[] segTree) {

            if (l == r) {
                segTree[ind].rangeSum = newVal;
                segTree[ind].prefMaxSum = Math.max(0, newVal);
                segTree[ind].suffMaxSum = Math.max(0, newVal);
                segTree[ind].maxSubArrSum = Math.max(0, newVal);
                return;
            }

            int mid = l + (r - l) / 2;

            if (pos <= mid) {
                pointUpdate(2 * ind + 1, l, mid, pos, newVal, segTree);
            } else {
                pointUpdate(2 * ind + 2, mid + 1, r, pos, newVal, segTree);
            }

            pull(ind, segTree);
        }

        private SegTreeNode getRangeMaxSubSum(int ind, int l, int r, int start, int end, SegTreeNode[] segTree) {

            if (l > end || r < start) {
                return new SegTreeNode(0, (long) -1e17, (long) -1e17, (long) -1e17);
            }

            if (l >= start && r <= end) {
                return segTree[ind];
            }

            int mid = l + (r - l) / 2;

            SegTreeNode left = getRangeMaxSubSum(2 * ind + 1, l, mid, start, end, segTree);

            SegTreeNode right = getRangeMaxSubSum(2 * ind + 2, mid + 1, r, start, end, segTree);

            return merge(left, right);
        }

        private SegTreeNode merge(SegTreeNode left, SegTreeNode right) {

            long rangeSum = left.rangeSum + right.rangeSum;

            long prefMaxSum = Math.max(left.prefMaxSum, left.rangeSum + right.prefMaxSum);

            long suffMaxSum = Math.max(left.suffMaxSum + right.rangeSum, right.suffMaxSum);

            long maxSubArrSum = Math.max(Math.max(left.maxSubArrSum, right.maxSubArrSum),
                    left.suffMaxSum + right.prefMaxSum);

            return new SegTreeNode(rangeSum, prefMaxSum, suffMaxSum, maxSubArrSum);
        }

        private void pull(int ind, SegTreeNode[] segTree) {

            SegTreeNode left = segTree[2 * ind + 1];
            SegTreeNode right = segTree[2 * ind + 2];

            segTree[ind].rangeSum = left.rangeSum + right.rangeSum;

            segTree[ind].prefMaxSum = Math.max(left.prefMaxSum, left.rangeSum + right.prefMaxSum);

            segTree[ind].suffMaxSum = Math.max(left.suffMaxSum + right.rangeSum, right.suffMaxSum);

            segTree[ind].maxSubArrSum = Math.max(Math.max(left.maxSubArrSum, right.maxSubArrSum),
                    left.suffMaxSum + right.prefMaxSum);
        }

        public List<Boolean> getResults(int[][] queries) {

            int n = queries.length;

            int maxDist = 0;

            for (int i = 0; i < n; i++) {
                if (queries[i][0] == 1) {
                    maxDist = Math.max(maxDist, queries[i][1]);
                } else {
                    maxDist = Math.max(maxDist, Math.max(queries[i][1], queries[i][2]));
                }
            }

            int[] arr = new int[2 * maxDist];

            for (int i = 0; i < maxDist; i++) {
                arr[2 * i] = 1;
                arr[2 * i + 1] = 0;
            }

            SegTreeNode[] segTree = new SegTreeNode[8 * maxDist];

            for (int i = 0; i < segTree.length; i++) {
                segTree[i] = new SegTreeNode();
            }

            buildSegTree(0, 0, 2 * maxDist - 1, arr, segTree);

            List<Boolean> ans = new ArrayList<>();

            for (int i = 0; i < n; i++) {

                if (queries[i][0] == 1) {

                    pointUpdate(0, 0, 2 * maxDist - 1, 2 * queries[i][1] - 1, -(int) 1e8, segTree);
                } else {

                    long maxSubArr = getRangeMaxSubSum(0, 0, 2 * maxDist - 1, 0, 2 * queries[i][1] - 1,
                            segTree).maxSubArrSum;

                    ans.add(queries[i][2] <= maxSubArr);
                }
            }

            return ans;
        }
    }
}
