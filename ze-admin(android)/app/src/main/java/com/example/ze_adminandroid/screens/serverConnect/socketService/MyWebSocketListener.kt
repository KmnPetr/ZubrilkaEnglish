package com.example.ze_adminandroid.screens.serverConnect.socketService

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import okio.ByteString.Companion.toByteString

class MyWebSocketListener(val ping: MutableStateFlow<Int?>) : WebSocketListener() {
    val NORMAL_CLOSURE_STATUS:Int = 1000
    val gson = Gson()

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)

        pingCheck(webSocket)
        sendByteArray(webSocket)
    }
    /**
     * отправит массив по сокету
     */
    private fun sendByteArray(webSocket: WebSocket) {
        GlobalScope.launch {
            delay(5000)

            val data = ByteArray(1007)
            webSocket.send(data.toByteString())
            println("отправляем массив: "+ data.size)
        }
    }


    /**
     * будет отсылать ping на сервер
     */
    private fun pingCheck(webSocket: WebSocket) {
        GlobalScope.launch {
            while(true) {
                webSocket.send(
                    SocketEvent("ping", System.currentTimeMillis()).toJson()
                )
                delay(2000)
            }
        }
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        println("Receiving: " + text)

        try {
            val socketEvent = gson.fromJson(text, SocketEvent::class.java)
            if (socketEvent.ping!=null){
                val ping:Long = (System.currentTimeMillis()- socketEvent.ping)
                changePing(ping.toInt())
            }else changePing(null)
        }catch (e: JsonSyntaxException){ println("непотдерживаемый формат gson") }
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        super.onMessage(webSocket, bytes)
        println("Receiving: " + bytes.hex())
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
     */
    private fun changePing(ping: Int?){
        this.ping.value = ping
    }

}