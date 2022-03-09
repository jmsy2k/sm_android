package com.extacy.ms.components.dialog

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.extacy.ms.R
import com.extacy.ms.databinding.DialogCombineBinding

class PopupCombin(context: Context): Dialog(context, R.style.Dialog_Radio_DiscoverAlert) {
    private val binding: DialogCombineBinding by lazy {
        DialogCombineBinding.inflate(layoutInflater)
    }

    init {
//        window?.attributes?.windowAnimations = R.style.Dialog_Radio_DiscoverAlert_Anim
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
    }
    fun show(views: ArrayList<View>, bgResourceId: Int = R.drawable.dialog_bg, cancelable: Boolean = false) {

        binding.layoutContents.setBackgroundResource(bgResourceId)
        views.forEach { view ->
            binding.layoutContents.addView(view)
        }


        setCancelable(cancelable)
        setOnShowListener {
            val xanim = SpringAnimation(binding.layoutContents, DynamicAnimation.SCALE_X, 1.0f)
            val yanim = SpringAnimation(binding.layoutContents, DynamicAnimation.SCALE_Y, 1.0f)

            xanim.setStartValue(1.05f)
            yanim.setStartValue(1.05f)

            xanim.setStartVelocity(0.5f)
            yanim.setStartVelocity(0.5f)

            xanim.spring.dampingRatio = SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY
            yanim.spring.dampingRatio = SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY

            xanim.spring.stiffness = SpringForce.STIFFNESS_MEDIUM
            yanim.spring.stiffness = SpringForce.STIFFNESS_MEDIUM

            xanim.start()
            yanim.start()
        }
        show()


//        xanim.animateToFinalPosition(1.0f)
//        yanim.animateToFinalPosition(1.0f)
    }
}