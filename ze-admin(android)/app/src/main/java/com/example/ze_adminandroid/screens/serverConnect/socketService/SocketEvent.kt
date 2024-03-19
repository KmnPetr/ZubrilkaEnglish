package com.example.ze_adminandroid.screens.serverConnect.socketService

import com.google.gson.Gson

class SocketEvent(
    val type: String,
    val ping: Long?
) {
    fun toJson():String{
        return Gson().toJson(this)
    }
}