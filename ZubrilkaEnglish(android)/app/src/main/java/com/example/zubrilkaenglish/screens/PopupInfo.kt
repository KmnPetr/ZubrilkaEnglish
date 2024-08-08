package com.example.zubrilkaenglish.screens

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Html
import android.view.Window
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.PopupInfoBinding

/**
 * покажет окошко с информацией о какой то части приложения
 * вызывается по кнопке из тулбара по кнопке "i"
 * MainActivity отправляет уведомление в EventBus, далее каждый фрагмент сам решает, с какой информацией показать этот попап
 */
class PopupInfo(
    context: Context,
    textResource: Int
) : Dialog(context) {
    private var binding = PopupInfoBinding.inflate(layoutInflater)

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val displayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels
        binding.root.layoutParams.width = (screenWidth*0.90).toInt()

        // Устанавливаем максимальную высоту на 90% от высоты экрана
        val maxHeight = (screenHeight * 0.90).toInt()
        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            val params = binding.root.layoutParams
            if (binding.root.height > maxHeight) {
                params.height = maxHeight
                binding.root.layoutParams = params
            }
        }

        val formattedText:String = context.getString(textResource)
        binding.text.setText(Html.fromHtml(formattedText, Html.FROM_HTML_MODE_COMPACT))

        setListeners()
    }


    /**
     * функция установит слушатели на все кнопки диалогового окна
     */
    private fun setListeners() {
        binding.closeButton.setOnClickListener { this@PopupInfo.dismiss() }
    }
}