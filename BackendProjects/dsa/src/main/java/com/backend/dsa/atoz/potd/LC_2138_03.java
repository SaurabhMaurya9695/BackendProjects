package com.backend.dsa.atoz.potd;

import java.util.ArrayList;
import java.util.List;

public class LC_2138_03 {

    public String[] divideString(String s, int k, char fill) {
        int n = s.length();
        List<String> ans = new ArrayList<>();
        int rem = n % k;

        for (int i = 0; i < n; i++) {
            int end = Math.min(i + k, n);
            String part = s.substring(i, end);
            System.out.println(part);  // Debug print, like cout
            ans.add(part);
            i += (k - 1);
        }

        // Handle padding for the last chunk if needed
        String t = ans.get(ans.size() - 1);
        if (t.length() < k) {
            ans.remove(ans.size() - 1);
            int left = k - t.length();
            StringBuilder sb = new StringBuilder(t);
            while (rem > 0 && left-- > 0) {
                sb.append(fill);
            }
            ans.add(sb.toString());
        }

        return ans.toArray(new String[0]);
    }
}
