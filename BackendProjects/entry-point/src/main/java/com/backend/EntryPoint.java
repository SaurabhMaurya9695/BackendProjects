package com.backend;

import com.backend.dsa.atoz.CommonUtil;
import com.backend.dsa.atoz.trees.binaryTrees.BalancedBinaryTree;
import com.backend.dsa.atoz.trees.binaryTrees.CreateBinaryTree;
import com.backend.dsa.atoz.trees.binaryTrees.DiameterOfBT;
import com.backend.dsa.atoz.trees.binaryTrees.IterativePrePostInTraversal;
import com.backend.dsa.atoz.trees.binaryTrees.LevelOrderTraversalBFS;
import com.backend.dsa.atoz.trees.binaryTrees.MultiSolverBT;
import com.backend.dsa.atoz.trees.binaryTrees.NodeToRootPathBT;
import com.backend.dsa.atoz.trees.binaryTrees.PrintKLevelDown;
import com.backend.dsa.atoz.trees.binaryTrees.PrintNodesKDistanceAway;
import com.backend.dsa.atoz.trees.binaryTrees.PrintSingleChildNodes;
import com.backend.dsa.atoz.trees.binaryTrees.RemoveLeafs;
import com.backend.dsa.atoz.trees.binaryTrees.RootToLeftPaths;
import com.backend.dsa.atoz.trees.binaryTrees.TiltOfBinaryTree;
import com.backend.dsa.atoz.trees.binaryTrees.TransformBackFromALeftClonedTree;
import com.backend.dsa.atoz.trees.binaryTrees.TransformedToLeftCloneTree;
import com.backend.dsa.atoz.trees.nAryTree.*;

public class EntryPoint {

    public static void main(String[] args) {

        // ---------------------------------------------- GENERIC TREE ------------------------------------
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
        Node cloneTree = CommonUtil.cloneTree(root);
        new MirrorInGenericTree(cloneTree);
        CommonUtil.displayNAryTree(cloneTree);
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("REMOVING LEAFS");
        Node cloneTree1 = CommonUtil.cloneTree(root);
        new RemoveLeavesFromAGenericTrees(cloneTree1);
        CommonUtil.displayNAryTree(cloneTree1);
        System.out.println("-------------------------------------------------------------------------");
        System.out.println(
                "FINDING ELEMENT 120 IN N-ARY TREE : " + new FindElementInGenericTree(root, 120).isPresent());
        System.out.println("NODE TO ROOT PATH IS : " + new NodeToRootPath(root, 110).getNodeToRootPath());
        System.out.println("LCA IN N-ARY TREE : " + new LCAInNAryTree(root, 70, 80).getLca());
        System.out.println(
                "DISTANCE BW OF TWO NODES IN N-ARY TREE : " + new DistanceBetweenTwoNodes(root, 110, 40).getDistance());
        System.out.println("-------------------------------------------------------------------------");
        int[] array2 = {
                1, 2, 5, -1, 6, -1, -1, 3, 7, -1, 8, 11, -1, 12, -1, 9, -1, -1, -1, 4, 10, -1, -1, -1 };
        Node rootOfArray2 = new CreateGenericTree(array2).getRoot();
        CommonUtil.displayNAryTree(rootOfArray2);
        System.out.println("-------------------------------------------------------------------------");
        System.out.println(
                "ARE TREE IS IN SIMILAR SHAPE : " + new AreTreeSimilerInShape(root, rootOfArray2).isInSimilerShape());
        System.out.println("STARTING MULTI-SOLVER : " + new MultiSolverBT(root).toString());
        System.out.println(new PredecessorAndSuccessorOfAnElement(root, 90).toString());
        System.out.println("-------------------------------------------------------------------------");
        int[] array3 = {
                10, 20, -50, -1, -60, -1, -1, 30, -70, -1, 80, -110, -1, 120, -1, 90, -1, -1, -1, 40, -100, -1, -1,
                -1 };
        Node rootOfArray3 = new CreateGenericTree(array3).getRoot();
        System.out.println(new MaximumSumSubtree(rootOfArray3).toString());
        System.out.println("DIAMETER OF A N-ARY TREE IS : " + new DiameterOfNAryTree(root).getDiameter());

        //---------------------------------------------------- BINARY TREE -------------------------------

        System.out.println("---------------------- STARTING BINARY TREE -----------------------------------");
        Integer[] binaryTree = {
                50, 25, 12, null, null, 37, 30, null, null, null, 75, 62, null, 70, null, null, 87, null, null };
        Node btRoot = new CreateBinaryTree(binaryTree).getRoot();
        CommonUtil.displayBT(btRoot);
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("ROOT OF THE BT IS : " + btRoot.getValue());
        System.out.println(new MultiSolverBT(btRoot));
        System.out.println("BFS OF BT IS : " + new LevelOrderTraversalBFS(btRoot).getBfs());
        new IterativePrePostInTraversal(btRoot).printIterative();
        System.out.println("NODE TO ROOT PATH IS : " + new NodeToRootPathBT(btRoot, 30).getPath());
        System.out.println("KTH LEVEL DOWN NODES IS : " + new PrintKLevelDown(btRoot, 3).getLst());
        System.out.println("KTH LEVEL FAR IS : " + new PrintNodesKDistanceAway(btRoot, new Node(25), 2).getkLevelFar());
        System.out.println("ROOT TO LEAF PATH IS : " + new RootToLeftPaths(btRoot).getBListT());
        Node clonedNodeBt = CommonUtil.cloneTree(btRoot);
        CommonUtil.displayBT(clonedNodeBt);
        System.out.println("LEFT CLONED TREE IS : ");
        Node transformedNode = new TransformedToLeftCloneTree(clonedNodeBt).getNode();
        CommonUtil.displayBT(transformedNode);
        System.out.println("NORMALIZING LEFT CLONED TREE IS : ");
        Node normalizedNode = new TransformBackFromALeftClonedTree(transformedNode).getNode();
        CommonUtil.displayBT(normalizedNode);
        System.out.println("PRINTING ALL MISSING ONE CHILD NODES : " + new PrintSingleChildNodes(
                btRoot).getAllOneChildMissingNodes());
        System.out.println("AFTER REMOVING LEAFS TREE LOOK LIKE THIS : ");
        Node nodes = new RemoveLeafs(normalizedNode).getNode();
        CommonUtil.displayBT(nodes);
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("DIAMETER OF A BINARY TREE INEFFICIENTS WAY :" + new DiameterOfBT(btRoot).getDiamater());
        System.out.println(
                "DIAMETER OF A BINARY TREE EFFICIENTS WAY :" + new DiameterOfBT(btRoot).getEfficientDiamater());
        System.out.println("TILT OF A BINARY TREE : " + new TiltOfBinaryTree(btRoot).tilt);
        System.out.println("IS A BALANCE TREE : " + new BalancedBinaryTree(btRoot).balanced);
    }
}
