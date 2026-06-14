package com.backend.dsa.atoz.arrays;

public class TotalWavinessOfNumbersInRangeI {

    public static void main(String[] args) {
        int nums1 = 4848;
        int nums2 = 4848;
        System.out.println(totalWaviness(nums1, nums2));
    }

    public static int totalWaviness(int nums1, int nums2) {
        int cnt = 0;
        for (int x = nums1; x <= nums2; x++) {
            String str = Integer.toString(x);
            if (str.length() >= 3) {
                cnt += lengthMoreThanThree(str);
            }
        }

        return cnt;
    }

    private static int lengthMoreThanThree(String str) {
        int cnt = 0;
        for (int i = 0; i <= str.length() - 3; i++) {
            String substr = str.substring(i, i + 3);

            if (isValley(substr) || isPeak(substr)) {
                cnt++;
            }
        }
        return cnt;
    }

    private static boolean isPeak(String str) {
        return str.charAt(0) < str.charAt(1) && str.charAt(1) > str.charAt(2);
    }

    private static boolean isValley(String str) {
        return str.charAt(0) > str.charAt(1) && str.charAt(1) < str.charAt(2);
    }
}
