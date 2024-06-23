package com.example.zubrilkaenglish.repositories

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.example.zubrilkaenglish.events.CardEvent
import com.example.zubrilkaenglish.events.CrEvEnum
import com.example.zubrilkaenglish.events.NfEvEnum
import com.example.zubrilkaenglish.events.NotificationEvent
import com.example.zubrilkaenglish.screens.training.ICard
import com.example.zubrilkaenglish.models.ProgressWord
import com.example.zubrilkaenglish.models.Word
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.repositories.retrofit.RetrofitService
import com.example.zubrilkaenglish.repositories.room.RoomService
import com.example.zubrilkaenglish.screens.training.Modes
import com.example.zubrilkaenglish.screens.training.additionalCards.FewCards
import com.example.zubrilkaenglish.screens.training.additionalCards.NoMemosCard
import com.example.zubrilkaenglish.screens.training.additionalCards.ReviewCard
import com.example.zubrilkaenglish.utils.LIMIT_ACTIVE_CARDS
import com.example.zubrilkaenglish.utils.LOG
import com.example.zubrilkaenglish.services.apiNotification.NotifProp
import com.example.zubrilkaenglish.utils.SIM_FORM_DATE
import com.example.zubrilkaenglish.utils.StatProgress
import com.example.zubrilkaenglish.utils.numAnsForSleep
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

/**
 * этот класс будет сосредоточен на обработке логики связанной с карточками
 * постепенно вся логика с картами должна переноситься сюда
 */
class CardsRepository private constructor(){
    companion object{
        val instance: CardsRepository by lazy { CardsRepository() }
    }
    private val roomService = RoomService()
    private val retrofitService= RetrofitService()
    private val memoRepository = MemoRepository.instance
    private val propRepository = PropRepository.instance

    var countActiveCards: AtomicInteger = AtomicInteger(0) //обновляемые сведения о количестве активных карточек

    init {
        EventBus.getDefault().register(this)

        //обновляем значение countActiveCards в фоновом процессе
        GlobalScope.launch {
            roomService.getProgressDAO().getAllProgressUnlearnedCards().collect {
                countActiveCards.set(getCountActiveCards(it))
            }
        }
    }


    /**
     * метод используется библиотечкой EventBus
     * для прослушивания запросов от различных view
     */
    @Subscribe
    fun subscribeOnCardEvent(event: CardEvent){
        when(event.typeEvent){
            CrEvEnum.INTENT_SLEEP -> {
                event.wordCard = setSleepCard(event.wordCard,event.properties.get("countDay") as Int)
                notifyChangeCard(event)
            }
            CrEvEnum.INCREASE_PROGRESS -> {
                event.wordCard = increaseProgressCard(event.wordCard)
                notifyChangeCard(event)
                //проверим, не пора ли карточке спать
                if (checkCardSleep(event.wordCard)) EventBus.getDefault().post(CardEvent(CrEvEnum.SLEEP_EVENT, event.wordCard, event.properties))
            }
            CrEvEnum.RESET_numCorrAnsv -> {
                event.wordCard = resetNumCorrAnsv(event.wordCard)
                notifyChangeCard(event)
            }
            CrEvEnum.SET_AS_LEARNED -> {
                GlobalScope.launch(Dispatchers.Default) {
                    event.wordCard = setCardAsLearned(event.wordCard)
                    withContext(Dispatchers.Main) {
                        event.properties.putAll(NotifProp.learnCard.pair) //apiNotification надо знать что мы изменили в карточке
                        notifyChangeCard(event)
                    }
                }
            }
            CrEvEnum.ADD_WORD_TO_TRAINING -> {
                if (checkLimitActiveCards(event)){
                    GlobalScope.launch(Dispatchers.Default) {
                        event.wordCard = addWordToTraining(event.wordCard)
                        withContext(Dispatchers.Main) {
                            event.properties.putAll(NotifProp.addToWordTrain.pair) //apiNotification надо знать что мы изменили в карточке
                            notifyChangeCard(event)
                        }
                    }
                }
            }
            CrEvEnum.RESET_PROGRESS -> {
                if (compareDate(event.wordCard.progressWord?.sleepTime) || checkLimitActiveCards(event)){
                    event.wordCard = resetProgressCard(event.wordCard)
                    event.properties.putAll(NotifProp.resetProgress.pair) //apiNotification надо знать что мы изменили в карточке
                    notifyChangeCard(event)
                }
            }
            CrEvEnum.DELETE_CARD -> {
                event.wordCard = deleteProgressCard(event.wordCard)
                event.properties.putAll(NotifProp.deleteCard.pair) //apiNotification надо знать что мы изменили в карточке
                notifyChangeCard(event)
            }
            else -> {}
        }
    }

