package com.example.ruru.customview.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

/**
 * 例子：随手指全屏滑动
 * 不能用scrollTo/scrollBy，可以用动画或改变布局参数。
 * 动画：根据两次滑动距离实现滑动
 */
public class MoveView1 extends View {

    private int mLastX;
    private int mLastY;

    public MoveView1(Context context) {
        super(context);
    }

    public MoveView1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MoveView1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case ACTION_DOWN:
                break;
            case ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                setTranslationX(getTranslationX() + deltaX);
                setTranslationY(getTranslationY() + deltaY);
                break;
            case ACTION_UP:
                break;
        }
        mLastX = x;
        mLastY = y;
        return true;
    }
}
