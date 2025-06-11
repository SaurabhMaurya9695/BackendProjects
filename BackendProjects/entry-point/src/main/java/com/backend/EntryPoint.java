package com.backend;

import com.backend.dsa.atoz.CommonUtil;
import com.backend.dsa.atoz.trees.CreateGenericTree;
import com.backend.dsa.atoz.trees.HeightInNAryTree;
import com.backend.dsa.atoz.trees.LevelOrderLineWise;
import com.backend.dsa.atoz.trees.LevelOrderTraversal;
import com.backend.dsa.atoz.trees.MaximumInNAryTree;
import com.backend.dsa.atoz.trees.MirrorInGenericTree;
import com.backend.dsa.atoz.trees.Node;
import com.backend.dsa.atoz.trees.RemoveLeavesFromAGenericTrees;
import com.backend.dsa.atoz.trees.SizeOfNAryTree;

public class EntryPoint {

    public static void main(String[] args) {
        int[] genericTree = {
                10, 20, 50, -1, 60, -1, -1, 30, 70, -1, 80, 110, -1, 120, -1, 90, -1, -1, -1, 40, 100, -1, -1, -1 };
        Node root = new CreateGenericTree(genericTree).getRoot();
        CommonUtil.displayNAryTree(root);
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("ROOT OF THE N-ARY TREE IS: " + root._value);
        System.out.println("SIZE OF A N-ARY TREE IS : " + new SizeOfNAryTree(root).getSize());
        System.out.println("MAX IN A N-ARY TREE IS : " + new MaximumInNAryTree(root).getMaxi());
        System.out.println("HEIGHT OF A N-ARY TREE IS : " + new HeightInNAryTree(root).getHeight());
        System.out.println("BFS ON A N-ARY TREE IS : " + new LevelOrderTraversal(root).bfsOnNAry());
        System.out.println("BFS ON A N-ARY TREE IS LEVEL-WISE : " + new LevelOrderLineWise(root).bfsOnNAry());
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("MIRROR OF A GENERIC TREE :");
        new MirrorInGenericTree(root);
        CommonUtil.displayNAryTree(root);
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("BACK TO ORIGINAL MAIN TREE ");
        new MirrorInGenericTree(root);
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("REMOVING LEAFS");
        // FIXME
        Node temp = new Node(root, root.getChildren());
        System.out.println("CHECKING HASHCODE " + (temp.hashCode() == root.hashCode()) + " " + temp.getChildren());
        new RemoveLeavesFromAGenericTrees(temp);
        CommonUtil.displayNAryTree(temp);
        System.out.println("-------------------------------------------------------------------------");
    }
}
