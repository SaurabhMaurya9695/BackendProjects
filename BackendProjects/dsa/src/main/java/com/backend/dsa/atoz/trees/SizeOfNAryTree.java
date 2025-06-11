package com.backend.dsa.atoz.trees;

public class SizeOfNAryTree {

    public int _size;

    public SizeOfNAryTree(Node root) {
        this._size = sizeOfNAryTree(root);
    }

    private int sizeOfNAryTree(Node node) {
        if (node == null) {
            return 0;
        }

        int _size = 0;
        for (Node child : node._children) {
            int childOfANode = sizeOfNAryTree(child);
            _size += childOfANode;
        }
        _size += 1;
        return _size;
    }

    public int getSize() {
        return _size;
    }
}
