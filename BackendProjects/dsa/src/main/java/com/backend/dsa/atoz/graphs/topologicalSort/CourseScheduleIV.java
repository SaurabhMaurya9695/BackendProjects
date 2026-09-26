package com.backend.dsa.atoz.graphs.topologicalSort;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class CourseScheduleIV {

    public static void main(String[] args) {
        int numCourses = 2;
        int[][] prerequisites = { { 1, 0 } };
        int[][] queries = { { 0, 1 }, { 1, 0 } };

        List<Integer> topo = checkIfPrerequisite(numCourses, prerequisites);
        // we need to give the query answer {0 -> 1} means 1 is a pre of 0 - which is false
        List<Boolean> ans = new ArrayList<>();
        for (int i = 0; i < queries.length; i++) {
            int course = queries[i][0];
            int per = queries[i][1];

            int foundCourse = -1;
            int foundPer = -1;
            for (int j = 0; j < topo.size(); j++) {
                Integer val = topo.get(j);
                if (val == course) {
                    foundCourse = j;
                } else if (val == per) {
                    foundPer = j;
                }

                if (foundPer != -1 && foundCourse != -1) {
                    break;
                }
            }

            if (foundPer > foundCourse) {
                ans.add(false);
            } else {
                ans.add(true);
            }
        }

        System.out.println(ans);
    }

    private static List<Integer> checkIfPrerequisite(int n, int[][] arr) {
        // naive approach would be check in map whether queries are present or not
        // if present then that would be answer but this is "DEFINITE WRONG"/

        // so go with Topological sort only -> kahn algo

        // step 1 - create a graph
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

        if (ans.size() != n) {
            return new ArrayList<>(); // Cycle detected
        }

        return ans;
    }
}
