package com.example.wangduwei.demos.view.customview.matrix;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;


/**
 * @author : wangduwei
 * @date : 2020/3/3
 * @description :
 */
public class MatrixImageView extends androidx.appcompat.widget.AppCompatImageView {
    private IMatrixProcessor matrixProcessor;

    public MatrixImageView(Context context) {
        this(context,null);
    }

    public MatrixImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MatrixImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        matrixProcessor = new MatrixProcessorImpl();
        setScaleType(ScaleType.FIT_CENTER);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        int saveCount = canvas.save();
        canvas.concat(matrixProcessor.getMatrix());
        super.onDraw(canvas);
        canvas.restoreToCount(saveCount);
    }

    public IMatrixProcessor getProcessor(){
        return matrixProcessor;
    }
}
