package com.example.zubrilkaenglish.screens.training.additionalCards

import com.example.zubrilkaenglish.screens.training.ICard

/**
 * класс карточка вставляется последней в учебный список карточек,
 * говорит пользователю, что он изучает слишком мало карточек
 * предлагает перейти в каталог карт за пополнением
 */
class FewCards: ICard {

    override fun getItemViewType(): Int {
        return ICard.FEW_CARDS_TYPE
    }
}