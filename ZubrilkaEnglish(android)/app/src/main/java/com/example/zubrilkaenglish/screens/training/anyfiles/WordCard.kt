package com.example.zubrilkaenglish.screens.training.anyfiles

data class WordCard(val foreignWord:String,val transcription:String,val translation:String): ICard {
    var cardHasChanged:Boolean=false
    var lookButtonPressed:Boolean=false
    var cardProgress:Int=0


    override fun getItemViewType(): Int {
        return ICard.WORD_CARD_TYPE
    }
}