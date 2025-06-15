package com.backend.dsa.atoz.trees.binaryTrees;

import com.backend.dsa.atoz.trees.nAryTree.Node;

import java.util.ArrayList;
import java.util.List;

public class NodeToRootPathBT {

    private List<Integer> _path = new ArrayList<>();
    private List<Node> _pathInNodes = new ArrayList<>();

    public NodeToRootPathBT(Node root, int val) {
        nodeToRootPath(root, val);
    }

    private boolean nodeToRootPath(Node root, int val) {
        if (root == null) {
            return false;
        }
        if (root.getValue() == val) {
            // once it matched with the value , euler start going back to root
            return true;
        }

        if (nodeToRootPath(root.getLeft(), val)) {
            // so we need to catch all of the portion from where euler came back
            _path.add(root.getLeft()._value);
            _pathInNodes.add(root.getLeft());
            return true;
        }

        if (nodeToRootPath(root.getRight(), val)) {
            // so we need to catch all of the portion from where euler came back
            _path.add(root.getRight()._value);
            _pathInNodes.add(root.getRight());
            return true;
        }

        return false;
    }

    public List<Integer> getPath() {
        return _path;
    }

    public List<Node> getPathInNodes() {
        return _pathInNodes;
    }
}
