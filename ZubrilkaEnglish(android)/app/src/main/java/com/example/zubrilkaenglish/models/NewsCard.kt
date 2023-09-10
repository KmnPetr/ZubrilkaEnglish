package com.example.zubrilkaenglish.models

import com.example.zubrilkaenglish.models.ICard

class NewsCard(val news:String): ICard {

    override fun getItemViewType(): Int {
        return ICard.NEWS_CARD_TYPE
    }
}