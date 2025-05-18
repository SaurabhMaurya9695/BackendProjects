package com.backend.dsa.atoz.hashmapAndHeaps;

import com.backend.dsa.atoz.CommonUtil;

import java.util.HashMap;
import java.util.HashSet;

public class CountOfEquivalentSubarrays_20 {

    //Count the number of subarray == Count of number of unique element in array
    public CountOfEquivalentSubarrays_20(int[] arr) {
        // 2 5 3 5 2 4 1 3 1 4 , k = 5
        int n = arr.length;
        HashSet<Integer> set = new HashSet<>();
        for (int x : arr) {
            set.add(x);
        }

        int k = set.size();
        int i = -1, j = -1;
        int ans = 0;
        HashMap<Integer, Integer> mp = new HashMap<>();
        while (true) {

            boolean acquire = false;
            boolean release = false;

            // acquire
            while (j < n - 1) {
                acquire = true;
                j++;
                int val = arr[j];
                mp.put(val, mp.getOrDefault(val, 0) + 1);
                if (mp.size() == k) {
                    ans += (n - j); // (n - i) these many subarray present in array
                    break;
                }
            }

            // release
            while (i < j) {
                release = true;
                i++;
                int val = arr[i];
                CommonUtil.removeIntHashMap(mp, val);
                // after removing there could be some condition : < k || == k
                if (mp.size() == k) {
                    ans += (n - j); // if equal k then count those values (subarray)
                } else {
                    break;
                }
            }

            if ((acquire == false) && (release == false)) {
                break;
            }
        }

        System.out.println("Total number of subarray : " + ans);
    }
}
