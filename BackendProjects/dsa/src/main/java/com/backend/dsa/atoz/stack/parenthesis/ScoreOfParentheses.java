package com.backend.dsa.atoz.stack.parenthesis;

import java.util.Stack;

public class ScoreOfParentheses {

    public static void main(String[] args) {
        String s = "(()(()))";
        System.out.println(scoreOfParentheses(s));
    }

    private static int scoreOfParentheses(String s) {
        int n = s.length();

        Stack<Integer> stack = new Stack<>();
        stack.push(0);

        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);

            if (c == '(') {
                stack.push(0);
            } else {
                int cur = stack.pop();

                if (cur == 0) {
                    cur = 1;
                } else {
                    cur *= 2;
                }

                int prev = stack.pop();
                stack.push(prev + cur);
            }
        }

        return stack.peek();
    }

    private static int scoreOfParentheses1(String s) {
        int n = s.length();
        int ans = 0, balance = 0;
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            if (c == '(') {
                balance++;
            } else {
                balance--;
                if (s.charAt(i - 1) == '(') {
                    ans += (1 << balance);
                }
            }
        }
        return ans;
    }
}