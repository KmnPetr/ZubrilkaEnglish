package com.example.zubrilkaenglish.screens.wordDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zubrilkaenglish.models.Word
import com.example.zubrilkaenglish.repositories.Repository
import kotlinx.coroutines.launch

class WordDetailsViewModel : ViewModel() {

    val repository = Repository()

    val idWord: MutableLiveData<Int> = MutableLiveData()
    private val wordDetails: MutableLiveData<Word> = MutableLiveData()

    /**
     * если wordDetails пуст
     * достанет обьект Word из БД
     */
    fun getWordDetails(): MutableLiveData<Word> {
        if (wordDetails.value==null|| wordDetails.value!!.id!=idWord.value){
            viewModelScope.launch {
                wordDetails.value = idWord.value?.let { repository.getWordByIdFromDB(it) }
            }
        }
        return wordDetails
    }

    /**
     * добавит Word, находящийся на экране,
     * в изучаемые слова
     */
    fun addWordToTraining(){
        viewModelScope.launch {
            repository.addWordToTraining(wordDetails.value?.id)
        }
    }
}