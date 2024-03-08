package com.example.ze_adminandroid.screens.catalogWords

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.ze_adminandroid.databinding.FragmentCatalogWordsBinding
import com.example.ze_adminandroid.models.Word
import com.example.ze_adminandroid.screens.catalogWords.fragments.CatalogItemFragment
import com.example.ze_adminandroid.utils.SearchObject
import com.google.android.material.tabs.TabLayout

class CatalogWordsFragment : Fragment() {

    private lateinit var viewModel: CatalogWordsViewModel
    private lateinit var binding: FragmentCatalogWordsBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: ViewPager2Adapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatalogWordsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CatalogWordsViewModel::class.java)


        tabLayout = binding.tabLayout
        viewPager2 = binding.viewPager2
        adapter = ViewPager2Adapter(this)
        viewPager2.adapter = adapter

        SearchObject.instance //требует заранее прогрузки для скачивания данных с БД


        setListFragment()
    }

    /**
     * настроит список показываемых фрагментов viewPager2 а также настроит isRecyclerChanged в viewModel
     */
    private fun setListFragment() {
        val list: MutableList<Fragment> = mutableListOf(
            CatalogItemFragment(//фрагмент для показа всех слов по темам
                viewModel,
                0,
                viewModel.mapWordsByTopic,
                viewModel.namesTopics,
                this)/*,
            CatalogItemFragment(//фрагмент для показа слов находящихся в собственности юзера и показа их по степени изученности
                viewModel,
                1,
                viewModel.mapUserCards,
                viewModel.namesTopicsUserCards,
                this)*/
        )
        adapter.setList(list)

        viewModel.isRecyclerChanged.value = arrayListOf(false,false,false)//сразу 3 элемента добавлю, чтобы избежать проблем с третьим фрагментом для поиска слов
    }

    /**
     * показывает popup окошко при нажатии на слово
     */
    fun onClickCard(wordCard: Word,position: Int){
//        PopupWordCard(requireContext(),viewModel,wordCard,position).show()
    }
}