package com.example.zubrilkaenglish.events

import android.widget.Toast

class NotificationEvent(
    val message: String,
    override val typeEvent: NfEvEnum,
    override var properties: MutableMap<String, Any> = mutableMapOf()
    ) : iEvent<NfEvEnum>

enum class NfEvEnum{
    LIMIT_ACTIVE_WORDS
}