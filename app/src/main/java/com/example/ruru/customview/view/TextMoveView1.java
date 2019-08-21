package com.example.ruru.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.example.ruru.customview.R;

/**
 * 从TextMoveView引出问题：
 * 宽度设置为match_parent和设置为200dp怎么进行测量：MyTextMoveView
 * <p>
 */
public class TextMoveView1 extends View {

    //text属性
    private String text;
    private int textColor;
    private float textSize;

    //画笔
    private Paint mPaint;

    //坐标
    int x;
    int y;
    private Rect mRect;

    public TextMoveView1(Context context) {
        this(context, null);
    }

    public TextMoveView1(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextMoveView1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TextMoveView1(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextMoveView1, 0, 0);
        text = a.getString(R.styleable.TextMoveView1_myText);
        textColor = a.getColor(R.styleable.TextMoveView1_myTextColor, ContextCompat.getColor(context, R.color.colorPrimary));
        textSize = a.getDimension(R.styleable.TextMoveView1_myTextSize, 18);
        a.recycle();

        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(textColor);
        mPaint.setTextSize(textSize);

        mRect = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), mRect);

        x = getWidth() / 2 - mRect.width() / 2;
        y = getHeight() / 2 + mRect.height() / 2;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawText(text, getWidth() / 2 - mRect.width() / 2, getHeight() / 2 + mRect.height() / 2, mPaint);
    }
}
