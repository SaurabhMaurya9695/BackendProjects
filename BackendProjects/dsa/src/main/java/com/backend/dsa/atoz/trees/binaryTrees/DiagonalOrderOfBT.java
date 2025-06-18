package com.backend.dsa.atoz.trees.binaryTrees;

import com.backend.dsa.atoz.trees.nAryTree.Node;

import java.util.ArrayList;
import java.util.LinkedList;

public class DiagonalOrderOfBT {

    public ArrayList<ArrayList<Integer>> ans = new ArrayList<>();
    public ArrayList<Integer> temp = new ArrayList<>();

    public DiagonalOrderOfBT(Node node) {
        temp = diagonal(node);
    }

    public ArrayList<Integer> diagonal(Node root) {
        ArrayList<ArrayList<Integer>> ans = _diagonal(root);
        ArrayList<Integer> temp = new ArrayList<>();
        for (ArrayList<Integer> x : ans) {
            temp.addAll(x);
        }

        return temp;
    }

    private ArrayList<ArrayList<Integer>> _diagonal(Node root) {
        LinkedList<Node> q = new LinkedList<>();
        q.addLast(root);
        ArrayList<ArrayList<Integer>> ans = new ArrayList<>();
        while (!q.isEmpty()) {
            int size = q.size();
            ArrayList<Integer> diagonalAns = new ArrayList<>();
            while (size-- > 0) {
                Node node = q.removeFirst();
                // add all the elements of node to which are right to it
                while (node != null) {
                    // but before going the right add elements
                    if (node.getLeft() != null) {
                        q.addLast(node.getLeft());
                    }
                    diagonalAns.add(node._value); // add all the values which are present in the right
                    node = node.getRight();
                }
            }
            ans.add(diagonalAns);
        }
        return ans;
    }
}
