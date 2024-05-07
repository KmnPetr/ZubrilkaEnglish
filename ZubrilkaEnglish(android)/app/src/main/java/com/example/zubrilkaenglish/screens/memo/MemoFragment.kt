package com.example.zubrilkaenglish.screens.memo

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.FragmentMemoBinding
import com.example.zubrilkaenglish.repositories.memoService.MemoReceiver
import com.example.zubrilkaenglish.utils.buttonAnimationClick
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MemoFragment : Fragment() {

    private lateinit var viewModel: MemoViewModel
    private lateinit var binding: FragmentMemoBinding
    private lateinit var memoAdapter: MemoAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMemoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MemoViewModel::class.java)
        memoAdapter = MemoAdapter()
        recyclerView = binding.recyclerView
        recyclerView.adapter = memoAdapter

        viewModel.allMemos.observe(viewLifecycleOwner){list ->
            memoAdapter.setList(list)
        }


        setListeners()
    }

    /**
     * установит слушатели
     */
    private fun setListeners() {
        binding.buttonAddMemo.setOnClickListener {
            buttonAnimationClick(it)
            PopupCreateMemo(requireActivity()).show()
        }
    }
}