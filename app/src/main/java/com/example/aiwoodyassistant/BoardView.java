package com.example.aiwoodyassistant;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class BoardView extends View {
    public static final int SIZE = 8;
    private int[][] filled = new int[SIZE][SIZE];
    private int[][] highlight = null;
    private Paint paint = new Paint();
    private Paint gridPaint = new Paint();
    private static final int[] PIECE_COLORS = {
        Color.parseColor("#E53935"), Color.parseColor("#1E88E5"), Color.parseColor("#43A047")
    };

    public BoardView(Context ctx) {
        super(ctx);
        gridPaint.setColor(Color.GRAY);
        gridPaint.setStrokeWidth(2);
    }

    public int[][] getState() {
        int[][] c2 = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) c2[i] = filled[i].clone();
        return c2;
    }

    public void clearBoard() {
        filled = new int[SIZE][SIZE];
        highlight = null;
        invalidate();
    }

    public void setHighlight(int[][] hl) {
        highlight = hl;
        invalidate();
    }

    @Override protected void onMeasure(int widthSpec, int heightSpec) {
        int w = MeasureSpec.getSize(widthSpec);
        setMeasuredDimension(w, w);
    }

    @Override protected void onDraw(Canvas canvas) {
        int w = getWidth();
        float cell = w / (float) SIZE;
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (highlight != null && highlight[r][c] >= 0) {
                    paint.setColor(PIECE_COLORS[highlight[r][c] % PIECE_COLORS.length]);
                } else if (filled[r][c] != 0) {
                    paint.setColor(Color.DKGRAY);
                } else {
                    paint.setColor(Color.WHITE);
                }
                canvas.drawRect(c * cell, r * cell, (c + 1) * cell, (r + 1) * cell, paint);
            }
        }
        for (int i = 0; i <= SIZE; i++) {
            canvas.drawLine(i * cell, 0, i * cell, w, gridPaint);
            canvas.drawLine(0, i * cell, w, i * cell, gridPaint);
        }
    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int w = getWidth();
            float cell = w / (float) SIZE;
            int c = (int) (event.getX() / cell);
            int r = (int) (event.getY() / cell);
            if (r >= 0 && r < SIZE && c >= 0 && c < SIZE) {
                filled[r][c] = filled[r][c] == 0 ? 1 : 0;
                highlight = null;
                invalidate();
            }
            return true;
        }
        return true;
    }
}
