package com.example.zubrilkaenglish.repositories

import com.example.zubrilkaenglish.events.VcEvEnum
import com.example.zubrilkaenglish.events.VoiceEvent
import com.example.zubrilkaenglish.models.Voice
import com.example.zubrilkaenglish.repositories.retrofit.RetrofitService
import com.example.zubrilkaenglish.repositories.room.RoomService
import com.example.zubrilkaenglish.utils.VoiceHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class VoiceRepository private constructor() {
    companion object {
        val instance: VoiceRepository by lazy { VoiceRepository() }
    }
    init {
        EventBus.getDefault().register(this)
    }
    val retrofitService: RetrofitService = RetrofitService()
    val roomService: RoomService = RoomService()
    val voiceHandler: VoiceHandler = VoiceHandler()

    /**
     * метод используется библиотечкой EventBus
     * для прослушивания запросов от различных view
     */
    @Subscribe
    fun subscribeOnVoiceEvent(event: VoiceEvent){
        when(event.typeEvent){
            VcEvEnum.PLAY_VOICE -> {
                GlobalScope.launch(Dispatchers.Default) {
                    getVoiceFromServices(event.voice.voiceName)?.let { voiceHandler.play(it) }

                }
            }
            else -> {}
        }
    }

    /**
     * функция достанет data для voice из ДБ или если там нет из сети
     */
    private suspend fun getVoiceFromServices(name: String):Voice?{
        println("попытка взять Voice из БД")
        var voice: Voice? = roomService.getVoiceByName(name)
        if (voice?.voiceData == null){
            println("попытка взять Voice из сети")
            voice = retrofitService.getVoiceDataByName(name)
            if (voice?.voiceData != null){
                GlobalScope.launch(Dispatchers.Default) {
                    println("сохранение voice в БД")
                    roomService.insertNewVoice(voice)
                }
            }
        }
        return voice
    }
}