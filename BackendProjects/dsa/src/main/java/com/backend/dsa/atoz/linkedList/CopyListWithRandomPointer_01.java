package com.backend.dsa.atoz.linkedList;

import java.util.HashMap;
import java.util.Map;

public class CopyListWithRandomPointer_01 {

    public Node copyRandomList(Node head) {
        if (head == null) {
            return null;
        }

        // before Create all nodes (NO connections yet)
        // A → A'
        // B → B'
        // C → C'

        // STEP 1
        // At this point:
        // A'   B'   C'
        // no next, no random yet

        // STEP 2 : Connect next pointers
        // Now traverse again:
        // copy->next = map[original->next];

        // STEP 3: Connect random pointers
        // copy->random = map[original->random];

        // return map[head];

        Map<Node, Node> mp = new HashMap<>();
        Node curr = head;

        // STEP 1
        while (curr != null) {
            mp.put(curr, new Node(curr.val));
            curr = curr.next;
        }

        // STEP 2 & STEP 3
        curr = head;
        while (curr != null) {
            Node copy = mp.get(curr);
            copy.next = mp.get(curr.next);
            copy.random = mp.get(curr.random);

            curr = curr.next;
        }

        return mp.get(head);
    }

    private static class Node {

        int val;
        Node next;
        Node random;

        public Node(int val) {
            this.val = val;
            this.next = null;
            this.random = null;
        }
    }
}

