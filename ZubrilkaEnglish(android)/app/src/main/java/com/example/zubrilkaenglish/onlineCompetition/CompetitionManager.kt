package com.example.zubrilkaenglish.onlineCompetition

import android.util.Log
import com.example.zubrilkaenglish.events.CardEvent
import com.example.zubrilkaenglish.events.CmpEvEnum
import com.example.zubrilkaenglish.events.CompetitionEvent
import com.example.zubrilkaenglish.events.CrEvEnum
import com.example.zubrilkaenglish.models.SockMessType
import com.example.zubrilkaenglish.models.SocketMessage
import com.example.zubrilkaenglish.models.Word
import com.example.zubrilkaenglish.onlineCompetition.socketDto.DuelInfo
import com.example.zubrilkaenglish.onlineCompetition.socketDto.NextWord
import com.example.zubrilkaenglish.repositories.CardsRepository
import com.example.zubrilkaenglish.services.apiNotification.NotifProp
import com.example.zubrilkaenglish.utils.LOG
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    init {
        EventBus.getDefault().register(this)
    }

    fun receiveMessage(message: SocketMessage?) {
        when(message?.type){
            SockMessType.PING -> receivePing(message)
            SockMessType.REQUEST_ACTIVE_CARDS -> sendActiveCards()
            SockMessType.START_INFO -> startInfo(message)
            SockMessType.START_COUNTDOWN -> startCountdown(message)
            SockMessType.NEXT_WORD -> nextWord(message)
            else -> {
                Log.d(LOG,message?.toJson().toString())
            }
        }
    }
    /**
     * метод используется библиотечкой EventBus
     * для прослушивания запросов от различных view
     */
    @Subscribe
    fun subscribeOnCompetitionEvent(event: CompetitionEvent){
        when(event.typeEvent){
            CmpEvEnum.CLICK_ANSWER -> sendClickAnswer(event)
            else -> {}
        }
    }

    /**
     * отошлет на сервер соощение о том что пользователь сделал свой выбор ответа
     */
    private fun sendClickAnswer(event: CompetitionEvent) {
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
        val nextWord: NextWord? = NextWord.fromJson(message.map["nextWord"])
        println("nextWord")
        GlobalScope.launch {
            val word: Word? = cardsRepository.getWordFromDbById(nextWord?.idWord)

            println("GlobalScope")
            nextWord?.word=word //сразу выберем из бд дополнительную инфу, так как с бэка она не приходит
            this@CompetitionManager.nextWord.value = nextWord
        }

    }

    /**
     * обратный отсчет перед началом игры
     */
    private fun startCountdown(message: SocketMessage) {
        val tick:Int? = message.map.get("tick")?.toInt()
        startCountDown.value = tick
        if (tick == 1){
            GlobalScope.launch {
                delay(1000)
                startCountDown.value = null //обналим tick чтоб он убрался с экрана
            }
        }
    }

    /**
     * первоначальная информация по поединку пользователей
     */
    private fun startInfo(message: SocketMessage) {
        val duelInfo = DuelInfo.fromJson(message.map["duelInfo"])
        this.duelInfo.value = duelInfo
    }

    //отошлет на север список активных карточек пользователя
    private fun sendActiveCards() {
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
            while (pingJob!=null){
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