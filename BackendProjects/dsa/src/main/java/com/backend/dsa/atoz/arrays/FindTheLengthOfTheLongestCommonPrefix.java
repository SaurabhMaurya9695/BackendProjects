package com.backend.dsa.atoz.arrays;

public class FindTheLengthOfTheLongestCommonPrefix {

    public static void main(String[] args) {
        int[] a = { 1, 2, 10000 };
        int[] b = { 1000 };
        System.out.println(longestCommonPrefix(a, b));

        int[] a1 = { 9932, 9857, 4494 };
        int[] b1 = { 9699, 6286, 4281 };
        System.out.println(longestCommonPrefix(a1, b1));

        int[] a2 = { 10 };
        int[] b2 = { 17, 11 };
        System.out.println(longestCommonPrefix(a2, b2));
    }

    public static int longestCommonPrefix(int[] a, int[] b) {
        int n = a.length;

        int result = 0;
        for (int i = 0; i < n; i++) {
            int x = a[i];
            result = Math.max(result, findLongestCommonPrefix(Integer.toString(x), b));
        }
        return result;
    }

    private static int findLongestCommonPrefix(String x, int[] b) {
        int ans = 0;
        for (int i = 0; i < b.length; i++) {
            String value = Integer.toString(b[i]);
            String value2 = x;
            int cnt = 0;
            for (int j = 0; j < Math.min(value2.length(), value.length()); j++) {
                if (value2.charAt(j) == value.charAt(j)) {
                    cnt++;
                    ans = Math.max(cnt, ans);
                } else {
                    break;
                }
            }
        }
        return ans;
    }
}
