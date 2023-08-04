package com.example.zubrilkaenglish.screens.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.FragmentMenuBinding

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
        binding.buttonListWeb.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_allWordsFragment)
        }
    }
}