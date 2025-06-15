package com.backend.dsa.atoz.hashmapAndHeaps;

import com.backend.dsa.atoz.CommonUtil;
import com.backend.dsa.atoz.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class MergeKSortedList_06 {

    public MergeKSortedList_06(int[][] arr) {
        mergeKList(arr);
    }

    private void mergeKList(int[][] arr) {
        PriorityQueue<Pair> pq = new PriorityQueue<>();
        for (int i = 0; i < arr.length; i++) {
            Pair pair = new Pair(i, 0, arr[i][0]);
            pq.add(pair);
        }

        List<Integer> list = new ArrayList<>();
        while (!pq.isEmpty()) {
            Pair pair = pq.remove(); // this will give us smaller value
            list.add(pair.state);
            pair.dataIdx++;
            // if the size of the list exist then increase the idx of data and insert the value
            int indexOfTheList = pair.dataIdx;
            if (indexOfTheList < arr[pair.listIdx].length) {
                pair.state = arr[pair.listIdx][pair.dataIdx];
                pq.add(pair);
            }
        }
        System.out.print("After Merging k Sorted list is : ");
        CommonUtil.printArray(list);
    }
}

