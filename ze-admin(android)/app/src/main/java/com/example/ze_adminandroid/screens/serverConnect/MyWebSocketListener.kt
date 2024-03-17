package com.example.ze_adminandroid.screens.serverConnect

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class MyWebSocketListener(val viewModel: ServerConnectViewModel) : WebSocketListener() {
    val NORMAL_CLOSURE_STATUS:Int = 1000
    val gson = Gson()

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)

        pingCheck(webSocket)
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

        val socketEvent = gson.fromJson(text, SocketEvent::class.java)
        if (socketEvent.ping!=null){
            val ping:Long = (System.currentTimeMillis()-socketEvent.ping)
            changePing(ping)
        }else changePing(null)
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
    private fun changePing(ping: Long?){
        viewModel.ping.postValue(ping)
    }

}