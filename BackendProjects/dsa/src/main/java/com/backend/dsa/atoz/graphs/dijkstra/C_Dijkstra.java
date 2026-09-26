package com.backend.dsa.atoz.graphs.dijkstra;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class C_Dijkstra {

    public static void main(String[] args) {

        Scanner fs = new Scanner(System.in);
        int t = fs.nextInt();
        while (t-- > 0) {
            int n = fs.nextInt();
            int w = fs.nextInt();
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = fs.nextInt();
            }
            System.out.println(solve(n, w, arr));
        }
        fs.close();
    }

    private static long solve(int n, int w, int[] arr) {

        Arrays.sort(arr);

        HashMap<Long, Long> mp = new HashMap<>();

        for (long x : arr) {
            mp.put(x, mp.getOrDefault(x, 0L) + 1);
        }

        int i = 0;
        long sum = 0;
        long cnt = 0;

        while (i < n) {

            long x = arr[i];

            // remove current element from remaining elements
            if (mp.containsKey(x)) {
                mp.put(x, mp.get(x) - 1);

                if (mp.get(x) == 0) {
                    mp.remove(x);
                }
            }

            sum += x;

            long target = w - sum;

            // find exact target
            if (mp.containsKey(target)) {

                mp.put(target, mp.get(target) - 1);

                if (mp.get(target) == 0) {
                    mp.remove(target);
                }

                cnt++;
                sum = 0;
            }

            // current group is full
            if (sum == w) {
                cnt++;
                sum = 0;
            }

            i++;
        }

        // remaining elements form the last group
        if (sum != 0) {
            cnt++;
        }

        return cnt;
    }
}