package com.backend.dsa.atoz.recursion;

import java.util.ArrayList;

public class KSubset {

    public KSubset(String n, int k) {
        int[] arr = new int[k];
        solve(1 , n , k , 0 , new ArrayList<ArrayList<Integer>>());
    }

    private void solve(int idx, String n, int k, int countOfSubset, ArrayList<ArrayList<Integer>> ans) {

    }


}
