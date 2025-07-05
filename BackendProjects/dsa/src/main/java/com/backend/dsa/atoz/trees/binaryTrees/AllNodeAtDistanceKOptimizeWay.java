package com.backend.dsa.atoz.trees.binaryTrees;

import com.backend.dsa.atoz.trees.nAryTree.Node;

import java.util.ArrayList;
import java.util.List;

public class AllNodeAtDistanceKOptimizeWay {

    List<Integer> _list = new ArrayList<>();

    public AllNodeAtDistanceKOptimizeWay(Node node, Node target, int k) {
        distanceK(node, target, k);
    }

    public List<Integer> distanceK(Node root, Node target, int k) {
        dfs(root, target, k);
        return _list;
    }

    // This function returns the distance from current root to target node
    private int dfs(Node root, Node target, int k) {
        if (root == null) {
            return -1;
        }

        if (root == target) {
            // If root is the target node, collect all nodes k distance down from it
            kDown(root, k, null);
            return 1;
        }

        int left = dfs(root._left, target, k);
        if (left != -1) {
            // target found in left subtree
            kDown(root, k - left, root._left); // avoid going back to left child
            return left + 1;
        }

        int right = dfs(root._right, target, k);
        if (right != -1) {
            // target found in right subtree
            kDown(root, k - right, root._right); // avoid going back to right child
            return right + 1;
        }

        return -1; // target not found in either subtree
    }

    // Collect nodes k distance down from current node, avoid going to blocked node
    private void kDown(Node node, int k, Node blocked) {
        if (node == null || node == blocked || k < 0) {
            return;
        }

        if (k == 0) {
            _list.add(node._value);
            return;
        }

        kDown(node._left, k - 1, blocked);
        kDown(node._right, k - 1, blocked);
    }
}
