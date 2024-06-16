package com.example.zubrilkaenglish.screens.catalogCards.fragments

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import com.example.zubrilkaenglish.databinding.PopupWordcardBinding
import com.example.zubrilkaenglish.events.CardEvent
import com.example.zubrilkaenglish.events.CrEvEnum
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.screens.catalogCards.CatalogCardsViewModel
import com.example.zubrilkaenglish.utils.StatProgress
import org.greenrobot.eventbus.EventBus

class PopupWordCard(
    context: Context,
    viewModel: CatalogCardsViewModel,
    wordCard: WordCard,
    position: Int
): Dialog(context) {
    val binding: PopupWordcardBinding = PopupWordcardBinding.inflate(layoutInflater)
    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //тоже не могу вспомнить
        val displayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        binding.root.layoutParams.width = (screenWidth*0.66).toInt()

        //расставляем тексты
        binding.foreignWord.text = wordCard.word.foreignWord
        binding.transcription.text = wordCard.word.transcription
        binding.translation.text = wordCard.word.translation
        binding.description.text = wordCard.word.description

        //пытаемся уменьшить количество кода
        val addToTrain = binding.addToTrain
        val markLearned = binding.markLearned
        val resetProgress = binding.resetProgress
        val deleteCard = binding.deleteCard

        //настраиваем отображение кнопок
        if(wordCard.progressWord!=null) {
            addToTrain.isEnabled = false
            addToTrain.visibility = View.GONE

            if(wordCard.progressWord?.statProgress == StatProgress.LEARNED.value){
                markLearned.isEnabled = false
                markLearned.visibility = View.GONE
            }

            if(wordCard.progressWord?.statProgress== StatProgress.NEW.value &&
                wordCard.progressWord?.numCorrAnsv==0){
                resetProgress.isEnabled = false
                resetProgress.visibility = View.GONE
            }

        }else{
            resetProgress.isEnabled = false
            resetProgress.visibility = View.GONE
            deleteCard.isEnabled = false
            deleteCard.visibility = View.GONE
        }

        //вешаем слушатели
        //в CardEvent добавляем позицию адаптера для дальнейшего обновления элемента адаптера а не всего списка адаптера
        if (addToTrain.isEnabled){
            addToTrain.setOnClickListener {
                EventBus.getDefault().post(CardEvent(CrEvEnum.ADD_WORD_TO_TRAINING,wordCard, mutableMapOf("positionAdapter" to position)))
                dismiss()
            }
        }
        if (markLearned.isEnabled){
            markLearned.setOnClickListener {
                EventBus.getDefault().post(CardEvent(CrEvEnum.SET_AS_LEARNED,wordCard, mutableMapOf("positionAdapter" to position)))
                dismiss()
            }
        }
        if (resetProgress.isEnabled){
            resetProgress.setOnClickListener {
                EventBus.getDefault().post(CardEvent(CrEvEnum.RESET_PROGRESS,wordCard, mutableMapOf("positionAdapter" to position)))
                dismiss()
            }
        }
        if (deleteCard.isEnabled){
            deleteCard.setOnClickListener {
                EventBus.getDefault().post(CardEvent(CrEvEnum.DELETE_CARD,wordCard, mutableMapOf("positionAdapter" to position)))
                dismiss()
            }
        }

    }
}