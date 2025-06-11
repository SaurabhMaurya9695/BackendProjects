package com.backend.dsa.atoz.trees;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class LevelOrderLineWise {

    private List<List<Integer>> _lst = new ArrayList<>();

    public LevelOrderLineWise(Node root) {
        _lst = bfs(root);
    }

    private List<List<Integer>> bfs(Node root) {
        Queue<Node> q = new ArrayDeque<>();
        q.add(root);
        List<List<Integer>> ans = new ArrayList<>();
        while (!q.isEmpty()) {
            // remove
            int currentLevelSize = q.size();

            // print
            List<Integer> currentLevelChildren = new ArrayList<>();
            for (int i = 0; i < currentLevelSize; i++) {
                Node removeNode = q.remove();
                currentLevelChildren.add(removeNode._value);

                // add children's
                q.addAll(removeNode.getChildren());
            }
            ans.add(currentLevelChildren);
        }
        return ans;
    }

    public List<List<Integer>> bfsOnNAry() {
        return _lst;
    }
}

