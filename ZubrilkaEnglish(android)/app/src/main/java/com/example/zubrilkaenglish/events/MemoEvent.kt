package com.example.zubrilkaenglish.events

import com.example.zubrilkaenglish.models.Memo

class MemoEvent(
    override val typeEvent: MmEvEnum,
    val memo: Memo,
    var properties: Map<String, Any>? = null
): iEvent<MmEvEnum>

enum class MmEvEnum{
    DELETE_MEMO,
    CREATE_MEMO
}