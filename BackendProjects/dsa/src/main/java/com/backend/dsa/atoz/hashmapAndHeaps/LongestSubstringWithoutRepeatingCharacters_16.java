package com.backend.dsa.atoz.hashmapAndHeaps;

import java.util.HashSet;

public class LongestSubstringWithoutRepeatingCharacters_16 {

    public LongestSubstringWithoutRepeatingCharacters_16(String originalString4) {
        solve(originalString4);
    }

    private void solve(String s) {
        int i = 0;
        int j = 0;
        int n = s.length();
        HashSet<Character> st = new HashSet<>();
        int ans = 1; // Min length
        while (j < n) {
            if (st.contains(s.charAt(j))) {
                ans = Math.max(ans, j - i);
                st.remove(s.charAt(i));
                i++;
            } else {
                st.add(s.charAt(j));
                j++;
            }
        }
        System.out.println("Longest Substring Without repeated char length is : " + ans);
    }
}
