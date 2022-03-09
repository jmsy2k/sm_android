package com.extacy.ms.net.test

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.ThumbnailUtils
import android.util.AttributeSet



class TestImageView : androidx.appcompat.widget.AppCompatImageView {
    private val attrs: AttributeSet
    private var paint: Paint? = null
    private var mSrcRect: Rect? = null
    private var mDestRect: Rect? = null
    private var mBitmap: Bitmap? = null

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.attrs = attrs
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        this.attrs = attrs
        initView()
    }

    private fun initView() {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint?.setStyle(Paint.Style.FILL_AND_STROKE)
        paint?.setStrokeWidth(10f)
        paint?.setColor(-0x1000000)
    }
    fun getHoledBitmap(bm: Bitmap): Bitmap? {
        val sice = Math.min(bm.width, bm.height)
        val holesize = (19f * sice) / 132f
        val output = ThumbnailUtils.extractThumbnail(bm, sice, sice)
//        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val color:Int = -1
        val paint = Paint()

        val halfsize = (sice / 2f)
        val halfhole = (holesize / 2f)
        val rectF = RectF(halfsize - halfhole, halfsize - halfhole, halfsize + halfhole, halfsize + halfhole)

        paint.isAntiAlias = true
        paint.isDither = true
        paint.isFilterBitmap = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        paint.color = Color.TRANSPARENT
        canvas.drawOval(rectF, paint)
//        paint.color = Color.BLUE
//        paint.style = Paint.Style.STROKE
//        paint.strokeWidth = 4.toFloat()
//        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
//        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }

    override fun setImageBitmap(bm: Bitmap?) {
        if( bm != null ) {
            var bitmap = getHoledBitmap(bm)
            super.setImageBitmap(bitmap)
            return
        }
        super.setImageBitmap(bm)
    }

    override fun setImageDrawable(drawable: Drawable?) {
        if( drawable != null && drawable is BitmapDrawable ) {
            var bitmapDrawable = (drawable as BitmapDrawable)

            val newBitmap = getHoledBitmap(bitmapDrawable.bitmap)
            super.setImageDrawable(BitmapDrawable(resources, newBitmap))
            return
        }

        super.setImageDrawable(drawable)

    }
//    override fun onDraw(canvas: Canvas) {
//        setBackground
//        setBackgroundDrawable(Drawable())
//        setBackgroundDrawable()
//        19 : 132 = imagesize : holesize
//        132 * imagesize = 19 * holesize
//
//
////        super.onDraw(canvas);
//        //create the drawable.Maybe you can cache it.
//        val mDrawable = resources.getDrawable(R.drawable.cover_def, null) as BitmapDrawable
//                ?: return  // couldn't resolve the URI
//        val dWidth = width// mDrawable.intrinsicWidth
//        val dHeight = height//mDrawable.intrinsicHeight
//        var scale = 1.0f
//        scale = Math.max(
//            getWidth() * 1.0f / dWidth, getHeight()
//                    * 1.0f / dHeight
//        )
//        val nWidth = (dWidth * scale).toInt()
//        val nHeight = (dHeight * scale).toInt()
//        val offsetLeft = (nWidth - getWidth()) / 2
//        val offsetTop = (nHeight - getHeight()) / 2
//        //cache mBitmap
//        mBitmap = if (mBitmap == null) mDrawable.bitmap else mBitmap
//        //custom mSrcRect mDestRect to achieve centerCrop
//        mSrcRect = Rect(0, 0, dWidth, dWidth)
//        mDestRect = Rect(-offsetLeft, -offsetTop, getWidth() + offsetLeft, getHeight() + offsetTop)
//        val x = 250
//        val r = 100
//        val sc: Int = canvas.saveLayer(
//            0f, 0f, getWidth().toFloat(), getHeight().toFloat(), null
////            ,
////            Canvas.MATRIX_SAVE_FLAG
////                    or Canvas.CLIP_SAVE_FLAG
////                    or Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
////                    or Canvas.FULL_COLOR_LAYER_SAVE_FLAG
////                    or Canvas.CLIP_TO_LAYER_SAVE_FLAG
//        )
//        paint?.setColor(-0x1)
//        canvas.drawBitmap(mBitmap!!, mSrcRect, mDestRect!!, paint)
//        paint?.setColor(-0x1000000)
//        paint?.setXfermode(PorterDuffXfermode(PorterDuff.Mode.DST_OUT))
//        canvas.drawCircle(x.toFloat(), x.toFloat(), r.toFloat(), paint!!)
//        paint?.setXfermode(null)
//        canvas.restoreToCount(sc)
//    }
}