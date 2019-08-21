package com.example.ruru.customview.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;

import com.example.ruru.customview.R;

import java.util.TimeZone;

public class ClockView extends View {

    //图片：表盘，时针，分针。
    private Drawable mDial;
    private Drawable mHourHand;
    private Drawable mMinuteHand;

    //表盘的宽和高
    private int mDialWidth;
    private int mDialHeight;

    //跟踪view尺寸的变化，当发生尺寸变化时，在绘制自己时要进行适当的缩放。
    private boolean mChanged;

    //系统当前时间时分秒：https://www.jianshu.com/p/f66f51199f3d
    private Time mTime;

    //时间变化后的时分
    private float mHour;
    private float mMinutes;

    /**
     * 记录view是否被加入到了window中
     * view attached 到 window 时注册监听器，监听时间的变更，
     * 然后时间变更后，改变绘制
     * 总：注册时间变更监听器-->时间变更onTimeChanged-->重绘invalidate
     * <p>
     * view 从 window 中剥离时，解除注册，不需要再监听时间变更
     */
    private boolean mAttached;

    public ClockView(Context context) {
        this(context, null);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        //初始化资源:表盘，时针，分针
        if (mDial == null) {
            mDial = context.getDrawable(R.drawable.dial);
        }

        if (mHourHand == null) {
            mHourHand = context.getDrawable(R.drawable.hour_hand);
        }

        if (mMinuteHand == null) {
            mMinuteHand = context.getDrawable(R.drawable.minute_hand);
        }

        //初始化当前时间
        mTime = new Time();

        //获取Drawable表盘固有的宽度和高度，单位为dp，dp=px*160/dpi，返回的宽高以dp为单位。
        mDialWidth = mDial.getIntrinsicWidth();
        mDialHeight = mDial.getIntrinsicHeight();
    }

    /**
     * view关联到window时：
     * 注册广播接收器监听时间变化
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!mAttached) {
            mAttached = true;
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            getContext().registerReceiver(mIntentReceiver, filter);
        }
        //这里有点不太明白
        mTime = new Time();
        onTimeChanged();
    }

    /**
     * view与window取消关联：
     * 解除注册，不监听时间的变更。
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttached) {
            getContext().unregisterReceiver(mIntentReceiver);
            mAttached = false;
        }
    }

    /**
     * 确定view的大小：
     * 系统将View的LayoutParams根据父容器添加的规则转换成对应的MeasureSpec，
     * 再根据MeasureSpec测量出View的宽高。
     * <p>
     * 本例：
     * view想要的尺寸就是与表盘一样大的尺寸，保证view有最佳的展示，如果viewgroup给的尺寸较小，可以根据表盘图片的尺寸，进行适当的按比例缩放。
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        float widthScale = 1.0f;
        float heightScale = 1.0f;

        if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mDialWidth) {
            widthScale = (float) widthSize / (float) mDialWidth;
        }

        if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < mDialHeight) {
            heightScale = (float) heightSize / (float) mDialHeight;
        }

        float scale = Math.min(widthScale, heightScale);
        setMeasuredDimension(resolveSizeAndState((int) (mDialWidth * scale), widthMeasureSpec, 0), resolveSizeAndState((int) (mDialHeight * scale), heightMeasureSpec, 0));
    }

    /**
     * 是什么：
     * 当ViewGroup中的子View数量增加或者减少，导致ViewGroup给自己分配的屏幕区域大小发生变化时，系统会回调view的onSizeChanged方法，
     * 该方法中，view可以获取最新的尺寸，然后根据这个尺寸相应调整自己的绘制。
     * <p>
     * 为什么：
     * 确定了大小，是不是就可以进行绘制了。
     * 不是。在绘制前，我们先让自定义view感知下自己尺寸的变化。
     * 如果尺寸发生了变化，就及时调整我们的绘制策略。
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //onDraw中会用到
        mChanged = true;
    }

    /**
     * 绘制之前监听时间的变化：
     * 获取到现在的时和分。
     */
    private void onTimeChanged() {
        mTime.setToNow();
        int hour = mTime.hour;
        int minute = mTime.minute;
        int second = mTime.second;
        //注意：此处Calendar可以是Linient模式，second和minute可能超过60和24
        mMinutes = minute + second / 60.0f;
        mHour = hour + mMinutes / 60.0f;
        mChanged = true;
    }

    /**
     * 广播接收器：接收系统发出的时间变化广播，然后更新该view的mTime
     */
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED)) {
                String tz = intent.getStringExtra("time-zone");
                mTime = new Time(TimeZone.getTimeZone(tz).getID());
            }
            //更新时间
            onTimeChanged();
            //重绘
            invalidate();
        }
    };

    /**
     * 绘制view：从这里开始。
     * <p>
     * canvas.save():保存画布状态，save之后调用canvas的平移旋转缩放裁剪操作
     * canvas.restore():恢复canvas之前保存的状态，防止save方法代码之后对canvas运行的操作
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //View尺寸变化后，用changed变量记录下来，
        //同时，恢复mChanged为false，以便继续监听View的尺寸变化
        boolean changed = mChanged;
        if (changed) {
            mChanged = false;
        }

        //因为 onSizeChanged 尺寸可能会发生变化，所以 availableWidth 和 availableHeight 绘制时是可能变化的
        //尺寸变化后：需要为时针，分针重新设置Bounds，因为时针，分针始终在View的中心
        //知识点：getRight/getLeft为view在父容器中的放置位置(以viewGroup左上角为0点)
        int availableWidth = super.getRight() - super.getLeft();
        int availableHeight = super.getBottom() - super.getTop();

        //View中心点的坐标(以view左上角为0点)
        int x = availableWidth / 2;
        int y = availableHeight / 2;

        final Drawable dial = mDial;
        int w = dial.getIntrinsicWidth();
        int h = dial.getIntrinsicHeight();
        boolean scaled = false;

        //坐标系的缩放
        //缩放效果影响是全局的，下面绘制的表盘，时针，分针都会受到缩放的影响
        if (availableWidth < w || availableHeight < h) {
            scaled = true;
            float scale = Math.min((float) availableWidth / (float) w, (float) availableHeight / (float) h);

            canvas.save();
            canvas.scale(scale, scale, x, y);//以中心点缩放sx，sy
        }

        if (changed) {
            dial.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        dial.draw(canvas);

        canvas.save();
        //根据小时数，以点(x,y)为中心旋转坐标系(安卓群英传-2D绘图部分Canvas)
        canvas.rotate(mHour / 12.0f * 360.0f, x, y);

        final Drawable hourHand = mHourHand;

        //根据变化重新设置时针的Bounds
        if (changed) {
            w = hourHand.getIntrinsicWidth();
            h = hourHand.getIntrinsicHeight();

            //浪费一部分图片空间(时针下半部分为空白)，换来了建模的简单性
            hourHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }

        hourHand.draw(canvas);

        canvas.restore();
        canvas.save();
        canvas.rotate(mMinutes / 60.0f * 360.0f, x, y);

        final Drawable minuteHand = mMinuteHand;

        if (changed) {
            w = minuteHand.getIntrinsicWidth();
            h = minuteHand.getIntrinsicHeight();

            minuteHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }

        minuteHand.draw(canvas);

        canvas.restore();
        //最后：把缩放的坐标系复原
        if (scaled) {
            canvas.restore();
        }
    }
}
