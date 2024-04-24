package com.example.zubrilkaenglish.screens.memo

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import androidx.core.content.ContextCompat
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.PopupCreateMemoBinding
import com.example.zubrilkaenglish.events.MemoEvent
import com.example.zubrilkaenglish.events.MmEvEnum
import com.example.zubrilkaenglish.models.DayOfWeek
import com.example.zubrilkaenglish.models.Memo
import org.greenrobot.eventbus.EventBus

/**
 * содержит логику по созданию нового Memo
 */
class PopupCreateMemo(context: Context) : Dialog(context) {
    private var binding = PopupCreateMemoBinding.inflate(layoutInflater)

    val daysOfWeek: MutableList<DayOfWeek> = mutableListOf(DayOfWeek.DAILY)
    val GREEN = Color.parseColor("#C8E6C9")
    val DisableColor = ContextCompat.getColor(context, R.color.backDayOfWeakDisable)
    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.timePicker.setIs24HourView(true)
        binding.buttonDAILY.setBackgroundColor(GREEN)

        setListeners()
    }

    /**
     * функция установит слушатели на все кнопки диалогового окна
     */
    private fun setListeners() {
        binding.buttonAdd.setOnClickListener {
            createNewMemo()
            this.dismiss()
        }
        binding.buttonCancel.setOnClickListener { this.dismiss() }

        binding.buttonDAILY.setOnClickListener { changeDayOfWeek(DayOfWeek.DAILY) }
        binding.buttonMONDAY.setOnClickListener { changeDayOfWeek(DayOfWeek.MONDAY) }
        binding.buttonTUESDAY.setOnClickListener { changeDayOfWeek(DayOfWeek.TUESDAY) }
        binding.buttonWEDNESDAY.setOnClickListener { changeDayOfWeek(DayOfWeek.WEDNESDAY) }
        binding.buttonTHURSDAY.setOnClickListener { changeDayOfWeek(DayOfWeek.THURSDAY) }
        binding.buttonFRIDAY.setOnClickListener { changeDayOfWeek(DayOfWeek.FRIDAY) }
        binding.buttonSATURDAY.setOnClickListener { changeDayOfWeek(DayOfWeek.SATURDAY) }
        binding.buttonSUNDAY.setOnClickListener { changeDayOfWeek(DayOfWeek.SUNDAY) }
    }


    /**
     * функция изменит набор дней недели в списке и перерисует фон кнопок
     */
    private fun changeDayOfWeek(day: DayOfWeek) {
        if (day == DayOfWeek.DAILY){
            daysOfWeek.clear()
            daysOfWeek.add(DayOfWeek.DAILY)
        } else {
            if(daysOfWeek.contains(day)){
                daysOfWeek.remove(day)
                if (daysOfWeek.isEmpty()){
                    daysOfWeek.add(DayOfWeek.DAILY)
                }
            }else {
                daysOfWeek.add(day)
                daysOfWeek.remove(DayOfWeek.DAILY)
                if (daysOfWeek.size == 7){
                    daysOfWeek.clear()
                    daysOfWeek.add(DayOfWeek.DAILY)
                }
            }
        }
        redrawButtons()
    }

    //перерисует фон кнопок
    private fun redrawButtons() {
        if (daysOfWeek.contains(DayOfWeek.DAILY)) binding.buttonDAILY.setBackgroundColor(GREEN)
        else binding.buttonDAILY.setBackgroundColor(DisableColor)
        if (daysOfWeek.contains(DayOfWeek.MONDAY)) binding.buttonMONDAY.setBackgroundColor(GREEN)
        else binding.buttonMONDAY.setBackgroundColor(DisableColor)
        if (daysOfWeek.contains(DayOfWeek.TUESDAY)) binding.buttonTUESDAY.setBackgroundColor(GREEN)
        else binding.buttonTUESDAY.setBackgroundColor(DisableColor)
        if (daysOfWeek.contains(DayOfWeek.WEDNESDAY)) binding.buttonWEDNESDAY.setBackgroundColor(GREEN)
        else binding.buttonWEDNESDAY.setBackgroundColor(DisableColor)
        if (daysOfWeek.contains(DayOfWeek.THURSDAY)) binding.buttonTHURSDAY.setBackgroundColor(GREEN)
        else binding.buttonTHURSDAY.setBackgroundColor(DisableColor)
        if (daysOfWeek.contains(DayOfWeek.FRIDAY)) binding.buttonFRIDAY.setBackgroundColor(GREEN)
        else binding.buttonFRIDAY.setBackgroundColor(DisableColor)
        if (daysOfWeek.contains(DayOfWeek.SATURDAY)) binding.buttonSATURDAY.setBackgroundColor(GREEN)
        else binding.buttonSATURDAY.setBackgroundColor(DisableColor)
        if (daysOfWeek.contains(DayOfWeek.SUNDAY)) binding.buttonSUNDAY.setBackgroundColor(GREEN)
        else binding.buttonSUNDAY.setBackgroundColor(DisableColor)
    }

    private fun createNewMemo() {
        val newMemo = Memo(
            0,
            binding.timePicker.hour,
            binding.timePicker.minute,
            binding.note.text.toString(),
            daysOfWeek
            )
        EventBus.getDefault().post(MemoEvent(MmEvEnum.CREATE_MEMO,newMemo))
    }
}