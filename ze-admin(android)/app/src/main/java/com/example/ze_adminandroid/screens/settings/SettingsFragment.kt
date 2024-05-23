package com.example.ze_adminandroid.screens.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ze_adminandroid.databinding.FragmentSettingsBinding
import com.example.ze_adminandroid.utils.MYEFE_SWITCH

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        val textView: TextView = binding.textGallery
        settingsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //регулировка включенности роботизированного сценария на сайт myefe
        binding.myefeSwitch.isChecked = MYEFE_SWITCH
        binding.myefeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            MYEFE_SWITCH = isChecked
        }
    }
}