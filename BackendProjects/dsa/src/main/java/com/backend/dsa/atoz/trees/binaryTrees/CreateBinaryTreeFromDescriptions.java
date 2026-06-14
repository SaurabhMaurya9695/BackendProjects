package com.backend.dsa.atoz.trees.binaryTrees;

import java.util.HashMap;
import java.util.HashSet;

public class CreateBinaryTreeFromDescriptions {

    public static void main(String[] args) {
        int[][] descriptions = { { 20, 15, 1 }, { 20, 17, 0 }, { 50, 20, 1 }, { 50, 80, 0 }, { 80, 19, 1 } };
        System.out.println(createBinaryTree(descriptions));
    }

    public static TreeNode createBinaryTree(int[][] descriptions) {

        HashMap<Integer, TreeNode> map = new HashMap<>();
        HashSet<Integer> children = new HashSet<>();

        for (int[] d : descriptions) {

            int parentVal = d[0];
            int childVal = d[1];
            int isLeft = d[2];

            TreeNode parent = map.computeIfAbsent(parentVal, k -> new TreeNode(k));

            TreeNode child = map.computeIfAbsent(childVal, k -> new TreeNode(k));

            if (isLeft == 1) {
                parent.left = child;
            } else {
                parent.right = child;
            }

            children.add(childVal);
        }

        // Find root (node that never appears as a child)
        for (int[] d : descriptions) {
            int parentVal = d[0];
            if (!children.contains(parentVal)) {
                return map.get(parentVal);
            }
        }

        return null;
    }

    public static class TreeNode {

        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
}
