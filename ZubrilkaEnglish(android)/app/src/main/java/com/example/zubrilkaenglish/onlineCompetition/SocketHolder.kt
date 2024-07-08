package com.example.zubrilkaenglish.onlineCompetition

import com.example.zubrilkaenglish.models.Profile
import com.example.zubrilkaenglish.models.SocketMessage
import com.example.zubrilkaenglish.repositories.ProfileRepository
import com.example.zubrilkaenglish.utils.URL
import com.example.zubrilkaenglish.utils.prod_url
import com.example.zubrilkaenglish.utils.test_url
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

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
            val profile: Profile? = profileRepository.getTemporaryProfile();
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