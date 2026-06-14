package com.backend.dsa.atoz.arrays;

import java.util.Arrays;

public class CountTheNumberOfSpecialCharactersII {

    public static void main(String[] args) {
        String word = "AbBCab";
        System.out.println(numberOfSpecialChars(word));
    }

    public static int numberOfSpecialChars(String word) {
        int[] state = new int[26];
        Arrays.fill(state, -1);
        for (char ch : word.toCharArray()) {
            int idx = Character.toLowerCase(ch) - 'a';
            // first lowercase encountered
            if (Character.isLowerCase(ch)) {
                if (state[idx] == -1) {
                    state[idx] = 0;
                }
                // lowercase appears again after uppercase -> invalid
                else if (state[idx] == 1) {
                    state[idx] = 2;
                }
            }

            // uppercase encountered
            else {
                // uppercase comes after lowercase -> special char
                if (state[idx] == 0) {
                    state[idx] = 1;
                }

                // uppercase appears before lowercase -> invalid
                else if (state[idx] == -1) {
                    state[idx] = 2;
                }
            }
        }

        int count = 0;
        for (int val : state) {
            if (val == 1) {
                count++;
            }
        }

        return count;
    }
}
