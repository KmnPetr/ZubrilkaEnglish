package com.example.zubrilkaenglish.onlineCompetition

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.zubrilkaenglish.databinding.FragmentCompetitionBinding
import com.example.zubrilkaenglish.events.CmpEvEnum
import com.example.zubrilkaenglish.events.CompetitionEvent
import com.example.zubrilkaenglish.onlineCompetition.socketDto.NextWord
import com.example.zubrilkaenglish.utils.ui.CustButton
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class CompetitionFragment : Fragment() {

    private lateinit var viewModel: CompetitionViewModel
    private lateinit var binding: FragmentCompetitionBinding
    private val socketHolder = SocketHolder.instance

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

        setupListeners()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this) //отписываемся от EventBus
        socketHolder.socketConnect()

    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this) //подписываемся на EventBus
        socketHolder.closeConnect()
    }

    /**
     * метод используется библиотечкой green robot
     * при публикации кем-то события CompetitionEvent
     */
    @Subscribe
    fun competitionEvent(event: CompetitionEvent){
        when(event.typeEvent){

            else -> {}
        }
    }

    //установит слушатели на различные кнопки и LiveData
    private fun setupListeners() {
        viewModel.ping.observe(viewLifecycleOwner){
            if (it!=null){
                binding.ping.setText(it.toString())
                if (it<=200L) binding.ping.setTextColor(Color.GREEN)
                else binding.ping.setTextColor(Color.parseColor("#FF9800"))
            }else{
                binding.ping.setText("There is no connection")
                binding.ping.setTextColor(Color.parseColor("#FF9800"))
            }
        }
        viewModel.duelInfo.observe(viewLifecycleOwner){
            if (it == null){
                binding.ownHealth.text = "100"
                binding.ownName.text = viewModel.profile.value?.name ?: "Name"
                binding.opponentHealth.text = ""
                binding.opponentName.text = ""
                binding.opponentHealth.visibility = View.GONE
                binding.opponentName.visibility = View.GONE
            } else{
                binding.ownHealth.text = it.listHealth.get(it.ownPosition).toString()
                binding.ownName.text = it.listShortNames.get(it.ownPosition)

                var opponentPosition:Int? = null
                if(it.ownPosition == 0 && it.listHealth.size==2 && it.listShortNames.size==2){
                    opponentPosition = 1
                } else if(it.ownPosition == 1 && it.listHealth.size==2 && it.listShortNames.size==2){
                    opponentPosition = 0
                }

                binding.opponentHealth.text = opponentPosition?.let { it1 -> it.listHealth.get(it1).toString() }
                binding.opponentName.text = opponentPosition?.let { it1 -> it.listShortNames.get(it1).toString() }
                binding.opponentHealth.visibility = View.VISIBLE
                binding.opponentName.visibility = View.VISIBLE
            }
        }
        viewModel.startCountDown.observe(viewLifecycleOwner){
            if (it==null){
                binding.countDown.text = ""
                binding.countDown.visibility = View.GONE
            } else {
                binding.countDown.text = it.toString()
                binding.countDown.visibility = View.VISIBLE

            }
        }
        viewModel.nextWord.observe(viewLifecycleOwner){
            configureCountWords(it)
            configureWordBlock(it)}
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
//                it.requestLayout()
                }
//            root.requestLayout()

            } catch (e:Exception){e.printStackTrace()}
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
}