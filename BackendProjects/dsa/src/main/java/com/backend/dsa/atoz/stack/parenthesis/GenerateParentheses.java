package com.backend.dsa.atoz.stack.parenthesis;

import java.util.ArrayList;
import java.util.List;

public class GenerateParentheses {

    public static void main(String[] args) {
        int n = 3;
        System.out.println(generateParenthesis(n));
    }

    public static List<String> generateParenthesis(int n) {
        int open = 0;
        int close = 0;
        List<String> result = new ArrayList<>();
        return generateParenthesis(open, close, result, n, "");
    }

    public static List<String> generateParenthesis(int open, int close, List<String> result, int n, String ans) {
        if (open == n && close == n) {
            result.add(ans);
            return result;
        }

        if (open < n) {
            generateParenthesis(open + 1, close, result, n, ans + "(");
        }

        if (close < open) {
            generateParenthesis(open, close + 1, result, n, ans + ")");
        }

        return result;
    }
}