package com.backend.dsa.atoz;

import com.backend.dsa.atoz.trees.nAryTree.Node;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

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

    public static Node cloneTree(Node root) {
        if (root == null) {
            return null;
        }

        Node newRoot = new Node(root._value);
        newRoot.setLeft(root.getLeft());
        newRoot.setRight(root.getRight());
        if (root.getChildren() != null) {
            for (Node child : root.getChildren()) {
                newRoot.addChild(cloneTree(child));
            }
        }
        return newRoot;
    }

    public static void displayBT(Node node) {
        // you need to print in this way "leftChild" <- parent -> "rightChild"
        // faith on displayBT(leftChild) ,  displayBT(rightChild)
        // for meeting expectations -> displayBT(leftChild) , node.value ,  displayBT(rightChild)
        if (node == null) {
            return;
        }
        String str = "";
        str += node.getLeft() == null ? "." : node.getLeft().getValue() + " ";
        str += " <- " + node.getValue() + " -> ";
        str += node.getRight() == null ? "." : node.getRight().getValue() + " ";
        System.out.println(str);
        displayBT(node.getLeft());
        displayBT(node.getRight());
    }

    public static void sortArray(int[] A) {
        Arrays.sort(A);
    }

    public static int[] buildLPS(String s) {
        int n = s.length();
        int[] lps = new int[n];
        int len = 0;
        int i = 1;

        while (i < n) {
            char pref = s.charAt(len);
            char suff = s.charAt(i);

            if (pref == suff) {
                // if both prefix and suffix matched then move length by 1 and stored in lps
                // and move i as well
                len++;
                lps[i] = len;
                i++;
            } else {
                // if not matched then check with prev
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps; // full filled LPS
    }

    public static void display(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void display(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public static void reverse(List<Integer> arr, int left, int right) {
        while (left < right) {
            int temp = arr.get(left);
            arr.set(left, arr.get(right));
            arr.set(right, temp);
            left++;
            right--;
        }
    }

    public static boolean isPrime(int n) {
        if (n == 1) {
            return false;
        }
        if (n == 2 || n == 3 || n == 5) {
            return true;
        }

        for (int i = 5; i <= n; i++) {
            if (n % i == 0) {
                return true;
            }
        }

        return false;
    }

    public static Set<Integer> getPrimeFactors(int num) {
        Set<Integer> factors = new HashSet<>();

        // Check for factor 2
        if (num % 2 == 0) {
            factors.add(2);
            while (num % 2 == 0) {
                num /= 2;
            }
        }

        // Check for odd factors from 3 onwards
        for (int i = 3; i * i <= num; i += 2) {
            if (num % i == 0) {
                factors.add(i);
                while (num % i == 0) {
                    num /= i;
                }
            }
        }

        // If num > 1, then it's a prime factor
        if (num > 1) {
            factors.add(num);
        }

        return factors;
    }

    public static boolean[] sieveOfEratosthenes(int maxVal) {
        boolean[] isPrime = new boolean[maxVal + 1];

        // Initialize: assume all numbers are prime
        for (int i = 0; i <= maxVal; i++) {
            isPrime[i] = true;
        }

        // 0 and 1 are not prime
        isPrime[0] = false;
        isPrime[1] = false;

        // Mark non-primes
        for (int i = 2; i * i <= maxVal; i++) {
            if (isPrime[i]) {
                // Mark all multiples of i as not prime
                for (int j = i * i; j <= maxVal; j += i) {
                    isPrime[j] = false;
                }
            }
        }

        return isPrime;
    }
}