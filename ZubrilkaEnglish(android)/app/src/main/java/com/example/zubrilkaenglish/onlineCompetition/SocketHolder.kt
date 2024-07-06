package com.example.zubrilkaenglish.onlineCompetition

import com.example.zubrilkaenglish.models.SocketMessage
import com.example.zubrilkaenglish.repositories.ProfileRepository
import com.example.zubrilkaenglish.utils.URL
import com.example.zubrilkaenglish.utils.prod_url
import com.example.zubrilkaenglish.utils.test_url
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

class SocketHolder private constructor(){
    companion object{
        val instance: SocketHolder by lazy { SocketHolder() }
    }

    val profileRepository = ProfileRepository.instance

    val ping: MutableStateFlow<Long?> = MutableStateFlow(null)
    var webSocket: WebSocket? = null


    /**
     * установит сокет соединение
     */
    fun socketConnect() {
        val token: String? = profileRepository.profile.value?.refreshToken

        println(token.toString())

        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url(prod_url+"/competition")
            .addHeader("Authorization", "Bearer $token")  // Добавляем JWT токен в заголовок
            .build()
        val listener = SocketListener()
        webSocket?.close(1000,"reason") //в случае если оно было открыто ранее
        webSocket = client.newWebSocket(request, listener)


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