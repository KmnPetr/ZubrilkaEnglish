package com.zubrilka.zubrilkaenglish.screens.catalogCards.fragments

import com.zubrilka.zubrilkaenglish.screens.catalogCards.CatalogCardsFragment

interface FragmentItem {
    val owner: CatalogCardsFragment
    fun onClickFolder(positionFolder: Int)
    fun rollBackRecycler()
}