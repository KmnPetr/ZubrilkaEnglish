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
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.zubrilkaenglish.databinding.FragmentCatalogCardsBinding
import com.example.zubrilkaenglish.eventBus.events.CardEvent
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.screens.catalogCards.fragments.CatalogItemFragment
import com.example.zubrilkaenglish.screens.catalogCards.fragments.FragmentItem
import com.example.zubrilkaenglish.screens.catalogCards.fragments.PopupWordCard
import com.example.zubrilkaenglish.screens.catalogCards.fragments.searchCardFragment.SearchCardFragment
import com.example.zubrilkaenglish.screens.training.popup.PopupDialog
import com.example.zubrilkaenglish.utils.SIM_FORM_DATE
import com.example.zubrilkaenglish.utils.SearchObject
import com.example.zubrilkaenglish.utils.customizeBackground
import com.google.android.material.tabs.TabLayout
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.text.SimpleDateFormat
import java.util.Date

/**
 * основной фрагмент во вкладке каталога карт
 * содержит в себе другие фрагменты с показом списка папок, списков слов,
 * поисковика по словам
 */
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

        setListFragment()
        tabLayoutListener()
        viewPager2Listener()
        searchListener()
        overrideClickBack()
    }

    /**
     * показывает popup окошко при нажатии на элемент карточки
     */
    fun onClickCard(wordCard: WordCard,position: Int){
        PopupWordCard(requireContext(),viewModel,wordCard,position).show()
    }

    /**
     * переопределяет поведение системой кнопки "Back"
     * в случае если открыта какаято папка, сначала закроет и следующим нажатием закроет фрагмент
     */
    private fun overrideClickBack() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val position = viewPager2.currentItem

                if (viewModel.isRecyclerChanged.value?.get(position)==true){
                    viewModel.isRecyclerChanged.value?.set(position, false)
                    val fragment = adapter.getFragment(position)
                    if (fragment is FragmentItem){
                        fragment.rollBackRecycler()
                    }
                }else{
                    findNavController().popBackStack()
                }
            }
        })
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
                this),
            CatalogItemFragment(//фрагмент для показа слов находящихся в собственности юзера и показа их по степени изученности
                viewModel,
                1,
                viewModel.mapUserCards,
                viewModel.namesTopicsUserCards,
                this)
        )
        adapter.setList(list)

        viewModel.isRecyclerChanged.value = arrayListOf(false,false,false)//сразу 3 элемента добавлю, чтобы избежать проблем с третьим фрагментом для поиска слов
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
                        adapter.addSearchFragment(SearchCardFragment(viewModel,this@CatalogCardsFragment))
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
