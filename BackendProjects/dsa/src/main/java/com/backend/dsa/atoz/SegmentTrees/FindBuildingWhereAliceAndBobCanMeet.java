package com.backend.dsa.atoz.SegmentTrees;

public class FindBuildingWhereAliceAndBobCanMeet {

    public static void main(String[] args) {
        int[] nums = { 6, 4, 8, 5, 2, 7 };
        int[][] queries = { { 0, 1 }, { 0, 3 }, { 2, 4 }, { 3, 4 }, { 2, 2 } };

        for (int ans : leftmostBuildingQueries(nums, queries)) {
            System.out.println(ans);
        }
    }

    private static int[] segmentTree;

    private static int[] leftmostBuildingQueries(int[] nums, int[][] queries) {
        int n = nums.length;
        segmentTree = new int[4 * n];

        // This segment tree stores indexes of maximum heights
        buildSegTreeWithIndex(nums, 0, 0, n - 1);

        int[] ans = new int[queries.length];
        int i = 0;

        for (int[] q : queries) {
            int leftRange = q[0];
            int rightRange = q[1];

            // Ensure leftRange <= rightRange
            if (leftRange > rightRange) {
                int temp = leftRange;
                leftRange = rightRange;
                rightRange = temp;
            }

            if (leftRange == rightRange) {
                ans[i] = leftRange;
            } else if (nums[leftRange] < nums[rightRange]) {
                // Alice can directly reach Bob's building
                ans[i] = rightRange;
            } else {
                // Find the leftmost building after rightRange
                // whose height is greater than both starting heights
                int target = Math.max(nums[leftRange], nums[rightRange]);

                ans[i] = findMaxIndexInSegTree(nums, rightRange + 1, n - 1, target, 0, 0, n - 1);
            }

            i++;
        }

        return ans;
    }

    private static int findMaxIndexInSegTree(int[] nums, int leftRange, int rightRange, int target, int nodeIdx, int l,
            int r) {

        // No overlap
        if (r < leftRange || l > rightRange) {
            return -1;
        }

        // Maximum height in this segment is not greater than target
        if (nums[segmentTree[nodeIdx]] <= target) {
            return -1;
        }

        // Leaf node: this is the leftmost valid building
        if (l == r) {
            return l;
        }

        int mid = (l + r) / 2;

        // Search left side first to get the leftmost valid index
        int leftAns = findMaxIndexInSegTree(nums, leftRange, rightRange, target, 2 * nodeIdx + 1, l, mid);

        if (leftAns != -1) {
            return leftAns;
        }

        return findMaxIndexInSegTree(nums, leftRange, rightRange, target, 2 * nodeIdx + 2, mid + 1, r);
    }

    private static void buildSegTreeWithIndex(int[] nums, int nodeIdx, int l, int r) {
        if (l == r) {
            segmentTree[nodeIdx] = l;
            return;
        }

        int mid = (l + r) / 2;

        buildSegTreeWithIndex(nums, 2 * nodeIdx + 1, l, mid);
        buildSegTreeWithIndex(nums, 2 * nodeIdx + 2, mid + 1, r);

        // Store the index whose height is maximum
        int leftIdx = segmentTree[2 * nodeIdx + 1];
        int rightIdx = segmentTree[2 * nodeIdx + 2];

        if (nums[leftIdx] >= nums[rightIdx]) {
            segmentTree[nodeIdx] = leftIdx;
        } else {
            segmentTree[nodeIdx] = rightIdx;
        }
    }
}
