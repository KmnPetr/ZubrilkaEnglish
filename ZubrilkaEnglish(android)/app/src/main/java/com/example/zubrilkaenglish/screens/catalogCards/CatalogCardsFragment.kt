package com.example.zubrilkaenglish.screens.catalogCards

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.viewpager2.widget.ViewPager2
import com.example.zubrilkaenglish.databinding.FragmentCatalogCardsBinding
import com.example.zubrilkaenglish.screens.catalogCards.fragments.RecyclerItemListener
import com.example.zubrilkaenglish.utils.SearchObject
import com.example.zubrilkaenglish.utils.customizeBackground
import com.google.android.material.tabs.TabLayout

class CatalogCardsFragment : Fragment() {

    private lateinit var viewModel: CatalogCardsViewModel
    private lateinit var binding: FragmentCatalogCardsBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: ViewPager2Adapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatalogCardsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CatalogCardsViewModel::class.java)

        tabLayout = binding.tabLayout
        viewPager2 = binding.viewPager2
        adapter = ViewPager2Adapter(this,viewModel)
        viewPager2.adapter = adapter

        SearchObject.instance //требует заранее прогрузки для скачивания данных с БД


        customizeBackground(binding.background,resources)

        tabLayoutListener()
        viewPager2Listener()
        searchListener()

    }


    /**
     * прослушивает ввод текста в поисковик(editText)
     */
    private fun searchListener() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            // Выполняется перед изменением текста в EditText
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            // Выполняется во время изменения текста в EditText
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val updatedText = s.toString() // Получить новый текст из CharSequence
                if(updatedText.trim().isNotEmpty()){
                    //передаем данные с поисковой строки в viewModel
                    viewModel.changeListSearchWord(updatedText.trim())
                    //создаем фрагмент по поиску слов
                    if (!viewModel.searchCreated){
                        viewModel.searchCreated = true
                        viewModel.lastPositionTablayout = tabLayout.selectedTabPosition

                        val tabLayout: TabLayout = binding.tabLayout
                        val tab = tabLayout.newTab()
                        tab.text = "Поиск"
                        tabLayout.addTab(tab)
                        adapter.addSearchFragment()
                        tabLayout.getTabAt(tabLayout.tabCount - 1)?.select()
                    }
                }else{
                    //удаляем фрагмент по поиску слов
                    if (viewModel.searchCreated){
                        viewModel.searchCreated = false

                        tabLayout.getTabAt(viewModel.lastPositionTablayout)?.select()
                        adapter.removeSearchFragment()
                        tabLayout.removeTabAt(tabLayout.tabCount - 1)
                    }
                }
            }

            // Выполняется после изменения текста в EditText
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    /**
     * прослушивает смену страниц viewPager2
     */
    private fun viewPager2Listener() {
        viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback()
        {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.getTabAt(position)?.select()
                if (position!=2) viewModel.lastPositionTablayout = position
            }
        })
    }

    /**
     * прослушивает нажатия на tabLayout
     */
    private fun tabLayoutListener() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager2.currentItem = tab.position
                if (tab.position!=2) viewModel.lastPositionTablayout = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {/*Выполняется, когда выбор снят с вкладки*/ }
            override fun onTabReselected(tab: TabLayout.Tab) {/*Выполняется, когда выбирается уже выбранная вкладка*/ }
        })
    }

}
