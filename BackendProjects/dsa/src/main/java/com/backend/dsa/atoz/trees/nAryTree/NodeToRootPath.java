package com.backend.dsa.atoz.trees.nAryTree;

import java.util.ArrayList;
import java.util.List;

public class NodeToRootPath {

    private List<Integer> _list = new ArrayList<>();

    public NodeToRootPath(Node root, int val) {
        this._list = nodeToRootPath(root, val);
    }

    private List<Integer> nodeToRootPath(Node root, int val) {

        // faith on child and they know how to get the path
        // if 30 gives its path, let's suppose [110, 80, 20] for making a path, till 30 we just need to add 30 more ;
        if (root._value == val) {
            // we found it
            List<Integer> lst = new ArrayList<>();
            lst.add(root._value);
            return lst;
        }

        for (Node child : root.getChildren()) {
            // Faith
            List<Integer> lst = nodeToRootPath(child, val);
            if (!lst.isEmpty()) {
                // it means we found something -> Meeting expectations
                lst.add(root._value);
                return lst;
            }
        }
        return new ArrayList<>();
    }

    public List<Integer> getNodeToRootPath() {
        return _list;
    }
}