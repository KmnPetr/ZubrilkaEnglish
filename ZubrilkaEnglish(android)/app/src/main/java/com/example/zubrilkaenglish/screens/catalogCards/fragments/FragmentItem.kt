package com.example.zubrilkaenglish.screens.catalogCards.fragments

import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.screens.catalogCards.CatalogCardsFragment

interface FragmentItem {
    val owner: CatalogCardsFragment
    fun onClickFolder(positionFolder: Int)
    fun rollBackRecycler()
}