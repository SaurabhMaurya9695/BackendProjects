package com.backend.dsa.atoz.trees.nAryTree;

import java.util.Stack;

public class CreateGenericTree {

    public static final String INPUT_ARRAY_CANNOT_BE_NULL_OR_EMPTY = "Input array cannot be null or empty";
    public Node _root;

    public CreateGenericTree(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException(INPUT_ARRAY_CANNOT_BE_NULL_OR_EMPTY);
        }
        this._root = createNAryTree(arr);
    }

    private Node createNAryTree(int[] arr) {
        Stack<Node> stk = new Stack<>();

        for (int val : arr) {
            if (val == -1) {
                if (!stk.isEmpty()) {
                    stk.pop();
                }
            } else {
                Node currentNode = new Node(val);

                if (!stk.isEmpty()) {
                    stk.peek().getChildren().add(currentNode);
                } else {
                    _root = currentNode;
                }
                stk.push(currentNode);
            }
        }
        return _root;
    }

    public Node getRoot() {
        return _root;
    }
}
