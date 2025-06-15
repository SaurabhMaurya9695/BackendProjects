package com.backend.dsa.atoz.trees.binaryTrees;

import com.backend.dsa.atoz.trees.nAryTree.Node;

import java.util.ArrayList;
import java.util.List;

public class PrintSingleChildNodes {

    private List<Integer> lst = new ArrayList<>();

    public PrintSingleChildNodes(Node node) {
        printSingleChild(node, null);
    }

    private void printSingleChild(Node node, Node parent) {
        if (node == null) {
            return;
        }

        if (parent != null && parent.getLeft() == null && parent.getRight() != null) {
            lst.add(node._value);
        } else if (parent != null && parent.getLeft() != null && parent.getRight() == null) {
            lst.add(node._value);
        }
        printSingleChild(node.getLeft(), node);
        printSingleChild(node.getRight(), node);
    }

    public List<Integer> getAllOneChildMissingNodes() {
        return lst;
    }
}
