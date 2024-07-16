package com.example.zubrilkaenglish.events

/**
 * класс для передачи сообщений внутри приложения касающиеся статистики игроков
 */
class StatisticsEvent(
    override val typeEvent: StatEvEnum,
    override var properties: MutableMap<String, Any> = mutableMapOf()
) : iEvent<StatEvEnum>

enum class StatEvEnum{
    POINTS_INCR, //при прохождении карточек в офлайн режиме, для инкрементации их количества
    START_TRAINING, //посылаетля при начале офлайн тренировки
    STOP_TRAINING, //посылается при закрытии фрагмента офлайн тренировки
}