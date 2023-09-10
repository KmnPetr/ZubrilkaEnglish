package com.example.zubrilkaenglish.screens.training

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.repositories.Repository
import kotlinx.coroutines.launch

class TrainingViewModel : ViewModel() {

    private val repository = Repository()

    private val listWordsCards:MutableLiveData<List<WordCard>> = MutableLiveData()

    //служебная переменная используемая для защиты от автоперелистывания во время скролла пальцем
    var userScrolls: Int = 0

    fun getWordsCards(): MutableLiveData<List<WordCard>> {
        if (listWordsCards.value==null){
            viewModelScope.launch {
                listWordsCards.value = repository.getListWordsCards()
            }
        }
        return listWordsCards
    }

    /**
     * функция увеличит количество правильных ответов на 1
     */
    fun plusCorAnsv(wordId: Int): WordCard? {
        listWordsCards.value?.forEach {
            if (it.progressWord.wordId==wordId){
                var numCorAnsv = it.progressWord.numCorrAnsv
                numCorAnsv++
                it.progressWord.numCorrAnsv = numCorAnsv
                viewModelScope.launch {
                    repository.updateProgressWord(it.progressWord)
                }
                return it
            }
        }
        return null
    }

    /**
     * функция сбросит значение numCorrAnsv
     */
    fun resetCorAnsv(wordId: Int): WordCard? {
        listWordsCards.value?.forEach {
            if (it.progressWord.wordId==wordId){
                it.progressWord.numCorrAnsv = 0
                viewModelScope.launch {
                    repository.updateProgressWord(it.progressWord)
                }
                return it
            }
        }
        return null
    }

    fun updateWordCard(wordCard: WordCard) {
        viewModelScope.launch {
            repository.updateProgressWord(wordCard.progressWord)
        }

        listWordsCards.value?.forEach {
            if (it.progressWord.wordId==wordCard.progressWord.wordId){
                it.progressWord = wordCard.progressWord
            }
        }

    }
}