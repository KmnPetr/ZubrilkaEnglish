package com.example.ze_adminandroid.screens.serverConnect

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class NetworkHolder private constructor(){
    companion object{
        val instance: NetworkHolder by lazy { NetworkHolder() }
    }

    /**
     * проверит имеет ли ip адрес код 200 на http:<host>:8080/healthcheck
     */
    fun checkConnect(host: String, hostResult: MutableLiveData<Pair<String, Boolean>>) {

        if (host.equals("")) return

        var boolValue: Boolean

        GlobalScope.launch {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("http://"+host+":33333/healthcheck")
                .build()

            try {
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
                e.printStackTrace()
                boolValue = false
            }

            withContext(Dispatchers.Main) {
                if (boolValue){
                    hostResult.value = Pair(host,true)
                }
            }
        }
    }
}