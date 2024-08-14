package com.zubrilka.zubrilkaenglish.events

import com.zubrilka.zubrilkaenglish.models.Memo

class MemoEvent(
    override val typeEvent: MmEvEnum,
    val memo: Memo,
    override var properties: MutableMap<String, Any> = mutableMapOf()
): iEvent<MmEvEnum>

enum class MmEvEnum{
    DELETE_MEMO,
    CREATE_MEMO
}