package com.backend.dsa.atoz.recursion;

public class Factorial {

    public Factorial(int n) {
        System.out.println("Factorial of " + n + " is : " + fact(n));
    }

    private int fact(int n) {
        if (n == 1) {
            return 1;
        }
        return n * fact(n - 1);
    }
}
