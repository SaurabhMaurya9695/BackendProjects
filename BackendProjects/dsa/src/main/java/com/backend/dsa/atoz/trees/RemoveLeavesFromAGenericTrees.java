package com.backend.dsa.atoz.trees;

public class RemoveLeavesFromAGenericTrees {

    public RemoveLeavesFromAGenericTrees(Node node) {
        removeLeafFromChild(node);
    }

    private void removeLeafFromChild(Node root) {
        // those nodes whose children's size is zero we need to delete it
        // traverse from back
        for (int i = root._children.size() - 1; i >= 0; i--) {
            Node child = root.getChildren().get(i);
            if (root.getChildren().isEmpty()) {
                root.getChildren().remove(child);
            }
        }
        for (Node child : root.getChildren()) {
            removeLeafFromChild(child);
        }
    }
}
