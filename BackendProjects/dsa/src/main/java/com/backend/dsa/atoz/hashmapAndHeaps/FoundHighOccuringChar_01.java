package com.backend.dsa.atoz.hashmapAndHeaps;

import java.util.HashMap;

public class FoundHighOccuringChar_01 {

    public FoundHighOccuringChar_01(String[] s) {
        findHighestOccurringChar(s);
    }

    private void findHighestOccurringChar(String[] s) {
        HashMap<String, Integer> mp = new HashMap<>();
        for (String x : s) {
            mp.put(x, mp.getOrDefault(x, 0) + 1);
        }

        int ans = 0;
        String highestOccChar = "";
        for (String key : mp.keySet()) {
            if (mp.get(key) > ans) {
                ans = mp.get(key);
                highestOccChar = key;
            }
        }
        System.out.println("Highest occurring char: " + highestOccChar + " and occured " + ans + " times");
    }
}
