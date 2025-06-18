package com.backend.dsa.atoz.trees.binaryTrees;

import com.backend.dsa.atoz.trees.nAryTree.Node;

import java.util.Objects;

public class LongestZigZagPathInBinaryTree {

    public LongestZigZagPathInBinaryTree(Node node) {
        longestZigZag(node);
    }

    int pathLength = 0;

    private void dfs(Node node, String drxn, int steps) {
        if (node == null) {
            return;
        }
        pathLength = Math.max(pathLength, steps);
        if (Objects.equals(drxn, "LEFT")) {
            dfs(node._left, "RIGHT", steps + 1);
            dfs(node._right, "LEFT", 1);
        } else {
            dfs(node._left, "RIGHT", 1);
            dfs(node._right, "LEFT", steps + 1);
        }
    }

    public int longestZigZag(Node root) {
        dfs(root, "LEFT", 0);
        return pathLength;
    }
}
