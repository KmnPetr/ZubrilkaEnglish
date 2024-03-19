package com.example.ze_adminandroid.screens.serverConnect.socketService

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket

class NetworkHolder private constructor(){
    companion object{
        val instance: NetworkHolder by lazy { NetworkHolder() }
    }

    val host: MutableStateFlow<Pair<String,Boolean>?> = MutableStateFlow(null)
    val ping: MutableStateFlow<Int?> = MutableStateFlow(null)


    /**
     * установит сокет соединение
     */
    private fun socketConnect(host:String) {
        val client: OkHttpClient = OkHttpClient()
        val request: Request = Request.Builder().url("ws://192.168.9.182:33333/event-emitter").build()
        val listener: MyWebSocketListener = MyWebSocketListener(ping)
        val ws: WebSocket = client.newWebSocket(request, listener)


        // Trigger shutdown of the dispatcher's executor so this process can
        // exit cleanly.
//        client.dispatcher().executorService().shutdown();
    }

    /**
     * проверит имеет ли ip адрес код 200 на http:<host>:8080/healthcheck
     */
    fun checkConnect(host: String) {

        if (host.equals("")) return

        var boolValue: Boolean

        GlobalScope.launch {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                .url("http://"+host+":33333/healthcheck")
                .build()

                val response: Response = client.newCall(request).execute()
                val statusCode = response.code
                println("status: "+statusCode)

                if (statusCode == 200) {
                    println("Запрос на адрес: "+host+" выполнен успешно. Статус: $statusCode")
                    boolValue = true
                } else {
                    println("Запрос на адрес: "+host+"  выполнен с ошибкой. Статус: $statusCode")
                    boolValue = false
                }
                response.close()
            } catch (e: Exception) {
                println(e.message)
                boolValue = false
            }

            withContext(Dispatchers.Main) {
                if (boolValue){
                    //оповестим viewModel об удачной проверке хоста
                    //а также запустим сокет
                    this@NetworkHolder.host.value = Pair(host,true)
                    socketConnect(host)
                }
            }
        }
    }
}