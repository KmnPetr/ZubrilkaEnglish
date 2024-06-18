package com.example.zubrilkaenglish.events

class NotificationEvent(
    val message: String,
    override val typeEvent: NfEvEnum,
    override var properties: MutableMap<String, Any> = mutableMapOf()
    ) : iEvent<NfEvEnum>

enum class NfEvEnum{
    LIMIT_ACTIVE_WORDS,
    GO_TO_CATALOG, //попытка перейти в каталог карт из любого фрагмента
    GO_TO_MEMOS //переход в каталог напоминаний из любого фрагмента
}