package com.example.ruru.customview.drawable;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;

public interface IRefreshDrawable extends Animatable, Drawable.Callback {

    int getFinalDragOffset();

    void onDrag(float dragPercent);

    void onOffsetTopAndBottom(int offset);

    void setColorSchemeColors(int[] colorSchemeColors);

}