    /**
     * проверит, количество находящихся в обучении карточек и сверит с тарифным лимитом
     */
    private fun checkLimitActiveCards(failedEvent: CardEvent): Boolean {
        return if (countActiveCards.get() >= LIMIT_ACTIVE_CARDS){
            if(failedEvent.properties["approvedCard"] == true) return true
            else {
            EventBus.getDefault().post(NotificationEvent(
                "Лимит активных карточек (${countActiveCards.get()}/$LIMIT_ACTIVE_CARDS)",
                NfEvEnum.LIMIT_ACTIVE_WORDS,
                mutableMapOf("failedEvent" to failedEvent)))
           return false
        }
        } else true
    }

    /**
     * удалит ProgressWord по id Word
     */
    fun deleteProgressCard(wordCard: WordCard):WordCard {
        //удалим progressWord из БД
        GlobalScope.launch(Dispatchers.Default) {
            roomService.deleteProgressByWordId(wordCard.word.id)
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
            }
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
            val updatedWordCard = roomService.getWordCardById(wordCard.word.id)

            //обновляем progressWord во всех обьектах ссылочным образом
            wordCard.progressWord = updatedWordCard.progressWord
        }catch (e: SQLiteConstraintException){
            e.printStackTrace()
            //может выпасть эксепшен если попытаться добавить progressWord в БД второй раз
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
    suspend fun getListForTreining(mode: Modes): ArrayList<ICard> {
        val listAllCards: List<WordCard>? = roomService.getListWordsCards()
        val listForTreining: ArrayList<ICard> = ArrayList()

        listAllCards?.forEach { it ->
            if (it.progressWord?.statProgress!=StatProgress.LEARNED.value&&compareDate(it.progressWord?.sleepTime)){
                listForTreining.add(it)
            }
        }

        listForTreining.shuffle()

        if (mode == Modes.multipleChoice) fillAnswerVariants(listForTreining)

        listForTreining.add(installLatestCard())

        return listForTreining
    }

    /**
     * заполнит варианты ответов при многовариантном режиме обучения
     */
    private suspend fun fillAnswerVariants(listForTreining: ArrayList<ICard>) {

        var allTranslations:ArrayList<String?>? = ArrayList(roomService.getWordDAO().getAllTranslations())

        listForTreining.forEach {
            if (it is WordCard){
                it.rightPosition = Random.nextInt(0, 4)

                it.variants = MutableList(4) { null }
                it.variants!![it.rightPosition!!] = it.word.translation.toString()
                it.variants!!.forEachIndexed { index, s ->
                    if (index!= it.rightPosition) it.variants!![index] = setWrongAnswer(allTranslations, it.variants!!)
                }
            }
        }

        // Освобождение ресурсов поможем гарбадж коллектору
        allTranslations?.clear()
        allTranslations = null
    }

    /**
     * установит ложный вариант ответа в список variants
     */
    private fun setWrongAnswer(allTranslations: ArrayList<String?>?, listAnswers: MutableList<String?>): String {
        var wrongAnswer = allTranslations?.get(Random.nextInt(0,allTranslations.size))
        listAnswers.forEach {//проверим чтобы ответ не совпадал с предыдущими ответами
            if (wrongAnswer.equals(it)) wrongAnswer = setWrongAnswer(allTranslations,listAnswers)
        }
        return wrongAnswer.toString()
    }


    /**
     * определит какая дополнительная карточка будет в конце списка
     * это может быть карточка с предложением начать трен.заново, какое нибудь уведомление или подсказка
     */
    private suspend fun installLatestCard(): ICard {
        val countActiveCards = getCountActiveCards(roomService.getProgressDAO().getAllProgressUnlearnedCards2())
        val isUserHasOwnMemos:Boolean = memoRepository.isUserHasOwnMemos()
        val randomInt: Int = Random.nextInt(0, 100)

        if (randomInt in 50..75 && !isUserHasOwnMemos) return NoMemosCard() //если пользователь еще не добавлял напоминалку с вероятностью 25% предложим ему добавить

        if (countActiveCards<50 && randomInt in 0..49){ //если активных карточек меньше 50 с вероятностью 50% подскажем где найти новые
            return FewCards()
        } else if (countActiveCards<40){//если активных карточек меньше 40 то точно подскажем где найти новые:)
            return FewCards()
        }
        return ReviewCard()
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
        return wordCard.progressWord?.numCorrAnsv!! >= numAnsForSleep
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
                CrEvEnum.CARD_CHANGED,
                event.wordCard,
                event.properties //там может передаваться например позиция адаптера или еще чтонибудь, поэтому вернем проперти таким каким оно пришло в репозиторий
            )
        )
    }


    /**
     * функция получит список Words из сети и положит его в БД из БД вернет новые данные
     */
    suspend fun getAllWordsFromServerOrDB():List<Word>{
        if(checkDictionaryVersionOnServer()){
            val list=retrofitService.getAllWords()
            if (list != null) {
                Log.d(LOG,"получили список с инета. Его размер: ${list.size}")
                roomService.insertListWords(list)

                Log.d(LOG,"положили список в БД")

                val newDictionaryVersion=retrofitService.getDictionaryVersion()
                roomService.insertNewDictionaryVersion(newDictionaryVersion)

            }else{
                Log.d(LOG,"список с сервера был null")
            }
        }
        return roomService.getAllWords()
    }

    /**
     * если на сервере имеется более свежая версия списков данных, метод вернет true или если в базе нет сведений о дате последней версии вернет true
     */
    private suspend fun checkDictionaryVersionOnServer():Boolean{
        val serverDicVers: String? =retrofitService.getDictionaryVersion()
        Log.d(LOG,"Dictionary version from server: $serverDicVers")
        val roomDicVers:String?=roomService.getDictionaryVersion()
        Log.d(LOG,"Dictionary version from room: $roomDicVers")

        if (serverDicVers==null){
            Log.d(LOG,"server Dictionary Version is null")
            return false
        }else if(roomDicVers==null){
            Log.d(LOG,"room Dictionary Version is null")
            return true
        }else {

            if(serverDicVers==roomDicVers){
                Log.d(LOG,"Старая версия актуальна, оставляем...")
                return false
            }else{
                Log.d(LOG,"Версия словаря на сервере изменилась, загружаем новый список")
                return true
            }

        }
    }

    /**
     * функция вернет список всех WordCard, в том числе и тех, у которых нет(null) ProgressWord
     * Запрос будет произведен к БД без попытки подключения к сети
     */
    suspend fun getAllWordCards(): List<WordCard> {
        return roomService.getAllWordCards()
    }

    /**
     * достанет из базы все карточки с прогрессом пользователя и рассортирует в мапу
     */
    suspend fun getMapMyCards(): Map<String, List<WordCard>> {
        val listWordsCards = roomService.getListWordsCards()
        val mapMyCards = mutableMapOf<String,ArrayList<WordCard>>()

        mapMyCards["активные"] = ArrayList()
        mapMyCards["спящие"] = ArrayList()
        mapMyCards["выученные"] = ArrayList()
        listWordsCards?.forEach {
            if (it.progressWord?.statProgress!=StatProgress.LEARNED.value&&compareDate(it.progressWord?.sleepTime)){
                mapMyCards["активные"]?.add(it)
            }else if (it.progressWord?.statProgress!=StatProgress.LEARNED.value&&!compareDate(it.progressWord?.sleepTime)){
                mapMyCards["спящие"]?.add(it)
            }else if(it.progressWord?.statProgress==StatProgress.LEARNED.value){
                mapMyCards["выученные"]?.add(it)
            }
        }
        return mapMyCards
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

    //отфильтрует список и посчитает количество карт на данный момент выспавшихся, активных
    private fun getCountActiveCards(it: List<ProgressWord>): Int {
        var countAC:Int = 0
        it.forEach {
            if(it.statProgress!=StatProgress.LEARNED.value && compareDate(it.sleepTime)) countAC++
        }
        return countAC
    }
}