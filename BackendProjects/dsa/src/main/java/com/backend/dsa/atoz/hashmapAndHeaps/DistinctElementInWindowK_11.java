package com.backend.dsa.atoz.hashmapAndHeaps;

import java.util.ArrayList;
import java.util.HashMap;

public class DistinctElementInWindowK_11 {

    public DistinctElementInWindowK_11(int[] arr16, int k) {
        solve(arr16, k);
        System.out.println("DistinctElementInWindowK_11 is : " + countDistinct(arr16, k));
    }

    private void solve(int[] arr, int k) {
        int i = 0, j = 0;
        int ans = 1; // Min answer should be 1
        int n = arr.length - 1;
        HashMap<Integer, Integer> map = new HashMap<>();
        while (j <= n) {
            if ((j - i) != k) {
                map.put(arr[j], map.getOrDefault(arr[j], 0) + 1);
                j++;
            } else if ((j - i) == k) {
                ans = Math.max(ans, map.size());
                if (map.containsKey(arr[i])) {
                    map.put(arr[i], map.get(arr[i]) - 1);
                    if (map.get(arr[i]) == 0) {
                        map.remove(arr[i]);
                    }
                }
                map.put(arr[j], map.getOrDefault(arr[j], 0) + 1);
                i++;
                j++;
            }
        }
        System.out.println("Max Distinct Element in Window of size k is : " + ans);
    }

    ArrayList<Integer> countDistinct(int arr[], int k) {

        ArrayList<Integer> lst = new ArrayList<>();
        int i = 0, j = 0;
        int ans = 1; // Min answer should be 1
        int n = arr.length - 1;
        HashMap<Integer, Integer> map = new HashMap<>();
        while (j <= n) {
            if ((j - i) != k) {
                map.put(arr[j], map.getOrDefault(arr[j], 0) + 1);
                j++;
            } else if ((j - i) == k) {
                ans = map.size();
                lst.add(ans);
                if (map.containsKey(arr[i])) {
                    map.put(arr[i], map.get(arr[i]) - 1);
                    if (map.get(arr[i]) == 0) {
                        map.remove(arr[i]);
                    }
                }
                map.put(arr[j], map.getOrDefault(arr[j], 0) + 1);
                i++;
                j++;
            }
        }
        lst.add(map.size());
        return lst;
    }
}
