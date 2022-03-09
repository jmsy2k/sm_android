package com.extacy.ms.net.view

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.extacy.ms.R
import com.extacy.ms.common.utils.DPIUtil
import java.lang.Exception

class LoadingProgress {
    private var dialog: Dialog? = null
    val isShowing: Boolean
        get() {
            if (dialog == null) return false
            return dialog?.isShowing == true
        }

    fun show(context: Context?) {
        if (context == null ) {
            return
        }
        if (dialog != null && (dialog?.isShowing == false)) {
            dialog?.cancel()
            dialog = null
        }
        if (dialog == null) {
            dialog = Dialog(context, R.style.TransparentProgressDialog)
            dialog?.setCancelable(false)
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layoutLoading: View = inflater.inflate(R.layout.loading_image_view, null, false)
            dialog?.addContentView(
                layoutLoading, ViewGroup.LayoutParams(
                    DPIUtil.dp2px(80F).toInt(),
                    DPIUtil.dp2px(110F).toInt()
                )
            )
            dialog?.show()
        }
    }

    fun dismiss(context: Context?) {
        try {
            if (dialog != null) {
                if (dialog?.isShowing == true) {
                    dialog?.dismiss()
                    dialog = null
                }
            }
        } catch (e: Exception) {
            // nothing
        }
    }

    companion object {
        val shared: LoadingProgress = LoadingProgress()
    }
}
