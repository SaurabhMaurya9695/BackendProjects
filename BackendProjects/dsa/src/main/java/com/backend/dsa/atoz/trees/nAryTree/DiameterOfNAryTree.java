package com.backend.dsa.atoz.trees.nAryTree;

public class DiameterOfNAryTree {

    private int _diameter;

    public DiameterOfNAryTree(Node root) {
        _diameter = findDiameter(root);
    }

    // it is not necessary that diameter will pass through the root
    // it can be generated through every child
    private int findDiameter(Node root) {

        int deepestChildNode = -1;
        int secondDeepestChildNode = -1;
        for (Node child : root.getChildren()) {
            int diameter = findDiameter(child);
            if (diameter > deepestChildNode) {
                secondDeepestChildNode = deepestChildNode;
                deepestChildNode = diameter;
            } else if (diameter > secondDeepestChildNode) {
                secondDeepestChildNode = diameter;
            }
        }

        if (deepestChildNode + secondDeepestChildNode + 2 > _diameter) {
            _diameter = deepestChildNode + secondDeepestChildNode + 2;
        }

        deepestChildNode += 1;
        return deepestChildNode;
    }

    public int getDiameter() {
        return _diameter;
    }
}
