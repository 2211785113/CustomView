package com.example.ruru.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.ruru.customview.R;

/**
 * 分类1：直接继承View：需要自己处理padding和wrap_content，实现onMeasure和onDraw
 * <p>
 * 构造方法：
 * 第一种：不断this，最后一个super
 * 第二种：每个构造方法直接加一个init();
 * <p>
 * 效果：绘制一个圆，在自己的中心点以宽高的最小值为直径绘制一个红色的实心圆。
 * <p>
 * margin:效果正常
 * padding:onDraw中处理
 * wrap_content:onMeasure中处理
 * 解释：layout_width设置为match_parent和100dp效果都是对的，但是设置为wrap_content时效果却等同于match_parent
 */
public class CircleView2 extends View {

    private int mColor = Color.RED;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float circlePadding;

    //自定义宽度和高度
    private int mWidth = 200;
    private int mHeight = 200;

    public CircleView2(Context context) {
        this(context, null);
    }

    public CircleView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 自定义属性的声明和获取步骤：
     * 加载自定义属性集合CircleView，
     * 解析自定义属性：解析CircleView属性集合中的circle_color属性，id为R.styleable.CircleView_circle_color。
     * 如果在使用时没有指定circle_color这个属性，就会选择红色作为默认的颜色值。
     * recycle方法实现资源。
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CircleView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleView2);
//        circlePadding = a.getDimension(R.styleable.CircleView2_circlePadding, 0);
        a.recycle();

        init();
    }

    private void init() {
        mPaint.setColor(mColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;
        float radius = Math.min(width, height) / 2;
        canvas.drawCircle(paddingLeft + width / 2, paddingTop + height / 2, radius, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, mHeight);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, mHeight);
        }
    }
}
