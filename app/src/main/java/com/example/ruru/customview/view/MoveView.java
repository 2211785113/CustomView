package com.example.ruru.customview.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * View的滑动：
 * 第一种：layout()
 * 第二种：offsetLeftAndRight与offsetTopAndBottom
 * 第三种：LayoutParams
 */
public class MoveView extends View {

    private int lastX;
    private int lastY;

    public MoveView(Context context) {
        super(context);
    }

    public MoveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MoveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean onTouchEvent(MotionEvent event) {
        //获取手指触摸点的横坐标和纵坐标
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                /*
                 * 第一种
                 */
                /*int offsetX = x - lastX;
                int offsetY = y - lastY;
                //调用layout 方法来重新放置它的位置
                layout(getLeft() + offsetX, getTop() + offsetY, getRight() + offsetX, getBottom() + offsetY);*/


                /*
                 * 第二种
                 */
                int offsetX = x - lastX;
                int offsetY = y - lastY;
                //对left 和right 进行偏移
                offsetLeftAndRight(offsetX);
                //对top 和bottom 进行偏移
                offsetTopAndBottom(offsetY);


                /*
                 * 第三种
                 */
                //可以使用父布局的布局参数，也可以使用ViewGroup.MarginLayoutParams
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
                layoutParams.leftMargin = getLeft() + offsetX;
                layoutParams.topMargin = getTop() + offsetY;
                setLayoutParams(layoutParams);
                break;

        }
        return true;
    }
}
