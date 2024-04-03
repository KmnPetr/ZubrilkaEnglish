package com.example.ze_adminandroid.screens.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ze_adminandroid.databinding.FragmentGalleryBinding
import com.example.ze_adminandroid.utils.MYEFE_SWITCH

class GalleryFragment : Fragment() {
    private lateinit var binding: FragmentGalleryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        binding = FragmentGalleryBinding.inflate(inflater, container, false)

        val textView: TextView = binding.textGallery
        galleryViewModel.text.observe(viewLifecycleOwner) {
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