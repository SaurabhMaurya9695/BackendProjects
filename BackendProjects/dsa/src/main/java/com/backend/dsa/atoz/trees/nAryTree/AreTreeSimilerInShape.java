package com.backend.dsa.atoz.trees.nAryTree;

public class AreTreeSimilerInShape {

    private boolean flag;

    public AreTreeSimilerInShape(Node root1, Node root2) {
        this.flag = isInSimilarShape(root1, root2);
    }

    private boolean isInSimilarShape(Node root1, Node root2) {
        if (root1.getChildren().size() != root2.getChildren().size()) {
            return true;
        }

        for (int i = 0; i < root1.getChildren().size(); i++) {
            if (!isInSimilarShape(root1.getChildren().get(i), root2._children.get(i))) {
                return false;
            }
        }
        return true;
    }

    public boolean isInSimilerShape() {
        return flag;
    }
}
