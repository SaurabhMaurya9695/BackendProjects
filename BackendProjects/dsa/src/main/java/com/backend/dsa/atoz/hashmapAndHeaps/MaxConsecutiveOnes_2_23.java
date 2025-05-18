package com.backend.dsa.atoz.hashmapAndHeaps;

public class MaxConsecutiveOnes_2_23 {

    public MaxConsecutiveOnes_2_23(int[] arr, int k) {
        solve(arr, k);
    }

    private void solve(int[] arr, int k) {
        // at most means -> 1, 2, 3
        int n = arr.length;
        int i = -1, j = -1;
        int ans = 0, cnt = 0;
        while (true) {
            boolean f1 = false;
            boolean f2 = false;

            //acquire
            while (j < n - 1) {
                f1 = true;
                j++;
                if (arr[j] == 0) {
                    cnt++; //flips
                }

                if (cnt > k) {
                    break;
                }
                ans = Math.max(ans, j - i);
            }

            // release
            while (i < j && cnt > k) {
                f2 = true;
                i++;
                if (arr[i] == 0) {
                    cnt--;
                }
                ans = Math.max(ans, j - i);
            }

            if (!f1 && !f2) {
                break;
            }
        }

        System.out.println("By doing AtMost K flips Max Length would be : " + ans);
    }
}
