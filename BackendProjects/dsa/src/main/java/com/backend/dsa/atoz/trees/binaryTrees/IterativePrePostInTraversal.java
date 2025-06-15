package com.backend.dsa.atoz.trees.binaryTrees;

import com.backend.dsa.atoz.CommonUtil;
import com.backend.dsa.atoz.Pair;
import com.backend.dsa.atoz.trees.nAryTree.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class IterativePrePostInTraversal {

    private final List<Integer> _preorder = new ArrayList<>();
    private final List<Integer> _inorder = new ArrayList<>();
    private final List<Integer> _postorder = new ArrayList<>();

    public IterativePrePostInTraversal(Node root) {
        solve(root);
    }

    private void solve(Node root) {
        if (root == null) {
            return;
        }

        Stack<Pair> stk = new Stack<>();
        stk.push(new Pair(root, 1));

        while (!stk.isEmpty()) {
            Pair top = stk.peek();

            if (top.state == 1) {
                // Preorder
                _preorder.add(top._node.getValue());
                top.state++;
                if (top._node.getLeft() != null) {
                    stk.push(new Pair(top._node.getLeft(), 1));
                }
            } else if (top.state == 2) {
                // Inorder
                _inorder.add(top._node.getValue());
                top.state++;
                if (top._node.getRight() != null) {
                    stk.push(new Pair(top._node.getRight(), 1));
                }
            } else {
                // Postorder
                _postorder.add(top._node.getValue());
                stk.pop();
            }
        }
    }

    public void printIterative() {
        System.out.print("PRE-ORDER IS : ");
        CommonUtil.printArray(_preorder);
        System.out.print("IN-ORDER IS : ");
        CommonUtil.printArray(_inorder);
        System.out.print("POST-ORDER IS : ");
        CommonUtil.printArray(_postorder);
    }
}