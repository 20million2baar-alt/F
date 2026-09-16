package com.example.aiwoodyassistant;

import java.util.ArrayList;
import java.util.List;

public class Solver {
    public static final int SIZE = 8;

    public static class Move {
        public int pieceIndex, row, col, cleared;
        public Move(int p, int r, int c, int cl) { pieceIndex = p; row = r; col = c; cleared = cl; }
    }

    public static class Result {
        public List<Move> moves = new ArrayList<>();
        public int totalCleared = -1;
        public boolean solvable = false;
    }

    public static Result solve(int[][] grid, List<List<int[]>> pieces) {
        Result best = new Result();
        boolean[] used = new boolean[pieces.size()];
        List<Move> moves = new ArrayList<>();
        int[][] gridCopy = copy(grid);
        solveRec(gridCopy, pieces, used, moves, 0, best);
        return best;
    }

    private static void solveRec(int[][] grid, List<List<int[]>> pieces, boolean[] used, List<Move> moves, int cleared, Result best) {
        boolean allUsed = true;
        for (boolean u : used) if (!u) { allUsed = false; break; }
        if (allUsed) {
            if (cleared > best.totalCleared || !best.solvable) {
                best.solvable = true;
                best.totalCleared = cleared;
                best.moves = new ArrayList<>(moves);
            }
            return;
        }
        for (int i = 0; i < pieces.size(); i++) {
            if (used[i]) continue;
            List<int[]> shape = pieces.get(i);
            if (shape.isEmpty()) continue;
            for (int r = 0; r < SIZE; r++) {
                for (int c = 0; c < SIZE; c++) {
                    if (canPlace(grid, shape, r, c)) {
                        int[][] g2 = place(grid, shape, r, c);
                        int linesCleared = clearLines(g2);
                        used[i] = true;
                        moves.add(new Move(i, r, c, linesCleared));
                        solveRec(g2, pieces, used, moves, cleared + linesCleared, best);
                        moves.remove(moves.size() - 1);
                        used[i] = false;
                    }
                }
            }
        }
    }

    private static boolean canPlace(int[][] grid, List<int[]> shape, int r, int c) {
        for (int[] off : shape) {
            int rr = r + off[0], cc = c + off[1];
            if (rr < 0 || rr >= SIZE || cc < 0 || cc >= SIZE) return false;
            if (grid[rr][cc] != 0) return false;
        }
        return true;
    }

    private static int[][] place(int[][] grid, List<int[]> shape, int r, int c) {
        int[][] g2 = copy(grid);
        for (int[] off : shape) {
            g2[r + off[0]][c + off[1]] = 1;
        }
        return g2;
    }

    private static int clearLines(int[][] g) {
        List<Integer> fullRows = new ArrayList<>();
        List<Integer> fullCols = new ArrayList<>();
        for (int r = 0; r < SIZE; r++) {
            boolean full = true;
            for (int c = 0; c < SIZE; c++) if (g[r][c] == 0) { full = false; break; }
            if (full) fullRows.add(r);
        }
        for (int c = 0; c < SIZE; c++) {
            boolean full = true;
            for (int r = 0; r < SIZE; r++) if (g[r][c] == 0) { full = false; break; }
            if (full) fullCols.add(c);
        }
        for (int r : fullRows) for (int c = 0; c < SIZE; c++) g[r][c] = 0;
        for (int c : fullCols) for (int r = 0; r < SIZE; r++) g[r][c] = 0;
        return fullRows.size() + fullCols.size();
    }

    private static int[][] copy(int[][] g) {
        int[][] g2 = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) g2[i] = g[i].clone();
        return g2;
    }
}
