package com.backend.dsa.atoz.trees.binaryTrees;

import com.backend.dsa.atoz.trees.nAryTree.Node;

import java.util.ArrayList;
import java.util.List;

public class RootToLeftPaths {

    private List<String> _lst = new ArrayList<>();

    public RootToLeftPaths(Node node) {
        binaryTreePaths(node);
    }

    public void binaryTreePaths(Node root) {
        solve(root, "");
    }

    private void solve(Node root, String ans) {
        if (root == null) {
            return;
        }
        if (root.getLeft() == null && root.getRight() == null) {
            ans = ans + root.getValue();
            _lst.add(ans);
            return;
        }
        solve(root.getLeft(), ans + root.getValue() + "->");
        solve(root.getRight(), ans + root.getValue() + "->");
    }

    public List<String> getBListT() {
        return _lst;
    }
}
