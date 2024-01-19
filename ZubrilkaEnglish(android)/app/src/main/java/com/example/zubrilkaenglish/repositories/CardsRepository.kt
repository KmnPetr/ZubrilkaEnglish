package com.example.zubrilkaenglish.repositories

import com.example.zubrilkaenglish.events.CardEventBus
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.repositories.room.RoomService
import com.example.zubrilkaenglish.utils.SIM_FORM_DATE
import com.example.zubrilkaenglish.utils.StatProgress
import com.example.zubrilkaenglish.models.ICard
import com.example.zubrilkaenglish.models.NewsCard
import java.text.SimpleDateFormat
import java.util.Date

/**
 * этот класс будет сосредоточен на обработке логики связанной с карточками
 * постепенно вся логика с картами должна переноситься сюда
 */
class CardsRepository private constructor(){
    companion object{
        val instance: CardsRepository by lazy { CardsRepository() }
    }
    private val roomService = RoomService()
    private var cardEventBus: CardEventBus

    init {
        subscribeAnCardsIntents()
        cardEventBus = CardEventBus.instance
    }

    /**
     * подпишется на различные пожелания других классов об изменениях карточек
     */
    private fun subscribeAnCardsIntents() {
        cardEventBus.subscribeAnEvent("intention_increase_progress_card")?.observe(this)
    }

    /**
     * функция установит прогресс карточки как выученная
     * и вернет результатом измененный обьект WordCard
     */
    suspend fun setCardAsLearned(wordCard: WordCard): WordCard? {
        val progressWord = roomService.getWordCardById(wordCard.word.id).progressWord
        if (progressWord != null) {
            progressWord.statProgress = StatProgress.LEARNED.value
            progressWord.numCorrAnsv = 0
            progressWord.sleepTime = SimpleDateFormat(SIM_FORM_DATE).format(Date())
            roomService.updateProgressWord(progressWord)

            return roomService.getWordCardById(wordCard.word.id)
        }else {
            return null
        }
    }

    /**
     * выдаст список для изучения
     */
    suspend fun getListForTreining(): ArrayList<ICard> {
        val listAllCards: List<WordCard>? = roomService.getListWordsCards()
        val listForTreining: ArrayList<ICard> = ArrayList()

        listAllCards?.forEach { it ->
            if (it.progressWord?.statProgress!=StatProgress.LEARNED.value&&compareDate(it.progressWord?.sleepTime)){
                listForTreining.add(it)
//                countWordCards++

            }
        }

        listForTreining.shuffle()
        listForTreining.add(NewsCard("news will be here"))

        return listForTreining
    }

    /**
     * функция вернет false, если входящая в параметры дата еще не наступила
     */
    private fun compareDate(sleepTime: String?): Boolean{
        try {
            if (sleepTime==null){
                return true
            }else if(SimpleDateFormat(SIM_FORM_DATE).parse(sleepTime).before(Date())){
                return true
            }
        } catch (e: Exception) {
            return false
        }
        return false
    }

    /**
     * обрабатывает запрос на увеличение прогресса по карточке
     */
    fun increaseProgressCard(wordCard: WordCard): WordCard? {

    }
}