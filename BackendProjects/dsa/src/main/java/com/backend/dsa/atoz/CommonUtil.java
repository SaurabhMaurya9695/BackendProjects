package com.backend.dsa.atoz;

import com.backend.dsa.atoz.trees.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    public static void removeIntHashMap(HashMap<Integer, Integer> mp, int x) {
        if (mp.containsKey(x)) {
            if (mp.get(x) == 1) {
                mp.remove(x);
            } else {
                mp.put(x, mp.get(x) - 1);
            }
        } else {
            System.out.println("Char not found in HashMap");
        }
    }

    /**
     * Checks if all char of p String MapPattern is present in s String MapString
     *
     * @param s
     * @param p
     * @return true or false based on condition, if all present then true or else false
     */
    public static boolean compare(HashMap<Character, Integer> s, HashMap<Character, Integer> p) {
        for (char c : s.keySet()) {
            if (!p.containsKey(c)) {
                return false;
            }
            if (!Objects.equals(s.get(c), p.get(c))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks how many char are different in it
     *
     * @param s
     * @param p
     * @return true or false based on condition, if all present then true or else false
     */
    public static int cntDifference(HashMap<Character, Integer> s, HashMap<Character, Integer> p) {
        int cnt = 0;
        for (char c : s.keySet()) {
            if (p.containsKey(c)) {
                cnt += Math.abs(s.get(c) - p.get(c));
            }
        }
        return cnt;
    }

    public static StringBuilder reverse(String s) {
        StringBuilder sb = new StringBuilder(s);
        sb.reverse();
        return sb;
    }

    public static Character AlphabetValueOfChar(char ch) {
        return (char) ('a' + (ch - '0') - 1);
    }

    public static void displayNAryTree(Node node) {
        //  we need to print like this
        // 10 -> 20, 30, 40 , .
        // 20 -> 50 , 60 .
        // ..
        // ..
        // so on

        // Faith -> 20, 30, 40 knows how to print 10 will just add yourself in first

        // Meeting expectations
        System.out.print(node._value + "-> ");  // printing 10
        for (Node child : node._children) {
            System.out.print(child._value + " ");
        }
        System.out.println(".");
        for (Node child : node._children) {
            displayNAryTree(child);
        }
    }
}