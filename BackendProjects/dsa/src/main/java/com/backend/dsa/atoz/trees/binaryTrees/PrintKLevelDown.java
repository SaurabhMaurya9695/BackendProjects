package com.backend.dsa.atoz.trees.binaryTrees;

import com.backend.dsa.atoz.trees.nAryTree.Node;

import java.util.ArrayList;
import java.util.List;

public class PrintKLevelDown {

    private List<Integer> lst = new ArrayList<>();

    public PrintKLevelDown(Node root, int k) {
        solve(root, k);
    }

    private void solve(Node root, int k) {
        if (root == null || k < 0) {
            return;
        }
        if (k == 0) {
            lst.add(root.getValue());
            return;
        }

        solve(root.getLeft(), k - 1);
        solve(root.getRight(), k - 1);
    }

    public List<Integer> getLst() {
        return lst;
    }
}
