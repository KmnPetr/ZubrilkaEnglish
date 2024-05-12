package com.example.zubrilkaenglish.screens.profile.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.zubrilkaenglish.databinding.FragmentProfileEnterBinding
import com.example.zubrilkaenglish.screens.profile.ProfileViewModel

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
    }

    /**
     * регистрация слушателей на различные кнопки и ссылки
     */
    private fun setListeners() {
        binding.registrationLink.setOnClickListener {
            enterLinkClick(it)
        }
    }

    /**
     * обработает нажатие на ссылку перехода на страницу регистрации
     */
    private fun enterLinkClick(it: View?) {
        findNavController().popBackStack()
    }
}