package com.example.zubrilkaenglish.screens.training

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.zubrilkaenglish.databinding.FragmentTrainingBinding
import com.example.zubrilkaenglish.events.CardEvent
import com.example.zubrilkaenglish.events.CrEvEnum
import com.example.zubrilkaenglish.events.VcEvEnum
import com.example.zubrilkaenglish.events.VoiceEvent
import com.example.zubrilkaenglish.models.ICard
import com.example.zubrilkaenglish.models.Voice
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.screens.training.popup.PopupDialog
import com.example.zubrilkaenglish.screens.training.popup.PopupOptions
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * фрагмент отвечает за отображение основного экрана с процессом изучения карточек
 */
class TrainingFragment : Fragment(), CardAdapter.Listener {

    private lateinit var viewModel: TrainingViewModel
    private lateinit var binding: FragmentTrainingBinding
    private val adapter = CardAdapter(this)
    private lateinit var countCards : TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentTrainingBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(TrainingViewModel::class.java)
        countCards = binding.countCards


        binding.viewPager2.adapter=adapter

        binding.viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                viewModel.userScrolls = 0
            }
        })

        /**
         * Функция заполняет cardList адаптера
         */
        viewModel.getListForTreining().observe(viewLifecycleOwner){list->
            if (list != null) {
                adapter.setList(list)
            }
            countCards.text = "( ${binding.viewPager2.currentItem + 1} / ${viewModel.countWordCards} )"
        }

        showCountCards()
        automaticVoicePlayback()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }

    /**
     * метод используется библиотечкой green robot
     * при публикации кем-то события Event_Changed
     */
    @Subscribe
    fun event_CardChanged(event: CardEvent){
        when(event.typeEvent){
            CrEvEnum.CARD_CHANGED -> {
                adapter.notifyItemChanged(binding.viewPager2.currentItem)
                flippingCard()
            }
            CrEvEnum.SLEEP_EVENT -> {
                //отменим перелистывание
                viewModel.userScrolls = 0
                //покажем окошко
                PopupDialog(requireContext(),event.wordCard).show()
            }
            else -> {}
        }
    }

    /**
     * воспроизведение голоса при перелистывании новой карточки
     */
    private fun automaticVoicePlayback() {
        binding.viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == 0){
                    val card: ICard = adapter.getCurrentCard(binding.viewPager2.currentItem)
                    if (card is WordCard && !card.voiceSounded){
                        playVoice(card)
                    }
                }
            }
        })
    }


    /**
     * слушатель при нажатии на кнопку "Yes"
     * если пользователь подтверждает, что знает карточку
     */
    override fun onClickYesButton(wordCard: WordCard) {

        wordCard.cardHasChanged=true
        EventBus.getDefault().post(CardEvent(CrEvEnum.INCREASE_PROGRESS,wordCard))
    }

    /**
     * слушатель при нажатии на кнопку "No"
     * если пользователь не узнает карточку
     */
    override fun onClickNoButton(wordCard: WordCard) {
        wordCard.cardHasChanged=true
        //отправим запрос на сброс значения numCorrAnsv
        EventBus.getDefault().post(CardEvent(CrEvEnum.RESET_numCorrAnsv, wordCard))
    }

    /**
     * метод реагирует на нажатие кнопки Look
     */
    override fun onClickLookButton(wordCard: WordCard) {
        wordCard.lookButtonPressed=true
        adapter.notifyItemChanged(binding.viewPager2.currentItem)
    }

    /**
     * функция вызывается при нажатии на кнопку "три точки"
     */
    override fun onClickOptionsButton(wordCard: WordCard) {
        PopupOptions(requireActivity(),wordCard).show()
    }

    /**
     *  метод инициирует озвучку карточки
     */
    override fun playVoice(wordCard: WordCard) {
        if (wordCard.word.link_voice != null){
            //отправим запрос на воспроизведение звука
            EventBus.getDefault().post(VoiceEvent(VcEvEnum.PLAY_VOICE, Voice(wordCard.word.link_voice,null)))}
    }


    /**
     * перелистывание фрагмента на следующий
     * с защитой от перелистывания во время скролла пальцем
     */
    private fun flippingCard(){
        viewModel.userScrolls =1
        GlobalScope.launch{
            delay(700L)
            if (viewModel.userScrolls !=0) {
                viewModel.userScrolls =0
                binding.viewPager2.setCurrentItem((binding.viewPager2.currentItem + 1),true)
            }
        }
    }

    /**
     * метод занимается показом количества карточек находящихся в обудении на данный момент
     */
    fun showCountCards() {
        val viewPager2 = binding.viewPager2

        countCards.text = "( ${binding.viewPager2.currentItem + 1} / ${viewModel.countWordCards} )"

        binding.viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == 0&&adapter.isWordCard(viewPager2.currentItem)){
                    countCards.text = "( ${binding.viewPager2.currentItem + 1} / ${viewModel.countWordCards} )"
                } else if (state == 0&&!adapter.isWordCard(viewPager2.currentItem)){
                    countCards.text = ""
                }
            }
        })
    }
}