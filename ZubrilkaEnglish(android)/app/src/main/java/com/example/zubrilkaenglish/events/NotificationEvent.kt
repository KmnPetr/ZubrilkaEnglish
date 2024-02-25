package com.example.zubrilkaenglish.events

import android.widget.Toast

class NotificationEvent(
    val message: String,
    override val typeEvent: NfEvEnum = NfEvEnum.SHOW_TOAST,
    var properties: Map<String, Any>? = mapOf("duration" to Toast.LENGTH_SHORT)
    ) : iEvent<NfEvEnum>

enum class NfEvEnum{
    SHOW_TOAST
}