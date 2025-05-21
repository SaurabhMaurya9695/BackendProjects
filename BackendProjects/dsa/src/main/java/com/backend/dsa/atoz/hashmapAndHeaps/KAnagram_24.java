package com.backend.dsa.atoz.hashmapAndHeaps;

import com.backend.dsa.atoz.CommonUtil;

import java.util.HashMap;

public class KAnagram_24 {

    public KAnagram_24(String s1, String s2, int swaps) {
        System.out.println("after doing k swaps ans is : " + areKAnagrams(s1, s2, swaps));
    }

    boolean areKAnagrams(String s1, String s2, int swaps) {
        int n = s1.length();
        int m = s2.length();

        if (n != m) {
            return false;
        }

        int k = swaps;

        HashMap<Character, Integer> map = new HashMap<>();
        for (Character c : s2.toCharArray()) {
            map.put(c, map.getOrDefault(c, 0) + 1);
        }

        for (Character c : s1.toCharArray()) {
            if (map.getOrDefault(c, 0) > 0) {
                map.put(c, map.get(c) - 1);
            }
        }

        int cnt = 0;
        for (Character c : map.keySet()) {
            cnt += map.get(c);
        }

        if (cnt <= k) {
            return true;
        } else {
            return false;
        }
    }

    // NOT all TestCases passed
    private boolean areKAnagramsMethod1(String s, String p, int k) {
        int n = s.length();
        int m = p.length();

        if (m > n) {
            return false;
        }

        //  we have to create a hashMap for p first
        HashMap<Character, Integer> map = new HashMap<>();
        for (Character c : p.toCharArray()) {
            map.put(c, map.getOrDefault(c, 0) + 1);
        }

        // now add m char in another original hashMap
        // Acquire Strategy
        HashMap<Character, Integer> map2 = new HashMap<>();
        for (int i = 0; i < p.length(); i++) {
            map2.put(s.charAt(i), map2.getOrDefault(s.charAt(i), 0) + 1);
        }

        // now both of the maps have two freq stored for s & p, then now check  if freq for both of the maps
        // for every char is same then these two are anagram

        int i = m;
        int j = 0;
        int cnt = 0;

        // release Strategy
        while (i < n) {
            // now we have to compare these two maps
            cnt += CommonUtil.cntDifference(map2, map);
            if (cnt > k) {
                return false;
            }

            // now release time
            // first add i
            char ch = s.charAt(i);
            map2.put(ch, map2.getOrDefault(ch, 0) + 1);

            // now remove from j
            char ch2 = s.charAt(j);
            CommonUtil.removeCharHashMap(map2, ch2);

            i++;
            j++;
        }

        // at the end some parts of string left which is stored in a map
        cnt += CommonUtil.cntDifference(map2, map);
        if (cnt > k) {
            return false;
        }
        return true;
    }
}
