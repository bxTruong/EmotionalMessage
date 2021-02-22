package com.truongbx.emotionalmessage.helper

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.WindowInsets
import kotlin.math.roundToInt

class BaseStatusBar(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var mStatusBarHeight = 0

    override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mStatusBarHeight = dpToPx(26.0f)
            return insets.consumeSystemWindowInsets()
        }
        return insets
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), mStatusBarHeight)
    }

    private fun dpToPx(dp: Float): Int {
        val density =
            Resources.getSystem().displayMetrics.density
        return (dp * density).roundToInt()
    }
}