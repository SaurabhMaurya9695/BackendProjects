package com.backend.dsa.atoz.hashmapAndHeaps;

import com.backend.dsa.atoz.CommonUtil;

import java.util.HashMap;

public class CountSubstringsWithKDistinctCharacters_19 {

    public CountSubstringsWithKDistinctCharacters_19(String s, int k) {
        solve(s, k);
    }

    private void solve(String s, int k) {
        // we have to count number of substr which has k distinct char
        // so for aba -> ab , ba & aba
        // we will go with acquire and release strategy only
        // for that we need two hashMaps which can have k & k - 1 char
        // in short one hashMap called smaller one : k - 1 distinct char can hold
        // other big hashMap called bigger one : k distinct char can hold

        if (k == 1) {
            // this is a special case, in this one we don't want two hasMap, only one is sufficient
            solveForOne(s);
        }

        int n = s.length();
        int ans = 0;
        int iBig = -1;
        int iSmall = -1;
        int j = -1;

        HashMap<Character, Integer> mpBig = new HashMap<>();
        HashMap<Character, Integer> mpSmall = new HashMap<>();
        while (true) {
            boolean acquireInBigHasMap = false;
            boolean acquireInSmallHasMap = false;
            boolean release = false;
            // now for big hashMap -> mpBig
            while (iBig < n - 1) { // this should go  < n - 1
                acquireInBigHasMap = true;
                iBig++;
                char ch = s.charAt(iBig);
                mpBig.put(ch, mpBig.getOrDefault(ch, 0) + 1);
                // while putting you have 3 conditions < k || > k || == k
                if (mpBig.size() == k + 1) { // means this time it has k  char
                    CommonUtil.removeCharHashMap(mpBig, ch); // remove this char now go back - 1
                    iBig--;
                    break;
                }
            }

            // now for small hashMap -> mpBig
            while (iSmall < iBig) { // this should go < iBig
                acquireInSmallHasMap = true;
                iSmall++;
                char ch = s.charAt(iSmall);
                mpSmall.put(ch, mpSmall.getOrDefault(ch, 0) + 1);
                // while putting you have 3 conditions < k || > k || == k
                if (mpSmall.size() == k) { // means this time it has k - 1 char
                    CommonUtil.removeCharHashMap(mpSmall, ch);
                    iSmall--;
                    break;
                }
            }

            // now release strategy

            while (j < iSmall) {
                release = true;
                if (mpBig.size() == k || mpSmall.size() == k - 1) {
                    ans += (iBig - iSmall);
                }
                j++;
                char ch = s.charAt(j);
                // if both condition full-fill means we are at correct stage
                CommonUtil.removeCharHashMap(mpBig, ch);
                CommonUtil.removeCharHashMap(mpSmall, ch);

                if (mpBig.size() < k || mpSmall.size() < k - 1) {
                    break;
                }
            }

            if (!acquireInBigHasMap && !acquireInSmallHasMap && !release) {
                break;
            }
        }

        System.out.println("Count Substring with K Distinct Characters: " + ans);
    }

    private void solveForOne(String s) {
        System.out.println("Solving cases for k == 1");
        int n = s.length();
        int i = -1;
        int j = -1;
        int ans = 0;
        HashMap<Character, Integer> mp = new HashMap<>();
        while (true) {

            boolean acquire = false;
            boolean release = false;
            // acquire
            while (i < n - 1) {
                acquire = true;
                i++;
                char ch = s.charAt(i);
                mp.put(ch, mp.getOrDefault(ch, 0) + 1);
                // now after adding it three cases < k , > k , == k
                if (mp.size() > 1) {
                    CommonUtil.removeCharHashMap(mp, ch);
                    i--; // we are not one step ahead , so fall back -1
                    break;
                }
            }

            // release
            while (j < i) {
                release = true;
                if (mp.size() == 1) {
                    ans += (i - j);
                }
                j++;
                char ch = s.charAt(j);
                CommonUtil.removeCharHashMap(mp, ch);
                if (mp.isEmpty()) {
                    break;
                }
            }

            if (acquire == false && release == false) {
                break;
            }
        }
        System.out.println("Count Substring with K Distinct Characters: " + ans);
    }
}
