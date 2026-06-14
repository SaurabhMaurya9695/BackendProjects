package com.backend.dsa.atoz.arrays;

import java.util.Arrays;

public class DestroyingAsteroids {

    public static void main(String[] args) {
        int mass = 5;
        int[] asteroids = { 4,9,23,4 };
        System.out.println(asteroidsDestroyed(mass, asteroids));
    }

    public static boolean asteroidsDestroyed(int mass, int[] arr) {
        Arrays.sort(arr);
        for (int i = 0; i < arr.length; i++) {
            if (mass >= arr[i]) {
                mass += arr[i];
            } else {
                return false;
            }
        }
        return true;
    }
}
