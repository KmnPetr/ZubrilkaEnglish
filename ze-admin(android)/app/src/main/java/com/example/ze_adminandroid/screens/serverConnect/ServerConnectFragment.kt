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
}