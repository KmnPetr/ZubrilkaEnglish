package com.example.zubrilkaenglish.onlineCompetition

import com.example.zubrilkaenglish.models.Profile
import com.example.zubrilkaenglish.models.SocketMessage
import com.example.zubrilkaenglish.repositories.ProfileRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class SocketListener: WebSocketListener() {
    private val competitionManager = CompetitionManager.instance
    val profileRepository = ProfileRepository.instance



    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        println("onOpen. The socket connection is open.")
        competitionManager.onOpenConection()
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        competitionManager.receiveMessage(SocketMessage.fromJson(text))
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        super.onMessage(webSocket, bytes)

        println("onMessage (bytes). Message: $bytes")
    }
    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        println("Closing: " + code + " " + reason)
        competitionManager.onCloseConection()
    }
    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        t.printStackTrace()
        competitionManager.onCloseConection()
        response?.let {
            if (it.code == 401) {
                GlobalScope.launch {
                    val profile: Profile? = profileRepository.getTemporaryProfile()
                    SocketHolder.instance.socketConnect()
                }
            } else {
                println("WebSocket connection failed: ${it.code} / ${t.message}")
            }
        } ?: run {
            println("WebSocket connection failed: ${t.message}")
        }
    }
}