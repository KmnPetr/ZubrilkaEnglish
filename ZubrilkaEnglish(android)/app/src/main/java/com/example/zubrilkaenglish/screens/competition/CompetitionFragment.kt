package com.example.zubrilkaenglish.screens.competition

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.FragmentCompetitionBinding
import com.example.zubrilkaenglish.events.CmpEvEnum
import com.example.zubrilkaenglish.events.CompetitionEvent
import com.example.zubrilkaenglish.events.NfEvEnum
import com.example.zubrilkaenglish.events.NotificationEvent
import com.example.zubrilkaenglish.events.VcEvEnum
import com.example.zubrilkaenglish.events.VoiceEvent
import com.example.zubrilkaenglish.events.iEvent
import com.example.zubrilkaenglish.models.Profile
import com.example.zubrilkaenglish.models.Voice
import com.example.zubrilkaenglish.models.socketDto.ClickResult
import com.example.zubrilkaenglish.models.socketDto.DuelInfo
import com.example.zubrilkaenglish.models.socketDto.NextWord
import com.example.zubrilkaenglish.models.socketDto.StatusInfo
import com.example.zubrilkaenglish.models.socketDto.StatusPlayer
import com.example.zubrilkaenglish.onlineCompetition.SocketHolder
import com.example.zubrilkaenglish.screens.PopupInfo
import com.example.zubrilkaenglish.screens.competition.popup.PopupSearchOpponent
import com.example.zubrilkaenglish.screens.training.popup.PopupFinishInfo
import com.example.zubrilkaenglish.services.VibrationHandler
import com.example.zubrilkaenglish.utils.ui.CustButton
import com.example.zubrilkaenglish.utils.ui.HealthStrip
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class CompetitionFragment : Fragment() {

    private lateinit var viewModel: CompetitionViewModel
    private lateinit var binding: FragmentCompetitionBinding
    private val socketHolder = SocketHolder.instance
    private lateinit var popupSearchOpponent: PopupSearchOpponent

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCompetitionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel= ViewModelProvider(this).get(CompetitionViewModel::class.java)
        popupSearchOpponent = PopupSearchOpponent(requireContext(),viewModel,viewLifecycleOwner)

        setupListeners()
        overrideClickBack()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        //смена фона
        EventBus.getDefault().post(NotificationEvent(R.drawable.bac32.toString(), NfEvEnum.CHANGE_BACKGROUND))
        EventBus.getDefault().post(NotificationEvent("Online", NfEvEnum.CHANGE_TITLE)) //смена титла на тулбаре
        GlobalScope.launch {
            socketHolder.socketConnect()
        }
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().post(CompetitionEvent(CmpEvEnum.CLOSE_SESSION))
        EventBus.getDefault().unregister(this) //подписываемся на EventBus
    }

    /**
     * метод используется библиотечкой green robot
     * при публикации кем-то события CompetitionEvent
     */
    @Subscribe
    fun <T : Enum<T>, E : iEvent<T>> receiveEvent(event: E){
        when(event){
            is CompetitionEvent -> {
                when(event.typeEvent){
                    CmpEvEnum.CLICK_RESULT -> receiveClickResult(event.properties["clickResult"] as ClickResult)
                    else -> {}
                }
            }
            is NotificationEvent -> {
                when(event.typeEvent){
                    NfEvEnum.POPUP_INFO -> PopupInfo(requireContext(),R.string.information_online).show()
                    else -> {}
                }
            }
        }
    }

    /**
     * обработает пришедший с сервера результат по выбору ответа
     */
    private fun receiveClickResult(clickResult: ClickResult) {

        val listButtons: Array<CustButton> = arrayOf(binding.variant0,binding.variant1,binding.variant2,binding.variant3)

        if (clickResult.idPlayer == viewModel.duelInfo.value?.ownId){ //если информация о пользователе этой мобилы
            //настройка вибрации
            if(clickResult.isRight == true) VibrationHandler.instance.vibratePositive()
            else VibrationHandler.instance.vibrateNegative()

            listButtons.forEachIndexed { index, it->
                it.isEnabled = false
                if (index.equals(clickResult.rightPos)){
                    it.setBackgroundColor(Color.parseColor("#B4F469"))
                } else if(index.equals(clickResult.wrongPos)){
                    it.setBackgroundColor(Color.parseColor("#FF7B3D"))
                } else if(index.equals(viewModel.opponentWrongPos)){
                   //скорее всего раньше окрашена была в серый
                } else{
                    it.visibility = View.INVISIBLE
                }
            }

            binding.ownHealthStrip.setHealth(clickResult.newHealth)

        } else if (clickResult.idPlayer == viewModel.opponentId){ //если информация пришла о противнике

            //настройка вибрации
            if(clickResult.isRight == true) VibrationHandler.instance.vibratePositive()
            else VibrationHandler.instance.vibratePositive()

            viewModel.opponentWrongPos = clickResult.wrongPos


            listButtons.forEachIndexed { index, it->
                if(index.equals(clickResult.wrongPos)){
                    it.visibility = View.VISIBLE
                    it.isEnabled = false
                    it.setBackgroundColor(Color.parseColor("#CCCCCC"))
                }
            }

            binding.opponentHealthStrip.setHealth(clickResult.newHealth)
        }

    }

    //установит слушатели на различные кнопки и LiveData
    private fun setupListeners() {
        viewModel.profile.observe(viewLifecycleOwner){showProfile(it)}
        viewModel.ping.observe(viewLifecycleOwner){ showPing(it) }
        viewModel.duelInfo.observe(viewLifecycleOwner){ showDuelInfo(it) }
        viewModel.startCountDown.observe(viewLifecycleOwner){ showCountDown(it) }
        viewModel.nextWord.observe(viewLifecycleOwner){
            configureCountWords(it)
            configureWordBlock(it)
        }
        viewModel.ownHealth.observe(viewLifecycleOwner){ changeHealth(it,binding.ownHealthStrip) }
        viewModel.opponentHealth.observe(viewLifecycleOwner){ changeHealth(it,binding.opponentHealthStrip) }
        viewModel.finishInfo.observe(viewLifecycleOwner){
            if (it!=null) PopupFinishInfo(requireContext(),it,viewModel).show()
        }
        viewModel.statusInfo.observe(viewLifecycleOwner){ receiveStatusInfo(it) }
    }

    /**
     * покажетнекоторые данные по профилю
     */
    private fun showProfile(profile: Profile?) {
        if (profile!=null) binding.ownName.text = profile.name
    }

    private fun receiveStatusInfo(statusInfo: StatusInfo?) {
        if (statusInfo!=null){
            if (statusInfo.statusPlayer == StatusPlayer.BUSY||statusInfo.statusPlayer == StatusPlayer.WAITING){
                popupSearchOpponent.show()
            }
        }
    }

    /**
     * вызывается при изменении показателя здоровья игрока
     */
    private fun changeHealth(health: Int?, healthStrip: HealthStrip) {
        if (health != null){
            healthStrip.visibility = View.VISIBLE
            healthStrip.setHealth(health)
        } else {
            binding.opponentHealthStrip.visibility = View.GONE
        }
    }

    /**
     * покажет информацию о соперниках их имена, количество здоровья и другое
     */
    private fun showDuelInfo(duelInfo: DuelInfo?) {
        if (duelInfo == null){
            binding.ownName.text = viewModel.profile.value?.name ?: "Name"
            binding.opponentName.text = ""
            binding.opponentName.visibility = View.GONE
        } else{
            binding.ownName.text = duelInfo.listShortNames.get(duelInfo.ownPosition)

            var opponentPosition:Int? = null
            if(duelInfo.ownPosition == 0 && duelInfo.listHealth.size==2 && duelInfo.listShortNames.size==2){
                opponentPosition = 1
            } else if(duelInfo.ownPosition == 1 && duelInfo.listHealth.size==2 && duelInfo.listShortNames.size==2){
                opponentPosition = 0
            }

            binding.opponentName.text = opponentPosition?.let { it1 -> duelInfo.listShortNames.get(it1).toString() }
            binding.opponentName.visibility = View.VISIBLE
        }
    }

    /**
     * покажет обратный отсчет перед стартом игры
     */
    private fun showCountDown(it: Int?) {
        if (it==null){
            binding.countDown.text = ""
            binding.countDown.visibility = View.GONE
        } else {
            binding.countDown.text = it.toString()
            binding.countDown.visibility = View.VISIBLE
        }
    }

    /**
     * показывает пинг на экране
     */
    private fun showPing(ping: Long?) {
        if (ping!=null){
            binding.ping.setText(ping.toString())
            if (ping<=200L) binding.ping.setTextColor(Color.GREEN)
            else binding.ping.setTextColor(Color.parseColor("#FF9800"))
        }else{
            binding.ping.setText("There is no connection")
            binding.ping.setTextColor(Color.parseColor("#FF9800"))
        }
    }

    /**
     * настроит отображение информации о количестве карточек в поединке и о номере текущей карточки
     * информация получена с сервера
     */
    private fun configureCountWords(nextWord: NextWord?) {
        if (nextWord!=null){
            binding.countCards.text = (nextWord.curWordPos+1).toString()+" из "+nextWord.countWords
            binding.countCards.visibility = View.VISIBLE
        }else{
            binding.countCards.text = ""
            binding.countCards.visibility = View.INVISIBLE
        }
    }

    /**
     * настроит отображение блока со словом и выборами вариантов ответов
     */
    private fun configureWordBlock(nextWord: NextWord?) {
        val listButtons: Array<CustButton> = arrayOf(binding.variant0,binding.variant1,binding.variant2,binding.variant3)

        if (nextWord!=null){
            binding.wordBlock.visibility = View.VISIBLE
            binding.foreignWord.text = nextWord.word?.foreignWord ?: ""
            binding.transcription.text = nextWord.word?.transcription ?: ""
            try {
                listButtons.forEachIndexed{ index, it ->
                    it.text = nextWord.listAnswers[index]
                    it.setBackgroundColor(Color.parseColor("#FFFFFFFF"))
                    it.setOnClickListener { clickVariant(index) }
                    it.isEnabled = true
                    it.visibility = View.VISIBLE
                it.requestLayout()
                }
            binding.wordBlock.requestLayout()
            } catch (e:Exception){e.printStackTrace()}
            if (nextWord.word!=null&& nextWord.word?.link_voice !=null)
                EventBus.getDefault().post(VoiceEvent(VcEvEnum.PLAY_VOICE, Voice(nextWord.word!!.link_voice!!,null)))
        } else {
            binding.wordBlock.visibility = View.GONE
            binding.foreignWord.text = ""
            binding.transcription.text = ""
            listButtons.forEach {
                it.text = ""
//                it.setBackgroundColor(Color.parseColor("#FFFFFFFF"))
                it.setOnClickListener(null)
            }
        }
    }

    /**
     * обработает клик по одному из вариантов
     */
    private fun clickVariant(position: Int) {
        EventBus.getDefault().post(CompetitionEvent(CmpEvEnum.CLICK_ANSWER, mutableMapOf("position" to position)))
        println("Click position: $position")
    }


    /**
     * переопределяет поведение системой кнопки "Back"
     */
    private fun overrideClickBack() {
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    EventBus.getDefault().post(NotificationEvent("",NfEvEnum.GO_TO_UPSTACK))
                }
            })
    }
}