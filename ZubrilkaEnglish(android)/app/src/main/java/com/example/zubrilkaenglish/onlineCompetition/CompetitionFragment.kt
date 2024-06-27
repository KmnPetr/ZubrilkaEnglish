package com.example.zubrilkaenglish.onlineCompetition

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.zubrilkaenglish.databinding.FragmentCompetitionBinding
import com.example.zubrilkaenglish.events.CompetitionEvent
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
    }
}