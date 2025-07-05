package com.backend.dsa.atoz.trees.binaryTrees;

import com.backend.dsa.atoz.trees.nAryTree.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BurningTree {

    private final List<Node> _pathInNodes = new ArrayList<>();
    HashMap<Integer, ArrayList<Integer>> _map = new HashMap<>();
    private int ans = 0;

    public BurningTree(Node node) {

    }

    public int amountOfTime(Node root, int start) {
        nodeToRootPath(root, start);

        Node block = null;
        for (int i = 0; i < _pathInNodes.size(); i++) {
            Node curr = _pathInNodes.get(i);
            burnTree(curr, block, i);
            block = curr; // block the direction where fire came from
        }

        for (Map.Entry<Integer, ArrayList<Integer>> entry : _map.entrySet()) {
            System.out.print("[ " + entry.getKey());
            ArrayList<Integer> list = entry.getValue();
            System.out.print(" : ");
            for (Integer x : list) {
                System.out.print(x + " ");
            }
            System.out.println("]\n");
        }

        return ans;
    }

    private void burnTree(Node node, Node blocker, int time) {
        if (node == null || node == blocker) {
            return;
        }

        _map.putIfAbsent(time, new ArrayList<>());
        _map.get(time).add(node._value);

        ans = Math.max(ans, time);
        burnTree(node._left, blocker, time + 1);
        burnTree(node._right, blocker, time + 1);
    }

    private boolean nodeToRootPath(Node node, int target) {
        if (node == null) {
            return false;
        }

        if (node._value == target) {
            _pathInNodes.add(node);
            return true;
        }

        if (nodeToRootPath(node._left, target) || nodeToRootPath(node._right, target)) {
            _pathInNodes.add(node); // going back toward root
            return true;
        }

        return false;
    }

/*
*
class Solution {
    int ans = 0;

    public int amountOfTime(TreeNode root, int start) {
        dfs(root, start);
        return ans;
    }

    // Returns distance from current node to target (if exists), otherwise -1
    private int dfs(TreeNode node, int target) {
        if (node == null) return -1;

        if (node.val == target) {
            // Start the burning from here
            burn(node, null, 0);
            return 1;
        }

        int left = dfs(node.left, target);
        if (left != -1) {
            // burn everything in the right subtree except the left child (blocker)
            burn(node, node.left, left);
            return left + 1;
        }

        int right = dfs(node.right, target);
        if (right != -1) {
            // burn everything in the left subtree except the right child (blocker)
            burn(node, node.right, right);
            return right + 1;
        }

        return -1;
    }

    private void burn(TreeNode node, TreeNode blocker, int time) {
        if (node == null || node == blocker) return;

        ans = Math.max(ans, time);
        burn(node.left, blocker, time + 1);
        burn(node.right, blocker, time + 1);
    }
}
*
*/
}
