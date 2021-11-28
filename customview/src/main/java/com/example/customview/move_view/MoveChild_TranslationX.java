package com.example.customview.move_view;

import android.content.Context;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @desc: 实现所有的子控件跟随手指移动
 * getHitRect、
 * getDrawingRect、
 * getLocalVisibleRect、
 * getGlobalVisibleRect
 * 以上四个方法的区分
 * @auther:duwei
 * @date:2018/2/2
 */

public class MoveChild_TranslationX extends FrameLayout {

    private Rect rect = new Rect();
    private float downX, downY;
    private View view0;

    public MoveChild_TranslationX(@NonNull Context context) {
        super(context);
    }

    public MoveChild_TranslationX(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MoveChild_TranslationX(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        view0 = getChildAt(0);
        view0.getDrawingRect(rect);
        //getDrawingRect --- left = 0,right = 100,top = 0,bottom = 100
        //获取View的绘制范围，即左、上、右、下边界相对于此View的左顶点的距离（偏移量），即0、0、View的宽、View的高
        Log.d("wdw", "getDrawingRect --- left = " + rect.left +
                ",right = " + rect.right +
                ",top = " + rect.top +
                ",bottom = " + rect.bottom);

        //getLocalVisibleRect --- left = 0,right = 100,top = 0,bottom = 100
        view0.getLocalVisibleRect(rect);
        Log.d("wdw", "getLocalVisibleRect --- left = " + rect.left +
                ",right = " + rect.right +
                ",top = " + rect.top +
                ",bottom = " + rect.bottom);
        //getGlobalVisibleRect --- left = 0,right = 100,top = 72,bottom = 172
        view0.getGlobalVisibleRect(rect);
        Log.d("wdw", "getGlobalVisibleRect --- left = " + rect.left +
                ",right = " + rect.right +
                ",top = " + rect.top +
                ",bottom = " + rect.bottom);

        // getHitRect --- left = 0,right = 100,top = 0,bottom = 100
        //获取View可点击矩形左、上、右、下边界相对于父View的左顶点的距离（偏移量）
        view0.getHitRect(rect);
        Log.d("wdw", "getHitRect --- left = " + rect.left +
                ",right = " + rect.right +
                ",top = " + rect.top +
                ",bottom = " + rect.bottom);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        if (!rect.contains((int) event.getRawX(), (int) event.getRawY())) {
//            return super.onTouchEvent(event);
//        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getRawX();
                downY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float distanceX = event.getRawX() - downX;
                float distanceY = event.getRawY() - downY;

                view0.setTranslationX(view0.getX()+distanceX);//getX会一直变化，一句话：x = left（不变） + translationX
                view0.setTranslationY(view0.getY()+distanceY);
                view0.getHitRect(rect);

                downY = event.getRawY();
                downX = event.getRawX();
                break;
        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}
