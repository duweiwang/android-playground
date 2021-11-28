package com.example.customview.basic_api;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;


/**
 * 贝塞尔曲线探索
 */
public class BazierView extends View {
    private Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG) {
        {
            setColor(Color.RED);
            setStyle(Style.FILL);
        }
    };

    private Paint pathPaint = new Paint(Paint.ANTI_ALIAS_FLAG) {
        {
            setColor(Color.BLUE);
            setStyle(Style.STROKE);
            setStrokeWidth(4);
        }
    };

    private Path mPathCubic = new Path();
    private Path mPathQuad = new Path();

    private static int screenWidth;
    private static int screenHeight;

    private Point a, b, c;
    private Point m, n, p, q;

    public BazierView(Context context) {
        this(context, null);
    }

    public BazierView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BazierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenHeight = wm.getDefaultDisplay().getHeight();
        screenWidth = wm.getDefaultDisplay().getWidth();


        //各点的初始坐标
        a = new Point(screenWidth / 5, screenHeight / 6);
        b = new Point(screenWidth / 2, screenHeight / 8);
        c = new Point(screenWidth - screenWidth / 5, screenHeight / 6);

        m = new Point(screenWidth / 5, screenHeight / 2 + screenHeight / 3);
        n = new Point(2 * screenWidth / 5, screenHeight / 2 + screenHeight / 6);
        p = new Point(3 * screenWidth / 5, screenHeight / 2 + screenHeight / 6);
        q = new Point(4 * screenWidth / 5, screenHeight / 2 + screenHeight / 3);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(screenWidth, screenHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawARGB(0, 0, 0, 0);

        drawQuad(canvas);

        canvas.drawLine(0, screenHeight / 2, screenWidth, screenHeight / 2, pathPaint);

        drawCubic(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        Point p = getInsidePoint(x, y);
        if (p != null) {
            p.x = x;
            p.y = y;
        }
        invalidate();
        return super.onTouchEvent(event);

    }

    /**
     * 二阶
     */
    private void drawQuad(Canvas canvas) {
        a.draw(canvas, circlePaint);
        b.draw(canvas, circlePaint);
        c.draw(canvas, circlePaint);

        mPathQuad.reset();

        mPathQuad.moveTo(a.x, a.y);
        mPathQuad.quadTo(b.x, b.y, c.x, c.y);

        canvas.drawPath(mPathQuad, pathPaint);
    }

    /**
     * 三阶
     */
    private void drawCubic(Canvas canvas) {
        m.draw(canvas, circlePaint);
        n.draw(canvas, circlePaint);
        p.draw(canvas, circlePaint);
        q.draw(canvas, circlePaint);

        mPathCubic.reset();
        mPathCubic.moveTo(m.x, m.y);
        mPathCubic.cubicTo(n.x, n.y, p.x, p.y, q.x, q.y);
        canvas.drawPath(mPathCubic, pathPaint);
    }


    private Point getInsidePoint(int x, int y) {
        if (a.inside(x, y)) {
            return a;
        } else if (b.inside(x, y)) {
            return b;
        } else if (c.inside(x, y)) {
            return c;
        } else if (m.inside(x, y)) {
            return m;
        } else if (n.inside(x, y)) {
            return n;
        } else if (p.inside(x, y)) {
            return p;
        } else if (q.inside(x, y)) {
            return q;
        } else {
            return null;
        }
    }

    private static class Point {
        public int x;
        public int y;
        public int radius = 30;

        public Point() {

        }

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void draw(Canvas canvas, Paint paint) {
            canvas.drawCircle(x, y, radius, paint);
        }

        public boolean inside(int p, int q) {
            return p >= x - 30 &&
                    p <= x + 30 &&
                    q >= y - 30 &&
                    q <= y + 30;
        }
    }


}
