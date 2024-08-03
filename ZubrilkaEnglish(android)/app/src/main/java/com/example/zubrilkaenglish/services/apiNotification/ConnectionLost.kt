package com.example.zubrilkaenglish.services.apiNotification

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.PopupConnectionLostBinding
import com.example.zubrilkaenglish.events.NotificationEvent
import com.example.zubrilkaenglish.screens.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * всплывающее сообщение при потере соединения с сервером
 */
class ConnectionLost(event: NotificationEvent,
                            activity: MainActivity
) : Dialog(activity) {
    private var binding = PopupConnectionLostBinding.inflate(layoutInflater)

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        setContentView(binding.root)

        window?.apply {
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND) // Убираем затемнение заднего фона даем возможность взаимодействовать с ним
            attributes.gravity = Gravity.BOTTOM
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        )

        window?.setWindowAnimations(R.style.DialogAnimation)


        if (!event.message.isBlank()){
            binding.message.text = event.message + "\nПотеряно соединение"
        }else{
            binding.message.text = "Потеряно соединение"
        }

        callDismiss()
    }

    private fun callDismiss() {
        GlobalScope.launch {
            delay(4000)
            withContext(Dispatchers.Main){
                dismiss()
            }
        }
    }
}