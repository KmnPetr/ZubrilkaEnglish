package com.example.zubrilkaenglish.screens.catalogCards.fragments

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Window
import com.example.zubrilkaenglish.databinding.PopupWordsOptionsBinding
import com.example.zubrilkaenglish.events.PrEvEnum
import com.example.zubrilkaenglish.events.PropEvent
import com.example.zubrilkaenglish.models.PropModel
import com.example.zubrilkaenglish.repositories.room.PropKey
import com.example.zubrilkaenglish.screens.catalogCards.CatalogCardsViewModel
import com.example.zubrilkaenglish.utils.LOG
import org.greenrobot.eventbus.EventBus

class PopupWordsOptions(
    context: Context,
    viewModel_CC: CatalogCardsViewModel
): Dialog(context) {
    val binding: PopupWordsOptionsBinding = PopupWordsOptionsBinding.inflate(layoutInflater)
    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val displayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        binding.root.layoutParams.width = (screenWidth*0.66).toInt()

        binding.hideLearned.isChecked = viewModel_CC.filterProperties.value?.get(PropKey.catalogFilter_hideLearned.key).toBoolean()
        binding.hideSleepingAndActive.isChecked = viewModel_CC.filterProperties.value?.get(PropKey.catalogFilter_hideSleepingAndActive.key).toBoolean()

        binding.hideLearned.setOnClickListener {
            Log.d(LOG,"${binding.hideLearned.isChecked}")
            EventBus.getDefault().post(
                PropEvent(
                    PrEvEnum.UPDATE_REQUEST,
                    PropModel(
                        PropKey.catalogFilter_hideLearned.key,
                        binding.hideLearned.isChecked.toString())))
        }

        binding.hideSleepingAndActive.setOnClickListener {
            Log.d(LOG,"${binding.hideSleepingAndActive.isChecked}")
            EventBus.getDefault().post(
                PropEvent(
                    PrEvEnum.UPDATE_REQUEST,
                    PropModel(
                        PropKey.catalogFilter_hideSleepingAndActive.key,
                        binding.hideSleepingAndActive.isChecked.toString())))
        }
    }
}