package com.example.zubrilkaenglish.eventBus.events

import android.widget.Toast

class NotificationEvent(
    val message: String,
    override val typeEvent: String = "show_toast",
    var properties: Map<String, Any>? = mapOf("duration" to Toast.LENGTH_SHORT)
    ) : iEvent