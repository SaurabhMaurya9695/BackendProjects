package com.backend.dsa.atoz.recursion;

import com.backend.dsa.atoz.CommonUtil;

public class PrintEncoding {

    public PrintEncoding(String s) {
        System.out.print("Encoding for " + s + " is [");
        printEncoding(s, "");
        System.out.println("]");
    }

    private void printEncoding(String s, String ans) {
        // we can make call between 1 to 26

        if (s.isEmpty()) {
            System.out.print(ans + ",");
            return;
        } else if (s.length() == 1) {
            if (s.charAt(0) == '0') {
                return;
            } else {
                System.out.print(CommonUtil.AlphabetValueOfChar(s.charAt(0)) + ",");
                return;
            }
        }

        char first = s.charAt(0);
        String second = s.substring(0, 2);
        int firstNo = first - '0';
        if (firstNo >= 1 && firstNo <= 9) {
            // we are good to go with the calls
            String rightString = s.substring(1);
            printEncoding(rightString, ans + CommonUtil.AlphabetValueOfChar(first));
        }

        int secondValue = Integer.parseInt(second);
        if (second.charAt(0) != '0' && secondValue >= 10 && secondValue <= 26) {
            String leftString = s.substring(2);
            printEncoding(leftString, ans + (char) ('a' + secondValue - 1));
        }
    }
}
