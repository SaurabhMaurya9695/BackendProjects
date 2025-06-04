package com.backend.dsa.atoz.recursion;

import java.util.ArrayList;
import java.util.List;

public class GetSubsequence {

    public GetSubsequence(String s) {
        List<String> lst = getSubseq(s);
        System.out.println(lst);
    }

    private List<String> getSubseq(String s) {
        // subsequence of -> ab -> a_ , ab , _b, __
        // subsequence of -> abc -> a + sub(ab)
        //                                -> _ + sub(ab)

        if (s.isEmpty()) {
            List<String> l = new ArrayList<>();
            l.add("");
            return l;
        }

        char ch = s.charAt(0);
        String leftChar = s.substring(1);
        List<String> subs = getSubseq(leftChar);
        List<String> ans = new ArrayList<>();
        ans.addAll(subs);
        for (int i = 0; i < subs.size(); i++) {
            ans.add(ch + subs.get(i));
        }
        return ans;
    }
}
