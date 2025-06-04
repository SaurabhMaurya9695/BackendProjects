package com.backend;

import com.backend.dsa.atoz.recursion.ArrayOperRec;
import com.backend.dsa.atoz.recursion.Factorial;
import com.backend.dsa.atoz.recursion.GetKPC;
import com.backend.dsa.atoz.recursion.GetStairsPath;
import com.backend.dsa.atoz.recursion.GetSubsequence;
import com.backend.dsa.atoz.recursion.MazePath;
import com.backend.dsa.atoz.recursion.PrintEncoding;
import com.backend.dsa.atoz.recursion.PrintOrder;
import com.backend.dsa.atoz.recursion.PrintPermutation;
import com.backend.dsa.atoz.recursion.PrintSubseqeuence;
import com.backend.dsa.atoz.recursion.XpowerN;

import java.util.List;

public class EntryPoint {

    public static void main(String[] args) {
        int n = 5;
        new PrintOrder(n);

        new Factorial(n);

        new XpowerN(2, 3);

        List<Integer> lst = List.of(1, 2, 3, 4, 5);
        new ArrayOperRec(lst);

        new GetSubsequence("abc");

        new GetStairsPath(3);
        new GetStairsPath(4);

        int[][] arr = new int[3][3];
        new MazePath(arr);

        new PrintSubseqeuence("abc");

        new GetKPC("32");

        new PrintPermutation("123");

        new PrintEncoding("123");
        new PrintEncoding("12309");
    }
}
