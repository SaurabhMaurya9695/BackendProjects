package com.backend.dsa.atoz.stack.parenthesis;

import java.util.ArrayList;
import java.util.List;

public class RemoveInvalidParentheses {

    public static void main(String[] args) {
        String s = ")(f";
        System.out.println(removeInvalidParentheses(s));
    }

    public static List<String> removeInvalidParentheses(String s) {
        List<String> list = new ArrayList<>();

        int open = 0;
        int close = 0;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                open++;
            } else if (s.charAt(i) == ')') {
                if (open > 0) {
                    open--;
                } else {
                    close++;
                }
            }
        }

        // System.out.println(open + " " + close);

        solve(s, 0, close, open, list, "");
        return list;
    }

    private static void solve(String s, int idx, int closeRemoval, int openRemoval, List<String> list, String ans) {
        if (idx == s.length()) {
            if (closeRemoval == 0 && openRemoval == 0 && isValid(ans) && !list.contains(ans)) {
                list.add(ans);
            }
            return;
        }

        char c = s.charAt(idx);
        // Remove ')'
        if (c == ')' && closeRemoval > 0) {
            solve(s, idx + 1, closeRemoval - 1, openRemoval, list, ans);
        }

        // Remove '('
        if (c == '(' && openRemoval > 0) {
            solve(s, idx + 1, closeRemoval, openRemoval - 1, list, ans);
        }

        // Keep current character
        solve(s, idx + 1, closeRemoval, openRemoval, list, ans + c);
    }

    private static boolean isValid(String s) {
        int balance = 0;

        for (char c : s.toCharArray()) {
            if (c == '(') {
                balance++;
            } else if (c == ')') {
                balance--;

                if (balance < 0) {
                    return false;
                }
            }
        }

        return balance == 0;
    }
}