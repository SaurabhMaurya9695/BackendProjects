package com.backend.dsa.atoz.recursion;

public class GetKPC {

    public GetKPC(String s) {
        System.out.print("All KPC is : ");
        getKpc(s, "");
        System.out.println();
    }

    String[] keypad = { "", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz" };

    private void getKpc(String s, String ans) {
        if (s.isEmpty()) {
            System.out.print(ans + " ");
            return;
        }
        char ch = s.charAt(0);
        String leftString = s.substring(1);
        String code = keypad[ch - '0'];
        for (Character x : code.toCharArray()) {
            getKpc(leftString, ans + x);
        }
    }
}
