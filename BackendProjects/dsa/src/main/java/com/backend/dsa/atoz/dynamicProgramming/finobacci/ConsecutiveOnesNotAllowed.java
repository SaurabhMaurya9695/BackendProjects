package com.backend.dsa.atoz.dynamicProgramming.finobacci;

import java.util.ArrayList;
import java.util.List;

/**
 * Given a positive integer n, count all possible distinct binary strings of length n such that there are no
 * consecutive 1’s.
 * Input: n = 3
 * Output: 5
 * Explanation: 5 strings are ("000", "001", "010", "100", "101").
 */
public class ConsecutiveOnesNotAllowed {

    public ConsecutiveOnesNotAllowed(int n) {
        String ans = "";
        List<String> lst = new ArrayList<>();
        solve(n, ans, lst);
    }

    private void solve(int n, String ans, List<String> lst) {
        if (n == 0) {
            lst.add(ans);
            return;
        }
        if (n == 1) {
            lst.add("1");
            lst.add("0");
        }
    }
}
