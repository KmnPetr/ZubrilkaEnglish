package com.example.zubrilkaenglish.screens.catalogCards.fragments.topicsCardsFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.MenuProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.zubrilkaenglish.databinding.FragmentTopicsCardsBinding
import com.example.zubrilkaenglish.screens.catalogCards.CatalogCardsViewModel
import com.example.zubrilkaenglish.screens.catalogCards.fragments.ListCardsAdapter
import com.example.zubrilkaenglish.screens.catalogCards.fragments.RecyclerItemListener

class TopicsCardsFragment(viewModel: CatalogCardsViewModel) : Fragment(), RecyclerItemListener {

    private lateinit var binding: FragmentTopicsCardsBinding
    private lateinit var folderAdapter: TopicsCardsAdapter
    private lateinit var cardAdapter: ListCardsAdapter
    private var viewModel_CC = viewModel
    private lateinit var recyclerView: RecyclerView
    override var isRecyclerChanged: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTopicsCardsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        folderAdapter = TopicsCardsAdapter(this)
        cardAdapter = ListCardsAdapter()
        recyclerView = binding.recyclerView
        recyclerView.adapter = folderAdapter

        var isRecyclerChanged: Boolean = false

        viewModel_CC.namesTopics.observe(viewLifecycleOwner){list->
            val modifiedList = list.map { it+"   (слов: "+ (viewModel_CC.mapWordsByTopic.value?.get(it)?.size ?: "null") + ")"}
            folderAdapter.setList(modifiedList)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                println("КНОПКА НАЗАД")
                if (isRecyclerChanged){
                    println("КНОПКА НАЗАД")
                    isRecyclerChanged = false
                }else{
                    requireActivity().onBackPressed()
                }
                // Здесь вы можете определить желаемое поведение кнопки "назад"
                // Например, закрытие фрагмента или выполнение определенных действий перед закрытием
                // Если вы не хотите ничего делать при нажатии кнопки "назад", то просто оставьте этот метод пустым
            }
        })
    }


    /**
     * слушатель срабатывает при выборе папки с карточками
     */
    override fun onClickFolder(positionFolder: Int) {
        recyclerView.adapter = cardAdapter

        isRecyclerChanged = true

        viewModel_CC.mapWordsByTopic.observe(viewLifecycleOwner){
            it[viewModel_CC.namesTopics.value?.get(positionFolder)]?.let { it1 -> cardAdapter.setList(it1) }
        }
    }

}