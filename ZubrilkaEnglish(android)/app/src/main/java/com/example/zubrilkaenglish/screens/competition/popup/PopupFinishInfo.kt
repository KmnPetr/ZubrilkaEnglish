package com.example.zubrilkaenglish.screens.training.popup

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager
import com.example.zubrilkaenglish.databinding.PopupFinishInfoCompetitionBinding
import com.example.zubrilkaenglish.events.CmpEvEnum
import com.example.zubrilkaenglish.events.CompetitionEvent
import com.example.zubrilkaenglish.events.NfEvEnum
import com.example.zubrilkaenglish.events.NotificationEvent
import com.example.zubrilkaenglish.models.socketDto.FinishInfo
import com.example.zubrilkaenglish.screens.competition.CompetitionViewModel
import org.greenrobot.eventbus.EventBus

/**
 * в обязанности класса входит
 * показ всплывающего popup диалогового окошка
 * с различной инвормацией о завершении поединка между игроками
 */
class PopupFinishInfo(
    context: Context,
    finishInfo: FinishInfo,
    viewModel: CompetitionViewModel
) : Dialog(context) {
    private var binding = PopupFinishInfoCompetitionBinding.inflate(layoutInflater)

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        setContentView(binding.root)
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
        binding.buttonBack.setOnClickListener {
            this.dismiss()
            EventBus.getDefault().post(CompetitionEvent(CmpEvEnum.CLOSE_SESSION))
            EventBus.getDefault().post(NotificationEvent("",NfEvEnum.GO_TO_UPSTACK))
        }
        binding.buttonNextRound.setOnClickListener {

        }
    }
}