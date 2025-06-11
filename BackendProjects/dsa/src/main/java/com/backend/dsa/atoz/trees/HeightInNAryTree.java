package com.backend.dsa.atoz.trees;

public class HeightInNAryTree {

    // depth of the deepest node
    private int _height;

    public HeightInNAryTree(Node root) {
        this._height = heightOfGenericTree(root);
    }

    private int heightOfGenericTree(Node root) {
        if (root == null) {
            return 0;
        }
        int height = 0;
        for (Node child : root.getChildren()) {
            height = Math.max(height, heightOfGenericTree(child));
        }

        return height + 1;
    }

    public int getHeight() {
        return _height;
    }
}
