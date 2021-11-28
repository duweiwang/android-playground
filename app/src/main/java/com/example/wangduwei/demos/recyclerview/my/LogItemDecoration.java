package com.example.wangduwei.demos.recyclerview.my;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * <p>
 *
 * @auther : wangduwei
 * @since : 2019/10/22  17:42
 **/
public class LogItemDecoration extends RecyclerView.ItemDecoration {

    private Paint paint = new Paint() {
        {
            setColor(Color.GRAY);
        }
    };

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        c.drawColor(Color.YELLOW);
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        c.drawCircle(parent.getMeasuredWidth() / 2, parent.getMeasuredHeight() / 2, 30, paint);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(30,30,30,30);
    }
}
