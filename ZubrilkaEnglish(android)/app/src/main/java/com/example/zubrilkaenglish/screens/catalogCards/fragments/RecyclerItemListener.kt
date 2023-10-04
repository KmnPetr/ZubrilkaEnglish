package com.example.zubrilkaenglish.screens.catalogCards.fragments

import java.text.FieldPosition

interface RecyclerItemListener {

    var isRecyclerChanged: Boolean
    fun onClickFolder(positionFolder: Int)
}