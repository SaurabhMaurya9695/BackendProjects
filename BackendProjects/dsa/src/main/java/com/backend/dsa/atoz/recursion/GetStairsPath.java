package com.backend.dsa.atoz.recursion;

import java.util.ArrayList;
import java.util.List;

public class GetStairsPath {

    public GetStairsPath(int n) {
        System.out.println("These are the ways to reach at 0 " + getStairsPath(n));
    }

    private List<String> getStairsPath(int n) {
        if (n == 0) {
            List<String> lst = new ArrayList<>();
            lst.add("");
            return lst;
        }

        if (n < 0) {
            return new ArrayList<>();
        }
        List<String> path1 = getStairsPath(n - 1);
        List<String> path2 = getStairsPath(n - 2);
        List<String> path3 = getStairsPath(n - 3);

        List<String> ans = new ArrayList<>();
        for (int i = 0; i < path1.size(); i++) {
            ans.add("1" + path1.get(i));
        }

        for (int i = 0; i < path2.size(); i++) {
            ans.add("2" + path2.get(i));
        }

        for (int i = 0; i < path3.size(); i++) {
            ans.add("3" + path3.get(i));
        }
        return ans;
    }
}
