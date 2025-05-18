package com.backend.dsa.atoz.hashmapAndHeaps;

public class MaximumConsecutiveOnes_1_22 {

    public MaximumConsecutiveOnes_1_22(int[] arr, int k) {
        SolveByMethod1(arr, k);
        SolveByMethod2(arr, k);
    }

    private void SolveByMethod2(int[] arr, int k) {
        int n = arr.length;
        int i = 0, j = -1;
        int ans = 0;
        int cnt = 0;
        while (i < n) {
            if (arr[i] == 0) {
                cnt++;
            }

            while (cnt > k) {
                j++;
                if (arr[j] == 0) {
                    cnt--;
                }
            }

            ans = Math.max(ans, i - j);
            i++;
        }
        System.out.println("Solved by Method 2 ans is : " + ans);
    }

    private void SolveByMethod1(int[] arr, int k) {
        int n = arr.length;
        int i = -1;
        int j = -1;
        int ans = 0;
        int cntZero = 0;
        int check = 0;
        for (int x : arr) {
            check += (x == 1) ? 1 : 0;
        }
        if (check == n) {
            System.out.println("All Zeros then answere is : " + check);
            return;
        }
        while (true) {

            boolean acquire = false;
            boolean release = false;
            // acquire
            while (j < n - 1) {
                acquire = true;
                j++;
                cntZero += (arr[j] == 0) ? 1 : 0;
                if (cntZero > k) {
                    cntZero--;
                    j--;
                    break;
                }
            }

            // release
            while (i < j) {
                release = true;

                if (cntZero == k) {
                    if (j - i > ans) {
                        ans = j - i;
                    }
                }
                i++;
                if (arr[i] == 0) {
                    cntZero--;
                }
                // after that there are some cases < k , == k
                if (cntZero == k) {
                    if (j - i > ans) {
                        ans = j - i;
                    }
                } else if (cntZero < k) {
                    break;
                }
            }

            if (release == false && acquire == false) {
                break;
            }
        }

        System.out.println("Maximum subarray of consecutive 1's is of size : " + ans);
    }
}
