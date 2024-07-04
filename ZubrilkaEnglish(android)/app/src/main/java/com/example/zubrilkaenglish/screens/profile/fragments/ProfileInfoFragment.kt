package com.example.zubrilkaenglish.screens.profile.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.FragmentProfileInfoBinding
import com.example.zubrilkaenglish.models.Profile
import com.example.zubrilkaenglish.repositories.ProfileRepository
import com.example.zubrilkaenglish.screens.profile.ProfileViewModel
import com.example.zubrilkaenglish.utils.LOG

/**
 * покажет всю доступную информацию по профилю
 */
class ProfileInfoFragment : Fragment() {

    private lateinit var binding: FragmentProfileInfoBinding
    private lateinit var viewModel: ProfileViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileInfoBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)


        setListeners()
    }

    /**
     * функции слушателей на кнопки и другое
     */
    private fun setListeners() {
        viewModel.profile.observe(viewLifecycleOwner){profileChange(it)}
        binding.logOut.setOnClickListener { logOut() }
        binding.changeName.setOnClickListener { PopupChangeProfileField("name",requireContext()).show() }
        binding.changeEmail.setOnClickListener { PopupChangeProfileField("email",requireContext()).show() }
    }

    /**
     * разлогинит пользователя
     */
    private fun logOut() {
        ProfileRepository.instance.logOut()
    }

    /**
     * реагирует на изменеие профиля в БД
     */
    private fun profileChange(profile: Profile?) {
        Log.d(LOG,"profileChange ")
        if (profile!=null){
            showProfileInfo(profile)
        } else {
            Log.d(LOG,"findNavController().navigate(R.id.action_profileInfoFragment_to_profileRegistrationFragment)")
            findNavController().navigate(R.id.action_profileInfoFragment_to_profileRegistrationFragment)
        }
    }

    //заполняем поля на экране данными пользователя
    private fun showProfileInfo(profile: Profile) {
        binding.name.setText(profile.name)
        binding.email.setText(profile.email)
//        binding.dateOfCreation.setText(profile.created_at.toString())
    }
}