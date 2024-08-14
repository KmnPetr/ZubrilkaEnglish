package com.zubrilka.zubrilkaenglish.events

class CompetitionEvent(
    override val typeEvent: CmpEvEnum,
    override var properties: MutableMap<String, Any> = mutableMapOf()
): iEvent<CmpEvEnum>

enum class CmpEvEnum{
    CLICK_ANSWER, //оповестит о клике по одному из ответов на слово, позиция ответа под ключем "position"

    //содержит результат с сервера клика выбора варианта ответа
    //ключ "clickResult" содержит обьект ClickResult
    CLICK_RESULT,
    //"PenaltyWaiting" штраф игроку за ожидание ответа
    // ключ "idPlayer" id игрока просрочившего ответ
    // ключ "newHealth" новый показатель здоровья
    PEN_WAIT,
    //при желании юзера выйти
    //сигнал на очистку данных сессии поединков
    CLOSE_SESSION,
    //отправит запрос на сервер поставит юзера в режим ожидания соперника
    SET_WAITING_STATUS,
    //отправит запрос на сервер намерение пользователя играть с ботами или не играть
    //ключ "loyalToBots" содержит булевое значение, соответствующее решению юзера
    SET_LOYAL_TO_BOTS;
}