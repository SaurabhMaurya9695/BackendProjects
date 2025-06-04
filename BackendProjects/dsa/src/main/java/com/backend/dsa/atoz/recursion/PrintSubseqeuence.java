package com.backend.dsa.atoz.recursion;

public class PrintSubseqeuence {

    public PrintSubseqeuence(String s) {
        System.out.print("All SSQ for String " + s + " is : ");
        printSSQ(s, 0, "");
        System.out.println();
    }

    private void printSSQ(String s, int n, String ans) {
        // draw a tree diagram and see what is going on

        if (s.isEmpty()) {
            System.out.print(ans + ",");
            return;
        }

        Character ch = s.charAt(0);
        String leftString = s.substring(1);
        printSSQ(leftString, n, ch + ans);
        printSSQ(leftString, n, ans + " ");
    }
}
