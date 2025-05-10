package com.backend.dsa.atoz.hashmapAndHeaps;

import com.backend.dsa.atoz.CommonUtil;

import java.util.Comparator;
import java.util.PriorityQueue;

public class KLargestElements_03 {

    public KLargestElements_03(int[] arr, int k) {
        kLargestElements(arr, k);
    }

    private void kLargestElements(int[] arr, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int x : arr) {
            pq.add(x);
            if (pq.size() > k) {
                pq.remove();
            }
        }
        System.out.print("Kth largest Elements is : " + pq.peek());
        System.out.println();
    }
}
