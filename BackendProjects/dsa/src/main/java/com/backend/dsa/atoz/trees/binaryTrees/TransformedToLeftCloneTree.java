package com.backend.dsa.atoz.trees.binaryTrees;

import com.backend.dsa.atoz.trees.nAryTree.Node;

public class TransformedToLeftCloneTree {

    private final Node _node;

    public TransformedToLeftCloneTree(Node root) {
        _node = transformedToLeftCloneTree(root);
    }

    private Node transformedToLeftCloneTree(Node root) {
        if (root == null) {
            return null;
        }
        Node leftCloneTree = transformedToLeftCloneTree(root.getLeft());
        Node rightCloneTree = transformedToLeftCloneTree(root.getRight());

        Node tempNode = new Node(root._value);
        root.setLeft(tempNode);
        tempNode.setLeft(leftCloneTree);
        root.setRight(rightCloneTree);
        return root;
    }

    public Node getNode() {
        return _node;
    }
}
