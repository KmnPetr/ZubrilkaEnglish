package com.example.zubrilkaenglish.models

import androidx.room.Embedded
import androidx.room.Ignore

data class WordCard(
    @Embedded
    val word: Word,
    @Embedded
    var progressWord: ProgressWord
): ICard {
    @Ignore
    var cardHasChanged:Boolean=false
    @Ignore
    var lookButtonPressed:Boolean=false

    override fun getItemViewType(): Int {
        return ICard.WORD_CARD_TYPE
    }
}