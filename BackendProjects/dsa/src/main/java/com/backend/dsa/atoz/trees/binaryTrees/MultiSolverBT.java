package com.backend.dsa.atoz.trees.binaryTrees;

import com.backend.dsa.atoz.trees.nAryTree.Node;

public class MultiSolverBT {

    private int size;
    private int sum;
    private int max;
    private int min;

    public MultiSolverBT(Node node) {
        size = 0;
        sum = 0;
        max = Integer.MIN_VALUE;
        min = Integer.MAX_VALUE;
        size = getSize(node);
        sum = getSum(node);
        max = getMax(node);
        min = getMini(node);
    }

    private int getSize(Node node) {
        // faith on their children node.left & node.right and add 1 for yourself
        if (node == null) {
            return 0;
        }
        int leftSize = getSize(node.getLeft());
        int rightSize = getSize(node.getRight());
        return leftSize + rightSize + 1;
    }

    private int getSum(Node node) {
        if (node == null) {
            return 0;
        }
        int leftSum = getSum(node.getLeft());
        int rightSum = getSum(node.getRight());
        return leftSum + rightSum + node.getValue();
    }

    private int getMax(Node node) {
        if (node == null) {
            return Integer.MIN_VALUE;
        }
        int maxi = Integer.MIN_VALUE;
        maxi = Math.max(getMax(node.getLeft()), maxi);
        maxi = Math.max(getMax(node.getRight()), maxi);
        maxi = Math.max(maxi, node.getValue());
        return maxi;
    }

    private int getMini(Node node) {
        if (node == null) {
            return Integer.MAX_VALUE;
        }
        int mini = Integer.MAX_VALUE;
        mini = Math.min(getMini(node.getLeft()), mini);
        mini = Math.min(getMini(node.getRight()), mini);
        mini = Math.min(node._value, mini);
        return mini;
    }

    @Override
    public String toString() {
        return "MULTI-SOLVER IN BST BY CREATING DIFF DIFF METHODS  { " + "size=" + size + ", sum=" + sum + ", max="
                + max + ", min=" + min + '}';
    }
}
