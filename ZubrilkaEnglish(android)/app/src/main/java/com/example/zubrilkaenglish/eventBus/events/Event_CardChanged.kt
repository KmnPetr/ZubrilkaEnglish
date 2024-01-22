package com.example.zubrilkaenglish.eventBus.events

import com.example.zubrilkaenglish.models.WordCard

class Event_CardChanged(
    val wordCard: WordCard
) : iCardEvent