package com.backend.dsa.atoz.hashmapAndHeaps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FindTheMaximumNumberOfElementsInSubset {

    public static void main(String[] args) {
        int[] nums = { 5, 4, 1, 2, 2 };
        System.out.println(maximumLength(nums));
    }

    public static int maximumLength(int[] nums) {
        HashMap<Integer, List<Integer>> mp = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (mp.containsKey(nums[i] % (int) Math.pow(nums[i], 2))) {
                List<Integer> list = mp.get(nums[i]);
                list.add(nums[i]);
            } else {
                ArrayList<Integer> lst = new ArrayList<>();
                lst.add(nums[i]);
                mp.put(nums[i], lst);
            }
        }
        return 1;
    }
}
