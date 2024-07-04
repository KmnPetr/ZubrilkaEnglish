package com.example.zubrilkaenglish.screens.competition.popup

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.zubrilkaenglish.databinding.PopupSearchOpponentBinding
import com.example.zubrilkaenglish.events.CmpEvEnum
import com.example.zubrilkaenglish.events.CompetitionEvent
import com.example.zubrilkaenglish.models.socketDto.StatusInfo
import com.example.zubrilkaenglish.models.socketDto.StatusPlayer
import com.example.zubrilkaenglish.screens.competition.CompetitionViewModel
import org.greenrobot.eventbus.EventBus

/**
 * в обязанности класса входит
 * показ всплывающего popup диалогового окошка
 * с различной инвормацией о начальном поиске соперника перед поединком
 */
class PopupSearchOpponent(
    context: FragmentActivity,
    private val viewModel: CompetitionViewModel,
    private val viewLifecycleOwner: LifecycleOwner
) : Dialog(context) {
    private var binding = PopupSearchOpponentBinding.inflate(layoutInflater)

    private lateinit var observer:Observer<StatusInfo?>


    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        observer = setObserver()

        setListeners()
    }
    private fun setObserver(): Observer<StatusInfo?> {
        return Observer<StatusInfo?> { statusInfo ->
            if (statusInfo!=null){
                if (statusInfo.statusPlayer  == StatusPlayer.BUSY){
                    binding.buttonNextRound.visibility = View.VISIBLE
                    binding.buttonNextRound.isEnabled = true
                } else if (statusInfo.statusPlayer  == StatusPlayer.WAITING){
                    binding.buttonNextRound.visibility = View.GONE
                    binding.buttonNextRound.isEnabled = false
                } else if (statusInfo.statusPlayer == StatusPlayer.PLAYING){
                    this.dismiss()
                }
            }
        }
    }

    /**
     * функция установит слушатели на все кнопки диалогового окна
     */
    private fun setListeners() {
        binding.buttonNextRound.setOnClickListener {
            EventBus.getDefault().post(CompetitionEvent(CmpEvEnum.SET_WAITING_STATUS))
        }
    }

    override fun onStart() {
        // Подписка на изменения MutableLiveData
        viewModel.statusInfo.observe(viewLifecycleOwner, observer)
        super.onStart()
    }

    override fun onStop() {
        println("PopupSearchOpponent.onStop")
        // Отписка от изменений MutableLiveData
        viewModel.statusInfo.removeObserver(observer)
        super.onStop()
    }

    override fun show() {
        if (!isShowing){
            super.show() //так как эта функция вызывается несколько раз на каждое изменение viewModel.statusInfo
        }
    }
}