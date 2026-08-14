package com.backend.dsa.atoz.graphs.topologicalSort;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class CourseScheduleII {

    public static void main(String[] args) {
        //Test case 1
        int numCourses1 = 2;
        int[][] prerequisites1 = { { 1, 0 } };
        System.out.println("Test 1: " + findOrder(numCourses1, prerequisites1));  // true

        // Test case 2 (with cycle)
        int numCourses2 = 4;
        int[][] prerequisites2 = { { 1, 0 }, { 2, 0 }, { 3, 1 }, { 3, 2 } };
        System.out.println("Test 2: " + findOrder(numCourses2, prerequisites2));  // false
    }

    public static int[] findOrder(int n, int[][] arr) {
        // Create a graph
        List<Integer>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        // Build graph & calculate indegree
        int[] indegree = new int[n];
        for (int i = 0; i < arr.length; i++) {
            int course = arr[i][0];
            int prerequisite = arr[i][1];

            graph[prerequisite].add(course);
            indegree[course]++;
        }

        // Add all nodes with indegree 0 to queue
        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < indegree.length; i++) {
            if (indegree[i] == 0) {
                q.add(i);
            }
        }

        List<Integer> ans = new ArrayList<>();
        while (!q.isEmpty()) {
            Integer course = q.poll();
            ans.add(course);

            // For each course that depends on this course
            for (Integer nextCourse : graph[course]) {
                indegree[nextCourse]--;
                if (indegree[nextCourse] == 0) {
                    q.add(nextCourse);
                }
            }
        }

        // Check if all courses can be completed (no cycle)
        if (ans.size() != n) {
            return new int[] {}; // Cycle detected
        }

        int[] res = new int[ans.size()];
        for (int i = 0; i < ans.size(); i++) {
            res[i] = ans.get(i);
        }
        return res;
    }
}
