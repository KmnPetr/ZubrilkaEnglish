package com.example.zubrilkaenglish.screens.directoryWords

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zubrilkaenglish.databinding.FragmentDirectoryWordsBinding

class DirectoryWords : Fragment() {

    private lateinit var viewModel: DirectoryWordsViewModel
    private lateinit var binding: FragmentDirectoryWordsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDirectoryWordsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}