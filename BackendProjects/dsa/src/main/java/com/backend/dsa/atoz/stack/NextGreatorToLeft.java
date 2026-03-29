package com.backend.dsa.atoz.stack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class NextGreatorToLeft {

    public static void main(String[] args) {
        //1, 3, 2, 4 -> -1 -1 3 -1
        List<Integer> lst = new ArrayList<>();
        lst.add(1);
        lst.add(3);
        lst.add(2);
        lst.add(4);

        List<Integer> ans = nextGreatorToLeft(lst);
        for (Integer x : ans) {
            System.out.print(x + " ");
        }
        System.out.println();
    }

    private static List<Integer> nextGreatorToLeft(List<Integer> lst) {
        int n = lst.size();
        List<Integer> ans = new ArrayList<>();
        Stack<Integer> stk = new Stack<>();
        for (int i = 0; i < n; i++) {
            if (stk.empty()) {
                ans.add(-1);
            } else if (!stk.empty() && stk.peek() < lst.get(i)) {
                while (!stk.empty() && stk.peek() < lst.get(i)) {
                    stk.pop();
                }

                if (stk.empty()) {
                    // means we didn't get any element for ans
                    ans.add(-1);
                } else {
                    // we got the answer
                    ans.add(stk.peek());
                }
            } else if (!stk.empty() && stk.peek() > lst.get(i)) { // if stk.top is greater means this is our ans
                ans.add(stk.peek());
            }
            stk.add(lst.get(i));
        }
        Collections.reverse(ans);
        return ans;
    }
}
