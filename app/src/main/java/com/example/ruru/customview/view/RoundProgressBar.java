package com.example.ruru.customview.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.ruru.customview.MainActivity;
import com.example.ruru.customview.R;

/**
 * 功能：
 * 当进度条满100的时候，跳转页面;
 * 当点击进度条的时候直接跳转页面
 */
public class RoundProgressBar extends View {

    //画笔
    private Paint mPaint;

    //属性
    private int mRoundColor;
    private int mRoundProgressColor;
    private float mRoundWidth;
    private int mMax;
    private int mTextColor;
    private float mTextSize;
    private boolean mTextIsDisplayable;
    private int mStyle;

    //
    private static final int STROKE = 0;
    private static final int FILL = 1;
    private int mProgress = 0;

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();

        //获取自定义的属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);
        mRoundColor = typedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.RED);//第二个参数均为默认值--圆的颜色
        mRoundProgressColor = typedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.RED);//进度条的颜色
        mRoundWidth = typedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 3);//进度条的宽度
        mMax = typedArray.getInteger(R.styleable.RoundProgressBar_max, 100);//进度条的最大值
        mTextColor = typedArray.getColor(R.styleable.RoundProgressBar_textColor, Color.RED);//进度条的文字颜色
        mTextSize = typedArray.getDimension(R.styleable.RoundProgressBar_textSize, 15);//进度条的文字大小
        mTextIsDisplayable = typedArray.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable, true);//进度条的百分比是否显示
        mStyle = typedArray.getInt(R.styleable.RoundProgressBar_style, 0);//--------------设置进度条是空心的
        typedArray.recycle();
    }

    //绘制
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //第一步：画圆-实心灰色圆
        int centerOfCircle = getWidth() / 2;//圆心
        int radius = (int) (centerOfCircle - mRoundWidth / 2);//半径
        mPaint.setAntiAlias(true);//设置画笔
        mPaint.setColor(mRoundColor);//设置圆的颜色
        mPaint.setStyle(Paint.Style.FILL);//设置圆是实心的还是空心的
        mPaint.setStrokeWidth(mRoundWidth);//设置画笔宽度
        canvas.drawCircle(centerOfCircle, centerOfCircle, radius, mPaint);//画图

        //第二步：设置进度条
        mPaint.setStrokeWidth(mRoundWidth);//画笔宽度
        mPaint.setColor(mRoundProgressColor);//设置进度
        mPaint.setAntiAlias(true);
        RectF oval = new RectF(centerOfCircle - radius, centerOfCircle - radius, centerOfCircle + radius, centerOfCircle + radius);
        switch (mStyle) {
            case STROKE://进度条是空心的
                mPaint.setStyle(Paint.Style.STROKE);//画圆弧
                canvas.drawArc(oval, 180, 360 * mProgress / mMax, false, mPaint);//开始的角度
                break;
            case FILL://进度条是实心的
                mPaint.setStyle(Paint.Style.FILL_AND_STROKE);//画圆弧
                if (mProgress != 0) {
                    canvas.drawArc(oval, 180, 360 * mProgress / mMax, true, mPaint);
                }
                break;
        }

        //第三步：设置字体
        mPaint.setStrokeWidth(0);//画百分比
        mPaint.setTextSize(mTextSize);//字体大小
        mPaint.setColor(mTextColor);//画笔颜色
        mPaint.setTypeface(Typeface.DEFAULT);//字体
        mPaint.setTextAlign(Paint.Align.CENTER);
        float textY = centerOfCircle - (mPaint.descent() + mPaint.ascent()) / 2;
        if (mTextIsDisplayable) {//如果文字显示那么就开始写文字
            canvas.drawText("跳过", centerOfCircle, textY, mPaint);
        }
        //字体不显示跳过显示百分比
//        int percent = (int) (((float) mProgress / (float) mMax) * 100);
//        float textWidth = mPaint.measureText(percent + "%");
//        判断是否显示进度文字 不是0，风格是空心的
//        if (mTextIsDisplayable && percent != 0 && mStyle == STROKE) {
//            canvas.drawText(percent + "%", centerOfCircle - textWidth / 2, centerOfCircle + textWidth / 2, mPaint);
//        }
    }

    public int getRoundColor() {
        return mRoundColor;
    }

    public void setRoundColor(int roundColor) {
        mRoundColor = roundColor;
    }

    public int getRoundProgressColor() {
        return mRoundProgressColor;
    }

    public void setRoundProgressColor(int roundProgressColor) {
        mRoundProgressColor = roundProgressColor;
    }

    public float getRoundWidth() {
        return mRoundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        mRoundWidth = roundWidth;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float textSize) {
        mTextSize = textSize;
    }

    public synchronized int getMax() {
        return mMax;
    }

    public synchronized void setMax(int max) {
        mMax = max;
    }

    public boolean isTextIsDisplayable() {
        return mTextIsDisplayable;
    }

    public void setTextIsDisplayable(boolean textIsDisplayable) {
        mTextIsDisplayable = textIsDisplayable;
    }

    public int getStyle() {
        return mStyle;
    }

    public void setStyle(int style) {
        mStyle = style;
    }

    public synchronized int getProgress() {
        return mProgress;
    }

    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress > mMax) {
            mProgress = progress;
        }
        if (progress <= mMax) {
            mProgress = progress;
            //非UI线程中用postInvalidate
            postInvalidate();
        }
    }
}



