package com.example.ze_adminandroid.screens.editWord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ze_adminandroid.repositories.WordRepository
import kotlinx.coroutines.launch

class EditWordViewModel : ViewModel() {
    private val wordRepository = WordRepository.instance

    val namesTopics: MutableList<String> = mutableListOf()

    init {
        fillNames()
    }

    private fun fillNames() {
        //слова из базы данных
        viewModelScope.launch {
            wordRepository.getFlowAllEditedWords().collect{
                it.forEach {
                    if (!namesTopics.contains(it.topic)){
                        namesTopics.add(it.topic)
                    }
                }
                println("размер списка: "+ namesTopics.size)
            }
        }
        //словарь пришедший из сети
        viewModelScope.launch {
            wordRepository.getAllWords().collect{
                it.forEach {
                    if (!namesTopics.contains(it.topic)){
                        namesTopics.add(it.topic)
                    }
                }
                println("размер списка: "+ namesTopics.size)
            }
        }
    }
}