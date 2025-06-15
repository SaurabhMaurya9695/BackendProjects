package com.backend.dsa.atoz.trees.nAryTree;

public class MaximumInNAryTree {

    private int Maxi;

    public MaximumInNAryTree(Node root) {
        this.Maxi = findMaxi(root);
    }

    private int findMaxi(Node root) {
        // we can do this ques in pre-order and post order as well now going with pre order
        // we can't do in pre-order because this method is not void type
        if (root == null) {
            return Integer.MIN_VALUE;
        }
        int maxi = Integer.MIN_VALUE;
        // faith on children
        for (Node child : root.getChildren()) {
            int ansComing = findMaxi(child);
            maxi = Math.max(ansComing, maxi);
        }

        // compare with self-value
        return Math.max(maxi, root._value);
    }

    public int getMaxi() {
        return Maxi;
    }
}
