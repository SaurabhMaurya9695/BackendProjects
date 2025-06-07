package com.backend;

import com.backend.dsa.atoz.dynamicProgramming.climbStairs.ClimbStairs;
import com.backend.dsa.atoz.dynamicProgramming.finobacci.Fibonacci;

public class EntryPoint {

    public static void main(String[] args) {
        int n = 7;
        new Fibonacci(n);

        new ClimbStairs(6);


    }
}
