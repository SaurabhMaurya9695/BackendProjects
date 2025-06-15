package com.backend.dsa.atoz.trees.binaryTrees;

import com.backend.dsa.atoz.trees.nAryTree.Node;

public class RemoveLeafs {

    private final Node _node;

    public RemoveLeafs(Node node) {
        _node = removeLeafs(node);
    }

    private Node removeLeafs(Node node) {
        if (node == null) {
            return null;
        }
        if (node.getLeft() == null && node.getRight() == null) {
            return null;
        }

        // euler will go in post order and attach whatever is coming from the upper
        node.setLeft(removeLeafs(node.getLeft()));
        node.setRight(removeLeafs(node.getRight()));
        return node;
    }

    public Node getNode() {
        return _node;
    }
}
