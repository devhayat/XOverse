package com.hyt.tictactoe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIPlayer {
    
    public enum Difficulty {
        EASY, MEDIUM, HARD
    }
    
    private Difficulty difficulty;
    private Random random;
    
    public AIPlayer(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.random = new Random();
    }
    
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }
    
    public int[] getBestMove(String[][] board) {
        switch (difficulty) {
            case EASY:
                return getEasyMove(board);
            case MEDIUM:
                return getMediumMove(board);
            case HARD:
                return getHardMove(board);
            default:
                return getEasyMove(board);
        }
    }
    
    private int[] getEasyMove(String[][] board) {
        // Easy: 70% random, 30% smart
        if (random.nextFloat() < 0.7f) {
            return getRandomMove(board);
        } else {
            return getSmartMove(board);
        }
    }
    
    private int[] getMediumMove(String[][] board) {
        // Medium: 30% random, 70% smart
        if (random.nextFloat() < 0.3f) {
            return getRandomMove(board);
        } else {
            return getSmartMove(board);
        }
    }
    
    private int[] getHardMove(String[][] board) {
        // Hard: Always use minimax algorithm
        return getMinimaxMove(board);
    }
    
    private int[] getRandomMove(String[][] board) {
        List<int[]> availableMoves = getAvailableMoves(board);
        if (availableMoves.isEmpty()) {
            return null;
        }
        return availableMoves.get(random.nextInt(availableMoves.size()));
    }
    
    private int[] getSmartMove(String[][] board) {
        // First, try to win
        int[] winMove = findWinningMove(board, "O");
        if (winMove != null) {
            return winMove;
        }
        
        // Then, try to block player from winning
        int[] blockMove = findWinningMove(board, "X");
        if (blockMove != null) {
            return blockMove;
        }
        
        // Take center if available
        if (board[1][1].isEmpty()) {
            return new int[]{1, 1};
        }
        
        // Take corners
        int[][] corners = {{0, 0}, {0, 2}, {2, 0}, {2, 2}};
        List<int[]> availableCorners = new ArrayList<>();
        for (int[] corner : corners) {
            if (board[corner[0]][corner[1]].isEmpty()) {
                availableCorners.add(corner);
            }
        }
        if (!availableCorners.isEmpty()) {
            return availableCorners.get(random.nextInt(availableCorners.size()));
        }
        
        // Take any available move
        return getRandomMove(board);
    }
    
    private int[] getMinimaxMove(String[][] board) {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].isEmpty()) {
                    board[i][j] = "O";
                    int score = minimax(board, 0, false);
                    board[i][j] = "";
                    
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new int[]{i, j};
                    }
                }
            }
        }
        
        return bestMove;
    }
    
    private int minimax(String[][] board, int depth, boolean isMaximizing) {
        String winner = checkWinner(board);
        
        if (winner.equals("O")) return 10 - depth;
        if (winner.equals("X")) return depth - 10;
        if (isBoardFull(board)) return 0;
        
        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j].isEmpty()) {
                        board[i][j] = "O";
                        int score = minimax(board, depth + 1, false);
                        board[i][j] = "";
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j].isEmpty()) {
                        board[i][j] = "X";
                        int score = minimax(board, depth + 1, true);
                        board[i][j] = "";
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }
    
    private int[] findWinningMove(String[][] board, String player) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].isEmpty()) {
                    board[i][j] = player;
                    if (checkWinner(board).equals(player)) {
                        board[i][j] = "";
                        return new int[]{i, j};
                    }
                    board[i][j] = "";
                }
            }
        }
        return null;
    }
    
    private List<int[]> getAvailableMoves(String[][] board) {
        List<int[]> moves = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].isEmpty()) {
                    moves.add(new int[]{i, j});
                }
            }
        }
        return moves;
    }
    
    private String checkWinner(String[][] board) {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (!board[i][0].isEmpty() && 
                board[i][0].equals(board[i][1]) && 
                board[i][0].equals(board[i][2])) {
                return board[i][0];
            }
        }
        
        // Check columns
        for (int i = 0; i < 3; i++) {
            if (!board[0][i].isEmpty() && 
                board[0][i].equals(board[1][i]) && 
                board[0][i].equals(board[2][i])) {
                return board[0][i];
            }
        }
        
        // Check diagonals
        if (!board[0][0].isEmpty() && 
            board[0][0].equals(board[1][1]) && 
            board[0][0].equals(board[2][2])) {
            return board[0][0];
        }
        
        if (!board[0][2].isEmpty() && 
            board[0][2].equals(board[1][1]) && 
            board[0][2].equals(board[2][0])) {
            return board[0][2];
        }
        
        return "";
    }
    
    private boolean isBoardFull(String[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }
}
