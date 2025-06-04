package com.backend.dsa.atoz.recursion;

public class PrintPermutation {

    public PrintPermutation(String s) {
        System.out.print("All permutation of " + s + " is : ");
        printPermutations(s, "");
        System.out.println();
    }

    private void printPermutations(String s, String ans) {
        // 123 -> 123 , 132 , 213 , 231 , 312 , 321
        // 1234 -> 1123 , 1132 , 1213 , 1231 , 1312 , 1321
        //         2123 , 2132 , 2213 , 2231 , 2312 , 2321
        //         3123 , 3132 , 3213 , 3231 , 3312 , 3321
        //........... so on
        if (s.isEmpty()) {
            System.out.print(ans + ",");
            return;
        }

        for (int idx = 0; idx < s.length(); idx++) {
            String right = s.substring(0, idx);
            char ch = s.charAt(idx);
            String left = s.substring(idx + 1);
            printPermutations(right + left, ans + ch);
        }
    }
}
