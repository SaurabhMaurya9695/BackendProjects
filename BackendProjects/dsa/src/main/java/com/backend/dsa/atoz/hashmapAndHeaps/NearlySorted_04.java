package com.backend.dsa.atoz.hashmapAndHeaps;

import java.util.PriorityQueue;

public class NearlySorted_04 {

    public NearlySorted_04(int[] arr, int k) {
        nearlySortedArray(arr, k);
    }

    private void nearlySortedArray(int[] arr, int k) {
        // enter k + 1 element in pq ;
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int i = 0; i < k + 1; i++) {
            pq.add(arr[i]);
        }

        System.out.print("Nearly sorted array: ");
        for (int i = k + 1; i < arr.length; i++) {
            System.out.print(pq.poll() + " ");
            pq.add(arr[i]);
        }

        while (!pq.isEmpty()) {
            System.out.print(pq.poll() + " ");
        }
        System.out.println();
    }
}
