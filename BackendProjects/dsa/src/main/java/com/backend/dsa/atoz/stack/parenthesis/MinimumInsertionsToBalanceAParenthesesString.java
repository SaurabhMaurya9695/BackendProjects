package com.backend.dsa.atoz.stack.parenthesis;

public class MinimumInsertionsToBalanceAParenthesesString {

    public static void main(String[] args) {
//        System.out.println(minInsertions("())"));
        System.out.println(minInsertions("(()))"));
//        System.out.println(minInsertions("))())("));
    }

    private static int minInsertions(String s) {
        int n = s.length();
        int open = 0, close = 0;
        for (int i = 0; i < n;) {
            char ch = s.charAt(i);
            if (ch == '(') {
                open++;
                i++;
            } else {
                if (i + 1 < n && s.charAt(i + 1) == ')') {
                    // We have a pair of closing parentheses
                    if (open > 0) {
                        open--;
                    } else {
                        close++;
                    }
                    i += 2;
                } else {
                    // Missing one ')' to complete the required '))'
                    close++;
                    if (open > 0) {
                        open--;
                    } else {
                        close++;
                    }
                    i++;
                }
            }
        }
        return open * 2 + close;
    }
}
