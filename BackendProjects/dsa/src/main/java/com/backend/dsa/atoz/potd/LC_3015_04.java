package com.backend.dsa.atoz.potd;

import java.util.*;

public class LC_3015_04 {

    public int minimumDeletions(String s, int k) {
        int[] freq = new int[26]; // Frequency array for characters 'a' to 'z'

        // Step 1: Count frequency of each character in the string
        for (char ch : s.toCharArray()) {
            freq[ch - 'a']++;
        }

        // Step 2: Sort the frequency array to try different base frequencies
        Arrays.sort(freq);

        int result = s.length(); // Initialize result with the maximum possible deletions

        // Step 3: Try every frequency as a "base" frequency
        for (int i = 0; i < 26; i++) {
            int del = 0;

            for (int j = 0; j < 26; j++) {
                if (i == j) {
                    continue; // Skip same element
                }

                if (freq[j] < freq[i]) {
                    // If frequency is less than base, delete all occurrences
                    del += freq[j];
                } else if (freq[j] - freq[i] > k) {
                    // If frequency is more than freq[i] + k, reduce it to freq[i] + k
                    del += (freq[j] - freq[i] - k);
                }
                // If within k range, do nothing
            }

            // Track the minimum number of deletions needed across all base choices
            result = Math.min(result, del);
        }

        return result;
    }
}
