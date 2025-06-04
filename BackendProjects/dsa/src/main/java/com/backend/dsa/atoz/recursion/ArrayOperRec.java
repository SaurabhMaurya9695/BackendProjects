package com.backend.dsa.atoz.recursion;

import java.util.List;

public class ArrayOperRec {

    public ArrayOperRec(List<?> lst) {
        System.out.print("Displaying array in dec order : ");
        solve(lst, 0, lst.size() - 1);
        System.out.println("\nMax in Array is : " + findOutMax(lst, 0, lst.size() - 1));
    }

    private void solve(List<?> lst, int idx, int n) {
        if (idx == n) {
            System.out.print(lst.get(idx) + " ");
            return;
        }
        solve(lst, idx + 1, n);
        System.out.print(lst.get(idx) + " ");
    }

    private int findOutMax(List<?> lst, int idx, int n) {
        int max = Integer.MIN_VALUE;
        if (idx == n) {
            return Math.max(max, (int) lst.get(n));
        }
        int x = findOutMax(lst, idx + 1, n);
        return Math.max(max, x);
    }
}
