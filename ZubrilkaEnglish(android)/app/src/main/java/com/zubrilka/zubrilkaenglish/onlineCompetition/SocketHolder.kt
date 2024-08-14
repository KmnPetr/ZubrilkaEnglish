package com.zubrilka.zubrilkaenglish.onlineCompetition

import com.zubrilka.zubrilkaenglish.models.Profile
import com.zubrilka.zubrilkaenglish.models.SocketMessage
import com.zubrilka.zubrilkaenglish.repositories.ProfileRepository
import com.zubrilka.zubrilkaenglish.utils.URL
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

class SocketHolder private constructor(){
    companion object{
        val instance: SocketHolder by lazy { SocketHolder() }
    }

    val profileRepository = ProfileRepository.instance
    var webSocket: WebSocket? = null


    /**
     * установит сокет соединение
     */
    suspend fun socketConnect() {
        var token: String? = profileRepository.profile.value?.refreshToken

        println(token.toString())

        if (token==null){
            val profile: Profile? = profileRepository.getTemporaryProfile()
            token = profile?.refreshToken
        }

        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url(URL+"/competition")
            .addHeader("Authorization", "Bearer $token")  // Добавляем JWT токен в заголовок
            .build()
        webSocket?.close(1000,"reason") //в случае если оно было открыто ранее
        webSocket = client.newWebSocket(request, SocketListener())


        // Trigger shutdown of the dispatcher's executor so this process can
        // exit cleanly.
//        client.dispatcher().executorService().shutdown();
    }


    /**
     * отошлет по сокету сообщение
     */
    fun sendSocketMessage(message: SocketMessage) {
        webSocket?.send(message.toJson())
    }

    fun closeConnect() {
        webSocket?.close(1000,"reason")
    }


}