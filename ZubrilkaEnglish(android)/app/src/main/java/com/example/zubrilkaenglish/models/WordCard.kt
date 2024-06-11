package com.example.zubrilkaenglish.models

import androidx.room.Embedded
import androidx.room.Ignore
import com.example.zubrilkaenglish.screens.training.ICard

data class WordCard(
    @Embedded
    val word: Word,
    @Embedded
    var progressWord: ProgressWord?
): ICard {
    @Ignore
    var cardHasChanged:Boolean=false
    @Ignore
    var lookButtonPressed:Boolean=false
    @Ignore
    var voiceSounded:Boolean=false
    @Ignore
    var sleepEvent:Boolean=false //указывает на то что было предложено усыпить карточку

    override fun getItemViewType(): Int {
        return ICard.WORD_CARD_TYPE
    }
}