package com.example.zubrilkaenglish.screens.catalogCards.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.FragmentTopicsCardsBinding

class TopicsCardsFragment : Fragment() {

    private lateinit var binding: FragmentTopicsCardsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTopicsCardsBinding.inflate(inflater,container,false)
        return binding.root
    }
}