package com.example.aiwoodyassistant;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class PieceView extends View {
    public static final int SIZE = 5;
    private boolean[][] cells = new boolean[SIZE][SIZE];
    private Paint paint = new Paint();
    private Paint gridPaint = new Paint();
    private int color;

    public PieceView(Context ctx, int color) {
        super(ctx);
        this.color = color;
        gridPaint.setColor(Color.LTGRAY);
        gridPaint.setStrokeWidth(2);
    }

    public void clearShape() {
        cells = new boolean[SIZE][SIZE];
        invalidate();
    }

    public List<int[]> getShape() {
        int minR = SIZE, minC = SIZE;
        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++)
                if (cells[r][c]) { if (r < minR) minR = r; if (c < minC) minC = c; }
        List<int[]> shape = new ArrayList<>();
        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++)
                if (cells[r][c]) shape.add(new int[]{r - minR, c - minC});
        return shape;
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
                paint.setColor(cells[r][c] ? color : Color.WHITE);
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
                cells[r][c] = !cells[r][c];
                invalidate();
            }
            return true;
        }
        return true;
    }
}
