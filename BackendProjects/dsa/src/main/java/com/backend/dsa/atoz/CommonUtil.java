package com.backend.dsa.atoz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class CommonUtil {

    public static void printArray(int[] array) {
        for (int value : array) {
            System.out.print(value + " ");
        }
        System.out.println();
    }

    public static void printArray(List<Integer> list) {
        for (int value : list) {
            System.out.print(value + " ");
        }
        System.out.println();
    }

    public static void swap(int[] arr, int l, int r) {
        int temp = arr[l];
        arr[l] = arr[r];
        arr[r] = temp;
    }

    public static int compare(int[] arr, int l, int r) {
        if (arr[l] > arr[r]) {
            return -1; // swap req
        } else if (arr[l] < arr[r]) {
            return +1; // swap not req
        } else {
            return 0; // nothing req
        }
    }

    public static void reverse(int[] arr, int i, int j) {
        int li = i;
        int ri = j;

        while (li < ri) {
            swap(arr, li, ri);
            li++;
            ri--;
        }
    }

    public static int back(int[] arr) {
        if (arr.length > 0) {
            return arr[arr.length - 1];
        }
        return 0;
    }

    public static int back(List<Integer> list) {
        if (!list.isEmpty()) {
            return list.get(list.size() - 1);
        }
        return 0;
    }

    public static void printMap(Map<?, ?> map) {
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    public static void printPQ(PriorityQueue<?> pq) {
        while (!pq.isEmpty()) {
            System.out.print(pq.poll() + " ");
        }
    }

    public static int max(int a, int b) {
        return Math.max(a, b);
    }

    public static int min(int a, int b) {
        return Math.min(a, b);
    }

    public static void removeCharHashMap(HashMap<Character, Integer> mp, char c) {
        if (mp.containsKey(c)) {
            if (mp.get(c) == 1) {
                mp.remove(c);
            } else {
                mp.put(c, mp.get(c) - 1);
            }
        } else {
            System.out.println("Char not found in HashMap");
        }
    }
}