package com.example.zubrilkaenglish.eventBus.events

import com.example.zubrilkaenglish.models.WordCard

/**
 * запрос установку прогресс карточки как выученная
 * в частности его прослушивает и обрабатывает CardsRepository
 */
class Event_SetCardAsLearned(
    val wordCard: WordCard
):iCardEvent