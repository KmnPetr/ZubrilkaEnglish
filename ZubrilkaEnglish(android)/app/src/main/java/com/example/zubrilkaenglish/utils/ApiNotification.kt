package com.example.zubrilkaenglish.utils

import android.widget.Toast
import com.example.zubrilkaenglish.events.NfEvEnum
import com.example.zubrilkaenglish.events.NotificationEvent
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
            NfEvEnum.SHOW_TOAST -> {
                Toast.makeText(MyApplication.context,event.message,event.properties?.get("duration") as Int).show()
            }

            else -> {}
        }
    }
}