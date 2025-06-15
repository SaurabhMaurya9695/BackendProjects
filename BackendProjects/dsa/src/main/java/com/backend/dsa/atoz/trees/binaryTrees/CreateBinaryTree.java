package com.backend.dsa.atoz.trees.binaryTrees;

import com.backend.dsa.atoz.CommonUtil;
import com.backend.dsa.atoz.Pair;
import com.backend.dsa.atoz.trees.nAryTree.Node;

import java.util.Stack;

public class CreateBinaryTree {

    private Node _root;

    public CreateBinaryTree(Integer[] arr) {
        _root = createBinaryTree(arr);
    }

    // there are three types of states 1, 2, 3
    // 1 means add in left
    // 2 means add in right
    // 3 means add in pop
    private Node createBinaryTree(Integer[] arr) {
        Stack<Pair> stk = new Stack<>();
        Node root = new Node(null, null, arr[0]);
        stk.push(new Pair(root, 1));
        int idx = 0;
        while (!stk.isEmpty() && idx < arr.length - 1) {

            Pair top = stk.peek();

            if (top.state == 1) {
                // preorder
                idx++;

                if (arr[idx] != null) {
                    Node child = new Node(arr[idx]);
                    top._node.setLeft(child);
                    stk.push(new Pair(child, 1));
                } else {
                    top._node.setLeft(null);
                }
                top.state++;
            } else if (top.state == 2) {
                // inorder
                idx++;

                if (arr[idx] != null) {
                    Node child = new Node(arr[idx]);
                    top._node.setRight(child);
                    stk.push(new Pair(child, 1));
                } else {
                    top._node.setRight(null);
                }
                top.state++;
            } else if (top.state == 3) {
                // postorder
                stk.pop();
            }
        }
        return root;
    }

    public Node getRoot() {
        return _root;
    }
}
