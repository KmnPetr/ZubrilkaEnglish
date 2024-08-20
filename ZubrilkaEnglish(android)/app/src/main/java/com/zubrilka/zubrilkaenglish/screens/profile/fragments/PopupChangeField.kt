package com.zubrilka.zubrilkaenglish.screens.profile.fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.View
import android.view.Window
import com.zubrilka.zubrilkaenglish.databinding.PopupProfileChangeFieldBinding
import com.zubrilka.zubrilkaenglish.repositories.ProfileRepository
import com.zubrilka.zubrilkaenglish.repositories.PropRepository
import com.zubrilka.zubrilkaenglish.repositories.room.PropKey
import com.zubrilka.zubrilkaenglish.utils.privacy_url
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PopupChangeProfileField(
    private val typeField:String,
    context: Context
): Dialog(context) {
    private val binding = PopupProfileChangeFieldBinding.inflate(layoutInflater)
    private val profileRepository = ProfileRepository.instance
    private val propRepository = PropRepository.instance
    private lateinit var job: Job
    private var job2: Job
    private var isNeedAgreePrivacy:Boolean = true
    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val displayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        binding.root.layoutParams.width = (screenWidth*0.90).toInt()

        if(propRepository.properties.value[PropKey.IS_AGREE_PRIVACY.key].toBoolean()){
            isNeedAgreePrivacy = false
            binding.agreementField.visibility = View.GONE
        }else{
            isNeedAgreePrivacy = true
            binding.agreementField.visibility = View.VISIBLE
        }

        profileRepository.clearValidationErrors()

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

        binding.buttonChange.setOnClickListener {
            if (!isNeedAgreePrivacy){
            }else{
                if (binding.isAgreePrivacy.isChecked){
                    propRepository.userIsAgreedPrivacy()
                    sendRequest()
                }
            }
        }
        binding.privacyPolicy.setOnClickListener { showPrivacyPolicy() }

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

    /**
     * покажет текст политики конфиденциальности
     * точнее переведет на сайт с этой политикой
     */
    private fun showPrivacyPolicy() {
        val url = privacy_url
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        context.startActivity(intent)
    }

    override fun dismiss() {
        job?.cancel()
        job2?.cancel()
        super.dismiss()
    }
}