package com.example.zubrilkaenglish.screens.training

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.ViewFewCardsBinding
import com.example.zubrilkaenglish.databinding.ViewNoMemosCardsBinding
import com.example.zubrilkaenglish.databinding.ViewReviewCardBinding
import com.example.zubrilkaenglish.databinding.ViewWordCardBinding
import com.example.zubrilkaenglish.events.CardEvent
import com.example.zubrilkaenglish.events.CrEvEnum
import com.example.zubrilkaenglish.events.NfEvEnum
import com.example.zubrilkaenglish.events.NotificationEvent
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.screens.training.additionalCards.FewCards
import com.example.zubrilkaenglish.screens.training.additionalCards.NoMemosCard
import com.example.zubrilkaenglish.screens.training.additionalCards.ReviewCard
import com.example.zubrilkaenglish.utils.StatProgress
import org.greenrobot.eventbus.EventBus

class ViewHolderFactory {
    class WordCardHolder(item: View): RecyclerView.ViewHolder(item){
        val binding = ViewWordCardBinding.bind(item)
        fun bind(wordCard: WordCard, listener: CardAdapter.Listener,position:Int){
            binding.numCorrAnsv.text = "("+wordCard.progressWord?.numCorrAnsv.toString()+")"
            binding.statusCard.text = "status: "+ wordCard.progressWord?.statProgress

            binding.foreignWord.text = wordCard.word.foreignWord
            binding.transcription.text = wordCard.word.transcription
            binding.translation.text = wordCard.word.translation

            //блок if else решает проблему переиспользуемости холдеров
            if (wordCard.cardHasChanged){
                binding.translation.visibility=View.VISIBLE
                binding.yesButton.isEnabled=false
                binding.noButton.isEnabled=false
                binding.lookButton.isEnabled=false
            }else{
                binding.translation.visibility=View.INVISIBLE
                binding.yesButton.isEnabled=true
                binding.noButton.isEnabled=true
                binding.lookButton.isEnabled=true
                if (wordCard.lookButtonPressed/*проверка нажатости кнопки look*/){
                    binding.translation.visibility=View.VISIBLE
                    binding.lookButton.isEnabled=false
                }
            }

            binding.yesButton.setOnClickListener {
                listener.onClickYesButton(wordCard, position)
            }

            binding.noButton.setOnClickListener {
                listener.onClickNoButton(wordCard, position)
            }

            binding.lookButton.setOnClickListener {
                listener.onClickLookButton(wordCard)
            }
            binding.optionsButton.setOnClickListener{
                listener.onClickOptionsButton(wordCard, position)
            }

            sleepDialogShow(wordCard)
        }

        /**
         * отрисует или спрячет окошко с предложением усыпить карточку
         */
        private fun sleepDialogShow(wordCard: WordCard) {
            if (wordCard.sleepEvent){
                binding.sleepDialog.visibility = View.VISIBLE

                binding.textDialog.text = "Кажется вы уже запомнили эту карточку. Рекомендуем вам повторить ее спустя некоторое время. Карточка уснет на "+binding.slider.value.toInt().toString()+ " дня(дней)."
                binding.slider.addOnChangeListener { slider, value, fromUser ->
                    binding.textDialog.text = "Кажется вы уже запомнили эту карточку. Рекомендуем вам повторить ее спустя некоторое время. Карточка уснет на "+value.toInt().toString()+ " дня(дней)."
                }
                //Настройка слайдера
                when(wordCard.progressWord?.statProgress){
                    StatProgress.NEW.value ->{
                        binding.slider.valueTo = 10F
                        binding.slider.value = 5F
                    }
                    StatProgress.PARTIALLY_LEARNED.value ->{
                        binding.slider.valueTo = 18F
                        binding.slider.value = 9F
                    }
                    StatProgress.ALMOST_LEARNED.value ->{
                        binding.slider.value = 0F
                        binding.slider.visibility = View.GONE
                        binding.textDialog.text = "Кажется вы уже запомнили эту карточку. Нажав \"OK\", вы перенесете эту карточку в группу \"изученные\"."
                    }
                }

                binding.btnYes.setOnClickListener {
                    wordCard.sleepEvent = false
                    //отправим желание пользователя на усыпление карточки
                    EventBus.getDefault().post(
                        CardEvent(
                            CrEvEnum.INTENT_SLEEP,
                            wordCard,
                            mutableMapOf(
                                "countDay" to binding.slider.value.toInt(),
                                "positionAdapter" to position
                            )
                        )
                    )
                    binding.sleepDialog.visibility = View.GONE
                }
                binding.btnCansel.setOnClickListener {
                    wordCard.sleepEvent = false
                    //просто уведомим view чтобы она перелистнула пейджер
                    //по сути изменений нет
                    EventBus.getDefault().post(
                        CardEvent(CrEvEnum.CARD_CHANGED, wordCard, mutableMapOf("positionAdapter" to position))
                    )
                    binding.sleepDialog.visibility = View.GONE
                }

            } else {
                binding.sleepDialog.visibility = View.GONE
            }
        }
    }
    class ReviewCardHolder(item: View): RecyclerView.ViewHolder(item){
        val binding= ViewReviewCardBinding.bind(item)

