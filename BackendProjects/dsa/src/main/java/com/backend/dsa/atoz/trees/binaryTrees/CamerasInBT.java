package com.backend.dsa.atoz.trees.binaryTrees;

import com.backend.dsa.atoz.trees.nAryTree.Node;

// link : https://leetcode.com/problems/binary-tree-cameras/description/
public class CamerasInBT {

    private int NOT_MONITORED = 0;
    private int MONITORED_WITHOUT_CAM = 1;
    private int MONITORED_WITH_CAM = 2;
    int camera = 0;
    public int minCameraCover(Node root) {
        if(solve(root) == NOT_MONITORED){
            camera++;
        }
        return camera;
    }

    private int solve(Node root){
        if(root == null) return MONITORED_WITHOUT_CAM;

        int left = solve(root._left);
        int right = solve(root._right);

        if(left == NOT_MONITORED || right == NOT_MONITORED){
            camera ++ ;
            return MONITORED_WITH_CAM; //notify parent you need camera
        }
        else if(left == MONITORED_WITHOUT_CAM && right == MONITORED_WITHOUT_CAM){
            return NOT_MONITORED;
        }

        return MONITORED_WITHOUT_CAM;
    }
}
