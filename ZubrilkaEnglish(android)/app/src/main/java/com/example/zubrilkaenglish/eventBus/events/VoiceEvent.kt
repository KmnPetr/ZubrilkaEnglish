package com.example.zubrilkaenglish.eventBus.events

import com.example.zubrilkaenglish.models.Voice
import com.example.zubrilkaenglish.models.WordCard

class VoiceEvent(
    override val typeEvent: String,
    val voice: Voice,
    var properties: Map<String, Any>? = null
): iEvent