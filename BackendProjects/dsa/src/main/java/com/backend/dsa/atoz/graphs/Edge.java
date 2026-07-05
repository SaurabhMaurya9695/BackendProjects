package com.backend.dsa.atoz.graphs;

public class Edge {

    public int src;
    public int nbr;
    public int wt;

    public Edge(int src, int nbr) {
        this.src = src;
        this.nbr = nbr;
    }

    public Edge(int src, int nbr, int wt) {
        this.src = src;
        this.nbr = nbr;
        this.wt = wt;
    }
}
