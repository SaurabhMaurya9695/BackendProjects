package com.backend.dsa.atoz.hashmapAndHeaps;

import com.backend.dsa.atoz.CommonUtil;

import java.util.HashMap;

public class MinimumWindowSubstring01_15 {

    public MinimumWindowSubstring01_15(String s, String t) {
        solve(s, t);
    }

    private void solve(String s, String t) {
        String ans = "";
        int matchCount = 0;
        int reqCount = t.length();
        int i = -1;
        int j = -1;

        HashMap<Character, Integer> windowStringHashMap = new HashMap<>();
        for (char ch : t.toCharArray()) {
            windowStringHashMap.put(ch, windowStringHashMap.getOrDefault(ch, 0) + 1);
        }
        HashMap<Character, Integer> mainStringHashMap = new HashMap<>();

        while (true) {

            boolean flag1 = false;
            boolean flag2 = false;

            //acquire
            while (i < s.length() - 1 && matchCount < reqCount) {
                i++;
                char ch = s.charAt(i);
                mainStringHashMap.put(ch, mainStringHashMap.getOrDefault(ch, 0) + 1);

                if (mainStringHashMap.getOrDefault(ch, 0) <= windowStringHashMap.getOrDefault(ch, 0)) {
                    matchCount++;
                }

                flag1 = true;
            }

            //Collect answer and release
            while (j < i && matchCount == reqCount) {

                // collect answer
                String pans = s.substring(j + 1, i + 1);
                if (ans.isEmpty() || pans.length() < ans.length()) {
                    ans = pans;
                }

                // release
                j++;
                char ch = s.charAt(j);
                if (mainStringHashMap.get(ch) == 1) {
                    mainStringHashMap.remove(ch);
                } else {
                    mainStringHashMap.put(ch, mainStringHashMap.get(ch) - 1);
                }

                if (mainStringHashMap.getOrDefault(ch, 0) < windowStringHashMap.getOrDefault(ch, 0)) {
                    matchCount--;
                }

                flag2 = true;
            }

            if (!flag2) {
                if (!flag1) {
                    break;
                }
            }
        }
        System.out.println("Min Window Substr is : " + (ans.isEmpty() ? "" : ans));
    }
}
