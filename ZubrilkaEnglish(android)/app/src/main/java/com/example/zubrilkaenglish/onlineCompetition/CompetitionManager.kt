package com.example.zubrilkaenglish.onlineCompetition

import android.util.Log
import com.example.zubrilkaenglish.events.CmpEvEnum
import com.example.zubrilkaenglish.events.CompetitionEvent
import com.example.zubrilkaenglish.models.SockMessType
import com.example.zubrilkaenglish.models.SocketMessage
import com.example.zubrilkaenglish.models.Word
import com.example.zubrilkaenglish.models.socketDto.ClickResult
import com.example.zubrilkaenglish.models.socketDto.DuelInfo
import com.example.zubrilkaenglish.models.socketDto.FinishInfo
import com.example.zubrilkaenglish.models.socketDto.Info_4
import com.example.zubrilkaenglish.models.socketDto.NextWord
import com.example.zubrilkaenglish.models.socketDto.StatusInfo
import com.example.zubrilkaenglish.repositories.CardsRepository
import com.example.zubrilkaenglish.utils.LOG
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * класс занимается обработкой различной логики в онлайн соревновании
 */
class CompetitionManager private constructor(){
    companion object{
        val instance: CompetitionManager by lazy { CompetitionManager() }
    }
    private val socketHolder = SocketHolder.instance
    private val cardsRepository = CardsRepository.instance

    private var pingJob: Job? = null
    val ping: MutableStateFlow<Long?> = MutableStateFlow(null)
    val duelInfo: MutableStateFlow<DuelInfo?> = MutableStateFlow(null)
    val startCountDown: MutableStateFlow<Int?> = MutableStateFlow(null)
    val nextWord: MutableStateFlow<NextWord?> = MutableStateFlow(null)
    val finishInfo: MutableStateFlow<FinishInfo?> = MutableStateFlow(null)
    val statusInfo: MutableStateFlow<StatusInfo?> = MutableStateFlow(null)
    val info_4: MutableStateFlow<Info_4?> = MutableStateFlow(null)

    init {
        EventBus.getDefault().register(this)
    }

    fun receiveMessage(message: SocketMessage?) {
        when(message?.type){
            SockMessType.PING -> receivePing(message)
            SockMessType.STATUS_INFO -> receiveStatusInfo(message)
            SockMessType.REQUEST_ACTIVE_CARDS -> sendActiveCards()
            SockMessType.START_COUNTDOWN -> startCountdown(message)
            SockMessType.NEXT_WORD -> nextWord(message)
            SockMessType.CLICK_RESULT -> receiveClickResult(message)
            SockMessType.PEN_WAIT -> receivePenaltyWaiting(message)
            SockMessType.FINISH_INFO -> receiveFinishInfo(message)
            SockMessType.INFO_4 -> receiveInfo_4(message)
            else -> { Log.d(LOG,message?.toJson().toString()) }
        }
    }

    /**
     * Метод в цикле рассылается участникам с различной информацией
     * о времени его ожидания поединка
     * о количестве игроков находящихся онлайн и другое
     */
    private fun receiveInfo_4(message: SocketMessage) {
        println(message.map["info_4"])
        info_4.value = Info_4.fromJson(message.map["info_4"])
    }

    /**
     * при получении обьекта StatusInfo с сервера
     */
    private fun receiveStatusInfo(message: SocketMessage) {
        println(message.map["statusInfo"])
        statusInfo.value = StatusInfo.fromJson(message.map["statusInfo"])
        duelInfo.value = DuelInfo.fromJson(message.map["duelInfo"])
    }

    /**
     * при подключении запросит первоначальные данные
     * кто знает можт он уже играет просто соединение моргнуло или вышел на пару секунд
     */
    private fun requestStatusInfo() {
        socketHolder.sendSocketMessage(SocketMessage(SockMessType.REQUEST_STATUS_INFO, mapOf()))
    }

    /**
     * вызывается при получении обьекта FinishInfo при завершении поединка
     */
    private fun receiveFinishInfo(message: SocketMessage) {
        println(message.map["finishInfo"])
        nextWord.value = null
        this.finishInfo.value = FinishInfo.fromJson(message.map["finishInfo"])
    }

    /**
     * метод используется библиотечкой EventBus
     * для прослушивания запросов от различных view
     */
    @Subscribe
    fun subscribeOnCompetitionEvent(event: CompetitionEvent){
        when(event.typeEvent){
            CmpEvEnum.CLICK_ANSWER -> sendClickAnswer(event)
            CmpEvEnum.CLOSE_SESSION -> closeConnection()
            CmpEvEnum.SET_WAITING_STATUS -> setWaitingStatus()
            CmpEvEnum.SET_LOYAL_TO_BOTS-> setLoyalToBots(event)
            else -> {}
        }
    }

