package com.example.ze_adminandroid.screens.serverConnect.socketService

import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import okio.ByteString.Companion.toByteString
import org.apache.lucene.analysis.CharArraySet

class MyWebSocketListener : WebSocketListener() {
    val sendDataManager = SendDataManager.instanse
    val NORMAL_CLOSURE_STATUS:Int = 1000

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        println("The socket connection is open.")
        sendDataManager.startPing()
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)

        val mp = MessageProtocol(text.toByteArray(Charsets.UTF_8))

        sendDataManager.route(mp)

    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        super.onMessage(webSocket, bytes)

        val mp = MessageProtocol(bytes.toByteArray())

        sendDataManager.route(mp)
    }
    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        println("Closing: " + code + " " + reason)
        changePing(null)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        t.printStackTrace()
        changePing(null)
    }

    /**
     * оповестит view об изменениях пинга и связи
     * в пинг нужно передать null при закрытии сети, чтобы вью об этом оповестила
     */
    private fun changePing(ping: Long?){
        NetworkHolder.instance.ping.value = ping
    }

}