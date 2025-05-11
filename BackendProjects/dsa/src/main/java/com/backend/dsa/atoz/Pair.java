package com.backend.dsa.atoz;

public class Pair implements Comparable<Pair> {

    public int listIdx;
    public int dataIdx;
    public int value;

    public Pair() {

    }

    public Pair(int listIdx, int dataIdx, int value) {
        this.listIdx = listIdx;
        this.dataIdx = dataIdx;
        this.value = value;
    }

    @Override
    public int compareTo(Pair x) {
        return this.value - x.value;
    }
}
