package com.extacy.ms.extensions

import android.content.res.ColorStateList
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat

fun View.setAlphaClickable(isAlpha: Boolean) {
    if (!isAlpha) return

    this.isClickable = isAlpha
    this.setOnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN   -> {
                v.alpha = 0.7F
            }
            MotionEvent.ACTION_UP     -> {
                v.alpha = 1F
                (parent as View).performClick()
            }
            MotionEvent.ACTION_CANCEL -> v.alpha = 1F
        }
        return@setOnTouchListener false
    }
}