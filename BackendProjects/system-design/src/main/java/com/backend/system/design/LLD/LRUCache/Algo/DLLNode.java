package com.backend.system.design.LLD.LRUCache.Algo;

public class DLLNode<E> {

    DLLNode<E> next;
    DLLNode<E> prev;

    E value;

    public DLLNode(E value) {
        this.value = value;
        this.next = null;
        this.prev = null;
    }
}