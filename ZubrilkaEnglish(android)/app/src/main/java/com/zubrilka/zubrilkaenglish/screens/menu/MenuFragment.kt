package com.zubrilka.zubrilkaenglish.screens.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.zubrilka.zubrilkaenglish.R
import com.zubrilka.zubrilkaenglish.databinding.FragmentMenuBinding
import com.zubrilka.zubrilkaenglish.events.NfEvEnum
import com.zubrilka.zubrilkaenglish.events.NotificationEvent
import com.zubrilka.zubrilkaenglish.screens.PopupInfo
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MenuFragment : Fragment() {
    private lateinit var viewModel: MenuViewModel
    private lateinit var binding: FragmentMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel=ViewModelProvider(this).get(MenuViewModel::class.java)

        initButton()
    }
    private fun initButton(){
        binding.buttonGo.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_trainingFragment)
        }
        binding.catalogCards.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_catalogCardsFragment)
        }
        binding.competitionFragment.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_competitionFragment)
        }
        binding.rating.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_ratingFragment)
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().post(NotificationEvent(R.drawable.bac21.toString(),NfEvEnum.CHANGE_BACKGROUND))
        EventBus.getDefault().post(NotificationEvent("Zubrilka English", NfEvEnum.CHANGE_TITLE)) //смена титла на тулбаре
        EventBus.getDefault().register(this)
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
    fun onReceiveNotificationEvent(event: NotificationEvent){
        when(event.typeEvent){
            NfEvEnum.POPUP_INFO -> PopupInfo(requireContext(),R.string.information_main).show()
            else -> {}
        }
    }
}