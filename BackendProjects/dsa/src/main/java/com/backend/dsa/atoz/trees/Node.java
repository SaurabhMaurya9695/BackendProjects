package com.backend.dsa.atoz.trees;

import com.backend.dsa.atoz.Pair;

import java.util.ArrayList;
import java.util.List;

public class Node {

    public int _value;
    public List<Node> _children;
    public List<Node> _childrenForNode;
    public Node node;

    public Node(Node node, List<Node> _childrenForNode) {
        this.node = node;
        this._childrenForNode = _childrenForNode;
    }

    public Node(int value) {
        this._value = value;
        this._children = new ArrayList<>();
    }

    public List<Node> getChildren() {
        return _children;
    }
}

