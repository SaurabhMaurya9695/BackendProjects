package com.backend.dsa.atoz.hashmapAndHeaps;

import java.util.Collections;
import java.util.PriorityQueue;

/**
 * The {@code ImplementMedianFinder_05} class provides an efficient way to add integers
 * and retrieve or remove the median value at any point. Internally, it uses two
 * {@link PriorityQueue} instances to maintain a balanced distribution of the input numbers:
 * a max-heap for the left half and a min-heap for the right half.
 *
 * <p>This implementation ensures logarithmic time complexity for add and remove operations
 * while always keeping the data structure balanced to return the median in constant time.
 *
 * <p>Use this class when you want to dynamically calculate the median of a running stream of numbers.
 */
public class ImplementMedianFinder_05 {

    /**
     * Max-heap to store the smaller half of the data
     */
    PriorityQueue<Integer> leftPQ;

    /**
     * Min-heap to store the larger half of the data
     */
    PriorityQueue<Integer> rightPQ;

    /**
     * Constructs an empty {@code ImplementMedianFinder_05}.
     * Initializes two priority queues: one max-heap for the lower half and one min-heap for the upper half.
     */
    public ImplementMedianFinder_05() {
        leftPQ = new PriorityQueue<>(Collections.reverseOrder());
        rightPQ = new PriorityQueue<>();
    }

    /**
     * Adds a new integer to the data structure.
     * The value is placed into one of the two priority queues while maintaining the size balance.
     *
     * @param value the integer to be added
     */
    public void add(int value) {
        if (!rightPQ.isEmpty() && value > rightPQ.peek()) {
            System.out.println("added in left PQ : " + value);
            rightPQ.add(value);
        } else {
            System.out.println("added in right PQ : " + value);
            leftPQ.add(value);
        }

        if (!isBalanced(leftPQ, rightPQ)) {
            if (leftPQ.size() > rightPQ.size()) {
                rightPQ.add(leftPQ.poll());
            } else {
                leftPQ.add(rightPQ.poll());
            }
        }
    }

    /**
     * Removes and returns the median value from the data structure.
     * If the data structure is empty, it prints an underflow warning and returns -1.
     *
     * @return the median value or -1 if the structure is empty
     */
    public int remove() {
        if (this.size() == 0) {
            System.out.println("UnderFlow Condition!!..");
            return -1;
        }

        int n = leftPQ.size();
        int m = rightPQ.size();
        if (n > m) {
            System.out.println("remove elements from left PQ : " + leftPQ.peek());
            return leftPQ.remove();
        } else if (n < m) {
            System.out.println("remove elements from right PQ : " + rightPQ.peek());
            return rightPQ.remove();
        } else {
            System.out.println("remove elements from left PQ : " + leftPQ.peek());
            return (n > 0) ? leftPQ.remove() : 0;
        }
    }

    /**
     * Returns the median value without removing it from the data structure.
     * If the structure is empty, it prints an underflow warning and returns -1.
     *
     * @return the median value or -1 if the structure is empty
     */
    public int peek() {
        if (this.size() == 0) {
            System.out.println("UnderFlow Condition!!..");
            return -1;
        }

        int n = leftPQ.size();
        int m = rightPQ.size();
        if (n > m) {
            System.out.println("peek from left PQ : " + leftPQ.peek());
            return leftPQ.peek();
        } else if (n < m) {
            System.out.println("peek from right PQ : " + leftPQ.peek());
            return rightPQ.peek();
        } else {
            return (n > 0) ? leftPQ.peek() : 0;
        }
    }

    /**
     * Returns the total number of elements stored in the data structure.
     *
     * @return the total size
     */
    public int size() {
        return leftPQ.size() + rightPQ.size();
    }

    /**
     * Checks if the two priority queues are balanced.
     * A balanced state means their sizes differ by at most 2.
     *
     * @param leftPQ  the max-heap representing the left half
     * @param rightPQ the min-heap representing the right half
     * @return {@code true} if balanced; {@code false} otherwise
     */
    private boolean isBalanced(PriorityQueue<Integer> leftPQ, PriorityQueue<Integer> rightPQ) {
        return Math.abs(leftPQ.size() - rightPQ.size()) <= 2;
    }
}
