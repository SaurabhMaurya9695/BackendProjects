package com.backend.dsa.atoz.trees.binaryTrees;

import com.backend.dsa.atoz.trees.nAryTree.Node;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class LevelOrderTraversalBFS {

    private List<List<Integer>> _lst = new ArrayList<>();

    public LevelOrderTraversalBFS(Node node) {
        _lst = bfs(node);
    }

    private List<List<Integer>> bfs(Node root) {
        List<List<Integer>> ans = new ArrayList<>();
        Queue<Node> q = new ArrayDeque<>();
        q.add(root);
        while (!q.isEmpty()) {
            int size = q.size();
            List<Integer> levelNodes = new ArrayList<>();
            while (size-- > 0) {
                Node removedNode = q.remove();
                levelNodes.add(removedNode.getValue());
                // add their leafs
                if (removedNode.getLeft() != null) {
                    q.add(removedNode.getLeft());
                }
                if (removedNode.getRight() != null) {
                    q.add(removedNode.getRight());
                }
            }
            ans.add(levelNodes);
        }
        return ans;
    }

    public List<List<Integer>> getBfs() {
        return _lst;
    }
}
