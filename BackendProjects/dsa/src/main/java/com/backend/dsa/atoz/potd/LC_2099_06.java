package com.backend.dsa.atoz.potd;

import java.util.*;

public class LC_2099_06 {

    public int[] maxSubsequence(int[] nums, int k) {
        // Step 1: Create a max-heap (PriorityQueue with custom comparator)
        // Each entry is a pair: [value, index]
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> b[0] - a[0] // sort by value descending
        );

        // Step 2: Push all elements and their original indices into the heap
        for (int i = 0; i < nums.length; i++) {
            pq.offer(new int[] { nums[i], i });
        }

        // Step 3: Extract top k elements from the max-heap
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            int[] top = pq.poll();           // get max element
            indices.add(top[1]);             // store its original index
        }

        // Step 4: Sort the indices to maintain original order
        Collections.sort(indices);

        // Step 5: Build the answer array using the sorted indices
        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            result[i] = nums[indices.get(i)];
        }

        // Step 6: Return the result
        return result;
    }
}
