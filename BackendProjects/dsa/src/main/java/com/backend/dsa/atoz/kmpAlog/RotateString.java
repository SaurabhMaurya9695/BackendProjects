package com.backend.dsa.atoz.kmpAlog;

import com.backend.dsa.atoz.CommonUtil;

public class RotateString {

    public static void main(String[] args) {
        String s = "abcde";
        String p = "cdeab";
        System.out.println(rotateString(s, p));
    }

    public static boolean rotateString(String ss, String p) {
        if (ss.length() != p.length()) {
            return false;
        }

        String s = ss + ss;
        int[] lps = CommonUtil.buildLPS(p);

        int iIdx = 0; // pointer for s
        int jIdx = 0; // pointer for p

        while (iIdx < s.length()) {

            if (s.charAt(iIdx) == p.charAt(jIdx)) {
                iIdx++;
                jIdx++;
            }

            if (jIdx == p.length()) {
                return true;
            } else if (iIdx < s.length() && s.charAt(iIdx) != p.charAt(jIdx)) {
                if (jIdx != 0) {
                    jIdx = lps[jIdx - 1]; // jump using LPS
                } else {
                    iIdx++;
                }
            }
        }
        return false;
    }
}
