package com.example.ze_adminandroid.screens.editWord.popupTopics

import android.app.Dialog
import android.content.Context
import android.view.Window
import com.example.ze_adminandroid.databinding.PopUpTopicsBinding

/**
 * класс покажет попап окошко со списком уже существующих topic в словаре и в БД
 */
class PopUpTopics(
    context: Context,
    namesTopics: List<String>,
    private val setTopic: (String) -> Unit
): Dialog(context) {
    val binding: PopUpTopicsBinding
    val adapter: TopicsAdapter

    init {
        binding = PopUpTopicsBinding.inflate(layoutInflater)
        adapter = TopicsAdapter(::onClickTopic)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        setContentView(binding.root)

        val displayMetrics = context.resources.displayMetrics
        binding.root.layoutParams.width = (displayMetrics.widthPixels*0.66).toInt()
        binding.root.layoutParams.height = (displayMetrics.heightPixels * 0.66).toInt()

        binding.recyclerView.adapter = adapter

        adapter.setList(namesTopics)
    }
    private fun onClickTopic(topicName: String){
        setTopic(topicName)
        this.dismiss()
    }
}