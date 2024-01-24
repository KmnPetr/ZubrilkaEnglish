package com.example.zubrilkaenglish.screens

import android.os.Looper
import android.widget.Toast
import com.example.zubrilkaenglish.eventBus.events.NotificationEvent
import com.example.zubrilkaenglish.utils.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * класс отвечает за показ различных уведомлений пользователю
 * в частности показ Toast сообщения
 */
class ApiNotification private constructor() {
    companion object{
        val instance: ApiNotification by lazy { ApiNotification() }
    }
    init {
        EventBus.getDefault().register(this)
    }

    /**
     * функция реагирует на события просьбы различных классов показать toast сообщение
     */
    @Subscribe
    fun showToast(event: NotificationEvent){
        when(event.typeEvent){
            "show_toast" -> {
                Toast.makeText(MyApplication.context,event.message,event.properties?.get("duration") as Int).show()
            }
        }
    }
}