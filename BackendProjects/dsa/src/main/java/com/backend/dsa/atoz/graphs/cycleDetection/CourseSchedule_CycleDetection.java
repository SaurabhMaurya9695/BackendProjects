package com.backend.dsa.atoz.graphs.cycleDetection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CourseSchedule_CycleDetection {

    public static void main(String[] args) {
        // Test case 1
        int numCourses1 = 2;
        int[][] prerequisites1 = { { 1, 0 } };
        System.out.println("Test 1: " + canFinish(numCourses1, prerequisites1));  // true

        // Test case 2 (with cycle)
        int numCourses2 = 2;
        int[][] prerequisites2 = { { 1, 0 }, { 0, 1 } };
        System.out.println("Test 2: " + canFinish(numCourses2, prerequisites2));  // false
    }

    public static boolean canFinish(int n, int[][] prerequisites) {

        // no need to build the whole topo logic, just check the cycle or not

        // Step 1: Build adjacency list
        List<Integer>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        // Step 2: Calculate indegree
        for (int[] prereq : prerequisites) {
            int course = prereq[0];
            int prerequisite = prereq[1];
            graph[prerequisite].add(course); // directed edge
        }

        int[] colors = new int[n];
        boolean ans = false;
        // 0 - white , 1 - grey , 2 - black
        Arrays.fill(colors, 0);
        for (int i = 0; i < n; i++) {
            if (colors[i] == 0) {
                if (hasCycle(i, graph, colors)) {
                    ans = true;
                    break;
                }
            }
        }
        return !ans;
    }

    private static boolean hasCycle(int i, List<Integer>[] graph, int[] colors) {
        colors[i] = 1;

        // get the nbrs
        for (Integer integer : graph[i]) {
            if (colors[integer] == 1) {
                //has cycle
                return true;
            } else if (colors[integer] == 0) {
                if (hasCycle(integer, graph, colors)) {
                    return true;
                }
            }
        }

        colors[i] = 2;
        return false;
    }
}
