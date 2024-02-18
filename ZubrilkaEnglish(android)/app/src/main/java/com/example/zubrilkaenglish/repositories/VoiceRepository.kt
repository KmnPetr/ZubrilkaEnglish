package com.example.zubrilkaenglish.repositories

import com.example.zubrilkaenglish.eventBus.events.VoiceEvent
import com.example.zubrilkaenglish.models.Voice
import com.example.zubrilkaenglish.repositories.retrofit.RetrofitService
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
    val voiceHandler: VoiceHandler = VoiceHandler()

    /**
     * метод используется библиотечкой EventBus
     * для прослушивания запросов от различных view
     */
    @Subscribe
    fun subscribeOnVoiceEvent(event: VoiceEvent){
        when(event.typeEvent){
            "playVoice" -> {
                playVoice(event.voice)
            }
        }
    }


    private fun playVoice(voice: Voice){
        GlobalScope.launch(Dispatchers.Default) {

            if (voice.voiceData == null){
                //получим Voice из сети
                voice.voiceData = retrofitService.getVoiceDataByName(voice.voiceName)?.voiceData
            }

            voiceHandler.play(voice)
        }
    }
}