package com.example.zubrilkaenglish.screens.training

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.FragmentTrainingBinding
import com.example.zubrilkaenglish.events.CardEvent
import com.example.zubrilkaenglish.events.CrEvEnum
import com.example.zubrilkaenglish.events.NfEvEnum
import com.example.zubrilkaenglish.events.NotificationEvent
import com.example.zubrilkaenglish.events.PrEvEnum
import com.example.zubrilkaenglish.events.PropEvent
import com.example.zubrilkaenglish.events.StatEvEnum
import com.example.zubrilkaenglish.events.StatisticsEvent
import com.example.zubrilkaenglish.events.VcEvEnum
import com.example.zubrilkaenglish.events.VoiceEvent
import com.example.zubrilkaenglish.events.iEvent
import com.example.zubrilkaenglish.models.PropModel
import com.example.zubrilkaenglish.models.Voice
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.repositories.room.PropKey
import com.example.zubrilkaenglish.screens.PopupInfo
import com.example.zubrilkaenglish.screens.training.popup.PopupOptions
import com.example.zubrilkaenglish.services.VibrationHandler
import com.example.zubrilkaenglish.services.ads.YandexAds
import com.example.zubrilkaenglish.utils.LOG
import com.example.zubrilkaenglish.utils.StatProgress
import com.example.zubrilkaenglish.utils.defaultMode
import com.example.zubrilkaenglish.utils.delayFlipping_0
import com.example.zubrilkaenglish.utils.delayFlipping_1
import com.example.zubrilkaenglish.utils.delayFlipping_3
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.IllegalArgumentException
import kotlin.math.abs

/**
 * фрагмент отвечает за отображение основного экрана с процессом изучения карточек
 */
class TrainingFragment() : Fragment(), CardAdapter.Listener {

    private lateinit var viewModel: TrainingViewModel
    private lateinit var binding: FragmentTrainingBinding
    private lateinit var adapter: CardAdapter
    private lateinit var countCards : TextView

    override lateinit var mode: Modes
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

        setupModes()

        adapter = CardAdapter(this)
        binding.viewPager2.adapter=adapter

