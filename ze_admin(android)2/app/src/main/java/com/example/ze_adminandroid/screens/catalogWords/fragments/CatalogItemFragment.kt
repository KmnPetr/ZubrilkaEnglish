package com.example.ze_adminandroid.screens.catalogWords.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ze_adminandroid.R
import com.example.ze_adminandroid.databinding.FragmentCatalogItemBinding
import com.example.ze_adminandroid.models.Word
import com.example.ze_adminandroid.screens.catalogWords.CatalogWordsFragment
import com.example.ze_adminandroid.screens.catalogWords.CatalogWordsViewModel
import com.example.ze_adminandroid.util.myBundle

class CatalogItemFragment(
    val viewModel_CW: CatalogWordsViewModel,
    val positionInPager: Int,
    val mapFoldersCards: MutableLiveData<Map<String, List<Word>>>,
    val namesFolders: MutableLiveData<List<String>>,
    override val owner: CatalogWordsFragment
) : Fragment(), FragmentItem {
    private lateinit var binding: FragmentCatalogItemBinding
    lateinit var folderAdapter: FoldersCardsAdapter
    lateinit var cardAdapter: ListCardsAdapter
    lateinit var recyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatalogItemBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        folderAdapter = FoldersCardsAdapter(this)
        cardAdapter = ListCardsAdapter(this)

        recyclerView = binding.recyclerView
        recyclerView.adapter = folderAdapter

        namesFolders.observe(viewLifecycleOwner){list->
            val modifiedList = list.map { it+"   (слов: "+ (mapFoldersCards.value?.get(it)?.size ?: "null") + ")"}
            folderAdapter.setList(modifiedList)
        }

        binding.rollBack.visibility = View.GONE
        binding.rollBack.isEnabled = false
        binding.rollBack.setOnClickListener { rollBackRecycler() }
    }

    /**
     * выполняется при нажатии на элемент папки
     */
    override fun onClickFolder(positionFolder: Int) {
        binding.rollBack.visibility = View.VISIBLE
        binding.rollBack.isEnabled = true

        recyclerView.adapter = cardAdapter

        viewModel_CW.isRecyclerChanged.value?.set(positionInPager,true)

        mapFoldersCards.observe(viewLifecycleOwner){
            it[namesFolders.value?.get(positionFolder)]?.let { it1 -> cardAdapter.setList(it1) }
        }
    }
    /**
     * выполняется при нажатии на элемент слова
     */
    override fun onClickWord(word: Word) {
        myBundle.put("editedWord",word)
        findNavController().navigate(R.id.action_catalogWordsFragment_to_editWordFragment)
    }

    /**
     * выполняется если надо вернуться от списка слов к списку папок
     */
    override fun rollBackRecycler() {
        viewModel_CW.isRecyclerChanged.value?.set(positionInPager,false)
        recyclerView.adapter = folderAdapter

        binding.rollBack.visibility = View.GONE
        binding.rollBack.isEnabled = false
    }

}