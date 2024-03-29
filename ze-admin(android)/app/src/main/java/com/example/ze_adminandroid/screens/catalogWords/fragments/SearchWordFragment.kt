package com.example.ze_adminandroid.screens.catalogWords.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ze_adminandroid.R
import com.example.ze_adminandroid.databinding.FragmentSearchWordBinding
import com.example.ze_adminandroid.events.VcEvEnum
import com.example.ze_adminandroid.events.VoiceEvent
import com.example.ze_adminandroid.models.Voice
import com.example.ze_adminandroid.models.Word
import com.example.ze_adminandroid.screens.catalogWords.CatalogWordsFragment
import com.example.ze_adminandroid.screens.catalogWords.CatalogWordsViewModel
import com.example.ze_adminandroid.utils.myBundle
import com.example.zubrilkaenglish.events.WordEvent
import com.example.zubrilkaenglish.events.WrEvEnum
import org.greenrobot.eventbus.EventBus

class SearchWordFragment(
    viewModel: CatalogWordsViewModel,
    override val owner: CatalogWordsFragment
) : Fragment(),FragmentItem {

    private lateinit var binding: FragmentSearchWordBinding
    private lateinit var adapter: ListCardsAdapter
    private var viewModel_CC = viewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchWordBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ListCardsAdapter(this)
        recyclerView = binding.recyclerView
        recyclerView.adapter = adapter

        viewModel_CC.listSearchWords.observe(viewLifecycleOwner){
            adapter.setList(it)
        }
    }

//    override fun onResume() {
//        super.onResume()
//        EventBus.getDefault().register(this)
//    }
//
//    override fun onPause() {
//        super.onPause()
//        EventBus.getDefault().unregister(this)
//    }

    /**
     * метод используется библиотечкой green robot
     * при публикации кем-то события "card_changed"
     */
//    @Subscribe
//    fun event_CardChanged(event: CardEvent){
//        when(event.typeEvent){
//            CrEvEnum.CARD_CHANGED -> {
//                    adapter.notifyItemChanged(event.properties!!.get("positionAdapter") as Int)
//            }
//
//            else -> {}
//        }
//    }


    /**
     * выполняется при нажатии на элемент слова
     */
    override fun onClickWord(word: Word) {
        myBundle.put("editedWord",word)
        findNavController().navigate(R.id.action_catalogWordsFragment_to_editWordFragment)
    }

    //излишний для данного фрагмента метод
    override fun onClickFolder(positionFolder: Int) {
        TODO("Not yet implemented")
    }


    //излишний для данного фрагмента метод
    override fun rollBackRecycler() {
        TODO("Not yet implemented")
    }

    /**
     * вызывается при нажатии на кнопку проигрывания голоса озвучки
     */
    override fun onClickButtonPlay(word: Word) {
        if (word.link_voice != null){
            //отправим запрос на воспроизведение звука
            EventBus.getDefault().post(VoiceEvent(VcEvEnum.PLAY_VOICE, Voice(word.link_voice,null)))
        }

    }
    /**
     * удалить word из базы данных
     */
    override fun onClickButtonDelete(word: Word) {
        EventBus.getDefault().post(WordEvent(WrEvEnum.DELETE_FROM_DATABASE,word))
    }

}