package com.backend.dsa.atoz.trees.nAryTree;

public class MultiSolver {

    private int mini;
    private int maxi;
    private int height;
    private int size;

    public MultiSolver(Node node) {
        height = size = 0;
        mini = Integer.MAX_VALUE;
        maxi = Integer.MIN_VALUE;
        multiSolver(node, 0);
    }

    private void multiSolver(Node node, int depth) {
        // check min max in preOrder
        maxi = Math.max(node._value, maxi);
        mini = Math.min(node._value, mini);
        size = size + 1;
        height = Math.max(height, depth);
        for (Node child : node.getChildren()) {
            multiSolver(child, depth + 1);
        }
    }

    @Override
    public String toString() {
        return "MultiSolverBT{" + "mini=" + mini + ", maxi=" + maxi + ", height=" + height + ", size=" + size + '}';
    }
}
