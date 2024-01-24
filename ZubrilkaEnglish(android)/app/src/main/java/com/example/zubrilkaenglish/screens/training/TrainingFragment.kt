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
import com.example.zubrilkaenglish.eventBus.events.CardEvent
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
            "card_changed" -> {
                adapter.notifyItemChanged(binding.viewPager2.currentItem)
                flippingCard()
            }
            "sleep_event" -> {
                //отменим перелистывание
                viewModel.userScrolls = 0
                //покажем окошко
                PopupDialog(requireContext(),event.wordCard).show()
            }
        }
    }

    /**
     * слушатель при нажатии на кнопку "Yes"
     * если пользователь подтверждает, что знает карточку
     */
    override fun onClickYesButton(wordCard: WordCard) {

        wordCard.cardHasChanged=true
        EventBus.getDefault().post(CardEvent("increase_progress",wordCard))
    }

    /**
     * слушатель при нажатии на кнопку "No"
     * если пользователь не узнает карточку
     */
    override fun onClickNoButton(wordCard: WordCard) {
        wordCard.cardHasChanged=true
        //отправим запрос на сброс значения numCorrAnsv
        EventBus.getDefault().post(CardEvent("reset_numCorrAnsv", wordCard))
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