package com.example.zubrilkaenglish.screens.training.popup

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.example.zubrilkaenglish.databinding.PopupOptionsTrainCardBinding

/**
 * в обязанности класса входит
 * показ всплывающего popup диалогового окошка с различными опциями
 * производимыми над карточкой во время ее изучения
 */
class PopupOptions(context: Context) : Dialog(context) {

    init {
        val binding = PopupOptionsTrainCardBinding.inflate(layoutInflater)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}