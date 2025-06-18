package com.backend.dsa.atoz.trees.binaryTrees;

import com.backend.dsa.atoz.trees.nAryTree.Node;

// link : https://leetcode.com/problems/house-robber-iii/
public class HouseRobberInBinaryTree {

    public HouseRobberInBinaryTree(Node node) {
        rob(node);
    }

    class Pair {

        public int withoutRobbery;
        public int withRobbery;

        public Pair() {
        }

        public Pair(int x, int y) {
            withoutRobbery = x;
            withRobbery = y;
        }
    }

    public int rob(Node root) {
        Pair ans = solve(root);
        return Math.max(ans.withoutRobbery, ans.withRobbery);
    }

    private Pair solve(Node root) {
        if (root == null) {
            return new Pair(0, 0);
        }

        Pair lp = solve(root._left);
        Pair rp = solve(root._right);

        // now we have two conditions , either we do robbery or not do at current level

        // if we do robbery at current place then for child return ans which is without robbery
        int robbery = lp.withoutRobbery + rp.withoutRobbery + root._value;

        int without_robbery = Math.max(lp.withRobbery, lp.withoutRobbery) + Math.max(rp.withRobbery, rp.withoutRobbery);

        return new Pair(without_robbery, robbery);
    }
}
