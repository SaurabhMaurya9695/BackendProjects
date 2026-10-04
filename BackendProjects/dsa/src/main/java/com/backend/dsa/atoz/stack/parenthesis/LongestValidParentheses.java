package com.backend.dsa.atoz.stack.parenthesis;

import java.util.Stack;

public class LongestValidParentheses {

    public static void main(String[] args) {
        String s = "()))))()))(";
        System.out.println(longestValidParentheses(s));
    }

    private static int longestValidParentheses(String s) {
        int n = s.length();
        Stack<Character> stack = new Stack<>();
        int cnt = 0 ;
        int ans = -1;
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            // we only need '(' to be push and ')' to be pop
            if(c == '('){
                stack.push(c);
            }
            else if(c == ')'){
                if(stack.isEmpty()){
                    stack.push(c);
                }
                else if(stack.peek() == '('){
                    stack.pop();
                    cnt += 2;

                }

                ans = Math.max(ans, cnt);
                cnt = 0 ;
            }
        }
        return cnt;
    }
}
