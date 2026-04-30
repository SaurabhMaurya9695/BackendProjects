package com.backend.dsa.atoz.kmpAlog;

public class KMP {

    public static void main(String[] args) {
        // this Algo is used for pattern matching
        // in string s it has to find a pattern p, there we can use KMP

        String s = "ababcabcabababd";
        String p = "ababd";

        // Naive approach is to use 2 pointers

        // case 1 : put i at s[0] and j at p[0], if both matched increase both pointers and
        //  keep increasing if found both matching, at any case if you don't find matching move i to second idx
        // and j to starting idx of pattern

        // code & the complexity for this is O(n * p.length()) => O(n * m)

        int i = 0, j = 0;
        int idx = 0;

        while (i < s.length() && j < p.length()) {
            if (s.charAt(i) == p.charAt(j)) {
                i++;
                j++;
            } else {
                idx++;
                i = idx;
                j = 0;
            }
        }

        if (j == p.length()) {
            System.out.println("FOUND PATTERN " + p + " in String " + s);
            return;
        }

        System.out.println("NOT FOUND PATTERN " + p + " in String " + s);

        // ---------------------------------------------------------------------------------------------------

        // USING KMP

        int[] lps = buildLPS(p);

        int iIdx = 0; // pointer for s
        int jIdx = 0; // pointer for p

        while (iIdx < s.length()) {

            if (s.charAt(iIdx) == p.charAt(jIdx)) {
                iIdx++;
                jIdx++;
            }

            if (jIdx == p.length()) {
                System.out.println("MATCH FOUND AT IDX " + (iIdx - jIdx)); // match found
            } else if (iIdx < s.length() && s.charAt(iIdx) != p.charAt(jIdx)) {
                if (jIdx != 0) {
                    jIdx = lps[jIdx - 1]; // jump using LPS
                } else {
                    iIdx++;
                }
            }
        }
    }

    public static int[] buildLPS(String s) {
        int n = s.length();
        int[] lps = new int[n];

        int len = 0; // length of previous longest prefix suffix
        int i = 1;

        while (i < n) {
            if (s.charAt(i) == s.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1]; // fallback
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }

        return lps;
    }
}
