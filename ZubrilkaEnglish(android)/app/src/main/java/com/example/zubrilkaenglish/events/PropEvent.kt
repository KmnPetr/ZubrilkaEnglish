package com.example.zubrilkaenglish.events

import com.example.zubrilkaenglish.models.PropModel

class PropEvent(
    override val typeEvent: PrEvEnum,
    //желательно чтобы ответом возвращалась та же ссылка на wordCard,
    // чтобы например в viewModels и adapters не писать дополнительный код по замене wordCard,
    // оно само там ссылочно обновляется
    var propModel: PropModel,
    override var properties: MutableMap<String, Any> = mutableMapOf()
): iEvent<PrEvEnum>

enum class PrEvEnum{
    //запрос на обновление проперти
    UPDATE_REQUEST
}