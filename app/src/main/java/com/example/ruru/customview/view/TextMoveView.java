package com.example.ruru.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.example.ruru.customview.R;

/**
 * 注意：
 * 当TextMoveView宽度和高度设置为100dp和50dp的时候：效果没错,且文字只能在100dp/50dp的空间内移动
 * 当TextMoveView宽度和高度都设置为match_parent的时候：效果正常，且文字在全屏内移动
 * <p>
 * 引出问题：
 * 宽度设置为match_parent和设置为200dp怎么进行测量：见MyTextMoveView
 * <p>
 * activity布局中添加：
 * app:textMove="文字移动"
 * app:textMoveColor="@color/white"
 * app:textMoveSize="@dimen/text_size"
 */
public class TextMoveView extends AppCompatTextView {
    private static final String TAG = "TextMoveView";

    //text属性
    private String text;
    private int textColor;
    private float textSize;

    //画笔
    private Paint mPaint;

    //移动的x/y点的坐标
    private int x;
    private int y;
    private Rect mRect;

    public TextMoveView(Context context) {
        this(context, null);
    }

    public TextMoveView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //自定义属性的声明和获取
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TextMoveView, 0, 0);
        text = array.getString(R.styleable.TextMoveView_textMove);
        textColor = array.getColor(R.styleable.TextMoveView_textMoveColor, ContextCompat.getColor(context, R.color.colorPrimary));
        textSize = array.getDimension(R.styleable.TextMoveView_textMoveSize, 18);
        array.recycle();

        //初始化
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(textColor);
        mPaint.setTextSize(textSize);
        mRect = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), mRect);
    }

    /**
     * 根据view的源码：
     * onDraw方法不需要写：super.onDraw();
     * draw方法需要写：super.draw();
     *
     * @param canvas 绘图
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Log.d(TAG, "draw");

        /*canvas的几种用法*/
//        canvas.drawArc();
//        canvas.drawLine(0,0,getWidth(),height,mPaint);
//        canvas.drawText("你好",getWidth()/2,height/2,mPaint);
//        Bitmap bitmap = BitmapFactory.decodeStream(getResources().openRawResource(R.raw.a));
//        canvas.drawBitmap(bitmap,getWidth()/2-bitmap.getWidth()/2,0,mPaint);

        //getHeight():整个控件的高度即定义的android:layout_height
        //textHeight：自定义属性的高度
        //注意：此处以TextView的左上角为原点
        //参数：x为textview开始的坐标，y为textview在y轴的基线/底线baseline
        canvas.drawText(text, x, y, mPaint);//宽度记得除以2否则会被挤出去
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                x = (int) event.getX();
                y = (int) event.getY();
                invalidate();//此方法 是重新触发 draw方法
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;//下面这个一定要改为return true;
    }
}
