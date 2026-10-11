package com.backend.dsa.atoz.stack.parenthesis;

import java.util.Stack;

public class RemoveOutermostParentheses {

    public static void main(String[] args) {
        String str = "(()())(())";
        System.out.println(removeOuterParentheses(str));
    }

//    private static String removeOuterParentheses(String s) {
//        int n = s.length();
//        Stack<Character> stack = new Stack<>();
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < n; i++) {
//            if (s.charAt(i) == '(') {
//                if (!stack.isEmpty()) {
//                    sb.append(s.charAt(i));
//                }
//                stack.push('(');
//            }
//            else if (s.charAt(i) == ')') {
//                stack.pop();
//                if (!stack.isEmpty()) {
//                    sb.append(s.charAt(i));
//                }
//            }
//        }
//
//        return sb.toString();
//    }

    public static String removeOuterParentheses(String s) {
        StringBuilder sb = new StringBuilder();
        int n = s.length();
        int open = 0;
        for(int i = 0 ; i < n ; i ++){
            if(s.charAt(i) == '('){
                open ++ ;
                if(open > 1){
                    sb.append('(');
                }
            }
            else{
                open --;
                if(open >= 1){
                    sb.append(')');
                }
            }
        }
        return sb.toString();
    }
}
