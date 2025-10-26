package com.backend.system.design.LLD.LRUCache.Policy;

import com.backend.system.design.LLD.LRUCache.Algo.DoublyLinkedList;

public class LRUEvictionPolicy<Key, Value> implements EvictionPolicy {

    DoublyLinkedList<Key> _doublyLinkedList;

    public LRUEvictionPolicy(Key key, int capacity) {
        _doublyLinkedList = new DoublyLinkedList<>(capacity);

    }
    @Override
    public void keyAccessed(Object o) {

    }

    @Override
    public void keyAdded(Object o) {

    }

    @Override
    public void keyRemoved(Object o) {

    }

    @Override
    public Object evictKey() {
        return null;
    }
}
