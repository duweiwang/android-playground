package com.example.wangduwei.demos.view.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import com.example.wangduwei.demos.R;

/**
 * @desc:探索Matrix的pre,post
 * 矩阵乘法不满足交换律，为什么pre,post之后矩阵不变
 * @auther:duwei
 * @date:2018/6/19
 */

public class MatrixUseView extends View {

    private Rect srcRect;
    private Bitmap bitmap;
    private float[] value = new float[9];


    public MatrixUseView(Context context) {
        super(context);
        srcRect = new Rect();

        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        srcRect.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, null, srcRect, null);
        canvas.translate(100, 100);

        Matrix matrix = canvas.getMatrix();
        matrix.getValues(value);
        Log.d("matrix", "preRotate之前 = " + printValue(value));
        /**
         * 1.0,     0.0,    100.0,
         * 0.0,     1.0,    100.0,
         * 0.0,     0.0,    1.0,
         */
        matrix.preRotate(15);
        canvas.drawBitmap(bitmap, matrix, null);
        matrix.getValues(value);
        Log.d("matrix", "preRotate之后 = " + printValue(value));
        /**
         * 0.49999997,  -0.86602545,    100.0,
         * 0.86602545,  0.49999997,     100.0,
         * 0.0,         0.0,            1.0,
         */
//-----------------------------------------------------------------------------------
        canvas.translate(200, 200);
        matrix.reset();

        Matrix m = canvas.getMatrix();
        m.getValues(value);
        Log.d("matrix", "postRotate之前 = " + printValue(value));
        /**
         *  1.0,    0.0,   300.0,
         *  0.0,    1.0,   300.0,
         *  0.0,    0.0,   1.0,
         */
        m.postRotate(15);
        canvas.drawBitmap(bitmap, m, null);
        m.getValues(value);
        Log.d("matrix", "postRotate之后 = " + printValue(value));
        /**
         * 0.49999997,  -0.86602545,    -109.80764,
         * 0.86602545,  0.49999997,     409.80762,
         * 0.0,         0.0,            1.0,
         */
    }


    private String printValue(float[] value) {
        String s = "";
        for (float va : value) {
            s += va + ",";
        }
        return s;
    }
}
