package com.backend.dsa.atoz.potd;

public class LC_2311_07 {

    public int longestSubsequence(String s, int k) {
        int n = s.length();
        int cnt = 0;

        for (char ch : s.toCharArray()) {
            if (ch == '0') {
                cnt++;
            }
        }

        long val = 0;
        int power = 0;
        int oneCount = 0;

        // Traverse from right to left
        for (int i = n - 1; i >= 0; i--) {
            if (s.charAt(i) == '1') {
                if (power < 63 && val + (1L << power) <= k) {
                    val += (1L << power);
                    oneCount++;
                }
            }
            power++;
        }

        // Total length = all 0's + valid 1's included
        return cnt + oneCount;
    }
}
