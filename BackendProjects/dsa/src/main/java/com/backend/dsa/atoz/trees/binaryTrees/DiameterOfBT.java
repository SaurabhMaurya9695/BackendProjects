package com.backend.dsa.atoz.trees.binaryTrees;

import com.backend.dsa.atoz.CommonUtil;
import com.backend.dsa.atoz.trees.nAryTree.Node;

public class DiameterOfBT {

    private final int _diamater;
    public final DiaHeight _efficientDiamater;

    public DiameterOfBT(Node node) {
        // diameter can lie on left side || right side || deepest height on => [ leftSide + RightSide + 2 ]
        _diamater = diameter(node);
        _efficientDiamater = getDiameterInEfficientWay(node);
    }

    private int diameter(Node node) {
        if (node == null) {
            return 0;
        }
        int leftD = diameter(node.getLeft());
        int rightD = diameter(node.getRight());
        int FirstCandidate = CommonUtil.max(leftD, rightD);
        int secondCandidate = height(node.getLeft()) + height(node.getRight()) + 2;
        return CommonUtil.max(FirstCandidate, secondCandidate);
    }

    private int height(Node node) {
        if (node == null) {
            return -1;
        }
        int leftHeight = height(node.getLeft());
        int rightHeight = height(node.getRight());
        return CommonUtil.max(leftHeight, rightHeight) + 1;
    }

    private static class DiaHeight {

        int diameter;
        int height;

        public DiaHeight() {
        }

        public DiaHeight(int diameter, int height) {
            this.diameter = diameter;
            this.height = height;
        }
    }

    public DiaHeight getDiameterInEfficientWay(Node node) {
        // so in the above code we are making 2 calls first for diameter then height,
        // so instead of making 2 calls go with 1 call

        if (node == null) {
            return new DiaHeight(0, -1);
        }

        DiaHeight leftPair = getDiameterInEfficientWay(node.getLeft()); // it would return both
        DiaHeight rightPair = getDiameterInEfficientWay(node.getRight()); // it would return both

        DiaHeight ans = new DiaHeight();
        // do the same cal
        ans.height = Math.max(leftPair.height, rightPair.height) + 1; // we are adding 1 for height
        int secondCheckForHeight = leftPair.height + rightPair.height + 2;
        ans.diameter = Math.max(secondCheckForHeight, Math.max(leftPair.diameter, rightPair.diameter));
        return ans;
    }

    public int getDiamater() {
        return _diamater;
    }

    public String getEfficientDiamater() {
        return "Diameter is " + _efficientDiamater.diameter + " Height is " + _efficientDiamater.height;
    }
}
