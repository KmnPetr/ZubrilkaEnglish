package com.example.zubrilkaenglish.repositories

import com.example.zubrilkaenglish.events.CardEventBus
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.repositories.room.RoomService
import com.example.zubrilkaenglish.utils.SIM_FORM_DATE
import com.example.zubrilkaenglish.utils.StatProgress
import com.example.zubrilkaenglish.models.ICard
import com.example.zubrilkaenglish.models.NewsCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
    private val cardEventBus: CardEventBus = CardEventBus.instance

    init {
        subscribeAnCardsIntents()
    }

    /**
     * подпишется на различные пожелания других классов об изменениях карточек
     */
    private fun subscribeAnCardsIntents() {
        cardEventBus.subscribeAnEvent("intention_increase_progress_card")?.observeForever{wordCard->
            increaseProgressCard(wordCard)
        }
        cardEventBus.subscribeAnEvent("intention_reset_numCorrAnsv")?.observeForever { wordCard->
            resetNumCorrAnsv(wordCard)
        }
        cardEventBus.subscribeAnEvent("set_card_as_learned")?.observeForever { wordCard->
            setCardAsLearned(wordCard)
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
     * сохраняет новое значение в БД
     * уведомляет eventBus об изменении карточки
     */
    fun increaseProgressCard(wordCard: WordCard) {

        //увеличиваем значение на 1
        if (wordCard.progressWord?.numCorrAnsv!=null) wordCard.progressWord!!.numCorrAnsv+=1

        println("new value: "+wordCard.progressWord?.numCorrAnsv)

        //обновим данные в репозитории
        GlobalScope.launch(Dispatchers.Default) {
            wordCard.progressWord?.let { roomService.updateProgressWord(it) }
        }

        cardEventBus.publishEventCard("wordCard_has_changed" ,wordCard)

//        if (wordCard.progressWord?.numCorrAnsv!! >=3){
//            //обновляем значение numCorrAnsv в viewModel и в репозитории
//            wordCard.progressWord!!.numCorrAnsv = viewModel.plusCorAnsv(wordCard.progressWord!!.wordId)?.progressWord?.numCorrAnsv!!
//            adapter.notifyItemChanged(binding.viewPager2.currentItem)
//            //показываем окошко диалога
//            showPopUpDialog(wordCard)
//        }else{
//            //обновляем значение numCorrAnsv в viewModel и в репозитории
//            wordCard.progressWord!!.numCorrAnsv = viewModel.plusCorAnsv(wordCard.progressWord!!.wordId)?.progressWord?.numCorrAnsv!!
//            adapter.notifyItemChanged(binding.viewPager2.currentItem)
//            flippingCard()

    }

    /**
     * сбрасывает поле numCorrAnsv до 0
     * сохраняет новое значение в БД
     * уведомляет eventBus об изменении карточки
     */
    private fun resetNumCorrAnsv(wordCard: WordCard){

        //сброс значения
        wordCard.progressWord?.numCorrAnsv = 0

        //обновим данные в репозитории
        GlobalScope.launch(Dispatchers.Default) {
                wordCard.progressWord?.let { roomService.updateProgressWord(it) }
        }

        cardEventBus.publishEventCard("wordCard_has_changed" ,wordCard)
    }

    /**
     * функция установит прогресс карточки как выученная
     * сохранит изменения в BD
     * уведомит cardEventBus об измении карточки
     */
    private fun setCardAsLearned(wordCard: WordCard) {
        wordCard.progressWord?.statProgress = StatProgress.LEARNED.value
        wordCard.progressWord?.numCorrAnsv = 0
        wordCard.progressWord?.sleepTime = SimpleDateFormat(SIM_FORM_DATE).format(Date())
        //обновим данные в репозитории
        GlobalScope.launch(Dispatchers.Default) {
            wordCard.progressWord?.let { roomService.updateProgressWord(it) }
        }

        cardEventBus.publishEventCard("wordCard_has_changed" ,wordCard)
    }
}