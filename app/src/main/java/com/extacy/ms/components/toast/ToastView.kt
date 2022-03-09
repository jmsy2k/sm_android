package com.extacy.ms.components.toast

import android.content.Context
import android.content.res.Resources
import android.graphics.Paint
import android.graphics.Rect
import android.view.LayoutInflater
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.extacy.ms.base.BaseActivity
import com.google.android.material.snackbar.BaseTransientBottomBar.ANIMATION_MODE_FADE
import com.google.android.material.snackbar.Snackbar
import com.extacy.ms.common.utils.DPIUtil
import com.extacy.ms.databinding.ViewToastBinding
import kotlin.math.min


object ToastView {
    fun show(activity: BaseActivity, message: String) {
        val binding = ViewToastBinding.inflate(LayoutInflater.from(activity))

        binding.textToast.text = message


        val textWidth = textWidth(binding.textToast, message)

        val swidth = Resources.getSystem().displayMetrics.widthPixels
        val maxTextWidth = swidth - DPIUtil.dp2px(80f).toInt()

        val dp20 = DPIUtil.dp2px(20f).toInt()

        val width = min(textWidth, maxTextWidth)
        val sidePadding = (swidth - (width + (dp20 * 2))) / 2
        val snackbar = Snackbar.make(activity.window.decorView, "", 4000)

        snackbar.animationMode = ANIMATION_MODE_FADE
        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout

        val navH = navHeight(activity)

        with(snackbarLayout) {
            removeAllViews()
            setPadding(sidePadding, 0, sidePadding, DPIUtil.dp2px(84f).toInt() + navH)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))

            addView(binding.root)
        }
        snackbar.show()
    }

    fun navHeight(context: Context): Int {
        val resources: Resources = context.getResources()
        val resourceId: Int = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
    }

    fun textWidth(textView: TextView, text: String): Int {
        val bounds = Rect()
        val textPaint: Paint = textView.paint
        textPaint.getTextBounds(text, 0, text.length, bounds)
        return bounds.width() + 10
    }

}