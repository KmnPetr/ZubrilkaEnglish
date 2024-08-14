package com.zubrilka.zubrilkaenglish.events

/**
 * класс со всякими разными ивентами
 * на которые не захотелось реализовывать отдельного класса эвента
 * прослушивается как правило из MainActivity который передает их классу ApiNotification
 */
class NotificationEvent(
    val message: String,
    override val typeEvent: NfEvEnum,
    override var properties: MutableMap<String, Any> = mutableMapOf()
    ) : iEvent<NfEvEnum>

enum class NfEvEnum{
    LIMIT_ACTIVE_WORDS,
    GO_TO_CATALOG, //попытка перейти в каталог карт из любого фрагмента
    GO_TO_MEMOS, //переход в каталог напоминаний из любого фрагмента
    GO_TO_UPSTACK, //поднимет по фрагментам приложения на верх стека
    GO_TO_RATING, //переход в фрагмент таблицы лидеров
    CHANGE_BACKGROUND, //смена фона при переходе на новый фрагмент, в качестве сообщения нужно передать id изображения
    CONNECTION_LOST, //при потере соединения с сервером, в сообщении может быть более подробная информация какую именно информацию не удалось подгрузить
    POPUP_INFO, //если пользователь в тулбаре нажал "i", отправляется с активити на фрагменты
    CHANGE_TITLE //посылается с фрагментов на MainActivity для смены титла на тулбаре
}