package com.backend.dsa.atoz.arrays;

import java.util.HashMap;
import java.util.Map;

/**
 * LRUCache implementation using Doubly Linked List and HashMap.
 * <p>
 * - get(key): O(1)
 * - put(key, value): O(1)
 * <p>
 * Logic:
 * - Use HashMap to store key → Node reference.
 * - Use a custom Doubly Linked List to maintain order of usage.
 * - Most recently used nodes stay near head.
 * - Least recently used nodes stay near tail.
 */
public class LRUCache {

    class Node {

        int key, value;
        Node prev, next;

        public Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    private final int capacity;
    private final Map<Integer, Node> map;
    private final Node head, tail;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();

        // Dummy head and tail to simplify edge cases
        head = new Node(-1, -1);
        tail = new Node(-1, -1);

        head.next = tail;
        tail.prev = head;
    }

    /**
     * Get the value for a given key.
     * If a key exists, move it to the head (most recently used).
     */
    public int get(int key) {
        if (!map.containsKey(key)) {
            return -1;
        }

        Node node = map.get(key);
        remove(node);
        insertToHead(node);
        return node.value;
    }

    /**
     * Put a key-value pair in the cache.
     * If a key exists, update and move it to the head.
     * If capacity is full, remove the least recently used node.
     */
    public void put(int key, int value) {
        if (map.containsKey(key)) {
            // Remove old node
            Node existing = map.get(key);
            remove(existing);
        } else if (map.size() == capacity) {
            // Remove least recently used (node before tail)
            Node lru = tail.prev;
            remove(lru);
            map.remove(lru.key);
        }

        // Insert new node
        Node node = new Node(key, value);
        insertToHead(node);
        map.put(key, node);
    }

    /**
     * Helper: Insert node right after head (most recently used position).
     */
    private void insertToHead(Node node) {
        Node nextNode = head.next;

        head.next = node;
        node.prev = head;

        node.next = nextNode;
        nextNode.prev = node;
    }

    /**
     * Helper: Remove a node from its current position.
     */
    private void remove(Node node) {
        Node prevNode = node.prev;
        Node nextNode = node.next;

        prevNode.next = nextNode;
        nextNode.prev = prevNode;
    }

    /**
     * For debugging: print the current cache state
     */
    public void printCacheState() {
        Node curr = head.next;
        System.out.print("Cache State: ");
        while (curr != tail) {
            System.out.print("(" + curr.key + ":" + curr.value + ") ");
            curr = curr.next;
        }
        System.out.println();
    }

    // Example test
    public static void main(String[] args) {
        LRUCache cache = new LRUCache(2);

        cache.put(1, 10);
        cache.printCacheState();
        cache.put(2, 20);
        cache.printCacheState();
        cache.put(3, 30);
        cache.printCacheState(); // (3:30) (2:20) (1:10)

        cache.get(1);
        cache.printCacheState(); // (1:10) (3:30) (2:20)

        cache.put(4, 40);
        cache.printCacheState(); // (4:40) (1:10) (3:30) → 2 is evicted
    }
}
