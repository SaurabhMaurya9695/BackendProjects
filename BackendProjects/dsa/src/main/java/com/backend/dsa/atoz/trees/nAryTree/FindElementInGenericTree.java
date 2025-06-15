package com.backend.dsa.atoz.trees.nAryTree;

public class FindElementInGenericTree {

    private boolean _present;

    public FindElementInGenericTree(Node root, int val) {
        this._present = findElementInNAryTree(root, val);
    }

    private boolean findElementInNAryTree(Node root, int val) {
        if (root._value == val) {
            return true;
        }

        for (Node child : root.getChildren()) {
            if (findElementInNAryTree(child, val)) {
                return true;
            }
        }

        return false;
    }

    public boolean isPresent() {
        return _present;
    }
}
