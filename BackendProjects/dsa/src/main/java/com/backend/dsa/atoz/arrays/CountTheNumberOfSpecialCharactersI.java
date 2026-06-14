package com.backend.dsa.atoz.arrays;

import java.util.HashSet;

public class CountTheNumberOfSpecialCharactersI {

    public static void main(String[] args) {
        String word = "aaAbcBC";
        System.out.println(numberOfSpecialChars(word));
    }

    public static int numberOfSpecialChars(String word) {
        HashSet<Character> st = new HashSet<>();
        for (Character x : word.toCharArray()) {
            st.add(x);
        }

        int cnt = 0;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (st.contains(Character.toLowerCase(c)) && st.contains(
                    Character.toUpperCase(c))) {
                cnt++;
                st.remove(c);
            }
        }
        return cnt;
    }
}
