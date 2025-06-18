package com.backend.dsa.atoz.trees.binaryTrees;

import com.backend.dsa.atoz.trees.nAryTree.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class VerticalOrderSumUsingBFS {

    public VerticalOrderSumUsingBFS(Node root) {
        System.out.println("VERTICAL ORDER SUM USING BFS : " + verticalSum(root));
    }

    public ArrayList<Integer> verticalSum(Node root) {
        ArrayList<Integer> res = new ArrayList<>();
        List<List<Integer>> temp = solve(root);
        for (List<Integer> lst : temp) {
            int sum = 0;
            for (Integer y : lst) {
                sum += y;
            }
            res.add(sum);
        }
        return res;
    }

    public List<List<Integer>> solve(Node node) {
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
}
