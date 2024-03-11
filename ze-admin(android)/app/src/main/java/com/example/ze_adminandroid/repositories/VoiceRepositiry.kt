package com.example.ze_adminandroid.repositories

import com.example.ze_adminandroid.events.VcEvEnum
import com.example.ze_adminandroid.events.VoiceEvent
import com.example.ze_adminandroid.models.Voice
import com.example.ze_adminandroid.services.RetrofitService
import com.example.ze_adminandroid.services.RoomService
import com.example.ze_adminandroid.utils.VoiceHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class VoiceRepository private constructor(){
    companion object{
        val instance: VoiceRepository by lazy { VoiceRepository() }
    }
    init {
        EventBus.getDefault().register(this)
    }

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

    private val retrofitService = RetrofitService.instance
    private var roomService: RoomService = RoomService()

    fun saveNewVoice(voice: Voice){
        GlobalScope.launch(Dispatchers.Default) {
            roomService.saveNewVoice(voice)
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
                    roomService.saveNewVoice(voice)
                }
            }
        }
        return voice
    }
}