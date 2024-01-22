package com.example.zubrilkaenglish.repositories

import com.example.zubrilkaenglish.eventBus.events.CardEvent
import com.example.zubrilkaenglish.eventBus.events.iEvent
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.repositories.room.RoomService
import com.example.zubrilkaenglish.utils.SIM_FORM_DATE
import com.example.zubrilkaenglish.utils.StatProgress
import com.example.zubrilkaenglish.models.ICard
import com.example.zubrilkaenglish.models.NewsCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.text.SimpleDateFormat
import java.util.Calendar
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

    init {
        EventBus.getDefault().register(this)
    }

    /**
     * метод используется библиотечкой EventBus
     * для прослушивания запросов от различных view
     */
    @Subscribe
    fun subscribeOnCardEvent(event: CardEvent){
        when(event.typeEvent){
            "intent_sleep" -> setSleepCard(event)
            "increase_progress" -> increaseProgressCard(event)
            "reset_numCorrAnsv" -> resetNumCorrAnsv(event)
            "set_as_learned" -> setCardAsLearned(event)
        }
    }

    /**
     * усыпит карточку
     */
    private fun setSleepCard(event: CardEvent) {
        //установим новый статус карточки
        when(event.wordCard.progressWord?.statProgress){
            StatProgress.NEW.value ->{
                event.wordCard.progressWord?.statProgress = StatProgress.PARTIALLY_LEARNED.value
            }
            StatProgress.PARTIALLY_LEARNED.value ->{
                event.wordCard.progressWord?.statProgress = StatProgress.ALMOST_LEARNED.value
            }
            StatProgress.ALMOST_LEARNED.value ->{
                event.wordCard.progressWord?.statProgress = StatProgress.LEARNED.value
            }
        }
        //сбросим колич. правильных ответов
        event.wordCard.progressWord?.numCorrAnsv = 0

        //вычисляем дату времени, до которой должна заснуть карточка
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DAY_OF_MONTH, event.properties!!.get("countDay") as Int)
        val newDateString = SimpleDateFormat(SIM_FORM_DATE).format(calendar.time)
        println("Новая дата: $newDateString")
        event.wordCard.progressWord?.sleepTime = newDateString


        //обновим данные в репозитории
        GlobalScope.launch(Dispatchers.Default) {
            event.wordCard.progressWord?.let { roomService.updateProgressWord(it) }
        }
        //уведомим view
        EventBus.getDefault().post(CardEvent(
            "card_changed",
            event.wordCard,
            event.properties)
        )
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
    fun increaseProgressCard(event: CardEvent) {

        //увеличиваем значение на 1
        if (event.wordCard.progressWord?.numCorrAnsv!=null) event.wordCard.progressWord!!.numCorrAnsv+=1

        println("new value: "+ event.wordCard.progressWord?.numCorrAnsv)

        //обновим данные в репозитории
        GlobalScope.launch(Dispatchers.Default) {
            event.wordCard.progressWord?.let { roomService.updateProgressWord(it) }
        }

        //уведомим view
        EventBus.getDefault().post(CardEvent(
            "card_changed",
            event.wordCard,
            event.properties))

        //проверим, не пора ли карточке спать
        suggestCardSleep(event.wordCard)
    }

    /**
     * в случае если юзер ответил правильно достаточное количество раз на карточку
     * функция предложит ему усыпить карточку
     */
    private fun suggestCardSleep(wordCard: WordCard) {
        if (wordCard.progressWord?.numCorrAnsv!! >= 3){
            EventBus.getDefault().post(CardEvent(
                "sleep_event",
                wordCard)
            )
        }
    }

    /**
     * сбрасывает поле numCorrAnsv до 0
     * сохраняет новое значение в БД
     * уведомляет eventBus об изменении карточки
     */
    private fun resetNumCorrAnsv(event: CardEvent){

        //сброс значения
        event.wordCard.progressWord?.numCorrAnsv = 0

        //обновим данные в репозитории
        GlobalScope.launch(Dispatchers.Default) {
            event.wordCard.progressWord?.let { roomService.updateProgressWord(it) }
        }

        EventBus.getDefault().post(CardEvent(
            "card_changed",
            event.wordCard,
            event.properties))
    }

    /**
     * функция установит прогресс карточки как выученная
     * сохранит изменения в BD
     * уведомит cardEventBus об измении карточки
     */
    private fun setCardAsLearned(event: CardEvent) {
        event.wordCard.progressWord?.statProgress = StatProgress.LEARNED.value
        event.wordCard.progressWord?.numCorrAnsv = 0
        event.wordCard.progressWord?.sleepTime = SimpleDateFormat(SIM_FORM_DATE).format(Date())
        //обновим данные в репозитории
        GlobalScope.launch(Dispatchers.Default) {
            event.wordCard.progressWord?.let { roomService.updateProgressWord(it) }
        }

        EventBus.getDefault().post(CardEvent(
            "card_changed",
            event.wordCard,
            event.properties))
    }
}