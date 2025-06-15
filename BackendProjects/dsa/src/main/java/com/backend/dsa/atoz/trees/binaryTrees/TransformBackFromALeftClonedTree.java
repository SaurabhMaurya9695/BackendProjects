package com.backend.dsa.atoz.trees.binaryTrees;

import com.backend.dsa.atoz.trees.nAryTree.Node;

public class TransformBackFromALeftClonedTree {

    private final Node _node;

    public TransformBackFromALeftClonedTree(Node node) {
        _node = solve(node);
    }

    private Node solve(Node node) {
        if (node == null) {
            return null;
        }
        Node getLeftChildNode = solve(node.getLeft().getLeft());
        Node getRightChildNode = solve(node.getRight());

        node.setLeft(getLeftChildNode);
        node.setRight(getRightChildNode);
        return node;
    }

    public Node getNode() {
        return _node;
    }
}
