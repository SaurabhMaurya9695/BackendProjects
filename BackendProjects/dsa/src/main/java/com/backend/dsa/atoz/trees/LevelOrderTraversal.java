package com.backend.dsa.atoz.trees;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class LevelOrderTraversal {

    private List<Integer> lst = new ArrayList<>();

    public LevelOrderTraversal(Node root) {
        bfs(root);
    }

    private void bfs(Node root) {
        Queue<Node> q = new ArrayDeque<>();
        q.add(root);
        while (!q.isEmpty()) {
            // remove print add
            Node removeNode = q.remove();
            lst.add(removeNode._value);
            for (Node node : removeNode.getChildren()) {
                q.add(node);
            }
        }
    }

    public List<Integer> bfsOnNAry() {
        return lst;
    }
}

