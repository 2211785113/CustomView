package com.example.ruru.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.example.ruru.customview.R;

/**
 * 分类3：不需要自己支持padding和wrap_content
 * onMeasure:ImageView已经定义好了
 * 源代码有：CircleImageView(自行分析)
 * <p>
 * 核心：canvas.drawCircle
 * <p>
 * activity-xml中代码：
 * app:border_color="@color/gray"
 * app:border_width="1dp"
 * 代码中需要：setImageDrawable(ContextCompat.get) 或 setImageResource
 */
public class CircleView extends AppCompatImageView {

    //其他格式转Bitmap时Bitmap的配置
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;

    //图片的大小
    private static final int COLORDRAWABLE_DIMENSION = 2;

    //图片和边框Rect
    private final RectF mDrawableRect = new RectF();
    private final RectF mBorderRect = new RectF();

    //矩阵
    private final Matrix mShaderMatrix = new Matrix();

    //边框宽度和边框颜色
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
    private static final int DEFAULT_BORDER_WIDTH = 0;

    private int mBorderColor = DEFAULT_BORDER_COLOR;
    private int mBorderWidth = DEFAULT_BORDER_WIDTH;

    //SCALE_TYPE 是什么：图像应该如何调整或移动以匹配此ImageView的大小
    //CENTER_CROP 是什么：均匀缩放图像使图像的宽和高是相等的,等于或大于ImageView的大小
    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

    //是否已经设置好边框宽度和颜色
    private boolean mReady;

    //是否进行改变，默认为false
    private boolean mSetupPending;

    //图片
    private Bitmap mBitmap;

    //绘制图片的着色器
    private BitmapShader mBitmapShader;

    //图片的宽度和高度
    private int mBitmapWidth;
    private int mBitmapHeight;

    //绘制：图片半径，边框半径
    private float mDrawableRadius;
    private float mBorderRadius;

    //绘制：图片和边框画笔
    private final Paint mBitmapPaint = new Paint();
    private final Paint mBorderPaint = new Paint();

    //给图片着色
    private ColorFilter mColorFilter;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //通过TypeArray获取xml layout中的属性值，使用完之后，通过recycle()方法将TypeArray回收
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleView, defStyleAttr, 0);
        mBorderWidth = a.getDimensionPixelSize(R.styleable.CircleView_border_width, DEFAULT_BORDER_WIDTH);
        mBorderColor = a.getColor(R.styleable.CircleView_border_color, DEFAULT_BORDER_COLOR);
        a.recycle();

        //这句话很重要：
        //是什么：控制图像应该如何调整或移动以匹配此ImageView的大小。
        super.setScaleType(SCALE_TYPE);

        //已经设置好边框宽度和颜色
        mReady = true;

        //默认为false，不执行
        if (mSetupPending) {
            setup();//改变完了该值设为false
            mSetupPending = false;
        }
    }

    /**
     * @param canvas 核心：绘制：canvas.drawCircle
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mDrawableRadius, mBitmapPaint);
        if (mBorderWidth != 0) {
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBorderRadius, mBorderPaint);
        }
    }

    /**
     * 初始化操作
     */
    private void setup() {
        if (!mReady) {
            mSetupPending = true;
            return;
        }

        //判断如果bitmap为空，return返回
        if (mBitmap == null) {
            return;
        }

        //初始化图片画笔着色
        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        //图片画笔paint
        mBitmapPaint.setAntiAlias(true);//设置画笔抗锯齿
        mBitmapPaint.setShader(mBitmapShader);//给画笔着色

        //边框paint：设置款式/颜色/宽度
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);

        //图片宽高
        mBitmapHeight = mBitmap.getHeight();
        mBitmapWidth = mBitmap.getWidth();

        //边框边界
        mBorderRect.set(0, 0, getWidth(), getHeight());
        //边框半径
        mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2, (mBorderRect.width() - mBorderWidth) / 2);
        //图片边界
        mDrawableRect.set(mBorderWidth, mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);
        //图片半径
        mDrawableRadius = Math.min(mDrawableRect.height() / 2, mDrawableRect.width() / 2);

        updateShaderMatrix();

        //重绘：外貌(颜色/宽度等)发生改变
        invalidate();
    }

    private void updateShaderMatrix() {
        float scale;
        float dx = 0;
        float dy = 0;
        mShaderMatrix.set(null);
        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            scale = mDrawableRect.height() / (float) mBitmapHeight;
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
        } else {
            scale = mDrawableRect.width() / (float) mBitmapWidth;
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
        }
        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth, (int) (dy + 0.5f) + mBorderWidth);
        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }


    /////////////////////////////下面部分比较好理解///////////////////////////////////////////////////////////////////////////////
    @Override
    public ScaleType getScaleType() {
        return SCALE_TYPE;
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
//        super.setScaleType(scaleType);
        if (scaleType != SCALE_TYPE) {
            throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
        }
    }

    @Override
    public void setAdjustViewBounds(boolean adjustViewBounds) {
//        super.setAdjustViewBounds(adjustViewBounds);
        if (adjustViewBounds) {
            throw new IllegalArgumentException("adjustViewBounds not supported.");
        }
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    /**
     * @param borderColor 设置边框颜色并重绘
     */
    public void setBorderColor(int borderColor) {
//        this.mBorderColor = mBorderColor;
        if (borderColor == mBorderColor) {
            return;
        }
        mBorderColor = borderColor;
        mBorderPaint.setColor(mBorderColor);
        //重绘：视图的外貌()需要改变
        invalidate();
    }

    public int getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        if (borderWidth == mBorderWidth) {
            return;
        }
        mBorderWidth = borderWidth;
        setup();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        mBitmap = bm;
        setup();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        mBitmap = getBitmapFromDrawable(drawable);
        setup();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        mBitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        mBitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        try {
            Bitmap bitmap;
            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }
            //Canvas绘制图片
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    /**
     * @param cf 给图片着色
     */
    @Override
    public void setColorFilter(ColorFilter cf) {
        if (cf == mColorFilter) {
            return;
        }
        mColorFilter = cf;
        mBitmapPaint.setColorFilter(mColorFilter);
        invalidate();
    }
}
