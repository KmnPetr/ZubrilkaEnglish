package com.zubrilka.zubrilkaenglish.screens.training.additionalCards

import com.zubrilka.zubrilkaenglish.screens.training.ICard

/**
 * класс карточка вставляется в конец учебного списка
 * предлагает начать изучение заново или закончить обучение
 */
class ReviewCard: ICard {

    override fun getItemViewType(): Int {
        return ICard.REVIEW_CARD_TYPE
    }
}