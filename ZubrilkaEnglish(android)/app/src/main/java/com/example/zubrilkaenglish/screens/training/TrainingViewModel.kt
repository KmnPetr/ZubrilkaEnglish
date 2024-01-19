package com.example.zubrilkaenglish.screens.training

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zubrilkaenglish.models.ICard
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.repositories.CardsRepository
import com.example.zubrilkaenglish.repositories.Repository
import kotlinx.coroutines.launch

class TrainingViewModel : ViewModel() {

    private val repository = Repository()
    private val cardsRepository = CardsRepository.instance

    private val listWordsCards:MutableLiveData<List<WordCard>> = MutableLiveData()

    private val listForTreining : MutableLiveData<ArrayList<ICard>?> = MutableLiveData()

    var countWordCards: Int = 0

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
            if (it.progressWord?.wordId==wordId){
                var numCorAnsv = it.progressWord?.numCorrAnsv
                numCorAnsv = numCorAnsv!! + 1
                it.progressWord?.numCorrAnsv = numCorAnsv
                viewModelScope.launch {
                    repository.updateProgressWord(it.progressWord!!)
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
            if (it.progressWord?.wordId==wordId){
                it.progressWord?.numCorrAnsv = 0
                viewModelScope.launch {
                    repository.updateProgressWord(it.progressWord!!)
                }
                return it
            }
        }
        return null
    }

    fun updateWordCard(wordCard: WordCard) {
        viewModelScope.launch {
            repository.updateProgressWord(wordCard.progressWord!!)
        }

        listWordsCards.value?.forEach {
            if (it.progressWord?.wordId==wordCard.progressWord?.wordId){
                it.progressWord = wordCard.progressWord
            }
        }

    }

    /**
     * функция вызывается на желание юзерa, пометить карточку выученной
     */
    fun setCardAsLearned(wordCard: WordCard) {
        viewModelScope.launch {
            val wordCard: WordCard? = cardsRepository.setCardAsLearned(wordCard)

            if (wordCard!=null) changeWordCardInList(wordCard)
        }
    }

    /**
     * функция изменит один элемент на новый
     */
    private fun changeWordCardInList(wordCard: WordCard) {

        val newList : ArrayList<ICard>? = listForTreining.value

        if (newList != null){
            newList.forEachIndexed{ index, it ->
                if (it is WordCard && it.word.id == wordCard.word.id){
                    newList[index] = wordCard
                }
            }
            listForTreining.value = newList
        }
    }

    /**
     * запросит у репозитория список карточек для изучения
     */
    fun getListForTreining(): MutableLiveData<ArrayList<ICard>?> {

        if (listForTreining.value==null){
            viewModelScope.launch {
                val newList: ArrayList<ICard> = cardsRepository.getListForTreining()

                newList.forEach {
                    //надо както посчитать количество именно слов среди других неучебных карточек
                    if (it is WordCard) countWordCards++
                }
                listForTreining.value = newList
            }
        }
        return listForTreining
    }

}