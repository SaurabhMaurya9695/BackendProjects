package com.backend.dsa.atoz.arrays;

public class RotateMatrixBy90 {

    public static void main(String[] args) {
        int[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        rotateArray(arr);
    }

    private static void rotateArray(int[][] arr) {
        int n = arr.length;
        int m = arr[0].length;

        // transpose of a matrix
        int[][] temp = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                temp[j][n - i - 1] = arr[i][j];
            }
        }

        // 90 degree rotation
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(temp[i][j] + " ");
            }
            System.out.println();
        }
    }

}
