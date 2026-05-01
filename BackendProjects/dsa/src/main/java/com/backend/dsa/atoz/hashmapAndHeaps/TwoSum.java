package com.backend.dsa.atoz.hashmapAndHeaps;

import java.util.HashMap;

public class TwoSum {

    public int[] twoSum(int[] nums, int target) {

        // map to store (number -> index)
        HashMap<Integer, Integer> mp = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {

            int complement = target - nums[i];

            // check if complement already exists
            if (mp.containsKey(complement)) {
                return new int[] { mp.get(complement), i };
            }

            // store current number with index
            mp.put(nums[i], i);
        }

        return new int[] { -1, -1 };
    }
}
