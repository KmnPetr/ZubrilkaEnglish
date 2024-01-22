package com.example.zubrilkaenglish.eventBus.events

import com.example.zubrilkaenglish.models.WordCard

/**
 * запрос на сброс поля numCorrAnsv по карточке
 * в частности его прослушивает и обрабатывает CardsRepository
 */
class Event_Reset_numCorrAnsv(
    val wordCard: WordCard
): iCardEvent