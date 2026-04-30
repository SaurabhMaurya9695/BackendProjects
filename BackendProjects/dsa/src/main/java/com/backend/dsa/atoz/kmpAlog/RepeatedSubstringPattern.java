package com.backend.dsa.atoz.kmpAlog;

import com.backend.dsa.atoz.CommonUtil;


public class RepeatedSubstringPattern {

    public static void main(String[] args) {
        String s = "abcabcabcabc";
        System.out.println(repeatedSubstringPattern(s));
    }

    public static boolean repeatedSubstringPattern(String s) {
        // now for "abcabcabcabc"
        // LPS would be - LPS Array: [0, 0, 0, 1, 2, 3, 4, 5, 6]
        int n = s.length();
        int[] lps = CommonUtil.buildLPS(s);

        // If LPS[n-1] is 0, no repeating pattern exists
        if (lps[n - 1] == 0) {
            return false;
        }

        // Pattern length = total length - overlapping length
        int patternLen = n - lps[n - 1];

        // Pattern length must divide total length evenly
        return n % patternLen == 0;
    }
}
