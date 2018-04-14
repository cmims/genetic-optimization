package com.slethron.evolution.type;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class NQueensBoard {
    private int[] board;
    
    public NQueensBoard(int n) {
        board = new int[n];
    }
    
    public NQueensBoard(NQueensBoard source) {
        board = source.board.clone();
    }
    
    public int get(int column) {
        return board[column];
    }
    
    public void set(int column, int row) {
        board[column] = row;
    }
    
    public int length() {
        return board.length;
    }
    
    public NQueensBoard randomize() {
        for (var c = 0; c < board.length; c++) {
            board[c] = ThreadLocalRandom.current().nextInt(board.length);
        }
        
        return this;
    }
    
    public int numberOfConflicts() {
        int conflicts = 0;
        for (var currentQueen = 0; currentQueen < board.length; currentQueen++) {
            for (var nextQueen = currentQueen + 1; nextQueen < board.length; nextQueen++) {
                if (board[currentQueen] == board[nextQueen]) {
                    conflicts++;
                }
                if (Math.abs(board[nextQueen] - board[currentQueen]) == Math.abs(nextQueen - currentQueen)) {
                    conflicts++;
                }
            }
        }
        
        return conflicts;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        
        if (!(obj instanceof NQueensBoard)) {
            return false;
        }
        
        var e = (NQueensBoard) obj;
        
        if (e.length() != length()) {
            return false;
        }
        
        for (var i = 0; i < length(); i++) {
            if (e.get(i) != get(i)) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (var i = 0; i < board.length; i++) {
            sb.append(board[i]);
            if (i != board.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        
        return sb.toString();
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(board, board.length);
    }
}

