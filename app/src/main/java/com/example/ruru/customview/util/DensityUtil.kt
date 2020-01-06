package com.example.ruru.customview.util

import android.content.Context

/**
 * Created by lyr on 2020/1/5 & content is dp和px的转换
 */
object DensityUtil {

  fun dip2px(context: Context, dpValue: Int): Float = dpValue * context.resources.displayMetrics.density

  fun px2dp(context: Context, px: Float): Float = px / context.resources.displayMetrics.density
}