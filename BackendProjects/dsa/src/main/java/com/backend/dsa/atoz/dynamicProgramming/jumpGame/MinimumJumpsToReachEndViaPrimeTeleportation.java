package com.backend.dsa.atoz.dynamicProgramming.jumpGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class MinimumJumpsToReachEndViaPrimeTeleportation {

    public static void main(String[] args) {
        int[] nums = { 4, 6, 5, 8 };
        System.out.println(minimumJumps(nums));
    }

    public static int minimumJumps(int[] nums) {
        int n = nums.length;
        int maxVal = 1000000; // Constraint: 1 <= nums[i] <= 10^6

        // Step 0: Precompute primes using Sieve of Eratosthenes
        // This is O(maxVal * log log maxVal) one-time cost
        boolean[] isPrime = sieveOfEratosthenes(maxVal);

        // Step 1: Build prime-to-indices map
        // Only for actual prime numbers in the array
        Map<Integer, List<Integer>> primeToIndices = new HashMap<>();

        for (int i = 0; i < n; i++) {
            if (isPrime[nums[i]]) {  // O(1) lookup!
                for (int j = 0; j < n; j++) {
                    if (i != j && nums[j] % nums[i] == 0) {
                        primeToIndices.computeIfAbsent(nums[i], k -> new ArrayList<>()).add(j);
                    }
                }
            }
        }

        // Step 2: BFS for shortest path
        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[n];

        queue.offer(0);
        visited[0] = true;
        int jumps = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();

            for (int i = 0; i < size; i++) {
                int curr = queue.poll();

                if (curr == n - 1) {
                    return jumps;
                }

                // Adjacent step right
                if (curr + 1 < n && !visited[curr + 1]) {
                    visited[curr + 1] = true;
                    queue.offer(curr + 1);
                }

                // Adjacent step left
                if (curr - 1 >= 0 && !visited[curr - 1]) {
                    visited[curr - 1] = true;
                    queue.offer(curr - 1);
                }

                // Prime teleportation: O(1) lookup with sieve!
                if (isPrime[nums[curr]]) {
                    List<Integer> targets = primeToIndices.get(nums[curr]);
                    if (targets != null) {
                        for (int j : targets) {
                            if (!visited[j]) {
                                visited[j] = true;
                                queue.offer(j);
                            }
                        }
                        primeToIndices.remove(nums[curr]);
                    }
                }
            }

            jumps++;
        }

        return -1;
    }

    // Sieve of Eratosthenes: efficiently find all primes up to maxVal
    // Time: O(maxVal * log log maxVal)
    // Space: O(maxVal)
    private static boolean[] sieveOfEratosthenes(int maxVal) {
        boolean[] isPrime = new boolean[maxVal + 1];

        // Initialize: assume all numbers are prime
        for (int i = 0; i <= maxVal; i++) {
            isPrime[i] = true;
        }

        // 0 and 1 are not prime
        isPrime[0] = false;
        isPrime[1] = false;

        // Mark non-primes
        for (int i = 2; i * i <= maxVal; i++) {
            if (isPrime[i]) {
                // Mark all multiples of i as not prime
                for (int j = i * i; j <= maxVal; j += i) {
                    isPrime[j] = false;
                }
            }
        }

        return isPrime;
    }
}