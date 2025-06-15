package com.backend.dsa.atoz;

import com.backend.dsa.atoz.trees.nAryTree.Node;

public class Pair implements Comparable<Pair> {

    public int listIdx;
    public int dataIdx;

    public Node _node;
    public int state;

    public Pair() {

    }

    public Pair(Node node, int state) {
        this._node = node;
        this.state = state;
    }

    public Pair(int listIdx, int dataIdx, int value) {
        this.listIdx = listIdx;
        this.dataIdx = dataIdx;
        this.state = value;
    }

    @Override
    public int compareTo(Pair x) {
        return this.state - x.state;
    }
}
