package com.extacy.ms.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.extacy.ms.common.utils.DPIUtil

enum class ImageShape(val load: (ImageView, String, Int, Boolean, Int, Int) -> Unit) {

    None({ imageView, url, value, transition, default, resize ->
        val manager = Glide.with(imageView.context)
//        if( resize > 0 ) {
//            manager.setDefaultRequestOptions(RequestOptions().override(resize))
//        }
        val builder = manager.load(url)


        if( default != 0 ) {
            builder.error(default)
                .placeholder(default)
        }

        if( transition ) {
            builder.transition(DrawableTransitionOptions.withCrossFade())
        }

        builder.into(imageView)
    }),
    Circle({ imageView, url, value, transition, default, resize ->
        val manager = Glide.with(imageView.context)

//        if( resize > 0 ) {
//            manager.setDefaultRequestOptions(com.bumptech.glide.request.RequestOptions().override(resize))
//        }

        val builder = manager.load(url).circleCrop()

        if( default != 0 ) {
            builder.error(default)
                .placeholder(default)
        }

        if( transition ) {
            builder.transition(DrawableTransitionOptions.withCrossFade())
        }
        builder.into(imageView)
    }),
    CornerRounded({ imageView, url, value, transition, default, resize ->
        val manager = com.bumptech.glide.Glide.with(imageView.context)

//        if( resize > 0 ) {
//            manager.setDefaultRequestOptions(com.bumptech.glide.request.RequestOptions().override(resize))
//        }

        val builder = manager
            .load(url)
            .transform(
                CenterCrop(),
                RoundedCorners(
                    DPIUtil.dp2px(value.toFloat()).toInt()
                )
            )
        if( default != 0 ) {
            builder.error(default)
                .placeholder(default)
        }

        if( transition ) {
            builder.transition(DrawableTransitionOptions.withCrossFade())
        }
        builder.into(imageView)
    }),
}

fun ImageView.loadUrl(url: String? = "", shape: ImageShape = ImageShape.None, value: Int = 8, useTransition: Boolean = true, clear: Boolean = true, default: Int = 0, resize: Int = 0) {
    if( clear ) {
        Glide.with(context).clear(this)
    }
    if (url.isNullOrEmpty()) {
        return
    }
    shape.load(this, url, value, useTransition, default, resize)
}

