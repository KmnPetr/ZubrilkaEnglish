package com.example.zubrilkaenglish.eventBus.events

import com.example.zubrilkaenglish.models.WordCard

class CardEvent(
    override val typeEvent: String,
    //желательно чтобы ответом возвращалась та же ссылка на wordCard,
    // чтобы например в viewModels и adapters не писать дополнительный код по замене wordCard,
    // оно само там ссылочно обновляется
    var wordCard: WordCard,
    var properties: Map<String, Any>? = null
): iEvent