package com.backend.system.design.LLD.Cache.policy;

import com.backend.system.design.LLD.Algo.DLLNode;
import com.backend.system.design.LLD.Algo.DoublyLinkedList;

import java.util.HashMap;
import java.util.Map;

/**
 * LRU (Least Recently Used) Eviction Policy.
 * 
 * Implementation Details:
 * - Uses a Doubly Linked List to maintain access order
 * - Uses a HashMap for O(1) access to nodes
 * - Most recently used items are at the front
 * - Least recently used items are at the back
 * 
 * Time Complexity:
 * - keyAccessed: O(1)
 * - keyAdded: O(1)
 * - keyRemoved: O(1)
 * - evictKey: O(1)
 * 
 * Space Complexity: O(n) where n is the cache capacity
 * 
 * @param <Key> The type of keys tracked by this policy
 */
public class LRUEvictionPolicy<Key> implements EvictionPolicy<Key> {

    // Doubly linked list to maintain LRU order
    private final DoublyLinkedList<Key> dll;
    
    // HashMap to store key -> node mapping for O(1) access
    private final Map<Key, DLLNode<Key>> keyNodeMap;

    /**
     * Creates a new LRU eviction policy with the specified capacity.
     * 
     * @param capacity The maximum number of keys this policy can track
     */
    public LRUEvictionPolicy(int capacity) {
        this.dll = new DoublyLinkedList<>(capacity);
        this.keyNodeMap = new HashMap<>((int) (capacity / 0.75) + 1);
    }

    /**
     * When a key is accessed, move it to the front (most recently used position).
     * 
     * @param key The key that was accessed
     */
    @Override
    public void keyAccessed(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        
        // If key exists, remove it from current position and add to front
        if (keyNodeMap.containsKey(key)) {
            DLLNode<Key> node = keyNodeMap.get(key);
            dll.removeNode(node);
            dll.addNodeToFront(node);
        }
        // If key doesn't exist, this is an error condition - should have been added first
        // In practice, this scenario is handled by the Cache implementation
    }

    /**
     * When a key is added, add it to the front (most recently used position).
     * 
     * @param key The key that was added
     */
    @Override
    public void keyAdded(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        
        // Create a new node and add it to the front of the DLL
        DLLNode<Key> newNode = new DLLNode<>(key);
        dll.addNodeToFront(newNode);
        keyNodeMap.put(key, newNode);
    }

    /**
     * When a key is removed, remove it from tracking.
     * 
     * @param key The key that was removed
     */
    @Override
    public void keyRemoved(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        
        if (keyNodeMap.containsKey(key)) {
            DLLNode<Key> node = keyNodeMap.get(key);
            dll.removeNode(node);
            keyNodeMap.remove(key);
        }
    }

    /**
     * Returns the least recently used key for eviction.
     * 
     * @return The LRU key, or null if no keys are tracked
     */
    @Override
    public Key evictKey() {
        // Get the LRU node (node before tail)
        DLLNode<Key> lruNode = dll.getLRUNode();
        
        if (lruNode == null) {
            return null;
        }
        
        // Get the key from the node
        Key keyToEvict = lruNode.getElement();
        
        // Remove it from tracking
        dll.removeNode(lruNode);
        keyNodeMap.remove(keyToEvict);
        
        return keyToEvict;
    }

    @Override
    public String getPolicyName() {
        return "LRU (Least Recently Used)";
    }
}



