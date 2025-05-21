package com.backend.dsa.atoz.hashmapAndHeaps;

import com.backend.dsa.atoz.CommonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FIndAllAnagaramInString_25 {

    List<Integer> lst = new ArrayList<>();

    public FIndAllAnagaramInString_25(String sr, String pattern) {
        lst = findAnagrams(sr, pattern);
        CommonUtil.printArray(lst);
    }

    private List<Integer> findAnagrams(String s, String p) {
        int n = s.length();
        int m = p.length();

        if (m > n) {
            return new ArrayList<>();
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

        // release Strategy
        while (i < n) {
            // now we have to compare these two maps
            if (CommonUtil.compare(map2, map)) {
                lst.add(j);
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
        if (CommonUtil.compare(map2, map)) {
            lst.add(j);
        }
        System.out.print("All Anagram starts from here : ");
        return lst;
    }
}
