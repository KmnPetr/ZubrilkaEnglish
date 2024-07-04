package com.example.zubrilkaenglish.services.apiNotification

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.PopupLimitActiveCardsBinding
import com.example.zubrilkaenglish.events.CardEvent
import com.example.zubrilkaenglish.events.NotificationEvent
import com.example.zubrilkaenglish.events.iEvent
import com.example.zubrilkaenglish.screens.MainActivity
import com.example.zubrilkaenglish.services.ads.YandexAds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * покажет всплывающее popup окошка с уведомлением о лимите активных карточек и с предложением просмотреть рекламу
 */
class PopupLimitActiveCards(event: NotificationEvent,
                            activity: MainActivity
) : Dialog(activity) {
    private var binding = PopupLimitActiveCardsBinding.inflate(layoutInflater)

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

        binding.message.text = event.message

        callDismiss()
        binding.adButton.setOnClickListener {
            clickAd(activity,event.properties["failedEvent"] as CardEvent)
        }
    }
    //клик по рекламе
    private fun <T : Enum<T>, E : iEvent<T>> clickAd(activity: MainActivity, failedEvent: E) {
        YandexAds.instanse.showRewardedAd(activity,failedEvent)
    }

    private fun callDismiss() {
        GlobalScope.launch {
            delay(5000)
            withContext(Dispatchers.Main){
                dismiss()
            }
        }
    }
}