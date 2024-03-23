package com.example.ze_adminandroid.screens.serverConnect.socketService

import com.example.ze_adminandroid.events.SctEvEnum
import com.example.ze_adminandroid.events.SocketEvent
import com.example.ze_adminandroid.models.Voice
import com.example.ze_adminandroid.repositories.VoiceRepository
import com.example.ze_adminandroid.repositories.WordRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okio.ByteString.Companion.toByteString
import org.greenrobot.eventbus.EventBus


class SendDataManager private constructor(){
    companion object{
        val instanse: SendDataManager by lazy { SendDataManager() }
    }
    val networkHolder = NetworkHolder.instance
    val wordRepository = WordRepository.instance
    val voiceRepository = VoiceRepository.instance

    /**
     * определяет тип сообщения и дальнейшие действия
     */
    fun route(mp: MessageProtocol) {
        when (mp.type){
            TypeEnum.PING ->{
                pingCheck(mp)
            }
            TypeEnum.VOICE_ERROR->{
                printError(mp)
            }

            else -> {}
        }
    }

    /**
     * выведет сообщение об ошибки на экран
     */
    private fun printError(mp: MessageProtocol) {
        val message = mp.headers["message"].toString()
        println(message)
        EventBus.getDefault().post(SocketEvent(SctEvEnum.VOICE_ERROR, mapOf(Pair("message",message))))
    }

    /**
     * просчитает задержку пинг
     * оповестит networkHolder о ней
     * отошлет ответный пинг
     */
    fun pingCheck(mp: MessageProtocol) {
        networkHolder.ping.value = (System.currentTimeMillis()- mp.headers.get("time")?.toLong()!!)

        GlobalScope.launch {
            delay(2000)
            networkHolder.webSocket?.send(
                //только мутабл мапу принимает
                MessageProtocol(TypeEnum.PING,
                    mutableMapOf(
                        Pair(
                            "time",
                            System.currentTimeMillis().toString())),
                    null).message.toByteString()
            )
        }
    }

    /**
     * метод отошлет первое сообщение проверки пинга
     */
    fun startPing(){
        networkHolder.sendSocketMessage(
            //только мутабл мапу принимает
            MessageProtocol(TypeEnum.PING,
                mutableMapOf(
                    Pair(
                        "time",
                        System.currentTimeMillis().toString())),
                null).message.toByteString()
        )
    }


    /**
     * отошлет имеющийся voice из БД первый по списку в таблице
     */
    fun sendNextVoice() {
        GlobalScope.launch{
            if (voiceRepository.countVoices.value!=null && voiceRepository.countVoices.value!! >0){
                val voice: Voice? = voiceRepository.getFirstVoice()

                if (voice!=null){
                    val mp = MessageProtocol(
                        TypeEnum.VOICE,
                        mutableMapOf(Pair("filename",voice.voiceName)),
                        voice.voiceData
                    )
                    networkHolder.sendSocketMessage(
                        mp.message.toByteString()
                    )
                }
            }
        }
    }
}