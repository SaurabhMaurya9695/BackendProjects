package com.backend.dsa.atoz.SegmentTrees;

import java.util.ArrayList;
import java.util.HashMap;

public class CountGoodTripletsInAnArray {

    public static void main(String[] args) {
        int[] nums1 = { 4, 0, 1, 3, 2 };
        int[] nums2 = { 4, 1, 0, 2, 3 };
        System.out.println(goodTriplets(nums1, nums2));
    }

    private static int[] segmentTree;

    private static long goodTriplets(int[] nums1, int[] nums2) {
        HashMap<Integer, Integer> map = new HashMap<>();
        int n = nums1.length;
        int m = nums2.length;
        for (int i = 0; i < m; i++) {
            map.put(nums2[i], i);
        }
        segmentTree = new int[4 * n];
        updateSegmentTree(0, 0, n - 1, map.get(nums1[0]));
        long ans = 0;
        for (int i = 1; i < n; i++) {
            int idx = map.get(nums1[i]);
            long leftCommonCount = querySegmentTree(0, idx, 0, 0, n - 1);
            long leftNotCommonCount = i - leftCommonCount;
            long elementsAfterIdxNums2 = (n - 1) - idx;
            long rightCommonCount = elementsAfterIdxNums2 - leftNotCommonCount;
            ans += leftCommonCount * rightCommonCount;

            updateSegmentTree(0, 0, n - 1, idx);
        }

        return ans;
    }

    public static void updateSegmentTree(int i, int l, int r, int updateIndex) {
        if (l == r) {
            segmentTree[i] = 1;
            return;
        }

        int mid = l + (r - l) / 2;
        if (updateIndex <= mid) {
            updateSegmentTree(2 * i + 1, l, mid, updateIndex);
        } else {
            updateSegmentTree(2 * i + 2, mid + 1, r, updateIndex);
        }

        segmentTree[i] = segmentTree[2 * i + 1] + segmentTree[2 * i + 2];
    }

    public static long querySegmentTree(int queryStart, int queryEnd, int i, int l, int r) {
        if (r < queryStart || l > queryEnd) {
            return 0;
        }

        if (l >= queryStart && r <= queryEnd) {
            return segmentTree[i];
        }

        int mid = l + (r - l) / 2;
        long left = querySegmentTree(queryStart, queryEnd, 2 * i + 1, l, mid);
        long right = querySegmentTree(queryStart, queryEnd, 2 * i + 2, mid + 1, r);
        return left + right;
    }

    private static long goodTriplets1(int[] nums1, int[] nums2) {
        HashMap<ArrayList<Integer>, Integer> map = new HashMap<>();
        int n = nums1.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = j + 1; k < n; k++) {
                    ArrayList<Integer> lst = new ArrayList<>();
                    lst.add(nums1[i]);
                    lst.add(nums1[j]);
                    lst.add(nums1[k]);
                    map.put(lst, map.getOrDefault(lst, 0) + 1);
                }
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = j + 1; k < n; k++) {
                    ArrayList<Integer> lst = new ArrayList<>();
                    lst.add(nums2[i]);
                    lst.add(nums2[j]);
                    lst.add(nums2[k]);
                    map.put(lst, map.getOrDefault(lst, 0) + 1);
                }
            }
        }

        long cnt = 0;
        for (ArrayList<Integer> integers : map.keySet()) {
            if (map.get(integers) > 1) {
                cnt++;
            }
        }

        return cnt;
    }
}
