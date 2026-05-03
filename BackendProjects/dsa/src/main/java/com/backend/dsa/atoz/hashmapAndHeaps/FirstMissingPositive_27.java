package com.backend.dsa.atoz.hashmapAndHeaps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class FirstMissingPositive_27 {

    public static int firstMissingPositive(int[] nums) {
        Arrays.sort(nums);

        //  0 1 2
        // -1 1 3 4
        //  7 8 9 11 12

        // we have to return a smallest positive number
        // so it means we don't want to consider negative numbers here
        boolean allNeg = true;
        for (Integer x : nums) {
            if (x > 0) {
                allNeg = false;
                break;
            }
        }

        if (allNeg) {
            // means we have all negative present
            return 1;
        }

        // it means we have some neg and pos number
        // extract all positive numbers
        Set<Integer> st = new TreeSet<>();
        for (Integer x : nums) {
            if (x > 0) {
                st.add(x);
            }
        }

        List<Integer> pos = st.stream().toList();

        // now we have all the positive numbers
        // now iterate over the array and check which number is missing
        int i;
        for (i = 0; i < pos.size(); i++) {
            // assuming all of them are positive and sorted
            if ((i + 1) != pos.get(i)) {
                return i + 1;
            }
        }

        return i + 1;
    }

    public static void main(String[] args) {
        //        int[] nums = { 1, 2, 0 };
        //        int[] nums = { 0, 2, 2, 1, 1 };
        int[] nums = { 100000, 3, 4000, 2, 15, 1, 99999 };
        System.out.println(firstMissingPositive(nums));
    }
}
