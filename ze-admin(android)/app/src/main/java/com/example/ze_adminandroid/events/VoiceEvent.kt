package com.example.ze_adminandroid.events

import com.example.ze_adminandroid.models.Voice
import com.example.zubrilkaenglish.events.iEvent

class VoiceEvent(
    override val typeEvent: VcEvEnum,
    val voice: Voice,
    var properties: Map<String, Any>? = null
): iEvent<VcEvEnum>

enum class VcEvEnum{
    //намерение воспроизвести озвучку карточки
    PLAY_VOICE,
    DELETE_FROM_DATABASE
}