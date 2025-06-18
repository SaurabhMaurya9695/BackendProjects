package com.backend.dsa.atoz.trees.binaryTrees;

import com.backend.dsa.atoz.trees.nAryTree.Node;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BinaryTreeRightSideView {

    public List<Integer> _list = new ArrayList<>();

    public BinaryTreeRightSideView(Node node) {
        _list = solve(node);
    }

    private List<Integer> solve(Node node) {
        if (node == null) {
            return new ArrayList<>();
        }
        LinkedList<Node> q = new LinkedList<>();
        q.add(node);
        List<Integer> lst = new ArrayList<>();
        while (!q.isEmpty()) {
            int size = q.size();
            lst.add(q.getFirst()._value);
            while (size-- > 0) {
                Node node1 = q.getFirst();
                q.removeFirst();
                if (node1._right != null) {
                    q.add(node1._right);
                }
                if (node1._left != null) {
                    q.add(node1._left);
                }
            }
        }
        return lst;
    }
}
