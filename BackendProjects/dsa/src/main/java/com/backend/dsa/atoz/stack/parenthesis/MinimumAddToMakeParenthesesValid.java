package com.backend.dsa.atoz.stack.parenthesis;

import java.util.Collections;
import java.util.Stack;

public class MinimumAddToMakeParenthesesValid {

    public static void main(String[] args) {
        String str = "()))((";
        System.out.println(minAddToMakeValid(str));
    }

    public static int minAddToMakeValid(String s) {
        Stack<Character> stack = new Stack<>();
        boolean popHUa = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                if (stack.isEmpty()) {
                    stack.push(c);
                } else {
                    if (stack.peek() == '(') {
                        stack.pop();
                        popHUa = true;
                    }else{
                        stack.push(c);
                    }
                }
            }
        }

        return stack.size();
    }
}
