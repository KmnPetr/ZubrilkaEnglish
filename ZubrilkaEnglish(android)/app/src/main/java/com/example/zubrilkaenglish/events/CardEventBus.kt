package com.example.zubrilkaenglish.events

import androidx.lifecycle.MutableLiveData
import com.example.zubrilkaenglish.models.WordCard
import java.lang.IllegalArgumentException

class CardEventBus private constructor(){
    companion object{
        val instance: CardEventBus by lazy { CardEventBus() }
    }


    private val cardEvent: Map<String,MutableLiveData<WordCard>> = mapOf(
        "intention_increase_progress_card" to MutableLiveData(),
        "intention_reset_numCorrAnsv" to MutableLiveData(),
        "wordCard_has_changed" to MutableLiveData(),
        "suggest_put_card_sleep" to MutableLiveData(),
        "set_card_as_learned" to MutableLiveData()
    )

    /**
     * функция выдаст MutableLiveData для подписания на нее как на события
     */
    fun subscribeAnEvent(event: String): MutableLiveData<WordCard>? {
        if (cardEvent.containsKey(event)) return cardEvent[event]
        else throw IllegalArgumentException("the cardEvent does not contain such a key: \"$event\"")
    }
    /**
     * функция позволит другим классам публиковать события
     */
    fun publishEventCard(event: String, wordCard: WordCard){
        if (cardEvent.containsKey(event)) cardEvent[event]?.value = wordCard
        else throw IllegalArgumentException("the cardEvent does not contain such a key: \"$event\"")
    }
}