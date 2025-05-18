package com.backend.dsa.atoz.hashmapAndHeaps;

import java.util.HashMap;

// FIXME - NOT SOLVED YET
public class CountNumberOfNiceSubarrays_21 {

    public CountNumberOfNiceSubarrays_21(int[] nums, int k) {
        numberOfSubarrays(nums, k);
    }

    public void numberOfSubarrays(int[] nums, int k) {
        int n = nums.length;
        int i = -1;
        int j = -1;
        int ans = 0;
        int odd = 0;
        while (true) {

            boolean acquire = false;
            boolean release = false;

            // acquire
            while (j < n - 1) {
                acquire = true;
                j++;
                int val = nums[j];
                if (val % 2 == 1) {
                    odd++;
                }
                // at the end it sizes becomes k or > k
                if (odd == k) {
                    ans += (n - j);
                    break;
                }
            }

            // release
            while (i < j) {
                release = true;
                i++;
                int val = nums[i];
                if (val % 2 == 1) {
                    odd--;
                }
                // after that it could be < k or == k;
                if (odd == k) {
                    ans += (n - j);
                } else {
                    break;
                }
            }

            if (acquire == false && release == false) {
                break;
            }
        }

        System.out.println("Total Nice Subarrays: " + ans);
    }

    public static void removeIntHashMap(HashMap<Integer, Integer> mp, int x) {
        if (mp.containsKey(x)) {
            if (mp.get(x) == 1) {
                mp.remove(x);
            } else {
                mp.put(x, mp.get(x) - 1);
            }
        } else {
            System.out.println("val not found in HashMap");
        }
    }
}
