package com.backend.dsa.atoz.SegmentTrees;


import java.util.ArrayList;

public class RangeMinimumQuery {

    public static void main(String[] args) {
        int[] arr = { 7, 2, 9, 4 };
        int[][] queries = { { 1, 1 }, { 0, 3 } };
        for (Integer x : rangeMinQuery(arr, queries)) {
            System.out.print(x + " ");
        }
        System.out.println();
    }

    private static int[] segmentTree;

    public static ArrayList<Integer> rangeMinQuery(int[] arr, int[][] queries) {
        segmentTree = new int[4 * arr.length];
        buildSegTree(0, 0, arr.length - 1, arr);

        // seg tree is ready, we have the minimal at every point right now
        ArrayList<Integer> ans = new ArrayList<>();
        for (int[] q : queries) {
            int leftRange = q[0];
            int rightRange = q[1];
            ans.add(findMiniInRange(leftRange, rightRange, 0, 0, arr.length - 1, arr));
        }
        return ans;
    }

    private static Integer findMiniInRange(int leftRange, int rightRange, int rootIdx, int left, int right, int[] arr) {

        // out of scope
        if (right < leftRange || left > rightRange) {
            return Integer.MAX_VALUE;
        }

        if (left >= leftRange && right <= rightRange) {
            // we are in a range
            return segmentTree[rootIdx];
        }

        int mid = left + (right - left) / 2;
        return Math.min(findMiniInRange(leftRange, rightRange, 2 * rootIdx + 1, left, mid, arr),
                findMiniInRange(leftRange, rightRange, 2 * rootIdx + 2, mid + 1, right, arr));
    }

    public static void buildSegTree(int rootIdx, int left, int right, int[] arr) {
        if (left > right) {
            return;
        }

        if (left == right) {
            segmentTree[rootIdx] = arr[left];
            return;
        }
        int mid = (left + right) / 2;
        buildSegTree(2 * rootIdx + 1, left, mid, arr);
        buildSegTree(2 * rootIdx + 2, mid + 1, right, arr);
        segmentTree[rootIdx] = Math.min(segmentTree[2 * rootIdx + 1], segmentTree[2 * rootIdx + 2]);
    }
}
