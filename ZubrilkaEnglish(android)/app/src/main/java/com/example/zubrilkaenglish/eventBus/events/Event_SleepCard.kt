package com.example.zubrilkaenglish.eventBus.events

import com.example.zubrilkaenglish.models.WordCard

/**
 * класс используется для уведомления пользователя
 * о возможности усыпить карточку на определенное количество дней
 * а также для передачи обратной информации от пользователя о его решении
 */
class Event_SleepCard(
    val wordCard: WordCard,
    //поле окончательного решения юзера, на сколько дней усыпить карточку
    var countDay: Int? = null
): iCardEvent