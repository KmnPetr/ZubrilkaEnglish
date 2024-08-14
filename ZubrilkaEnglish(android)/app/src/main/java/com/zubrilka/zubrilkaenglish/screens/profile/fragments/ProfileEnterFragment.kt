package com.zubrilka.zubrilkaenglish.screens.profile.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.zubrilka.zubrilkaenglish.R
import com.zubrilka.zubrilkaenglish.databinding.FragmentProfileEnterBinding
import com.zubrilka.zubrilkaenglish.events.NfEvEnum
import com.zubrilka.zubrilkaenglish.events.NotificationEvent
import com.zubrilka.zubrilkaenglish.models.Profile
import com.zubrilka.zubrilkaenglish.repositories.ProfileRepository
import com.zubrilka.zubrilkaenglish.screens.PopupInfo
import com.zubrilka.zubrilkaenglish.screens.catalogCards.fragments.FragmentItem
import com.zubrilka.zubrilkaenglish.screens.profile.ProfileViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class ProfileEnterFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileEnterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileEnterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        setListeners()
        overrideClickBack()
    }

    /**
     * регистрация слушателей на различные кнопки и ссылки
     */
    private fun setListeners() {
        binding.registrationLink.setOnClickListener { enterLinkClick(it) }
        binding.buttonEnter.setOnClickListener { loginRequest() }
        viewModel.listValidationErrors.observe(viewLifecycleOwner){ showErrors(it) }
        viewModel.profile.observe(viewLifecycleOwner){profileExists(it)}
    }
    /**
     * переведет на другой фрагмент с информацией о профиле если он найдется в БД
     */
    private fun profileExists(profile: Profile?) {
        if (profile!=null){
            findNavController().popBackStack()
        }
    }

    /**
     * покажет ошибки при заполнении полей
     */
    private fun showErrors(it: List<String>?) {
        if (it != null){
            binding.errorField.visibility = View.VISIBLE
        } else binding.errorField.visibility = View.GONE

        binding.errorText.text=""
        it?.forEach {
            binding.errorText.append(it+"\n")
        }
    }
    /**
     * запрос на аутентификацию
     */
    private fun loginRequest() {
        ProfileRepository.instance.loginRequest(binding.username.text.toString(),binding.password.text.toString())
    }

    /**
     * обработает нажатие на ссылку перехода на страницу регистрации
     */
    private fun enterLinkClick(it: View?) {
        findNavController().popBackStack()
    }


    /**
     * переопределяет поведение системой кнопки "Back"
     */
    private fun overrideClickBack() {
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    EventBus.getDefault().post(NotificationEvent("",NfEvEnum.GO_TO_UPSTACK))
                }
            })
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().post(NotificationEvent("Профиль", NfEvEnum.CHANGE_TITLE)) //смена титла на тулбаре
        EventBus.getDefault().register(this)
    }
    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
    /**
     * метод используется библиотечкой green robot
     * при публикации кем-то события CompetitionEvent
     */
    @Subscribe
    fun receiveEvent(event: NotificationEvent){
        when(event.typeEvent){
            NfEvEnum.POPUP_INFO -> PopupInfo(requireContext(), R.string.information_profile).show()
            else -> {}
        }
    }
}