package com.backend.dsa.atoz.potd;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LC_2200_05 {

    public List<Integer> findKDistantIndices(int[] nums, int key, int k) {
        List<Integer> ans = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < nums.length; j++) {
                if (nums[j] == key && Math.abs(i - j) <= k) {
                    ans.add(i);
                }
            }
        }

        return ans;
    }
}
