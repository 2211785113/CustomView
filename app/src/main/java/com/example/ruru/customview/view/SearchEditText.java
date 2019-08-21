package com.example.ruru.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.example.ruru.customview.R;

/**
 * 仿微信ios搜索框
 * 可以看看github的第一个和第二个
 */
public class SearchEditText extends AppCompatEditText {

    //属性：提示文字
    private String textHint;//提示字体
    private int textColorHint;//提示字体颜色
    private float textSizeHint;//提示字体大小

    //属性：搜索图片和文字的距离
    private float iconSpacing;

    //属性：搜索图片
    private float searchIconDimen;//搜索图片大小
    private Drawable drawable;//搜索图片

    //画笔
    private Paint paint;

    public SearchEditText(Context context) {
        this(context, null);
    }

    public SearchEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.searchEditText);

        //获取屏幕像素密度dp
        float density = context.getResources().getDisplayMetrics().density;

        textHint = a.getString(R.styleable.searchEditText_textHint);
        textColorHint = a.getColor(R.styleable.searchEditText_textColorHint, 0xFF999999);
        textSizeHint = a.getDimension(R.styleable.searchEditText_textSizeHint, density * 14 + 0.5F);
        iconSpacing = a.getDimension(R.styleable.searchEditText_iconSpacing, density * 5 + 0.5F);//dp转px
        searchIconDimen = a.getDimension(R.styleable.searchEditText_searchIconDimen, density * 15 + 0.5F);
        drawable = getContext().getResources().getDrawable(a.getResourceId(R.styleable.searchEditText_searchIcon, R.drawable.circle));

        drawable.setBounds(0, 0, (int) searchIconDimen, (int) searchIconDimen);//right和bottom为searchIconDimen

        a.recycle();

        //初始化文字画笔
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);//保证画的时候图形不失真，消除混叠现象。
        paint.setColor(textColorHint);
        paint.setTextSize(textSizeHint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (TextUtils.isEmpty(this.getText().toString())) {
            //paint得到text宽度和高度
            float textWidth = paint.measureText(textHint);
            float textHeight = paint.getFontMetrics().bottom - paint.getFontMetrics().top;

            //绘图坐标系x和y
            float x = (getWidth() - textWidth - searchIconDimen - iconSpacing) / 2;//图片和文字放在中间位置
            float y = (getHeight() - searchIconDimen) / 2;

            //保存现场
            canvas.save();
            //getScrollX和getScrollY为View移动内容的偏移量。有疑问？？放在这里的用处？？？？？？？？？？？？
            canvas.translate(x + getScrollX(), y + getScrollY());

            //canvas绘制图片
            drawable.draw(canvas);
            //canvas绘制文字，有疑问？？？？？？？？？？？
            canvas.drawText(textHint, searchIconDimen + iconSpacing + getScrollX(), getHeight() - (getHeight() - textHeight) / 2 + getScrollY() - paint.getFontMetrics().bottom - y, paint);

            //恢复现场
            canvas.restore();
        }
    }
}
