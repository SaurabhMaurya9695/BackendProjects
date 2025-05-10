package com.backend.dsa.atoz.arrays;

import java.util.ArrayList;
import java.util.List;

public class BasicOp {

    // int[] arr = new int[7]; -> if you define globally all valued initialized with 0

    public BasicOp() {
        solve();
    }

    private void solve() {
        int[] arr = new int[7]; //  same here as well
        System.out.println("printing lst " + arr.length);
        for (int i = 0; i < arr.length; i++) {
            System.out.println("val is " + arr[i]);
        }
    }
}
