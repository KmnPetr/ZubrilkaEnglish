package com.example.zubrilkaenglish.eventBus.events

import com.example.zubrilkaenglish.models.WordCard

/**
 * отправит в репозиторий желание юзера усыпить карточку
 * отправит желаемое количество дней для усыпления
 */
class Event_IntentSleepCard(
    val wordCard: WordCard,
    //в поле забивается число дней на которые юзер захотел усыпить карточку
    val countDay: Int = 0
): iCardEvent