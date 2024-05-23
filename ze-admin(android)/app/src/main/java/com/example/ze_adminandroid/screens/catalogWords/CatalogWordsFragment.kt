package com.example.ze_adminandroid.screens.catalogWords

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.ze_adminandroid.R
import com.example.ze_adminandroid.databinding.FragmentCatalogWordsBinding
import com.example.ze_adminandroid.models.Word
import com.example.ze_adminandroid.screens.catalogWords.fragments.CatalogItemFragment
import com.example.ze_adminandroid.screens.catalogWords.fragments.FragmentItem
import com.example.ze_adminandroid.screens.catalogWords.fragments.SearchWordFragment
import com.example.ze_adminandroid.utils.MYEFE_SWITCH
import com.example.ze_adminandroid.utils.SearchObject
import com.example.ze_adminandroid.utils.myBundle
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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


        buttonListeners()
        setListFragment()
        tabLayoutListener()
        viewPager2Listener()
        searchListener()
        overrideClickBack()
        checkNotReadyWord()
    }

    /**
     * слушатели кнопок
     */
    private fun buttonListeners() {
        binding.createNewWord.setOnClickListener {
            animationClick(it)
            MYEFE_SWITCH = false
            val word = Word(0,null,null,null,null,null,"",null,null,0,null,false,false)
            myBundle.put("editedWord",word)
            findNavController().navigate(R.id.action_catalogWordsFragment_to_editWordFragment)
        }
    }

    /**
     * меняет временно фон при нажатии
     */
    private fun animationClick(view: View?) {
        view?.let {
            it.backgroundTintList = ContextCompat.getColorStateList(it.context,R.color.myGray)
            GlobalScope.launch {
                delay(100)
                withContext(Dispatchers.Main){
                    it.backgroundTintList = ContextCompat.getColorStateList(it.context,android.R.color.white)
                }
            }
        }
    }

    /**
     * если в БД имелось незавершеное недавно слово
     * перенаправит на фрагмент для завершения его редактирования
     */
    private fun checkNotReadyWord() {
        viewModel.notReadyWord.observe(viewLifecycleOwner){
            if (it!=null){
                myBundle.put("editedWord",it)
                viewModel.notReadyWord.value = null
                findNavController().navigate(R.id.action_catalogWordsFragment_to_editWordFragment)
            }
        }
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
                viewModel.lastWordFolder,
                this),
            CatalogItemFragment(//фрагмент для показа слов измененных или новых созданных находящихся в БД
                viewModel,
                1,
                viewModel.mapEditedWords,
                viewModel.namesEditedTopics,
                viewModel.lastEditedWordFolder,
                this)
        )
        adapter.setList(list)

        viewModel.isRecyclerChanged.value = arrayListOf(false,false,false)//сразу 3 элемента добавлю, чтобы избежать проблем с третьим фрагментом для поиска слов
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
                        adapter.addSearchFragment(SearchWordFragment(viewModel,this@CatalogWordsFragment))
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
     * показывает popup окошко при нажатии на слово
     */
    fun onClickCard(wordCard: Word,position: Int){
//        PopupWordCard(requireContext(),viewModel,wordCard,position).show()
    }
}