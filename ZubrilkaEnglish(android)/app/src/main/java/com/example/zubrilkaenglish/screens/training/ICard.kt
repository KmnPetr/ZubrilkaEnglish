package com.example.zubrilkaenglish.screens.training

interface ICard {

    /**
     *константные переменные помогают при определении типа наследника в коллекции
     */
    companion object{
        const val WORD_CARD_TYPE: Int=0
        const val REVIEW_CARD_TYPE: Int=1
    }

    fun getItemViewType():Int
}