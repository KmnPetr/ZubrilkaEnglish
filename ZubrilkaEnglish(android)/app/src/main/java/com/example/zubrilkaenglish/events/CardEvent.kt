package com.example.zubrilkaenglish.events

import com.example.zubrilkaenglish.models.WordCard

class CardEvent(
    override val typeEvent: CrEvEnum,
    //желательно чтобы ответом возвращалась та же ссылка на wordCard,
    // чтобы например в viewModels и adapters не писать дополнительный код по замене wordCard,
    // оно само там ссылочно обновляется
    var wordCard: WordCard,
    var properties: Map<String, Any>? = null
): iEvent<CrEvEnum>

enum class CrEvEnum{
    //намерение пользователя усыпить карточку
    INTENT_SLEEP,
    //увеличение прогресса по карточке
    INCREASE_PROGRESS,
    //сброс значения карточки "numCorrAnsv"
    RESET_numCorrAnsv,
    //намерение пользователя пометить карточку выученной
    SET_AS_LEARNED,
    //намерение пользователя добавить карточку в изучаемые
    ADD_WORD_TO_TRAINING,
    //намерение пользователя сбросить достигнутый прогресс по карточке
    RESET_PROGRESS,
    //намерение пользователя удалить карточку из списка изучаемых
    DELETE_CARD,
    //информирует пользователя том что карточку пора усыпить
    SLEEP_EVENT,
    //репо уведомляет view о том что содержимое карточки изменено для изменения отображения ее на view
    CARD_CHANGED
}