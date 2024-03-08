package com.example.zubrilkaenglish.events

import com.example.ze_adminandroid.models.Word


class WordEvent(
    override val typeEvent: CrEvEnum,
    //желательно чтобы ответом возвращалась та же ссылка на wordCard,
    // чтобы например в viewModels и adapters не писать дополнительный код по замене wordCard,
    // оно само там ссылочно обновляется
    var word: Word,
    var properties: Map<String, Any>? = null
): iEvent<CrEvEnum>

enum class CrEvEnum{
}