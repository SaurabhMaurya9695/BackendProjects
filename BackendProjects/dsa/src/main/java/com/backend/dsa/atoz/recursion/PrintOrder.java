package com.backend.dsa.atoz.recursion;

public class PrintOrder {

    public PrintOrder(int n) {
        printInc(n);
        System.out.println();
        System.out.print("Decreasing Order : ");
        printDec(n);
        System.out.println();
    }

    private void printInc(int n) {
        if (n == 0) {
            System.out.print("Increasing Order : ");
            return;
        }
        printInc(n - 1);
        System.out.print(n + " ");
    }

    private void printDec(int n) {
        if (n == 0) {
            return;
        }
        System.out.print(n + " ");
        printDec(n - 1);
    }
}
