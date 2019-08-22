package com.example.ruru.customview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 分类2：继承ViewGroup，实现onMeasure和onLayout
 * 源码：中有HorizontalScrollView
 * 用途：类似ViewPager控件，类似于水平方向的LinearLayout控件，内部子元素可以进行水平滑动且子元素内部还可以进行竖直滑动。考虑滑动冲突。
 * 假定：所有子元素宽高一样。
 * <p>
 * 注意：
 * onMeasure中处理wrap_content
 * <p>
 * StickyLayout:
 * https://github.com/singwhatiwanna/PinnedHeaderExpandableListView
 */
public class HorizontalScrollViewEx extends ViewGroup {

    private int mChildrenSize;
    private int mChildWidth;
    private int mChildIndex;

    //分别记录上次滑动的坐标
    private int mLastX = 0;
    private int mLastY = 0;

    //分别记录上次滑动的坐标
    private int mLastXIntercept = 0;
    private int mLastYIntercept = 0;

    private Scroller mScroller;

    private VelocityTracker mVelocityTracker;

    public HorizontalScrollViewEx(Context context) {
        super(context);
        init();
    }

    public HorizontalScrollViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HorizontalScrollViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (mScroller == null) {
            mScroller = new Scroller(getContext());
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercepted = false;
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                    intercepted = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastXIntercept;
                int deltaY = y - mLastYIntercept;
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    intercepted = true;
                } else {
                    intercepted = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercepted = false;
                break;
        }
        mLastX = x;
        mLastY = y;
        mLastXIntercept = x;
        mLastYIntercept = y;
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int scrollX = getScrollX();
                mVelocityTracker.computeCurrentVelocity(1000);
                float xVelocity = mVelocityTracker.getXVelocity();

                //如果x方向向右移动的速度大于50
                if (Math.abs(xVelocity) >= 50) {
                    //如果x方向移动速度>0即向右移动,那么子view的索引-1,否则+1
                    mChildIndex = xVelocity > 0 ? mChildIndex - 1 : mChildIndex + 1;
                } else {
                    mChildIndex = (scrollX + mChildWidth / 2) / mChildWidth;//子view的索引
                }
                mChildIndex = Math.max(0, Math.min(mChildIndex, mChildrenSize - 1));
                int dx = mChildIndex * mChildWidth - scrollX;
                smoothScrollBy(dx, 0);
                mVelocityTracker.clear();
                break;
        }
        mLastX = x;
        mLastY = y;
        return true;
    }

    /**
     * 思路：
     * 判断是否有子元素，没有子元素直接把宽高设为0；
     * 判断宽高是否采用了wrap_content：
     * 宽采用了wrap_content：自定义View宽度为所有子元素宽度之和。
     * 高采用了wrap_content：自定义View高度为第一个子元素的高度。
     * 规范点：
     * 没有子元素时不应该把宽高设为0，应该根据LayoutParams宽高做相应处理。
     * 测量宽高时没有考虑它的padding及子元素的margin。因为会影响宽高，占用的是HorizontalScrollViewEx的空间。
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measureWidth = 0;
        int measureHeight = 0;

        final int childCount = getChildCount();
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getMode(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getMode(heightMeasureSpec);

        if (childCount == 0) {
            setMeasuredDimension(0, 0);
        } else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            final View childView = getChildAt(0);
            measureWidth = childView.getMeasuredWidth() * childCount;
            measureHeight = childView.getMeasuredHeight();
            setMeasuredDimension(measureWidth, measureHeight);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            final View childView = getChildAt(0);
            measureWidth = childView.getMeasuredWidth() * childCount;
            setMeasuredDimension(measureWidth, heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            final View childView = getChildAt(0);
            measureHeight = childView.getMeasuredHeight();
            setMeasuredDimension(widthSize, measureHeight);
        }
    }

    /**
     * 作用：完成子元素的定位。
     * 步骤：
     * 遍历所有的子元素，如果子元素不是处于GONE这个状态，通过layout方法将其放置在合适的位置上。
     * 放置过程：
     * 由左向右。和水平方向的LinearLayout类似。
     * 规范点：
     * 放置子元素的过程没有考虑到自身的padding以及子元素的margin。
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = 0;
        final int childCount = getChildCount();
        mChildrenSize = childCount;
        for (int i = 0; i < childCount; i++) {
            final View childView = getChildAt(i);
            final int childWidth = childView.getMeasuredWidth();
            mChildWidth = childWidth;
            childView.layout(childLeft, 0, childLeft + childWidth, childView.getMeasuredHeight());
            childLeft += childWidth;
        }
    }

    private void smoothScrollBy(int dx, int dy) {
        //View的滑动：Scroller
        mScroller.startScroll(getScrollX(), 0, dx, 0, 500);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            //View的滑动：scrollTo：只滑动view的内容，不滑动view的位置
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            //非UI线程中用postInvalidate
            postInvalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        //释放速度追踪器资源
        mVelocityTracker.recycle();
        super.onDetachedFromWindow();
    }
}
