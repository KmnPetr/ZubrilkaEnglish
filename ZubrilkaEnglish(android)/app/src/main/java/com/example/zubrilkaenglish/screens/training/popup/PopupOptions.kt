package com.example.zubrilkaenglish.screens.training.popup

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Toast
import com.example.zubrilkaenglish.databinding.PopupOptionsTrainCardBinding
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.screens.training.TrainingViewModel
import com.example.zubrilkaenglish.utils.MyApplication

/**
 * в обязанности класса входит
 * показ всплывающего popup диалогового окошка с различными опциями
 * производимыми над карточкой во время ее изучения
 */
class PopupOptions(
    context: Context,
    viewModel: TrainingViewModel,
    wordCard: WordCard
) : Dialog(context) {
    private var binding = PopupOptionsTrainCardBinding.inflate(layoutInflater)

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setListeners(wordCard,viewModel)
    }

    /**
     * функция установит слушатели на все кнопки диалогового окна
     */
    private fun setListeners(wordCard: WordCard, viewModel: TrainingViewModel) {
        binding.markLearned.setOnClickListener {
            viewModel.setCardAsLearned(wordCard)
            Toast.makeText(MyApplication.context,"click markLearned",Toast.LENGTH_SHORT).show()
            dismiss()
        }
        binding.resetProgress.setOnClickListener {
            Toast.makeText(MyApplication.context,"click resetProgress",Toast.LENGTH_SHORT).show()
            dismiss()
        }
        binding.deleteCard.setOnClickListener {
            Toast.makeText(MyApplication.context,"click deleteCard",Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }


}