package com.example.ze_adminandroid.screens.serverConnect

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ze_adminandroid.services.RoomService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ServerConnectViewModel : ViewModel() {
    val roomService = RoomService()

    //в строке хост, в значении если true значит хост прочекивается
    val host: MutableLiveData<Pair<String,Boolean>> = MutableLiveData()
    //в строке значение из изменяемого EditText
    val editedHost: MutableLiveData<String> = MutableLiveData()
    //значение из БД
    val lastHost: MutableLiveData<String> = MutableLiveData()

    init {
        var host: String
        GlobalScope.launch {
            host = roomService.getLastHost().toString()

            withContext(Dispatchers.Main) {
                if (host!=null&&!host.equals("null")){
                    lastHost.value = host
                }
            }
        }

    }

    /**
     * установит новое значение в таблице prop_table по ключу last-host
     */
    fun setNewLastHost(host: String){
        roomService.insertNewLastHost(host)
    }
}