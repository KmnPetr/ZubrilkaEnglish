package com.example.zubrilkaenglish.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import com.example.zubrilkaenglish.R

/**
 * функция обрезает картинку перед ее установкой в качестве фона
 * здесь некая логика обрезки левого края изображения по пропорциям экрана
 */
fun customizeBackground(background: ImageView, resources: Resources) {

    val displayMetrics = resources.displayMetrics
    val widthPixels = displayMetrics.widthPixels
    val heightPixels = displayMetrics.heightPixels

    val attitude = widthPixels.toFloat()/heightPixels.toFloat()


    val originalBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.fon_1)
    val desiredWidth = originalBitmap.height*attitude
    val desiredHeight = originalBitmap.height

    try {
        val croppedBitmap: Bitmap = Bitmap.createBitmap(originalBitmap, originalBitmap.width-desiredWidth.toInt(), 0, desiredWidth.toInt(), desiredHeight)
        background.setImageBitmap(croppedBitmap)
    }catch (e: Exception){//здесь выпадает ошибка если экран горизонтально повернут, картинка маловата чтобы ее обрезать
        e.printStackTrace()
        background.setImageBitmap(originalBitmap) //просто поставим картинку не во весь экран
    }
}