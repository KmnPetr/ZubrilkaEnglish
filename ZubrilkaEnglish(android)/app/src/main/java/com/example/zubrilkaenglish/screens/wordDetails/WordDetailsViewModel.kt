package com.example.zubrilkaenglish.screens.wordDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zubrilkaenglish.models.Word
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.repositories.Repository
import kotlinx.coroutines.launch

class WordDetailsViewModel : ViewModel() {

    private val repository = Repository()

    val idWord: MutableLiveData<Int> = MutableLiveData()
    private val wordDetails: MutableLiveData<Word> = MutableLiveData()
    private val wordCard: MutableLiveData<WordCard> = MutableLiveData()

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

    /**
     * функция достанет Word из репозитория в виде WordCard вместе с прогрессом пользователя по этой карточке
     */
    fun getWordCard(): MutableLiveData<WordCard> {
        viewModelScope.launch {
            wordCard.value = repository.getWordCardById(idWord.value)
        }
        return wordCard
    }
}