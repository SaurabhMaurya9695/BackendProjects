package com.backend.dsa.atoz.linkedList;

public class RotateList_02 {

    public ListNode rotateRight(ListNode head, int k) {
        if (head == null || head.next == null || k == 0) {
            return head;
        }

        // Step 1: Find length
        int n = 1;
        ListNode tail = head;
        while (tail.next != null) {
            tail = tail.next;
            n++;
        }

        // Step 2: Make it circular
        tail.next = head; // 1 2 3 4 5 1 2 3 4 5

        // Step 3: Normalize k
        k = k % n; // 2
        int stepsToNewTail = n - k - 1; // 5 - 2 - 1 => 2

        // Step 4: Find new tail
        ListNode newTail = head;
        for (int i = 0; i < stepsToNewTail; i++) {
            newTail = newTail.next;
        }

        // now new tail at 3

        // Step 5: Break the circle
        ListNode newHead = newTail.next; // means 4 is the new head
        newTail.next = null; // 3 ke next me null

        return newHead;
    }

    private int getSize(ListNode head) {
        int cnt = 0;
        while (head != null) {
            cnt++;
            head = head.next;
        }

        return cnt;
    }

    private static class ListNode {

        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }
}




