package com.zubrilka.zubrilkaenglish.screens.training.additionalCards

import com.zubrilka.zubrilkaenglish.screens.training.ICard

/**
 * карточка встает в конец учебного списка
 * предложит юзеру добавить memo памятку
 */
class NoMemosCard: ICard {

    override fun getItemViewType(): Int {
        return ICard.NO_MEMOS_CARD_TYPE
    }
}