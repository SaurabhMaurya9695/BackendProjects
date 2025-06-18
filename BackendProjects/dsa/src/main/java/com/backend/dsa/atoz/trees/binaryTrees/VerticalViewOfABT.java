package com.backend.dsa.atoz.trees.binaryTrees;

import com.backend.dsa.atoz.trees.nAryTree.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;

public class VerticalViewOfABT {

    public List<List<Integer>> _list = new ArrayList<>();
    public List<List<Integer>> _listByLeetCode = new ArrayList<>();

    public VerticalViewOfABT(Node node) {
        _list = solve(node);
        _listByLeetCode = solveLeetCode_987(node);
    }

    private List<List<Integer>> solveLeetCode_987(Node node) {
        return verticalTraversal(node);
    }

    private List<List<Integer>> solve(Node node) {
        List<List<Integer>> ans = new ArrayList<>();
        LinkedList<VerticalViewPair> ll = new LinkedList<>();
        VerticalViewPair vp = new VerticalViewPair(node, 0);
        ll.add(vp);
        HashMap<Integer, List<Integer>> mp = new HashMap<>();
        int maxi = 0, mini = 0;
        while (!ll.isEmpty()) {
            int size = ll.size();
            while (size-- > 0) {
                VerticalViewPair viewPair = ll.removeFirst();
                int height = viewPair.height;
                Node _node = viewPair.node;
                mp.putIfAbsent(height,
                        new ArrayList<>()); // if there is no value assign to that value then it will create it
                mp.get(height).add(_node._value);
                maxi = Math.max(maxi, height);
                mini = Math.min(mini, height);
                if (_node._left != null) {
                    ll.add(new VerticalViewPair(_node._left, height - 1));
                }

                if (_node._right != null) {
                    ll.add(new VerticalViewPair(_node._right, height + 1));
                }
            }
        }

        for (int i = mini; i <= maxi; i++) {
            List<Integer> list = mp.get(i);
            Collections.sort(list);
            Collections.reverse(list);
            ans.add(list);
        }

        return ans;
    }

    static class VerticalViewPair {

        Node node;
        int height;

        public VerticalViewPair() {
        }

        public VerticalViewPair(Node node, int height) {
            this.node = node;
            this.height = height;
        }
    }

    public List<List<Integer>> verticalTraversal(Node root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        // Queue for BFS: stores (node, column)
        Queue<Pair<Node, Integer>> q = new LinkedList<>();

        // Map<column, List<value>>
        TreeMap<Integer, List<Integer>> columnMap = new TreeMap<>();

        // Priority queue to sort nodes at same level by value
        PriorityQueue<Pair<Node, Integer>> pq = new PriorityQueue<>(
                (a, b) -> Integer.compare(a.getKey()._value, b.getKey()._value));

        q.offer(new Pair<>(root, 0));

        while (!q.isEmpty()) {
            int size = q.size();

            for (int i = 0; i < size; i++) {
                Pair<Node, Integer> current = q.poll();
                Node node = current.getKey();
                int col = current.getValue();

                columnMap.putIfAbsent(col, new ArrayList<>());
                columnMap.get(col).add(node._value);

                if (node._left != null) {
                    pq.offer(new Pair<>(node._left, col - 1));
                }

                if (node._right != null) {
                    pq.offer(new Pair<>(node._right, col + 1));
                }
            }

            while (!pq.isEmpty()) {
                q.offer(pq.poll());
            }
        }

        for (List<Integer> values : columnMap.values()) {
            result.add(values);
        }

        return result;
    }

    // Pair class (you can use javafx.util.Pair or AbstractMap.SimpleEntry in real use)
    class Pair<K, V> {

        private K key;
        private V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }
}
