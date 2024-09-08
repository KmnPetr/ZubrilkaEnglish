package com.zubrilka.zubrilkaenglish.screens.profile.fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.View
import android.view.Window
import androidx.core.widget.addTextChangedListener
import com.zubrilka.zubrilkaenglish.databinding.PopupProfileChangePasswordBinding
import com.zubrilka.zubrilkaenglish.repositories.ProfileRepository
import com.zubrilka.zubrilkaenglish.repositories.PropRepository
import com.zubrilka.zubrilkaenglish.repositories.room.PropKey
import com.zubrilka.zubrilkaenglish.utils.privacy_url
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PopupChangePassword(
    context: Context
): Dialog(context) {
    private val binding = PopupProfileChangePasswordBinding.inflate(layoutInflater)
    private val profileRepository = ProfileRepository.instance
    private val propRepository = PropRepository.instance
    private lateinit var job2: Job //джоба следит за появлением ошибок валидации и других
    private var isNeedAgreePrivacy:Boolean = true
    private var isNeedAscOldPassword: Boolean = true
    private var newPassword:String = ""
    private var repeatPassword:String = ""
    private var isPasswordsMatch:Boolean = true

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

        if (profileRepository.profile.value?.isTempProf==null||profileRepository.profile.value?.isTempProf==true)isNeedAscOldPassword = false

        if (isNeedAscOldPassword){
            binding.oldPasswordBlock.visibility = View.VISIBLE
        }else{
            binding.oldPasswordBlock.visibility = View.GONE
        }

        listeners()
    }

    private fun listeners(){
        binding.buttonChange.setOnClickListener {
            profileRepository.clearValidationErrors()
            if (!isPasswordsMatch) {
                ProfileRepository.instance.passwordMismatchError()
            }else{
                if (!isNeedAgreePrivacy){
                    sendRequest()
                }else{
                    if (binding.isAgreePrivacy.isChecked){
                        propRepository.userIsAgreedPrivacy()
                        sendRequest()
                    }
                }
            }
        }
        binding.privacyPolicy.setOnClickListener { showPrivacyPolicy() }
        binding.newPassword.addTextChangedListener { text ->
            newPassword = text.toString()
            comparePassword()
        }
        binding.repeatPassword.addTextChangedListener { text ->
            repeatPassword = text.toString()
            comparePassword()
        }

        job2 = GlobalScope.launch {
            profileRepository.validationErrors.collect{ withContext(Dispatchers.Main){showErrors(it)} }
        }


    }

    //сравнит первый и второй вариант пароля
    private fun comparePassword() {
        if ( newPassword == repeatPassword){
            binding.repeatPassword.setTextColor(Color.parseColor("#7FBD00"))
            isPasswordsMatch = true
        }else {
            binding.repeatPassword.setTextColor(Color.BLACK)
            isPasswordsMatch = false
        }
    }

    private fun sendRequest() {
        GlobalScope.launch {
            val result:Boolean = profileRepository.changePassword(binding.newPassword.text.toString(),binding.oldPassword.text.toString())
            if (result){
                successfulRequest()
            }
        }
    }

    /**
     * запускается при удачном изменении пароля
     * отобразит информацию об упешном изменении
     * потом закроет диалоговое окно
     */
    private suspend fun successfulRequest() {
        withContext(Dispatchers.Main) {
            binding.mainBlock.visibility = View.GONE
            binding.successNotif.visibility = View.VISIBLE
        }
        delay(1500)
        withContext(Dispatchers.Main) {
            dismiss()
        }
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
        job2.cancel()
        super.dismiss()
    }
}