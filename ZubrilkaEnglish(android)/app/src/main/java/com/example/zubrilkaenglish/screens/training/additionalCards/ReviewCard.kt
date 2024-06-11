package com.example.zubrilkaenglish.screens.training.additionalCards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.screens.training.ICard

class ReviewCard: ICard {

    override fun getItemViewType(): Int {
        return ICard.REVIEW_CARD_TYPE
    }


    companion object{
        fun getView(parent: ViewGroup):View{
            return LayoutInflater.from(parent.context).inflate(R.layout.view_review_card,parent,false)
        }
    }
}