package com.backend.dsa.atoz.potd;

public class LC_396_02 {

    public static int maxRotateFunction(int[] nums) {
        // F(k)     = (0 * a0) + (1 * a1) + (2 * a2) + ... + (n-1)*a(n-1);
        // F(k + 1) = (1 * a0) + (2 * a1) + (3 * a2) + ... + (n-2)*a(n-2) + 0 * a(n-1);
        // ------------------------------------------------------------------------------------
        // F(k + 1) - F(k) = a0 + a1 + a2 + ....... + 1 * a(n - 2) - (n - 1) * a(n - 1);
        // F(k + 1) - F(k) = a0 + a1 + a2 + ....... + 1 * a(n - 2) - (n - 1) * a(n - 1);
        // F(k + 1) - F(k) = a0 + a1 + a2 + ....... + a(n - 2) - (n - 1) * a(n - 1);
        // add and subtract a(n - 1)

        // F(k + 1) - F(k) = a0 + a1 + a2 + ....... + a(n - 2) + a(n - 1) - a(n - 1) - (n - 1) * a(n - 1);
        // F(k + 1) - F(k) = [ a0 + a1 + a2 + ....... + a(n - 2) + a(n - 1)] - a(n - 1) - (n - 1) * a(n - 1);
        // F(k + 1) - F(k) = sum of all elements - a(n - 1) - (n - 1) * a(n - 1);
        // F(k + 1) - F(k) = sum of all elements - a(n - 1) - (n) * a(n - 1) + a(n - 1);
        // F(k + 1) - F(k) = sum of all elements - (n) * a(n - 1);
        // F(k + 1) = F(k) +  sum of all elements - (n) * a(n - 1);

        // so the formula is

        // F(k + 1) - F(k) = sum of all elements - (n) * a(n - 1);
        // to calculate F(k + 1) we need F(k)

        long F_k = 0;

        // Calculate initial F(0)
        for (int i = 0; i < nums.length; i++) {
            F_k += (long) i * nums[i];
        }

        // Calculate total sum of array
        long sum = 0;
        for (int num : nums) {
            sum += num;
        }

        long n = nums.length;
        long ans = F_k;

        // Compute next rotations
        for (int rotation = 1; rotation < n; rotation++) {
            // F(k+1) = F(k) + sum - n * lastElement
            long F_k_plus_one = F_k + sum - n * nums[(int) (n - rotation)];

            ans = Math.max(ans, F_k_plus_one);
            F_k = F_k_plus_one;
        }

        return Math.toIntExact(ans);
    }

    public static void main(String[] args) {
        int[] nums = { 4, 3, 2, 6 };
        int ans = maxRotateFunction(nums);
        System.out.println(ans);
    }
}
