package com.zubrilka.zubrilkaenglish.screens.profile.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.view.Window
import com.zubrilka.zubrilkaenglish.databinding.PopupProfileChangeFieldBinding
import com.zubrilka.zubrilkaenglish.repositories.ProfileRepository
import com.zubrilka.zubrilkaenglish.utils.LOG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PopupChangeProfileField(
    private val typeField:String,
    context: Context
): Dialog(context) {
    private val binding = PopupProfileChangeFieldBinding.inflate(layoutInflater)
    private val profileRepository = ProfileRepository.instance
    private lateinit var job: Job
    private var job2: Job
    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val displayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        binding.root.layoutParams.width = (screenWidth*0.90).toInt()



        when(typeField){
            "name"->{
                binding.title.text = "Введите новое имя"
                binding.newValueField.hint = "Имя"

                job = GlobalScope.launch {
                    profileRepository.profile.collect{
                        if (it == null||it.name.equals(binding.newValueField.text.toString())) {
                            dismiss()
                        }
                    }
                }
            }
            "email"->{
                binding.title.text = "Введите новый email"
                binding.newValueField.hint = "Email"

                job = GlobalScope.launch {
                    profileRepository.profile.collect{
                        if (it == null||it.email.equals(binding.newValueField.text.toString())) {
                            dismiss()
                        }
                    }
                }
            }
        }

        binding.buttonChange.setOnClickListener { sendRequest() }

        job2 = GlobalScope.launch {
            profileRepository.validationErrors.collect{ withContext(Dispatchers.Main){showErrors(it)} }
        }
    }

    private fun sendRequest() {
        profileRepository.changeProfileField(typeField,binding.newValueField.text.toString())
    }
    /**
     * покажет ошибки при заполнении полей
     */
    private fun showErrors(it: List<String>?) {
        if (it != null){
            binding.errorField.visibility = View.VISIBLE
        } else binding.errorField.visibility = View.GONE

        binding.errorText.text=""
        it?.forEach {
            binding.errorText.append(it+"\n")
        }
    }

    override fun dismiss() {
        job?.cancel()
        job2?.cancel()
        super.dismiss()
    }
}