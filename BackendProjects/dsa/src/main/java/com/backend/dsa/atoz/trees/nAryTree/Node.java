package com.backend.dsa.atoz.trees.nAryTree;

import java.util.ArrayList;
import java.util.List;

public class Node {

    public Node _left;
    public Node _right;
    public int _value;
    public List<Node> _children;

    public Node() {

    }

    public Node(int value) {
        this._value = value;
        this._children = new ArrayList<>();
    }

    public Node(Node left, Node right, int value) {
        _left = left;
        _right = right;
        _value = value;
    }

    public Node(Node left, Node right) {
        _left = left;
        _right = right;
    }

    public List<Node> getChildren() {
        return _children;
    }

    public void addChild(Node node) {
        this._children.add(node);
    }

    public Node getLeft() {
        return _left;
    }

    public Node getRight() {
        return _right;
    }

    public void setLeft(Node left) {
        _left = left;
    }

    public void setRight(Node right) {
        _right = right;
    }

    public void setValue(int value) {
        _value = value;
    }

    public int getValue() {
        return _value;
    }
}

