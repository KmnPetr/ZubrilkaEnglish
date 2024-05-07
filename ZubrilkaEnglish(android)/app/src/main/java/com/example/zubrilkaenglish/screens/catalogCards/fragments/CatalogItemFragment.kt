package com.example.zubrilkaenglish.screens.catalogCards.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.zubrilkaenglish.databinding.FragmentCatalogItemBinding
import com.example.zubrilkaenglish.events.CardEvent
import com.example.zubrilkaenglish.events.CrEvEnum
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.screens.catalogCards.CatalogCardsFragment
import com.example.zubrilkaenglish.screens.catalogCards.CatalogCardsViewModel
import com.example.zubrilkaenglish.utils.buttonAnimationClick
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * фрагмент содержит список слов или список папок
 */
class CatalogItemFragment(
    val viewModel_CC: CatalogCardsViewModel,
    val positionInPager: Int,
    val mapFoldersCards: MutableLiveData<Map<String, List<WordCard>>>,
    val namesFolders: MutableLiveData<List<String>>,
    override val owner: CatalogCardsFragment
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
        binding.rollBack.setOnClickListener {
            buttonAnimationClick(it)
            rollBackRecycler()
        }
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        super.onPause()
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
                    cardAdapter.notifyItemChanged(event.properties!!.get("positionAdapter") as Int)
                }
            }

            else -> {}
        }
    }

    /**
     * выполняется при нажатии на элемент папки
     */
    override fun onClickFolder(positionFolder: Int) {
        binding.rollBack.visibility = View.VISIBLE
        binding.rollBack.isEnabled = true

        recyclerView.adapter = cardAdapter

        viewModel_CC.isRecyclerChanged.value?.set(positionInPager,true)

        mapFoldersCards.observe(viewLifecycleOwner){
            it[namesFolders.value?.get(positionFolder)]?.let { it1 -> cardAdapter.setList(it1) }
        }
    }

    /**
     * выполняется если надо вернуться от списка карочек к списку папок
     */
    override fun rollBackRecycler() {
        viewModel_CC.isRecyclerChanged.value?.set(positionInPager,false)
        recyclerView.adapter = folderAdapter

        binding.rollBack.visibility = View.GONE
        binding.rollBack.isEnabled = false
    }
}