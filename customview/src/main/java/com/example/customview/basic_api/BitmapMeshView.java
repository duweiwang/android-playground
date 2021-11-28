package com.example.customview.basic_api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.customview.R;

/**
 * <p>
 * {@link Canvas#drawBitmapMesh(Bitmap, int, int, float[], int, int[], int, Paint)}方法使图片变形
 * </p>
 *
 * @auther:duwei
 * @date:2018/7/11
 */

public class BitmapMeshView extends View {
    private static final int WIDTH = 9, HEIGHT = 9;// 分割数
    private static final int COUNT = (WIDTH + 1) * (HEIGHT + 1);// 交点数

    private Bitmap mBitmap;// 位图对象

    private float[] matrixOriganal = new float[COUNT * 2];// 基准点坐标数组
    private float[] matrixMoved = new float[COUNT * 2];// 变换后点坐标数组

    private float clickX, clickY;// 触摸屏幕时手指的xy坐标
    private float downX, downY;
    private float movedX, movedY;

    private Paint origPaint, movePaint, linePaint;// 基准点、变换点和线段的绘制Paint

    public BitmapMeshView(Context context) {
        this(context, null);
    }

    public BitmapMeshView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        setFocusable(true);

        // 实例画笔并设置颜色
        origPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        origPaint.setColor(0x660000FF);
        movePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        movePaint.setColor(0x99FF0000);
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(0xFFFFFB00);

        // 获取位图资源
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.default_background_cloudy_night_bg);

        // 初始化坐标数组
        int index = 0;
        for (int y = 0; y <= HEIGHT; y++) {
            float fy = mBitmap.getHeight() * y / HEIGHT;

            for (int x = 0; x <= WIDTH; x++) {
                float fx = mBitmap.getWidth() * x / WIDTH;
                setXY(matrixMoved, index, fx, fy);
                setXY(matrixOriganal, index, fx, fy);
                index += 1;
            }
        }
    }

    /**
     * 设置坐标数组
     *
     * @param array 坐标数组
     * @param index 标识值
     * @param x     x坐标
     * @param y     y坐标
     */
    private void setXY(float[] array, int index, float x, float y) {
        array[index * 2 + 0] = x;
        array[index * 2 + 1] = y;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // 绘制网格位图
        canvas.drawBitmapMesh(mBitmap, WIDTH, HEIGHT,
                matrixMoved, 0,
                null, 0, null);

        // 绘制参考元素
        drawGuide(canvas);
    }

    /**
     * 绘制参考元素
     *
     * @param canvas 画布
     */
    private void drawGuide(Canvas canvas) {
        for (int i = 0; i < COUNT * 2; i += 2) {
            float x = matrixOriganal[i + 0];
            float y = matrixOriganal[i + 1];
            canvas.drawCircle(x, y, 4, origPaint);

            float x1 = matrixOriganal[i + 0];
            float y1 = matrixOriganal[i + 1];
            float x2 = matrixMoved[i + 0];
            float y2 = matrixMoved[i + 1];
            canvas.drawLine(x1, y1, x2, y2, origPaint);
        }

        for (int i = 0; i < COUNT * 2; i += 2) {
            float x = matrixMoved[i + 0];
            float y = matrixMoved[i + 1];
            canvas.drawCircle(x, y, 4, movePaint);
        }

        canvas.drawCircle(clickX, clickY, 6, linePaint);
    }

    /**
     * 计算变换数组坐标
     */
    private void smudge() {
        for (int i = 0; i < COUNT * 2; i += 2) {//每次跳过两个点，xy

            float xOriginal = matrixOriganal[i + 0];
            float yOriginal = matrixOriganal[i + 1];

            float clickToOriX = clickX - xOriginal;
            float clickToOriY = clickY - yOriginal;

            float kv_kat = clickToOriX * clickToOriX + clickToOriY * clickToOriY;//斜边的平方
            double distanceToOrigin = Math.sqrt(kv_kat);//勾股定理，斜边长
            // //计算扭曲度，距离当前点越远的点扭曲度越小,,,,怎么算的
            float pull = (float) (1000000 / (kv_kat * distanceToOrigin));

            if (pull >= 1) {
                matrixMoved[i + 0] = clickX;
                matrixMoved[i + 1] = clickY;
            } else {
                matrixMoved[i + 0] = xOriginal + clickToOriX * pull;
                matrixMoved[i + 1] = yOriginal + clickToOriY * pull;
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        clickX = event.getX();
        clickY = event.getY();
        smudge();
        invalidate();
        return true;
    }
}

