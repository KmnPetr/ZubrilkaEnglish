package com.example.zubrilkaenglish.eventBus.events

import com.example.zubrilkaenglish.models.WordCard

/**
 * запрос на увеличение прогресса по карточке
 * в частности его прослушивает и обрабатывает CardsRepository
 */
class Event_IncreaseProgressCard(
    val wordCard: WordCard
):iCardEvent