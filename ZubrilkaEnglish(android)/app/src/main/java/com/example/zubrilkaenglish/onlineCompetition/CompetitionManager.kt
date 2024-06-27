package com.example.zubrilkaenglish.onlineCompetition

import android.util.Log
import com.example.zubrilkaenglish.models.SockMessType
import com.example.zubrilkaenglish.models.SocketMessage
import com.example.zubrilkaenglish.repositories.CardsRepository
import com.example.zubrilkaenglish.utils.LOG
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

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

    fun receiveMessage(message: SocketMessage?) {
        when(message?.type){
            SockMessType.PING -> receivePing(message)
            SockMessType.REQUEST_ACTIVE_CARDS -> sendActiveCards()
            SockMessType.START_COMPETITION -> println("${message.type} получен")
            else -> {
                Log.d(LOG,message?.toJson().toString())
            }
        }
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