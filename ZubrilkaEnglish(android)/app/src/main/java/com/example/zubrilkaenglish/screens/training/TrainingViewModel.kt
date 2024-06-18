package com.example.zubrilkaenglish.screens.training

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.repositories.CardsRepository
import kotlinx.coroutines.launch

class TrainingViewModel : ViewModel() {

    private val cardsRepository = CardsRepository.instance

    private val listForTreining : MutableLiveData<ArrayList<ICard>?> = MutableLiveData()

    //количество именно учебных карточек WordCard
    //так как в списке могут содержаться и неучебные карточки
    var countWordCards: Int = 0

    //служебная переменная используемая для защиты от автоперелистывания во время скролла пальцем,
    // при значении 0 планируемое перелистывание будет отменено
    var userScrolls: Int = 0

    //чтобы не показывать рекламу несколько раз
    var yandexAdWasShown = false


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

    /**
     * запросит список заново
     * при рестарте обучения
     */
    fun overwriteList() {
        viewModelScope.launch {
            val newList: ArrayList<ICard> = cardsRepository.getListForTreining()

            countWordCards = 0
            newList.forEach {
                //надо както посчитать количество именно слов среди других неучебных карточек неактуально уже но посчитать надо все равно
                if (it is WordCard) countWordCards++
            }
            listForTreining.value = newList
        }
    }
}