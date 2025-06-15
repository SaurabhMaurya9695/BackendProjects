package com.backend.dsa.atoz.trees.nAryTree;

public class PredecessorAndSuccessorOfAnElement {

    private int _predecessor;
    private int _successor;
    private int state = 0;

    public PredecessorAndSuccessorOfAnElement(Node node, int val) {
        solve(node, val);
    }

    private void solve(Node node, int val) {
        // if you write pre-order then for any value left value is _predecessor and right value is _successor
        if (state == 0) {
            if (node._value == val) {
                state = 1;
            } else {
                _predecessor = node._value;
            }
        } else if (state == 1) {
            _successor = node._value;
            state = 2;
        }

        for (Node child : node.getChildren()) {
            solve(child, val);
        }
    }

    @Override
    public String toString() {
        return "PREDECESSOR AND SUCCESSOR OF AN ELE IN N-ARY TREE IS  {" + "_successor=" + _successor
                + ", _predecessor=" + _predecessor + '}';
    }
}
