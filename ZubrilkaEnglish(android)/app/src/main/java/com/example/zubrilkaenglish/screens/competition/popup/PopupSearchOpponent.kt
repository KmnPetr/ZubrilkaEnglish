package com.example.zubrilkaenglish.screens.competition.popup

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager
import com.example.zubrilkaenglish.databinding.PopupSearchOpponentBinding
import com.example.zubrilkaenglish.models.socketDto.StatusInfo

/**
 * в обязанности класса входит
 * показ всплывающего popup диалогового окошка
 * с различной инвормацией о начальном поиске соперника перед поединком
 */
class PopupSearchOpponent(
    context: Context,
    statusInfo: StatusInfo?
) : Dialog(context) {
    private var binding = PopupSearchOpponentBinding.inflate(layoutInflater)

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
//        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        setListeners()
    }

    /**
     * функция установит слушатели на все кнопки диалогового окна
     */
    private fun setListeners() {
        binding.buttonNextRound.setOnClickListener {
            println("buttonNextRound")
        }
    }
}