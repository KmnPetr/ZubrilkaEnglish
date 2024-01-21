package com.example.zubrilkaenglish.screens.training

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zubrilkaenglish.events.CardEventBus
import com.example.zubrilkaenglish.models.ICard
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.repositories.CardsRepository
import com.example.zubrilkaenglish.repositories.Repository
import kotlinx.coroutines.launch

class TrainingViewModel : ViewModel() {

    private val repository = Repository()
    private val cardsRepository = CardsRepository.instance
    private val cardEventBus = CardEventBus.instance

    private val listWordsCards:MutableLiveData<List<WordCard>> = MutableLiveData()

    private val listForTreining : MutableLiveData<ArrayList<ICard>?> = MutableLiveData()

    var countWordCards: Int = 0

    //служебная переменная используемая для защиты от автоперелистывания во время скролла пальцем
    var userScrolls: Int = 0

    init {
        subscribeAnCardsEvents()
    }

    /**
     * подпишется на различные события по карточкам
     */
    private fun subscribeAnCardsEvents() {
        //TODO тут пока ничего не надо, карточки и так обновляются ссылочно
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