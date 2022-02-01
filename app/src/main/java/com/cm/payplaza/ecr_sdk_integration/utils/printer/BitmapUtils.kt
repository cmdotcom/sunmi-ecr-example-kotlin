package com.cm.payplaza.ecr_sdk_integration.utils.printer

import android.graphics.Bitmap
import android.graphics.Matrix

class BitmapUtils {
    companion object {
        fun scale(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
            val width = bitmap.width
            val height = bitmap.height
            val matrix = Matrix()
            matrix.postScale((newWidth.toFloat() / width), (newHeight.toFloat() / height))
            return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
        }

        fun replaceColor(bitmap: Bitmap, oldColor: Int, newColor: Int): Bitmap {
            val copy = bitmap.copy(Bitmap.Config.ARGB_8888, true)
            val width = copy.width
            val height = copy.height
            val raw = IntArray(width * height)
            copy.getPixels(raw, 0, width, 0, 0, width, height)
            val coloredRaw = raw.map { if(it == oldColor) newColor else it }.toIntArray()
            copy.setPixels(coloredRaw, 0, width, 0, 0, width, height)
            return copy
        }
    }
}