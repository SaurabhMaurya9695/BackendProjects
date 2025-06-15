package com.backend.dsa.atoz.trees.nAryTree;

import java.util.List;
import java.util.Objects;

public class DistanceBetweenTwoNodes {

    private final int _distance;

    public DistanceBetweenTwoNodes(Node node, int x, int y) {
        this._distance = distanceBWTwoNodes(node, x, y);
    }

    private int distanceBWTwoNodes(Node node, int x, int y) {
        List<Integer> nodeToRootPathX = new NodeToRootPath(node, x).getNodeToRootPath();
        List<Integer> nodeToRootPathY = new NodeToRootPath(node, y).getNodeToRootPath();

        // for 100 -> [100 , 40 , 10 ] -> 1
        // for 110 -> [110 , 80 , 30 , 10] -> 2

        int i = nodeToRootPathX.size() - 1, j = nodeToRootPathY.size() - 1;
        while (i >= 0 && j >= 0 && Objects.equals(nodeToRootPathX.get(i), nodeToRootPathY.get(j))) {
            i--;
            j--;
        }
        i++;
        j++;
        return i + j + 1;
    }

    public int getDistance() {
        return _distance;
    }
}
