package com.backend.dsa.atoz.trees.binaryTrees;

import com.backend.dsa.atoz.trees.nAryTree.Node;

public class TiltOfBinaryTree {

    public int tilt = 0;

    public TiltOfBinaryTree(Node node) {
        tiltOfBinaryTree(node);
    }

    private int tiltOfBinaryTree(Node node) {
        if (node == null) {
            return 0;
        }
        int leftVal = tiltOfBinaryTree(node.getLeft());
        int rightVal = tiltOfBinaryTree(node.getRight());
        tilt += Math.abs(leftVal - rightVal);
        return leftVal + rightVal + node._value;
    }
}
