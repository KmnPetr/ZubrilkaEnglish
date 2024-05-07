package com.example.zubrilkaenglish.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.example.zubrilkaenglish.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

/**
 * производит анимацию при нажатии кнопки
 */
fun buttonAnimationClick(view:View?){
    view?.let {
        it.backgroundTintList = ContextCompat.getColorStateList(it.context, R.color.myGray)
        GlobalScope.launch {
            delay(100)
            withContext(Dispatchers.Main){
                it.backgroundTintList = ContextCompat.getColorStateList(it.context, R.color.white)
            }
        }
    }
}