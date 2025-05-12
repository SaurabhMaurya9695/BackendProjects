package com.backend.dsa.atoz.hashmapAndHeaps;

import java.util.HashMap;

// if a number is x which is divisible by k => then their divider be x and k - x
public class CheckEveryPairIsDivisibleByK_10 {

    public CheckEveryPairIsDivisibleByK_10(int[] arr14, int divisibleBY) {
        checkEveryPairIsDivisibleByK_10(arr14, divisibleBY);
    }

    private void checkEveryPairIsDivisibleByK_10(int[] arr, int k) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            int rem = (((arr[i] % k) + k) % k);
            map.put(rem, map.getOrDefault(rem, 0) + 1);
        }

        boolean ok = false;
        for (Integer rem : map.keySet()) {
            if (rem == 0) {
                // then check for its freq
                if (map.get(rem) % 2 != 0) {
                    ok = true;
                    break;
                }
            } else {
                int fx = map.get(rem);
                if (!map.containsKey(k - rem)) {
                    ok = true;
                    break;
                }
                int f_x = map.get(Math.abs(k - rem));
                if (fx != f_x) {
                    ok = true;
                    break;
                }
            }
        }

        if (ok) {
            System.out.println("All number is divisible by K");
        } else {
            System.out.println("All number is Not divisible by K");
        }
    }
}
