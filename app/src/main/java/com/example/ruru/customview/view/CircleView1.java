package com.example.ruru.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * 体会自定义View的精髓：除了绘制onDraw,重绘invalidate()
 * 非常好的链接：https://www.2cto.com/kf/201611/567099.html
 * 两种方式：
 * 1着色器
 * 将图片压缩到和控件大小一致，先在画板上画一个圆，然后创建bitmap着色器shader，创建画笔paint并设置着色器，使用带有着色器的画笔在画板上画圆
 * 2图片混合模式
 * 使用Bitmap创建一个空的Canvas(画板)，在画板上画一个圆和显示的图片，paint图像混合模式显示
 */
public class CircleView1 extends AppCompatImageView {

    private int outWidth = 2;
    private int outColor = Color.RED;

    private Paint paint;

    public CircleView1(Context context) {
        this(context, null);
    }

    public CircleView1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        canvas.drawColor(Color.YELLOW);
        paint.setColor(outColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, paint);
        drawShader(canvas);
    }

    private void drawShader(Canvas canvas) {
        Drawable mDrawable = getDrawable();
        if (mDrawable == null) return;
        if (mDrawable instanceof BitmapDrawable) {
            Bitmap bmp = ((BitmapDrawable) mDrawable).getBitmap();
            if (bmp == null) return;
            //图片缩放，参数2 目标宽度，参数3目标高度，参数4 是否过滤
            bmp = Bitmap.createScaledBitmap(bmp, getWidth(), getHeight(), true);
            //着色器
            Shader shader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint = new Paint();
            paint.setShader(shader);
            canvas.drawCircle(getWidth() / 2, getWidth() / 2, (getWidth() - outWidth * 2) / 2, paint);
        }
    }
}
