package com.example.zubrilkaenglish.screens.payments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.zubrilkaenglish.databinding.FragmentPaymentsBinding
import com.example.zubrilkaenglish.services.payments.sbp.BuildConfig
import com.example.zubrilkaenglish.services.payments.sbp.Callback
import com.example.zubrilkaenglish.services.payments.sbp.SbpUtils
import java.net.URI
import java.net.URISyntaxException

class PaymentsFragment : Fragment() {

    private lateinit var viewModel: PaymentsViewModel
    private lateinit var binding: FragmentPaymentsBinding
    private val sbpLink:String = "https://qr.nspk.ru/AD10006K1GQ7788G9ACAAM970SGCOLNM?type=02&&sum=1100&cur=RUB&crc=CD70";

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel= ViewModelProvider(this).get(PaymentsViewModel::class.java)

        val redirect:String = String.format("redirect=%s", BuildConfig.scheme);
        var link:String = sbpLink
        try {
            link = appendParameter(sbpLink, redirect)
        } catch (e: URISyntaxException) {
            //TODO add exception handling code
        }
        val finalLink:String = link


        binding.buttonSbp.setOnClickListener{view ->
            SbpUtils.getInstance().showSbpListDialog(
                requireActivity(),
                finalLink,
                object: Callback{
                    override fun callingBack(success: Boolean?) {
                        Log.d("t", success.toString())
                    }

                }
            )
        }

    }

    private fun appendParameter(link:String, parameter:String):String{
        val oldUri = URI(link);

        var newQuery:String  = oldUri.getQuery();
        if (newQuery == null) {
            newQuery = parameter;
        } else {
            newQuery += "&" + parameter;
        }

        return URI(oldUri.getScheme(), oldUri.getAuthority(),
        oldUri.getPath(), newQuery, oldUri.getFragment()).toString();
    }
}