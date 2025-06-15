package com.backend.dsa.atoz.trees.binaryTrees;

import com.backend.dsa.atoz.trees.nAryTree.Node;

import java.util.ArrayList;
import java.util.List;

// for reference : https://www.youtube.com/watch?v=B89In5BctFA&list=PL-Jc9J83PIiHYxUk8dSu2_G7MR1PaGXN4&index=26
public class PrintNodesKDistanceAway {

    public List<Node> _pathInNodes = new ArrayList<>();
    private List<Integer> _kLevelFar = new ArrayList<>();
    private List<Integer> _ans = new ArrayList<>();

    public PrintNodesKDistanceAway(Node root, Node target, int k) {
        _ans = distanceK(root, target, k);
    }

    public List<Integer> distanceK(Node root, Node target, int k) {
        nodeToRootPath(root, target);
        _pathInNodes.add(root);
        // now _pathInNodes gets filled at this time with the nodeToRoot path
        // node ask for every node that is filled in _pathInNodes, ask to capture the nodes which is K level down
        for (int i = 0; i < _pathInNodes.size(); i++) {
            captureKLevelDown(_pathInNodes.get(i), k - i, (i == 0) ? -1 : _pathInNodes.get(i - 1)._value);
        }

        return _kLevelFar;
    }

    private void captureKLevelDown(Node root, int k, int blocker) {
        if (root == null || k < 0 || root._value == blocker) {
            return;
        }
        if (k == 0) {
            _kLevelFar.add(root._value);
            return;
        }
        captureKLevelDown(root.getLeft(), k - 1, blocker);
        captureKLevelDown(root.getRight(), k - 1, blocker);
    }

    private boolean nodeToRootPath(Node root, Node target) {
        if (root == null) {
            return false;
        }
        if (root._value == target._value) {
            // once it matched with the _value, euler start going back to root
            return true;
        }

        if (nodeToRootPath(root.getLeft(), target)) {
            // so we need to catch all the portion from where euler came back
            _pathInNodes.add(root.getLeft());
            return true;
        }

        if (nodeToRootPath(root.getRight(), target)) {
            // so we need to catch all the portion from where euler came back
            _pathInNodes.add(root.getRight());
            return true;
        }

        return false;
    }

    public List<Integer> getkLevelFar() {
        return _ans;
    }
}
