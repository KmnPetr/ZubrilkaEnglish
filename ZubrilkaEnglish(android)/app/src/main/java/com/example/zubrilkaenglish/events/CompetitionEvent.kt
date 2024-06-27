package com.example.zubrilkaenglish.events

class CompetitionEvent(
    override val typeEvent: CmpEvEnum,
    //желательно чтобы ответом возвращалась та же ссылка на wordCard,
    // чтобы например в viewModels и adapters не писать дополнительный код по замене wordCard,
    // оно само там ссылочно обновляется
    override var properties: MutableMap<String, Any> = mutableMapOf()
): iEvent<CmpEvEnum>

enum class CmpEvEnum{
}