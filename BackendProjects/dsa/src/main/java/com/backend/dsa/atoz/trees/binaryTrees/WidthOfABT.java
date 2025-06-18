package com.backend.dsa.atoz.trees.binaryTrees;

import com.backend.dsa.atoz.trees.nAryTree.Node;

public class WidthOfABT {

    int maxi = 0, mini = 0;

    // ans wil matched at gfg
    public int widthOfBinaryTree(Node root) {
        int[] ans = new int[2];
        solve(root, ans, 0);
        return ans[1] - ans[0] + 1;
    }

    void solve(Node root, int[] ans, int height) {
        if (root == null) {
            return;
        }

        ans[0] = Math.min(ans[0], height);
        ans[1] = Math.max(ans[1], height);
        // go to left deep with height -- ;
        solve(root._left, ans, height - 1);
        solve(root._right, ans, height + 1);
    }
}
