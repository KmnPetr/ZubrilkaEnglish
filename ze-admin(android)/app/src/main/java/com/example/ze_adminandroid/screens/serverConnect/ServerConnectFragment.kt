package com.example.ze_adminandroid.screens.serverConnect

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ze_adminandroid.databinding.FragmentServerConnectBinding
import com.example.ze_adminandroid.screens.serverConnect.socketService.MessageProtocol
import com.example.ze_adminandroid.screens.serverConnect.socketService.NetworkHolder
import com.example.ze_adminandroid.services.RoomService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ServerConnectFragment : Fragment() {

    private lateinit var viewModel: ServerConnectViewModel
    private lateinit var binding: FragmentServerConnectBinding
    private lateinit var networkHolder: NetworkHolder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentServerConnectBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ServerConnectViewModel::class.java)
        networkHolder = NetworkHolder.instance

        getLastHost()
        settingHost()
        viewModelLissen()
        setOnClicks()



        println("/////////////////////////////////////////////////////////////////////////////////////////")
        MessageProtocol()
    }

    //получит старый удачный хост из БД
    private fun getLastHost() {
        GlobalScope.launch {
            val roomService = RoomService()
            var host: String
            host = roomService.getLastHost().toString()

            withContext(Dispatchers.Main) {
                if (host!=null&&!host.equals("null")){
                    binding.editHost.setText(host)
                }
            }
        }
    }
    /**
     * установит новое значение в таблице prop_table по ключу last-host
     * TODO прямое обращение к руму из фрагмента
     */
    fun setNewLastHost(host: String){
        val roomService = RoomService()
        roomService.insertNewLastHost(host)
    }
    /**
     * установит слушатели на различные кнопки
     * TODO прямое обращение к руму из фрагмента
     */
    private fun setOnClicks() {
        //кнопка проверки хоста
        binding.buttonCheckHost.setOnClickListener {
            networkHolder.checkConnect(binding.editHost.text.toString())
        }

    }

    /**
     * прослушывает изменения различных обьектов в viewModel
     */
    private fun viewModelLissen() {
        viewModel.ping.observe(viewLifecycleOwner){
            if (it!=null){
                binding.ping.setText(it.toString())
                if (it<=100L) binding.ping.setTextColor(Color.GREEN)
                else binding.ping.setTextColor(Color.YELLOW)
            }else{
                binding.ping.setText("There is no connection")
                binding.ping.setTextColor(Color.parseColor("#FF9800"))
            }
        }
        //выведет количество слов на экран
        viewModel.listEditedWords.observe(viewLifecycleOwner){
            binding.countEditedWords.setText("countEditedWords: "+it.size)
        }
        //выведет количество Voice на экран
        viewModel.countVoices.observe(viewLifecycleOwner){
            binding.countVoices.setText("countVoices: "+it)
        }
    }

    /**
     * ставит прослушки на изменение текста в host EditText
     * и некоторые другие действия, напр следит за цветом текста
     */
    private fun settingHost(){
        binding.editHost.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                networkHolder.checkConnect(s.toString())
            }
        })

        viewModel.host.observe(viewLifecycleOwner){
            if (it != null) {
                if (it.second){
                    binding.textHost.setText(it.first)
                    binding.textHost.setTextColor(Color.GREEN)
                    setNewLastHost(it.first)
                }
            }
        }
    }

}