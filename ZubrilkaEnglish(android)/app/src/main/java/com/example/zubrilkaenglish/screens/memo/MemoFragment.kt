package com.example.zubrilkaenglish.screens.memo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.FragmentMemoBinding
import com.example.zubrilkaenglish.events.NfEvEnum
import com.example.zubrilkaenglish.events.NotificationEvent
import com.example.zubrilkaenglish.screens.PopupInfo
import com.example.zubrilkaenglish.utils.buttonAnimationClick
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

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
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().post(NotificationEvent("Напоминания", NfEvEnum.CHANGE_TITLE)) //смена титла на тулбаре
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
            NfEvEnum.POPUP_INFO -> PopupInfo(requireContext(), R.string.information_memo).show()
            else -> {}
        }
    }
}