        customizeViewPager2()

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
        registerOnPageChangeCallback()
    }

    /**
     * настроит выпадающий список с режимами обучения
     */
    private fun setupModes() {
        val learningModes = listOf(Modes.ofHonesty,Modes.multipleChoice)
        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.view_dropdown_item,learningModes.map { it.ruName })
        binding.modes.setAdapter(arrayAdapter)
        mode = viewModel.learningMode.value?: defaultMode
        binding.modes.setText(mode.ruName, false)
        viewModel.learningMode.observe(viewLifecycleOwner) {
            if (mode != it && it!=null) {
                mode = it
                binding.modes.setText(mode.ruName, false)
                Log.d(LOG,"новый мод: $mode")

                recreateAdapter()
            }
        }

        binding.modes.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            if (!learningModes[position].equals(mode)){
                EventBus.getDefault().post(PropEvent(PrEvEnum.UPDATE_REQUEST, PropModel(PropKey.learningMode.key,learningModes[position].name)))
            }
        }
    }

    //пересоздаст заново адаптер, в основном нужен при смене режима обучения
    private fun recreateAdapter(){
        adapter = CardAdapter(this)
        binding.viewPager2.adapter = adapter
        viewModel.overwriteList()
        countCards.text = "( ${binding.viewPager2.currentItem + 1} / ${viewModel.countWordCards} )"
    }

    //кастомизирует вид ViewPager2
    private fun customizeViewPager2() {
        binding.viewPager2.offscreenPageLimit =3
        binding.viewPager2.clipToPadding = false
        binding.viewPager2.clipChildren = false
        binding.viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val transformer = CompositePageTransformer()
//        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer{page,position->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.14f
        }
        binding.viewPager2.setPageTransformer(transformer)
    }


    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)

        EventBus.getDefault().post(NotificationEvent(R.drawable.bac40.toString(), NfEvEnum.CHANGE_BACKGROUND))  //смена фона
        EventBus.getDefault().post(NotificationEvent("Обучение", NfEvEnum.CHANGE_TITLE)) //смена титла на тулбаре
        //перезапустим счетчики поинтов
        EventBus.getDefault().post(StatisticsEvent(StatEvEnum.START_TRAINING))
    }

    override fun onPause() {
        super.onPause()
        //отправим на сервер поинты
        EventBus.getDefault().post(StatisticsEvent(StatEvEnum.STOP_TRAINING))
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    /**
     * метод используется библиотечкой green robot
     * при публикации кем-то события Event_Changed
     */
    @Subscribe
    fun <T : Enum<T>, E : iEvent<T>> receiveEvent(event: E){
        when(event){
            is CardEvent -> {
                when(event.typeEvent){
                    CrEvEnum.CARD_CHANGED -> {
                        adapter.notifyItemChanged(event.properties.get("positionAdapter") as Int)

                        flippingCard(event.wordCard)
                    }
                    CrEvEnum.SLEEP_EVENT -> {
                        if (viewModel.learningMode.value == Modes.ofHonesty){
                            //отменим перелистывание
                            viewModel.userScrolls = 0
                            //покажем вьюшку с предложением усыпить карточку
                            event.wordCard.sleepEvent = true
                            adapter.notifyItemChanged(event.properties.get("positionAdapter") as Int)
                        } else {
                            //просто отправим интент на усыпление,
                            // в режиме многовариантного выбора диалог по поводу количества дней спячки не предусмотрен
                            var countDay = 0
                            when(event.wordCard.progressWord?.statProgress){
                                StatProgress.NEW.value -> countDay = 5
                                StatProgress.PARTIALLY_LEARNED.value -> countDay = 9
                                StatProgress.ALMOST_LEARNED.value ->countDay = 0
                            }
                            EventBus.getDefault().post(
                                CardEvent(
                                    CrEvEnum.INTENT_SLEEP,
                                    event.wordCard,
                                    mutableMapOf(
                                        "countDay" to countDay,
                                        "positionAdapter" to event.properties.get("positionAdapter") as Int
                                    )
                                )
                            )
                        }
                    }
                    else -> {}
                }
            }
            is NotificationEvent -> {
                when(event.typeEvent){
                    NfEvEnum.POPUP_INFO -> PopupInfo(requireContext(), R.string.information_training).show()
                    else -> {}
                }
            }
        }
    }

    /**
     * слушатель при нажатии на кнопку "Yes"
     * если пользователь подтверждает, что знает карточку
     */
    override fun onClickYesButton(wordCard: WordCard, position: Int) {
        wordCard.cardHasChanged=true
        VibrationHandler.instance.vibratePositive()
        EventBus.getDefault().post(CardEvent(CrEvEnum.INCREASE_PROGRESS,wordCard, mutableMapOf("positionAdapter" to position)))
        EventBus.getDefault().post(StatisticsEvent(StatEvEnum.POINTS_INCR))
    }

    /**
     * слушатель при нажатии на кнопку "No"
     * если пользователь не узнает карточку
     */
    override fun onClickNoButton(wordCard: WordCard, position: Int) {
        wordCard.cardHasChanged=true
        VibrationHandler.instance.vibrateNegative()
        //отправим запрос на сброс значения numCorrAnsv
        EventBus.getDefault().post(CardEvent(CrEvEnum.RESET_numCorrAnsv, wordCard, mutableMapOf("positionAdapter" to position)))
        EventBus.getDefault().post(StatisticsEvent(StatEvEnum.POINTS_INCR))
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
    override fun onClickOptionsButton(wordCard: WordCard, position: Int) {
        PopupOptions(requireActivity(),wordCard,position).show()
    }

    /**
     *  метод инициирует озвучку карточки
     */
    fun playVoice(wordCard: WordCard) {
        if (wordCard.word.link_voice != null){
            //отправим запрос на воспроизведение звука
            EventBus.getDefault().post(VoiceEvent(VcEvEnum.PLAY_VOICE, Voice(wordCard.word.link_voice,null)))}
    }

    /**
     * покажет межстраничную рекламу
     */
    fun showYandexAds() {
        if (!viewModel.yandexAdWasShown){
            viewModel.yandexAdWasShown = true
            YandexAds.instanse.showAd(requireActivity())
        }
    }

    /**
     * перелистывание фрагмента на следующий
     * с защитой от перелистывания во время скролла пальцем
     */
    private fun flippingCard(wordCard: WordCard){
        viewModel.userScrolls = 1
        GlobalScope.launch{

            when(viewModel.learningMode.value){ //на разные режимы разное время задержки перелистывания
                Modes.ofHonesty -> delay(delayFlipping_0)
                Modes.multipleChoice -> {
                    if (wordCard.userAnswer == wordCard.rightPosition){
                        delay(delayFlipping_3) //при удачном откадывании карточки быстро перелистываем
                    } else delay(delayFlipping_1) //при неудачном отгадывании даем юзеру время на посмотреть подумать
                }
                else -> { throw IllegalArgumentException("LearningMode is invalid") }
            }

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

    /**
     * будет следить за изменением страницы и вызывать различные функции
     */
    fun registerOnPageChangeCallback(){

        val viewPager2 = binding.viewPager2

        countCards.text = "( ${binding.viewPager2.currentItem + 1} / ${viewModel.countWordCards} )"

        viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                //вызываем voice на первой странице
                if (position == 0 && adapter.isWordCard(position)){
                    GlobalScope.launch {
                        delay(500)
                        withContext(Dispatchers.Main){
                            playVoice(adapter.getCurrentCard(position) as WordCard)
                        }
                    }
                }
            }
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)

                val position = viewPager2.currentItem

                //вызываем voice если это учебная карточка
                if (state == 0 && adapter.isWordCard(position) && position != 0){
                    playVoice(adapter.getCurrentCard(position) as WordCard)
                }

                //покажем рекламу если это последняя карточка
                if (state == 0 && position == ((viewPager2.adapter?.itemCount ?: 0) - 1)){
                    //отправим поинты на сервер
                    EventBus.getDefault().post(StatisticsEvent(StatEvEnum.STOP_TRAINING))
                    //покажем рекламу
                    GlobalScope.launch {
                        delay(350) //небольшая задержка чтоб прогрузилось все и не сразу выпригивала реклама
                        withContext(Dispatchers.Main){
                            showYandexAds()
                        }
                    }
                }
            }
        })
    }

    /**
     * завершит обучение
     * закроет фрагмент
     */
    override fun completeTraining() {
        findNavController().popBackStack(findNavController().graph.startDestinationId, false)
    }

    /**
     * начнет обучение заново
     * перезапросит список для обучения
     */
    override fun restartTraining() {
        viewModel.overwriteList()
        binding.viewPager2.currentItem = 0
        //отправим поинты на сервер, перезапустим счетчики поинтов
        EventBus.getDefault().post(StatisticsEvent(StatEvEnum.STOP_TRAINING))
        EventBus.getDefault().post(StatisticsEvent(StatEvEnum.START_TRAINING))
    }
}

/**
 * содержит значения режимов обучения
 */
enum class Modes(val ruName:String){
    ofHonesty("На честность"), //в этом режиме пользователь сам должен ответить знает ли карточку
    multipleChoice("Многовариантный выбор") //пользователь должен будет выбрать из  нескольких предложенных
}