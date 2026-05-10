package com.backend.dsa.atoz.dynamicProgramming.jumpGame;

public class JumpGameIX {

    public static void main(String[] args) {
        int[] nums = { 20, 21, 25, 15 };
        int[] maxReachableValue = maxValue(nums);
        for (int i = 0; i < maxReachableValue.length; i++) {
            System.out.print(maxReachableValue[i] + " ");
        }
        System.out.println();
    }

    //    We can:
    //      - jump right only to a smaller value
    //      - jump left only to a greater value
    //      - If a bigger value exists on the left and a smaller value exists on the right, then those indices become
    //    connected and can reach the same maximum value.
    //
    //    To detect this:
    //      - pre[i] = maximum till i
    //      - suf[i] = minimum from i to end
    //      - While moving right to left:
    //
    //     if pre[i] > suf[i+1] current index connects with the right segment otherwise a new segment starts

    public static int[] maxValue(int[] nums) {
        int n = nums.length;

        int[] pre = new int[n];
        int[] suf = new int[n];
        int[] res = new int[n];

        // prefix max
        pre[0] = nums[0];
        for (int i = 1; i < n; i++) {
            pre[i] = Math.max(pre[i - 1], nums[i]);
        }

        // suffix min
        suf[n - 1] = nums[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            suf[i] = Math.min(suf[i + 1], nums[i]);
        }

        res[n - 1] = pre[n - 1];

        // build answer
        for (int i = n - 2; i >= 0; i--) {

            // merge segment
            if (pre[i] > suf[i + 1]) {
                res[i] = res[i + 1];
            }
            // new segment
            else {
                res[i] = pre[i];
            }
        }

        return res;
    }
}
