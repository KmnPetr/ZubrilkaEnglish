package com.example.ze_adminandroid.events

import com.example.zubrilkaenglish.events.iEvent

class SocketEvent(
    override val typeEvent: SctEvEnum,
    var properties: Map<String, Any>? = null
): iEvent<SctEvEnum>

enum class SctEvEnum{
    //намерение воспроизвести озвучку карточки
    VOICE_ERROR
}