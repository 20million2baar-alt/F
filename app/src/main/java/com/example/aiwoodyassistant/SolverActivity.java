package com.example.aiwoodyassistant;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class SolverActivity extends Activity {
    private BoardView boardView;
    private PieceView[] pieceViews = new PieceView[3];
    private TextView resultText;
    private static final int[] PIECE_COLORS = {
        Color.parseColor("#E53935"), Color.parseColor("#1E88E5"), Color.parseColor("#43A047")
    };

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);

        ScrollView scroll = new ScrollView(this);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(24, 40, 24, 40);

        TextView title = new TextView(this);
        title.setText("Block Blast Solver");
        title.setTextSize(24);
        title.setTextColor(Color.BLACK);
        root.addView(title);

        TextView hint1 = new TextView(this);
        hint1.setText("\nTap cells below to mark the CURRENT board (dark = filled):");
        root.addView(hint1);

        boardView = new BoardView(this);
        LinearLayout.LayoutParams boardParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        boardParams.topMargin = 16;
        boardParams.bottomMargin = 16;
        root.addView(boardView, boardParams);

        Button clearBoardBtn = new Button(this);
        clearBoardBtn.setText("Clear Board");
        clearBoardBtn.setOnClickListener(v -> boardView.clearBoard());
        root.addView(clearBoardBtn);

        TextView hint2 = new TextView(this);
        hint2.setText("\nDraw the 3 pieces shown in the game (tap cells to fill each shape):");
        root.addView(hint2);

        LinearLayout pieceRow = new LinearLayout(this);
        pieceRow.setOrientation(LinearLayout.HORIZONTAL);
        pieceRow.setPadding(0, 16, 0, 16);

        for (int i = 0; i < 3; i++) {
            LinearLayout col = new LinearLayout(this);
            col.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams colParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            colParams.setMargins(8, 0, 8, 0);

            TextView label = new TextView(this);
            label.setText("Piece " + (i + 1));
            label.setTextColor(PIECE_COLORS[i]);
            col.addView(label);

            PieceView pv = new PieceView(this, PIECE_COLORS[i]);
            col.addView(pv);
            pieceViews[i] = pv;

            Button clearBtn = new Button(this);
            clearBtn.setText("Clear");
            final PieceView fpv = pv;
            clearBtn.setOnClickListener(v -> fpv.clearShape());
            col.addView(clearBtn);

            pieceRow.addView(col, colParams);
        }
        root.addView(pieceRow);

        Button solveBtn = new Button(this);
        solveBtn.setText("Solve — Best Placement");
        solveBtn.setTextSize(18);
        solveBtn.setOnClickListener(v -> runSolve());
        root.addView(solveBtn);

        resultText = new TextView(this);
        resultText.setTextSize(16);
        resultText.setPadding(0, 24, 0, 0);
        root.addView(resultText);

        scroll.addView(root);
        setContentView(scroll);
    }

    private void runSolve() {
        resultText.setText("Solving...");
        int[][] grid = boardView.getState();
        List<List<int[]>> pieces = new ArrayList<>();
        for (PieceView pv : pieceViews) pieces.add(pv.getShape());

        new Thread(() -> {
            Solver.Result result = Solver.solve(grid, pieces);
            runOnUiThread(() -> showResult(result));
        }).start();
    }

    private void showResult(Solver.Result result) {
        if (!result.solvable) {
            resultText.setText("No arrangement fits all 3 pieces on this board. Game over is likely.");
            return;
        }
        int[][] hl = new int[BoardView.SIZE][BoardView.SIZE];
        for (int[] row : hl) java.util.Arrays.fill(row, -1);

        StringBuilder sb = new StringBuilder();
        sb.append("Lines cleared: ").append(result.totalCleared).append("\n\n");
        for (Solver.Move m : result.moves) {
            sb.append("Piece ").append(m.pieceIndex + 1)
              .append(" -> row ").append(m.row + 1).append(", col ").append(m.col + 1)
              .append(" (top-left of shape)")
              .append(m.cleared > 0 ? "  [clears " + m.cleared + " line(s)]" : "")
              .append("\n");
            for (int[] off : pieceViews[m.pieceIndex].getShape()) {
                int r = m.row + off[0], c = m.col + off[1];
                if (r >= 0 && r < BoardView.SIZE && c >= 0 && c < BoardView.SIZE) hl[r][c] = m.pieceIndex;
            }
        }
        boardView.setHighlight(hl);
        resultText.setText(sb.toString());
    }
}
