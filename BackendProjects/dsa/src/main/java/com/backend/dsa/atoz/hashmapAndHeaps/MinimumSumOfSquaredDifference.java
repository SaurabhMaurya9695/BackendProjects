package com.backend.dsa.atoz.hashmapAndHeaps;

import java.util.PriorityQueue;

public class MinimumSumOfSquaredDifference {

    public static void main(String[] args) {
        int[] num1 = { 7, 5, 0, 12, 14 };
        int[] num2 = { 7, 5, 0, 12, 14 };
        int k1 = 2;
        int k2 = 9;
        System.out.println(minSumSquareDiff(num1, num2, k1, k2));
    }

    public static long minSumSquareDiff(int[] num1, int[] num2, int k1, int k2) {
        int n = num1.length;
        int m = num2.length;
        PriorityQueue<Tuple> pq = new PriorityQueue<>((a, b) -> Long.compare(b.x, a.x));
        for (int i = 0; i < n; i++) {
            int diff = num1[i] - num2[i];
            pq.add(new Tuple((long) diff * diff, num1[i], num2[i]));
        }

        while (k1 != 0 && k2 != 0 && !pq.isEmpty()) {
            Tuple tuple = pq.poll();
            // get the max one from the top
            // decrease max by 1 and min by 1
            int a = tuple.y;
            int b = tuple.z;
            if (a == b) {
                continue;
            } else if (a > b) {
                if (k1 > 0) {
                    a -= 1;
                    k1 -= 1;
                }
            } else {
                if (k2 > 0) {
                    b -= 1;
                    k2 -= 1;
                }
            }

            long newDiff = (long) (a - b) * (a - b);
            pq.add(new Tuple(newDiff, a, b));
        }

        while (k1-- > 0 && !pq.isEmpty()) {
            Tuple tuple = pq.poll();
            // get the max one from the top
            // decrease max by 1 and min by 1
            int a = tuple.y;
            int b = tuple.z;
            if (a == b) {
                continue;
            }
            if (a > b) {
                a -= 1;
            } else {
                a += 1;
            }
            long newDiff = (long) (a - b) * (a - b);
            pq.add(new Tuple(newDiff, a, b));
        }

        while (k2-- > 0 && !pq.isEmpty()) {
            Tuple tuple = pq.poll();
            // get the max one from the top
            // decrease max by 1 and min by 1
            int a = tuple.y;
            int b = tuple.z;
            if (a == b) {
                continue;
            }
            if (b > a) {
                b -= 1;
            } else {
                b += 1;
            }
            long newDiff = (long) (a - b) * (a - b);
            pq.add(new Tuple(newDiff, a, b));
        }

        long sum = 0;
        while (!pq.isEmpty()) {
            Tuple tuple = pq.poll();
            sum += tuple.x;
        }

        return sum;
    }

    static class Tuple {

        long x;
        int y;
        int z;

        public Tuple(long x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
