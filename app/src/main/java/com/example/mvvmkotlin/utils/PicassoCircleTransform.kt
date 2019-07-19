package com.example.mvvmkotlin.utils

import android.graphics.*
import com.squareup.picasso.Transformation

class PicassoCircleTransform : Transformation {

    override fun transform(source: Bitmap): Bitmap {
        val size = Math.min(source.width, source.height)

        val width = (source.width - size) / 2
        val height = (source.height - size) / 2

        val circledBitmap = Bitmap.createBitmap(source, width, height, size, size)
        if (circledBitmap != source) {
            source.recycle()
        }

        val bitmap = Bitmap.createBitmap(size, size, source.config)

        val canvas = Canvas(bitmap)
        val paint = Paint()
        val shader = BitmapShader(circledBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = shader
        paint.isAntiAlias = true

        val radius = size / 2f
        canvas.drawCircle(radius, radius, radius, paint)

        circledBitmap.recycle()
        return bitmap
    }

    override fun key(): String {
        return "circle"
    }

}
