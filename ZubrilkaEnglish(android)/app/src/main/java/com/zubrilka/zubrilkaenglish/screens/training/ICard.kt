package com.zubrilka.zubrilkaenglish.screens.training

interface ICard {

    /**
     *константные переменные помогают при определении типа наследника в коллекции
     */
    companion object{
        const val WORD_CARD_TYPE: Int = 0
        const val REVIEW_CARD_TYPE: Int = 1
        const val FEW_CARDS_TYPE: Int = 2
        const val NO_MEMOS_CARD_TYPE: Int = 3
    }

    fun getItemViewType():Int
}