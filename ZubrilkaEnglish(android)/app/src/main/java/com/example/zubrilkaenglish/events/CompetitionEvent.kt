package com.example.zubrilkaenglish.events

class CompetitionEvent(
    override val typeEvent: CmpEvEnum,
    override var properties: MutableMap<String, Any> = mutableMapOf()
): iEvent<CmpEvEnum>

enum class CmpEvEnum{
    CLICK_ANSWER //оповестит о клике по одному из ответов на слово, позиция ответа под ключем "position"
}