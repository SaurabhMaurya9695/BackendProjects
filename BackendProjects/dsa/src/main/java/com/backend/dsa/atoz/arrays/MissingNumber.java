package com.backend.dsa.atoz.arrays;

// Assume the given array is: {1, 2, 4, 5} and N = 5.
// XOR of (1 to 5) i.e. xor1 = (1^2^3^4^5)
// XOR of array elements i.e. xor2 = (1^2^4^5)
// XOR of xor1 and xor2 = (1^2^3^4^5) ^ (1^2^4^5)
// 			= (1^1)^(2^2)^(3)^(4^4)^(5^5)
//			= 0^0^3^0^0 = 0^3 = 3.
// The missing number is 3.
public class MissingNumber {

    public MissingNumber(int[] arr, int n) {
        missingNumber(arr, n);
    }

    private void missingNumber(int[] arr, int N) {
        int xor1 = 0;
        int xor2 = 0;

        //  xor of same number is zero - read dry run above
        for (int i = 0; i < N; i++) {
            xor1 = xor1 ^ (i + 1);
        }

        for (int i = 0; i < arr.length; i++) {
            xor2 = xor2 ^ arr[i];
        }

        System.out.println("Missing number is : " + (xor1 ^ xor2));
    }
}
