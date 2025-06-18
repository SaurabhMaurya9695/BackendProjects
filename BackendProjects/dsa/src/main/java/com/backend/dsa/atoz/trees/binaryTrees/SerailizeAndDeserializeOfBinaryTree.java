package com.backend.dsa.atoz.trees.binaryTrees;

import com.backend.dsa.atoz.Pair;
import com.backend.dsa.atoz.trees.nAryTree.Node;

import java.util.Stack;

// https://leetcode.com/problems/serialize-and-deserialize-binary-tree/description/
public class SerailizeAndDeserializeOfBinaryTree {

    private StringBuilder sb = new StringBuilder();

    public SerailizeAndDeserializeOfBinaryTree() {
    }

    public SerailizeAndDeserializeOfBinaryTree(Node node) {
        serialize(node);
        deserialize(sb.toString());
    }

    // Encodes a tree to a single string.
    public String serialize(Node root) {
        sb = new StringBuilder(); // reset before use
        _serialize(root);
        return sb.toString();
    }

    private void _serialize(Node root) {
        if (root == null) {
            sb.append("null,");
            return;
        }

        sb.append(root.getValue()).append(",");
        _serialize(root.getLeft());
        _serialize(root.getRight());
    }

    private int idx; // will be reset in deserialize

    // Decodes your encoded data to tree.
    public Node deserialize(String data) {
        String[] s = data.split(",");
        idx = 0; // reset before use
        return _deserialize(s);
    }

    private Node _deserialize(String[] s) {
        if (idx >= s.length || s[idx].equals("null")) {
            idx++;
            return null;
        }

        Node node = new Node(Integer.parseInt(s[idx++]));
        node._left = _deserialize(s);
        node._right = _deserialize(s);
        return node;
    }
}
