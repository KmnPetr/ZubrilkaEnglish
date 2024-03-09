package com.example.ze_adminandroid.repositories

import com.example.ze_adminandroid.models.Voice
import com.example.ze_adminandroid.services.RetrofitService
import com.example.ze_adminandroid.services.RoomService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class VoiceRepository private constructor(){

    companion object{
        val instance: VoiceRepository by lazy { VoiceRepository() }
    }

    private val retrofitService = RetrofitService.instance
    private var roomService: RoomService = RoomService()

    fun saveNewVoice(voice: Voice){
        GlobalScope.launch(Dispatchers.Default) {
            roomService.saveNewVoice(voice)
        }
    }
}