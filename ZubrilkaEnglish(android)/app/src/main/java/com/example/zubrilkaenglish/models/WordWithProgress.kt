package com.example.zubrilkaenglish.models

import androidx.room.Embedded

data class WordWithProgress(
    @Embedded
    val word: Word,
    @Embedded
    val progressWord: ProgressWord
)
