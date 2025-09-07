package com.backend.design.pattern.common;

public class Utility {

    public static class Pair<T, U> {

        public final T left;
        public final U right;

        public Pair(T left, U right) {
            this.left = left;
            this.right = right;
        }
    }
}
