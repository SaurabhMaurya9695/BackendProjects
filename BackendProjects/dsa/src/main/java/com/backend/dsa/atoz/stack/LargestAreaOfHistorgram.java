package com.backend.dsa.atoz.stack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class LargestAreaOfHistorgram {

    public static void main(String[] args) {
        List<Integer> lst = List.of(2, 1, 5, 6, 2, 3);

        // find next smaller to right & left
        List<Integer> nsr = nextSmallerToRight(lst);
        List<Integer> nsl = nextSmallerToLeft(lst);

        int ans = 0;
        for (int i = 0; i < lst.size(); i++) {
            int width = nsr.get(i) - nsl.get(i) - 1;
            int area = width * lst.get(i);
            ans = Math.max(ans, area);
        }

        System.out.println(ans);
    }

    private static List<Integer> nextSmallerToRight(List<Integer> lst) {
        int n = lst.size();
        List<Integer> ans = new ArrayList<>();
        Stack<Integer> stk = new Stack<>();

        for (int i = n - 1; i >= 0; i--) {

            if (stk.empty()) {
                ans.add(n);
            } else if (!stk.empty() && lst.get(stk.peek()) >= lst.get(i)) {

                while (!stk.empty() && lst.get(stk.peek()) >= lst.get(i)) {
                    stk.pop();
                }

                if (stk.empty()) {
                    ans.add(n);
                } else {
                    ans.add(stk.peek());
                }
            } else if (!stk.empty() && lst.get(stk.peek()) < lst.get(i)) {
                ans.add(stk.peek());
            }

            stk.add(i); // store index
        }

        Collections.reverse(ans);
        return ans;
    }

    private static List<Integer> nextSmallerToLeft(List<Integer> lst) {
        int n = lst.size();
        List<Integer> ans = new ArrayList<>();
        Stack<Integer> stk = new Stack<>();

        for (int i = 0; i < n; i++) {

            if (stk.empty()) {
                ans.add(-1);
            } else if (!stk.empty() && lst.get(stk.peek()) >= lst.get(i)) {

                while (!stk.empty() && lst.get(stk.peek()) >= lst.get(i)) {
                    stk.pop();
                }

                if (stk.empty()) {
                    ans.add(-1);
                } else {
                    ans.add(stk.peek());
                }
            } else if (!stk.empty() && lst.get(stk.peek()) < lst.get(i)) {
                ans.add(stk.peek());
            }

            stk.add(i); // store index
        }

        return ans;
    }
}