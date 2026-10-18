package com.backend.dsa.atoz.arrays;

public class MaximizeSumOfSquaresOfDigits {

    public static void main(String[] args) {
        int num = 2, sum = 10;
        System.out.println(maxSumOfSquares(num, sum));
    }

    public static String maxSumOfSquares(int n, int s) {
        if (n == 1 && s >= 10) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        while (s > 9) {
            sb.append(9);
            s -= 9;
        }

        sb.append(s);

        if (sb.length() == n) {
            return sb.toString();
        }
        int left = n - sb.length();
        while (left > 0) {
            sb.append('0');
            left -= 1;
        }

        if (sb.length() == n) {
            return sb.toString();
        }
        return "";
    }
}
