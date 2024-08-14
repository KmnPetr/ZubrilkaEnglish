package com.zubrilka.zubrilkaenglish.events

import com.zubrilka.zubrilkaenglish.models.Voice

class VoiceEvent(
    override val typeEvent: VcEvEnum,
    val voice: Voice,
    override var properties: MutableMap<String, Any> = mutableMapOf()
): iEvent<VcEvEnum>

enum class VcEvEnum{
    //намерение воспроизвести озвучку карточки
    PLAY_VOICE
}