    /**
     * отправит запрос на сервер намерение пользователя играть с ботами или не играть
     */
    private fun setLoyalToBots(event: CompetitionEvent) {
        println("setLoyalToBots(event: CompetitionEvent)")
        socketHolder.sendSocketMessage(SocketMessage(SockMessType.SET_LOYAL_TO_BOTS, mapOf("loyalToBots" to event.properties["loyalToBots"].toString())))
    }

    /**
     * отправит запрос на сервер поставит юзера в режим ожидания соперника
     */
    private fun setWaitingStatus() {
        println("setWaitingStatus()")
        socketHolder.sendSocketMessage(SocketMessage(SockMessType.SET_WAITING_STATUS, mapOf()))
    }

    /**
     * закроет сокет сессию очистиит данные
     */
    private fun closeConnection() {
        println("closeConnection()")
        socketHolder.closeConnect()
        stopPing()
        ping.value = null
        duelInfo.value = null
        startCountDown.value = null
        nextWord.value = null
        finishInfo.value = null
        statusInfo.value = null
    }

    /**
     * вызывается при получении сообщении о штрафе за чрезмерную задержку времени
     */
    private fun receivePenaltyWaiting(message: SocketMessage) {
        println(message.map)
        GlobalScope.launch(Dispatchers.Main) {
            EventBus.getDefault().post(CompetitionEvent(CmpEvEnum.PEN_WAIT, message.map.toMutableMap()))
        }
    }

    /**
     * обработает пришедший с сервера результат по выбору ответа
     */
    private fun receiveClickResult(message: SocketMessage) {
        println(message.map)
        val clickResult: ClickResult = ClickResult.fromJson(message.map["clickResult"])
        if (clickResult.idWord == (nextWord.value?.idWord ?: false)){
            GlobalScope.launch(Dispatchers.Main){
                EventBus.getDefault().post(CompetitionEvent(CmpEvEnum.CLICK_RESULT, mutableMapOf("clickResult" to clickResult)))
            }
        }
    }


    /**
     * отошлет на сервер соощение о том что пользователь сделал свой выбор ответа
     */
    private fun sendClickAnswer(event: CompetitionEvent) {
        println("sendClickAnswer(event: CompetitionEvent)")
        socketHolder.sendSocketMessage(
            SocketMessage(
                SockMessType.CLICK_ANSWER,
                mutableMapOf(
                    "position" to event.properties["position"].toString(),
                    "wordId" to nextWord.value?.idWord.toString()
                )))
    }

    /**
     * вызывается при получении следующего слова при поединке с сокета
     */
    private fun nextWord(message: SocketMessage) {
        println("private fun nextWord(message: SocketMessage)")
        val nextWord: NextWord? = NextWord.fromJson(message.map["nextWord"])
        println(nextWord)
        GlobalScope.launch {
            val word: Word? = cardsRepository.getWordFromDbById(nextWord?.idWord)

            nextWord?.word=word //сразу выберем из бд дополнительную инфу, так как с бэка она не приходит
            this@CompetitionManager.nextWord.value = nextWord
        }

    }

    /**
     * обратный отсчет перед началом игры
     */
    private fun startCountdown(message: SocketMessage) {
        println(message.map)
        val tick:Int? = message.map.get("tick")?.toInt()
        startCountDown.value = tick
        if (tick == 1){
            GlobalScope.launch {
                delay(1000)
                startCountDown.value = null //обналим tick чтоб он убрался с экрана
            }
        }
    }

    //отошлет на север список активных карточек пользователя
    private fun sendActiveCards() {
        println("sendActiveCards()")
        GlobalScope.launch {
            val listIdActiveCards:List<Long> = cardsRepository.getIdAllActiveCards()
            socketHolder.sendSocketMessage(SocketMessage(SockMessType.ACTIVE_CARDS, mapOf("listIdActiveCards" to Gson().toJson(listIdActiveCards))))
        }
    }

    //вызывается при получении ответного пинга от сервера
    private fun receivePing(message: SocketMessage) {
        ping.value = (System.currentTimeMillis()- (message.map.get("time")?.toLong()!!))
    }

    /**
     * вызывается при открытии сессии
     */
    fun onOpenConection() {
        println("onOpenConection()")
        requestStatusInfo()
        startPing()
    }

    /**
     * вызывается при разрыве или штатном отключении сессии
     */
    fun onCloseConection() {
        Log.d(LOG,"onCloseConection")
        stopPing()
    }

    /**
     * стартует корутину с пингом
     */
    private fun startPing() {
        pingJob = GlobalScope.launch {
            while (true/*pingJob!=null*/){
                socketHolder.sendSocketMessage(SocketMessage(SockMessType.PING, mapOf("time" to System.currentTimeMillis().toString())))
                delay(1000)
            }
        }
    }

    /**
     * остановит пинг
     */
    private fun stopPing(){
        pingJob?.cancel()
        pingJob = null
        ping.value = null
    }
}