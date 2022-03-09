package com.extacy.ms.components.dialog

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.extacy.ms.databinding.ViewPopupTitleBinding


class PopupTitle: FrameLayout {
    lateinit var binding: ViewPopupTitleBinding
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    fun init() {
        binding = ViewPopupTitleBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)
    }

    fun setting(title: String?) {
        if( title != null )
            binding.textTitle.text = title
    }
}