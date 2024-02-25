package com.example.zubrilkaenglish.screens.training.popup

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import com.example.zubrilkaenglish.databinding.PopupDialogSleepCardBinding
import com.example.zubrilkaenglish.events.CardEvent
import com.example.zubrilkaenglish.events.CrEvEnum
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.utils.StatProgress
import org.greenrobot.eventbus.EventBus

class PopupDialog(
    context: Context,
    wordCard: WordCard
) : Dialog(context) {
    private val binding = PopupDialogSleepCardBinding.inflate(layoutInflater)
    init {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        showPopUpDialog(wordCard)
    }

    /**
     * функция создаст popUp окошко
     */
    private fun showPopUpDialog(wordCard: WordCard) {

        binding.textDialog.text = "Кажется вы уже запомнили эту карточку. Рекомендуем вам повторить ее спустя некоторое время. Карточка уснет на "+binding.slider.value.toInt().toString()+ " дня(дней)."
        binding.slider.addOnChangeListener { slider, value, fromUser ->
            binding.textDialog.text = "Кажется вы уже запомнили эту карточку. Рекомендуем вам повторить ее спустя некоторое время. Карточка уснет на "+value.toInt().toString()+ " дня(дней)."
        }
        //Настройка слайдера
        when(wordCard.progressWord?.statProgress){
            StatProgress.NEW.value ->{
                binding.slider.valueTo = 10F
                binding.slider.value = 5F
            }
            StatProgress.PARTIALLY_LEARNED.value ->{
                binding.slider.valueTo = 18F
                binding.slider.value = 9F
            }
            StatProgress.ALMOST_LEARNED.value ->{
                binding.slider.value = 0F
                binding.slider.visibility = View.GONE
                binding.textDialog.text = "Кажется вы уже запомнили эту карточку. Нажав \"OK\", вы перенесете эту карточку в группу \"изученные\"."
            }
        }

        binding.btnYes.setOnClickListener {
            //отправим желание пользователя на усыпление карточки
            EventBus.getDefault().post(
                CardEvent(
                CrEvEnum.INTENT_SLEEP,
                wordCard,
                mapOf("countDay" to binding.slider.value.toInt())
            )
            )
            dismiss()
        }
        binding.btnCansel.setOnClickListener {
            //просто уведомим view чтобы она перелистнула пейджер
            //по сути изменений нет
            EventBus.getDefault().post(
                CardEvent(CrEvEnum.CARD_CHANGED, wordCard)
            )
            dismiss()
        }
    }
}