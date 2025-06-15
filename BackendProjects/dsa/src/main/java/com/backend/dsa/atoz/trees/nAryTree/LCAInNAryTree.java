package com.backend.dsa.atoz.trees.nAryTree;

import java.util.List;
import java.util.Objects;

public class LCAInNAryTree {

    private final int _lca;

    public LCAInNAryTree(Node root, int x, int y) {
        _lca = lca(root, x, y);
    }

    private int lca(Node root, int x, int y) {
        // A common path where both nodes parents meet is called Lowest Common Ancestor
        List<Integer> nodeToRootPathForX = new NodeToRootPath(root, x).getNodeToRootPath();
        List<Integer> nodeToRootPathForY = new NodeToRootPath(root, y).getNodeToRootPath();

        int i = nodeToRootPathForX.size() - 1, j = nodeToRootPathForY.size() - 1;
        while (i >= 0 && j >= 0) {
            if (Objects.equals(nodeToRootPathForX.get(i), nodeToRootPathForY.get(j))) {
                i--;
                j--;
            } else {
                break;
            }
        }

        // jis time ye ni mile it means next one parent is our ans
        return nodeToRootPathForX.get(i + 1);
    }

    public int getLca() {
        return _lca;
    }
}
