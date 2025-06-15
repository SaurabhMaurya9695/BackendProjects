package com.backend.dsa.atoz.trees.nAryTree;

import java.util.Collections;

public class MirrorInGenericTree {

    public MirrorInGenericTree(Node root) {
        mirrorInNAryTree(root);
    }

    private void mirrorInNAryTree(Node root) {
        for (Node child : root.getChildren()) {
            mirrorInNAryTree(child);
        }
        Collections.reverse(root.getChildren());
    }
}
