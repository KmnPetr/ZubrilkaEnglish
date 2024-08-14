package com.zubrilka.zubrilkaenglish.screens.catalogCards.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.zubrilka.zubrilkaenglish.databinding.FragmentCatalogItemBinding
import com.zubrilka.zubrilkaenglish.events.CardEvent
import com.zubrilka.zubrilkaenglish.events.CrEvEnum
import com.zubrilka.zubrilkaenglish.models.WordCard
import com.zubrilka.zubrilkaenglish.screens.catalogCards.CatalogCardsFragment
import com.zubrilka.zubrilkaenglish.screens.catalogCards.CatalogCardsViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * фрагмент содержит список слов или список папок
 */
class CatalogItemFragment(
    var viewModel_CC: CatalogCardsViewModel,
    val positionInPager: Int,
    val mapFoldersCards: MutableLiveData<Map<String, List<WordCard>>>,
    val namesFolders: MutableLiveData<List<String>>,
    override val owner: CatalogCardsFragment
) : Fragment(), FragmentItem {

//    private lateinit var viewModel_CC: CatalogCardsViewModel
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
//        viewModel_CC = ViewModelProvider(requireActivity()).get(CatalogCardsViewModel::class.java)

        folderAdapter = FoldersCardsAdapter(this)
        cardAdapter = ListCardsAdapter(this)

        recyclerView = binding.recyclerView
        recyclerView.adapter = folderAdapter

        namesFolders.observe(viewLifecycleOwner){list->
            val modifiedList = list.map { it+"   (слов: "+ (mapFoldersCards.value?.get(it)?.size ?: "null") + ")"}
            folderAdapter.setList(modifiedList)
        }


    }


    override fun onResume() {
        super.onResume()
        viewModel_CC.refreshData() //обновим данные мог поменяться состав и количество карточек особенно в личных карточках пользователя
    }
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
    /**
     * метод используется библиотечкой green robot
     * при публикации кем-то события "card_changed"
     */
    @Subscribe
    fun event_CardChanged(event: CardEvent){
        when(event.typeEvent){
            CrEvEnum.CARD_CHANGED -> {
                if (recyclerView.adapter is ListCardsAdapter){
                    cardAdapter.notifyItemChanged(event.properties.get("positionAdapter") as Int)
                }
            }

            else -> {}
        }
    }

    /**
     * выполняется при нажатии на элемент папки
     */
    override fun onClickFolder(positionFolder: Int) {

        recyclerView.adapter = cardAdapter

        viewModel_CC.isRecyclerChanged.value?.set(positionInPager,true)

        val nameKey: String? = namesFolders.value?.get(positionFolder)
        mapFoldersCards.observe(viewLifecycleOwner){
            if (it.containsKey(nameKey)){
                it[nameKey]?.let { it1 -> cardAdapter.setList(it1) }
            } else {
                cardAdapter.setList(emptyList()) //особенность фильтровки если все слова отфильтрованы то пустой лист
            }
        }
    }

    /**
     * выполняется если надо вернуться от списка карочек к списку папок
     */
    override fun rollBackRecycler() {
        viewModel_CC.isRecyclerChanged.value?.set(positionInPager,false)
        recyclerView.adapter = folderAdapter
    }
}