        fun bind(reviewCard: ReviewCard, listener: CardAdapter.Listener){
            binding.btnNo.setOnClickListener {
                listener.completeTraining()
            }
            binding.btnYes.setOnClickListener {
                listener.restartTraining()
            }
        }
    }
    class FewCardsHolder(item: View): RecyclerView.ViewHolder(item){
        val binding= ViewFewCardsBinding.bind(item)

        fun bind(fewCards: FewCards, listener: CardAdapter.Listener){
            binding.goToCatalog.setOnClickListener {
                EventBus.getDefault().post(NotificationEvent("GO_TO_CATALOG_EVENT", NfEvEnum.GO_TO_CATALOG))
            }
        }
    }
    class NoMemosCardsHolder(item: View): RecyclerView.ViewHolder(item){
        val binding = ViewNoMemosCardsBinding.bind(item)

        fun bind(noMemosCard: NoMemosCard, listener: CardAdapter.Listener){
            binding.goToMemos.setOnClickListener {
                EventBus.getDefault().post(NotificationEvent("GO_TO_MEMOS_EVENT", NfEvEnum.GO_TO_MEMOS))
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            when(viewType){
                ICard.WORD_CARD_TYPE ->{
                    val view = LayoutInflater.from(parent.context).inflate(R.layout.view_word_card,parent,false)
                    return WordCardHolder(view)
                }
                ICard.REVIEW_CARD_TYPE ->{
                    val view = LayoutInflater.from(parent.context).inflate(R.layout.view_review_card,parent,false)
                    return ReviewCardHolder(view)
                }
                ICard.FEW_CARDS_TYPE -> {
                    val view = LayoutInflater.from(parent.context).inflate(R.layout.view_few_cards,parent,false)
                    return FewCardsHolder(view)
                }
                ICard.NO_MEMOS_CARD_TYPE -> {
                    val view = LayoutInflater.from(parent.context).inflate(R.layout.view_no_memos_cards,parent,false)
                    return NoMemosCardsHolder(view)
                }
                else->{
                    throw java.lang.IllegalStateException("Invalid rating param value")
                }
            }
        }

        /**
         * вынесли метод построения холдера из адаптера, чтоб все здесь было а не там
         */
        fun onBindViewHolder(holder: RecyclerView.ViewHolder, card: ICard, listener: CardAdapter.Listener, position: Int) {
            val viewType = card.getItemViewType()

            when(viewType){
                ICard.WORD_CARD_TYPE ->{
                    (holder as WordCardHolder).bind((card as WordCard),listener,position)
                }
                ICard.REVIEW_CARD_TYPE ->{
                    (holder as ReviewCardHolder).bind((card as ReviewCard),listener)
                }
                ICard.FEW_CARDS_TYPE -> {
                    (holder as FewCardsHolder).bind((card as FewCards),listener)
                }
                ICard.NO_MEMOS_CARD_TYPE -> {
                    (holder as NoMemosCardsHolder).bind((card as NoMemosCard),listener)
                }
                else->{
                    throw java.lang.IllegalStateException("Invalid rating param value")
                }
            }

        }
    }
}