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
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

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

//        networkHolder.checkConnect("192.168.218.182", viewModel.isHostAvailable)
        settingHost()
        viewModelLissen()


        socketConnect("it.first")
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
                networkHolder.checkConnect(s.toString(),viewModel.host)
            }
        })

        viewModel.host.observe(viewLifecycleOwner){
            if (it.second){
                binding.textHost.setText(it.first)
                binding.textHost.setTextColor(Color.GREEN)
                viewModel.setNewLastHost(it.first)

            }
        }
    }

    /**
     * установит сокет соединение
     */
    private fun socketConnect(host:String) {
        val client: OkHttpClient = OkHttpClient()
        val request: Request = Request.Builder().url(/*"ws://192.168.218.182:7000"*/"ws://192.168.3.182:33333/event-emitter").build()
        val listener: MyWebSocketListener = MyWebSocketListener(viewModel)
        val ws: WebSocket = client.newWebSocket(request, listener)


        // Trigger shutdown of the dispatcher's executor so this process can
        // exit cleanly.
//        client.dispatcher().executorService().shutdown();
    }
}