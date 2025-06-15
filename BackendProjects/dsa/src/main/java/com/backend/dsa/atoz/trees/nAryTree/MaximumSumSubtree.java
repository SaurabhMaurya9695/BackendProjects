package com.backend.dsa.atoz.trees.nAryTree;

public class MaximumSumSubtree {

    private int maxSumIsComingFromNode;
    private int maxSumTillNow;

    public MaximumSumSubtree(Node node) {
        maxSumIsComingFromNode = 0;
        maxSumTillNow = Integer.MIN_VALUE;
        calculateSum(node);
    }

    private int calculateSum(Node node) {
        int sum = 0;

        // Faith -> faith on child they can return their sum
        for (Node child : node.getChildren()) {
            int childSum = calculateSum(child);
            sum += childSum;
        }

        // Expectations: whatever the sum returns by children, we will add yourself
        sum += node._value;
        if (sum > maxSumTillNow) {
            maxSumTillNow = sum;
            maxSumIsComingFromNode = node._value;
        }
        return sum;
    }

    @Override
    public String toString() {
        return "MAX SUM OF SUBTREE IN N-ARY TREE IS : {" + "maxSumIsComingFromNode=" + maxSumIsComingFromNode
                + ", maxSumTillNow=" + maxSumTillNow + '}';
    }
}
