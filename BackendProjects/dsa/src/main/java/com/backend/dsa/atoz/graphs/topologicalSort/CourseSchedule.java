package com.backend.dsa.atoz.graphs.topologicalSort;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class CourseSchedule {

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

    private static boolean canFinish(int n, int[][] prerequisites) {
        // Step 1: Build adjacency list
        List<Integer>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        // Step 2: Calculate indegree
        int[] indegree = new int[n];
        for (int[] prereq : prerequisites) {
            int course = prereq[0];
            int prerequisite = prereq[1];

            // prerequisite → course (edge from prereq to course)
            graph[prerequisite].add(course);
            indegree[course]++;
        }

        // Step 3: Add all nodes with indegree 0 to queue
        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) {
                queue.add(i);
            }
        }

        // Step 4: Process nodes using Kahn's algorithm
        int count = 0;
        while (!queue.isEmpty()) {
            int course = queue.poll();
            count++;

            // For each course that depends on this course
            for (int nextCourse : graph[course]) {
                indegree[nextCourse]--;
                if (indegree[nextCourse] == 0) {
                    queue.add(nextCourse);
                }
            }
        }

        // Step 5: If we processed all courses, no cycle exists
        return count == n;
    }
}
