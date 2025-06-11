package com.backend.dsa.atoz.trees;

import com.backend.dsa.atoz.CommonUtil;

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
