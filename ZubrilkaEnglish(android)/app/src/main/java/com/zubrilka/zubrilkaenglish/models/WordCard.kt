package com.zubrilka.zubrilkaenglish.models

import androidx.room.Embedded
import androidx.room.Ignore
import com.zubrilka.zubrilkaenglish.screens.training.ICard

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
    @Ignore
    var variants:MutableList<String?>? = null //содержит список ответов при многовариантном режиме обучения
    @Ignore
    var rightPosition:Int? = null //при многовариантном режиме обучения содержит ответ юзера (позицию в списке)
    @Ignore
    var userAnswer:Int? = null //при многовариантном режиме обучения содержит ответ юзера (позицию в списке)



    override fun getItemViewType(): Int {
        return ICard.WORD_CARD_TYPE
    }
}