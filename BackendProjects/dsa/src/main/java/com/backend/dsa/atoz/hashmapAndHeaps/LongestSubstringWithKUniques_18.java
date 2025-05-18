package com.backend.dsa.atoz.hashmapAndHeaps;

import java.util.HashMap;
import java.util.HashSet;

public class LongestSubstringWithKUniques_18 {

    public LongestSubstringWithKUniques_18(String originalString6, int uniqueCharWeWant) {
        solve(originalString6, uniqueCharWeWant);
//        solve1(originalString6, uniqueCharWeWant);
//        bruteForce(originalString6, uniqueCharWeWant);
    }

    private void solve(String s, int uniqueCharWeWant) {

        int n = s.length();
        int i = 0;
        int j = 0;
        int k = uniqueCharWeWant;
        int cnt = 0;
        HashMap<Character, Integer> mp = new HashMap<>();
        int ans = 0;
        // a a b a c b e b e b e
        // 0 1 2 3 4 5 6 7 8 9 10
        String res = "";
        while (true) {
            // acquire
            boolean acquire = false;
            boolean release = false;
            while (j < n && cnt <= k) {
                char c = s.charAt(j);
                mp.put(c, mp.getOrDefault(c, 0) + 1);
                cnt = mp.size();
                if (cnt <= k) {
                    j++;
                } else {
                    if (mp.get(c) == 1) {
                        mp.remove(c);
                    }
                    break;
                }
                acquire = true;
            }
            // collect ans and release
            while (i < j && cnt > k) {
                // if cnt > k it means our j is at next idx
                if (j - i + 1 > ans) {
                    ans = j - i + 1;
                    res = s.substring(i, j);
                }
                // now release
                char c = s.charAt(i);
                if (mp.containsKey(c)) {
                    if (mp.get(c) == 1) {
                        mp.remove(c);
                        cnt = mp.size();
                        if (cnt == k) {
                            if (j - i + 1 > ans) {
                                ans = j - i + 1;
                                res = s.substring(i, j);
                            }
                        }
                    } else {
                        mp.put(c, mp.get(c) - 1);
                    }
                }
                i++;
                release = true;
            }

            if (!acquire && !release) {
                System.out.println("Longest Substring with K unique Characters :" + 0);
                break;
            }
        }

        if (j - i + 1 > ans) {
            ans = j - i + 1;
            res = s.substring(i, j);
        }
        System.out.println("Longest Substring with K unique Characters : " + ans + " String Found : " + res);
    }

    private void solve1(String s, int k) {
        int n = s.length();
        int i = -1, j = -1;
        HashMap<Character, Integer> mp = new HashMap<>();
        int ans = 0;
        int len = 0;
        String res = "";
        boolean acquire = false;
        boolean release = false;
        while (true) {
            while (i < n - 1) {
                acquire = true;
                i++;
                char c = s.charAt(i);
                mp.put(c, mp.getOrDefault(c, 0) + 1);
                if (mp.size() == k) {
                    ans = i - j;
                    if (ans > len) {
                        len = ans;
                    }
                } else if (mp.get(c) < k) {
                    continue;
                } else {
                    break;
                }
            }

            while (j < i) {
                release = true;
                j++;
                char c = s.charAt(j);
                if (mp.get(c) == 1) {
                    mp.remove(c);
                } else {
                    mp.put(c, mp.get(c) - 1);
                }

                if (mp.size() > k) {
                    continue;
                } else if (mp.size() == k) {
                    len = i - j;
                    if (ans > len) {
                        len = ans;
                    }
                    break;
                }
            }

            if (acquire == false && release == false) {
                System.out.println("No substring found");
                break;
            }
        }
        if (acquire && release) {
            System.out.println("Longest Substring with length : " + len + " String Found : " + res);
        }
    }

    private void bruteForce(String s, int k) {
        int n = s.length();
        int answer = 0;
        int ans = 0;
        String res = "";
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                HashSet<Character> distinct = new HashSet<Character>();
                for (int x = i; x < j; x++) {
                    distinct.add(s.charAt(x));
                }
                if (distinct.size() == k) {
                    answer = Math.max(answer, j - i);
                }
            }
        }

        System.out.println("Longest Substring with K unique Characters  BruteForce Approach is : " + ans);
    }
}
