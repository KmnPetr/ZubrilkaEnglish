package com.example.ze_adminandroid.screens.files

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ze_adminandroid.R
import com.example.ze_adminandroid.databinding.FragmentFilesBinding
import java.io.File

class FilesFragment : Fragment() {

    private lateinit var viewModel: FilesViewModel
    private lateinit var binding: FragmentFilesBinding
    private lateinit var adapter: FilesAdapter
    private lateinit var fileManager: FileManager
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(FilesViewModel::class.java)
        adapter = FilesAdapter()
        recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        fileManager = FileManager()

//        adapter.setList(listOf(fileManager.rootFile))
    }

}