package com.example.zubrilkaenglish.events

import com.example.zubrilkaenglish.models.Voice

class VoiceEvent(
    override val typeEvent: VcEvEnum,
    val voice: Voice,
    var properties: Map<String, Any>? = null
): iEvent<VcEvEnum>

enum class VcEvEnum{
    //намерение воспроизвести озвучку карточки
    PLAY_VOICE
}