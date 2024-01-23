package com.example.zubrilkaenglish.repositories

import android.database.sqlite.SQLiteConstraintException
import com.example.zubrilkaenglish.eventBus.events.CardEvent
import com.example.zubrilkaenglish.models.ICard
import com.example.zubrilkaenglish.models.NewsCard
import com.example.zubrilkaenglish.models.ProgressWord
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.repositories.room.RoomService
import com.example.zubrilkaenglish.utils.SIM_FORM_DATE
import com.example.zubrilkaenglish.utils.StatProgress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
            "intent_sleep" -> {
                event.wordCard = setSleepCard(event.wordCard,event.properties!!.get("countDay") as Int)
                notifyChangeCard(event)
            }
            "increase_progress" -> {
                event.wordCard = increaseProgressCard(event.wordCard)
                notifyChangeCard(event)
                //проверим, не пора ли карточке спать
                if (checkCardSleep(event.wordCard)) EventBus.getDefault().post(CardEvent("sleep_event", event.wordCard))
            }
            "reset_numCorrAnsv" -> {
                event.wordCard = resetNumCorrAnsv(event.wordCard)
                notifyChangeCard(event)
            }
            "set_as_learned" -> {
                GlobalScope.launch(Dispatchers.Default) {
                    event.wordCard = setCardAsLearned(event.wordCard)
                    withContext(Dispatchers.Main) {
                        notifyChangeCard(event)
                    }
                }
            }
            "add_word_to_training" -> {
                GlobalScope.launch(Dispatchers.Default) {
                    event.wordCard = addWordToTraining(event.wordCard)
                    withContext(Dispatchers.Main) {
                        notifyChangeCard(event)
                    }
                }
            }
            "reset_progress" -> {
                event.wordCard = resetProgressCard(event.wordCard)
                notifyChangeCard(event)
            }
            "delete_card" -> {
                event.wordCard = deleteProgressCard(event.wordCard)
                notifyChangeCard(event)
            }
        }
    }
    /**
     * удалит ProgressWord по id Word
     */
    fun deleteProgressCard(wordCard: WordCard):WordCard {
        //удалим progressWord из БД
        GlobalScope.launch(Dispatchers.Default) {
            roomService.deleteProgressByWordId(wordCard.word.id)
//        Toast.makeText(MyApplication.context,"Слово/фраза удалена из ваших карточек",Toast.LENGTH_SHORT).show()//TODO надо будет как нибудь поправить тосты а то они не очень вызываются из репозитория
        }
        //изменим progressWord для view без обращения к БД
        wordCard.progressWord = null

        return wordCard
    }

    /**
     * сбросит полностью учебный прогресс по карточке
     */
    private fun resetProgressCard(wordCard: WordCard): WordCard {
        //сбрасываем прогресс
        wordCard.progressWord?.numCorrAnsv = 0
        wordCard.progressWord?.statProgress = StatProgress.NEW.value
        wordCard.progressWord?.sleepTime = SimpleDateFormat(SIM_FORM_DATE).format(Date())
        //обновляем progressWord в БД
        GlobalScope.launch(Dispatchers.Default) {
            if (wordCard.progressWord != null) {
                roomService.updateProgressWord(wordCard.progressWord!!)
//                Toast.makeText(MyApplication.context,"Прогресс сброшен",Toast.LENGTH_SHORT).show() //TODO надо будет как нибудь поправить тосты а то они не очень вызываются из репозитория
            }/*else Toast.makeText(MyApplication.context,"Ошибка",Toast.LENGTH_SHORT).show()*/
        }
        return wordCard
    }

    /**
     * сохранит ProgressWord в БД
     * тем самым добавит новое слово/фразу в изучаемые
     */
    private suspend fun addWordToTraining(wordCard: WordCard): WordCard {
        val progressWord = ProgressWord(
            null,
            wordCard.word.id,
            0,
            StatProgress.NEW.value,
            SimpleDateFormat(SIM_FORM_DATE).format(Date())
        )
        try {
            roomService.addWordToTraining(progressWord)
//                Toast.makeText(MyApplication.context,"Слово/фраза добавлено(а) в изучаемые.", Toast.LENGTH_LONG).show() //TODO yflj xnj,s njcns gjrfpsdfkbcm
            val updatedWordCard = roomService.getWordCardById(wordCard.word.id)

            //обновляем progressWord во всех обьектах ссылочным образом
            wordCard.progressWord = updatedWordCard.progressWord
        }catch (e: SQLiteConstraintException){
            e.printStackTrace()
            //может выпасть эксепшен если попытаться добавить progressWord в БД второй раз
//                Toast.makeText(MyApplication.context,e.javaClass.name+"\n"+e.message, Toast.LENGTH_LONG).show() //TODO yflj xnj,s njcns gjrfpsdfkbcm
        }
        return wordCard
    }

    /**
     * усыпит карточку
     */
    private fun setSleepCard(wordCard: WordCard,countDay: Int): WordCard {
        //установим новый статус карточки
        when(wordCard.progressWord?.statProgress){
            StatProgress.NEW.value ->{
                wordCard.progressWord?.statProgress = StatProgress.PARTIALLY_LEARNED.value
            }
            StatProgress.PARTIALLY_LEARNED.value ->{
                wordCard.progressWord?.statProgress = StatProgress.ALMOST_LEARNED.value
            }
            StatProgress.ALMOST_LEARNED.value ->{
                wordCard.progressWord?.statProgress = StatProgress.LEARNED.value
            }
        }
        //сбросим колич. правильных ответов
        wordCard.progressWord?.numCorrAnsv = 0

        //вычисляем дату времени, до которой должна заснуть карточка
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DAY_OF_MONTH, countDay)
        val newDateString = SimpleDateFormat(SIM_FORM_DATE).format(calendar.time)
        wordCard.progressWord?.sleepTime = newDateString


        //обновим данные в репозитории
        GlobalScope.launch(Dispatchers.Default) {
            wordCard.progressWord?.let { roomService.updateProgressWord(it) }
        }
        return wordCard
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
    fun increaseProgressCard(wordCard: WordCard): WordCard {
        //увеличиваем значение на 1
        if (wordCard.progressWord?.numCorrAnsv!=null) wordCard.progressWord!!.numCorrAnsv+=1

        //обновим данные в репозитории
        GlobalScope.launch(Dispatchers.Default) {
            wordCard.progressWord?.let { roomService.updateProgressWord(it) }
        }
        return wordCard
    }

    /**
     * в случае если юзер ответил правильно достаточное количество раз на карточку
     * функция предложит ему усыпить карточку
     */
    private fun checkCardSleep(wordCard: WordCard): Boolean {
        return wordCard.progressWord?.numCorrAnsv!! >= 3
    }

    /**
     * сбрасывает поле numCorrAnsv до 0
     * сохраняет новое значение в БД
     * уведомляет eventBus об изменении карточки
     */
    private fun resetNumCorrAnsv(wordCard: WordCard): WordCard{
        //сброс значения
        wordCard.progressWord?.numCorrAnsv = 0

        //обновим данные в репозитории
        GlobalScope.launch(Dispatchers.Default) {
            wordCard.progressWord?.let { roomService.updateProgressWord(it) }
        }
        return wordCard
    }

    /**
     * функция установит прогресс карточки как выученная
     * сохранит изменения в BD
     * уведомит cardEventBus об измении карточки
     */
    private suspend fun setCardAsLearned(wordCard: WordCard): WordCard {
        if (wordCard.progressWord != null){
            wordCard.progressWord?.statProgress = StatProgress.LEARNED.value
            wordCard.progressWord?.numCorrAnsv = 0
            wordCard.progressWord?.sleepTime = SimpleDateFormat(SIM_FORM_DATE).format(Date())
            //обновим данные в репозитории
                wordCard.progressWord?.let { roomService.updateProgressWord(it) }
        }else{
            //возможно клиент захотел пометить ее изученной даже не начиная ее изучать

            return setCardAsLearned(addWordToTraining(wordCard))
        }
        return wordCard
    }
    /**
     * функция отправит уведомление в EventBus о смене карточки
     */
    private fun notifyChangeCard(event: CardEvent){
        EventBus.getDefault().post(
            CardEvent(
                "card_changed",
                event.wordCard,
                event.properties //там может передаваться например позиция адаптера или еще чтонибудь, поэтому вернем проперти таким каким оно пришло в репозиторий
            )
        )
    }
}