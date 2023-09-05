package com.example.zubrilkaenglish.screens.training.anyfiles

class NewsCard(val news:String): ICard {

    override fun getItemViewType(): Int {
        return ICard.NEWS_CARD_TYPE
    }
}