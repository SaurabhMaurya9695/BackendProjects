package com.backend.design.pattern.designs.TIcTacToe.model;

public class Board {

    // board has some size n * n ;
    private int n;
    private int m;

    private Symbol[][] arr;

    public Board() {
        System.out.println("No Board has been created yet, Please create and start a new game");
    }

    public Board(int n, int m) {
        this.n = n;
        this.m = m;
        arr = new Symbol[this.n][this.m];
        fillBoard();
        System.out.println("Board has been created of size " + n + " rows and " + m + " coloums");
    }

    public Symbol getCell(int r, int c) {
        if (isValid(r, c)) {
            return arr[r][c];
        }
        System.out.println("It's not a valid row & col, Please check again");
        return new Symbol();
    }

    public void fillBoard() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                arr[i][j] = new Symbol();
            }
        }
    }

    public void makeMove(Symbol s, int r, int c) {
        arr[r][c] = s;
    }

    private boolean isValid(int r, int c) {
        return arr.length <= r && arr[1].length <= c;
    }

    public int getRowSize() {
        return n;
    }

    public int getColSize() {
        return m;
    }

    public boolean isCellEmpty(Symbol cell) {
        return cell.getSymbol().toString().equals("_");
    }

    public void printBoard() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
    }
}
