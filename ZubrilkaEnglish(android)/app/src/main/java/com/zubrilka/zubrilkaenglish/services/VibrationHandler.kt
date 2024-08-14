package com.zubrilka.zubrilkaenglish.services

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import com.zubrilka.zubrilkaenglish.utils.MyApplication
import com.zubrilka.zubrilkaenglish.utils.isVibrationEnabled

/**
 * красс занимается различными вибрационными откликами в приложении
 */
class VibrationHandler private constructor(){
    companion object{
        val instance: VibrationHandler by lazy { VibrationHandler() }
    }
    val context = MyApplication.context

    //позитивная вибрация при положительном выборе карточки например
    fun vibratePositive() {
        if (isVibrationEnabled){
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val effect = VibrationEffect.createOneShot(80, VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator.vibrate(effect)
            } else {
                vibrator.vibrate(80)
            }
        }
    }

    //негативная вибрация например при ошибочном выборе карточки
    fun vibrateNegative() {
        if (isVibrationEnabled){
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val effect = VibrationEffect.createWaveform(longArrayOf(0, 100, 50, 100), -1)
                vibrator.vibrate(effect)
            } else {
                vibrator.vibrate(longArrayOf(0, 100, 50, 100), -1)
            }
        }
    }
}