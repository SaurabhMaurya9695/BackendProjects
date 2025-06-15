package com.backend.dsa.atoz.trees.binaryTrees;

import com.backend.dsa.atoz.trees.nAryTree.Node;

public class BalancedBinaryTree {

    public boolean balanced = true;

    public BalancedBinaryTree(Node node) {
        solve(node);
    }

    int solve(Node root) {
        if (root == null) {
            return 0;
        }

        int left = solve(root.getLeft());
        int right = solve(root.getRight());

        if (Math.abs(left - right) > 1) {
            this.balanced = false;
        }

        return Math.max(left, right) + 1;
    }
}
