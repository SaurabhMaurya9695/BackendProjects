package com.backend.dsa.atoz.arrays;

import java.util.ArrayList;
import java.util.List;

public class RotateMatrixByKInAntiClockWiseDir {

    public static void main(String[] args) {
        int[][] arr = { { 1, 2, 3, 4 }, { 5, 6, 7, 8 }, { 9, 10, 11, 12 }, { 13, 14, 15, 16 } };
        int k = 1;
        rotateGrid(arr, k);
    }

    public static void rotateGrid(int[][] arr, int k) {
        int n = arr.length;
        int m = arr[0].length;

        // now we have to rotate array in anticlockwise dir layer by layer
        // first focus on rotation of a matrix assuming no inner layer exist
        int outerLayer = Math.min(n, m) / 2;
        for (int layer = 0; layer < outerLayer; layer++) {
            int left = layer;
            int right = m - 1 - layer;
            int top = layer;
            int botton = n - 1 - layer;
            rotate(left, right, top, botton, arr, k);
        }
    }

    private static void rotate(int left, int right, int top, int bottom, int[][] arr, int k) {

        List<Integer> lst = new ArrayList<>();

        for (int i = left; i <= right; i++) {
            lst.add(arr[left][i]);
        }

        for (int i = top + 1; i <= bottom; i++) {
            lst.add(arr[i][right]);
        }

        for (int i = right - 1; i >= left; i--) {
            lst.add(arr[bottom][i]);
        }

        for (int i = bottom - 1; i > top; i--) {
            lst.add(arr[i][left]);
        }

        // now we have a boundary - now rotate that boundary in any clockwise dir
        k = k % lst.size();
        rotateAntiClockwise(lst, k);

        int idx = 0;
        for (int i = left; i <= right; i++) {
            arr[left][i] = lst.get(idx);
            idx++;
        }

        for (int i = top + 1; i <= bottom; i++) {
            arr[i][right] = lst.get(idx);
            idx++;
        }

        for (int i = right - 1; i >= left; i--) {
            arr[bottom][i] = lst.get(idx);
            idx++;
        }

        for (int i = bottom - 1; i > top; i--) {
            arr[i][left] = lst.get(idx);
            idx++;
        }
    }

    public static void rotateAntiClockwise(List<Integer> arr, int k) {

        int n = arr.size();
        k = k % n;
        // reverse first k
        reverse(arr, 0, k - 1);
        // reverse remaining
        reverse(arr, k, n - 1);
        // reverse whole array
        reverse(arr, 0, n - 1);
    }

    public static void reverse(List<Integer> arr, int left, int right) {
        while (left < right) {
            int temp = arr.get(left);
            arr.set(left, arr.get(right));
            arr.set(right, temp);
            left++;
            right--;
        }
    }
}
