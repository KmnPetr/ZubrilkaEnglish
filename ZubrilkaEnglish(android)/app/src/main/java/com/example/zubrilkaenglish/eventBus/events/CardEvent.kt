package com.example.zubrilkaenglish.eventBus.events

import com.example.zubrilkaenglish.models.WordCard

class CardEvent(
    override val typeEvent: String,
    val wordCard: WordCard,
    var properties: Map<String, Any>? = null
): iEvent