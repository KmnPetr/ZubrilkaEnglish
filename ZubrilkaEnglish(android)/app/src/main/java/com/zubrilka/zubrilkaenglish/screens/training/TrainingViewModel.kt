package com.zubrilka.zubrilkaenglish.screens.training

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zubrilka.zubrilkaenglish.models.WordCard
import com.zubrilka.zubrilkaenglish.repositories.CardsRepository
import com.zubrilka.zubrilkaenglish.repositories.PropRepository
import com.zubrilka.zubrilkaenglish.repositories.room.PropKey
import com.zubrilka.zubrilkaenglish.utils.LOG
import com.zubrilka.zubrilkaenglish.utils.defaultMode
import kotlinx.coroutines.launch

class TrainingViewModel : ViewModel() {

    private val cardsRepository = CardsRepository.instance
    private val propRepository = PropRepository.instance

    private val listForTreining : MutableLiveData<ArrayList<ICard>?> = MutableLiveData()

    val learningMode : MutableLiveData<Modes> = MutableLiveData(defaultMode)

    //количество именно учебных карточек WordCard
    //так как в списке могут содержаться и неучебные карточки
    var countWordCards: Int = 0

    //служебная переменная используемая для защиты от автоперелистывания во время скролла пальцем,
    // при значении 0 планируемое перелистывание будет отменено
    var userScrolls: Int = 0

    //чтобы не показывать рекламу несколько раз
    var yandexAdWasShown = false

    init {
        setupModes()
    }



    /**
     * некоторые настройки с режимом тренировки
     */
    private fun setupModes() {
        learningMode.value = propRepository.properties.value[PropKey.learningMode.key]?.let { Modes.valueOf(it) //сразу установим значение, чтобы лишний раз не перезагружать view адаптера
        }
        if (learningMode.value==null) learningMode.value = defaultMode //поставим а то иногда ошибки появляются
        viewModelScope.launch {
            learningMode.value = propRepository.getPropModelByKey(PropKey.learningMode.key)?.let { Modes.valueOf(it.value) }
            propRepository.properties.collect{
                if (it.containsKey(PropKey.learningMode.key)){
                    learningMode.value = it[PropKey.learningMode.key]?.let { Modes.valueOf(it) }
                }
                Log.d(LOG,"Новое значение ${it[PropKey.learningMode.key]}")
            }
        }
    }

    /**
     * запросит у репозитория список карточек для изучения
     */
    fun getListForTreining(): MutableLiveData<ArrayList<ICard>?> {
        if (listForTreining.value==null){
            viewModelScope.launch {
                val newList: ArrayList<ICard> = cardsRepository.getListForTreining(learningMode.value?: defaultMode)
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
            val newList: ArrayList<ICard> = cardsRepository.getListForTreining(learningMode.value!!)

            countWordCards = 0
            newList.forEach {
                //надо както посчитать количество именно слов среди других неучебных карточек неактуально уже но посчитать надо все равно
                if (it is WordCard) countWordCards++
            }
            listForTreining.value = newList
        }
    }
}