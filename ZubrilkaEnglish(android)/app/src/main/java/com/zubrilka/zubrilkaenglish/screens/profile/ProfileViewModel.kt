package com.zubrilka.zubrilkaenglish.screens.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zubrilka.zubrilkaenglish.models.Profile
import com.zubrilka.zubrilkaenglish.repositories.ProfileRepository
import com.zubrilka.zubrilkaenglish.utils.LOG
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val profileRepository = ProfileRepository.instance

    val profile: MutableLiveData<Profile?> = MutableLiveData()
    val listValidationErrors: MutableLiveData<List<String>?> = MutableLiveData()

    var regName: String = ""
    var regEmail: String = ""
    var regPassword: MutableLiveData<String> = MutableLiveData("")
    var regSecondPassword: MutableLiveData<String> = MutableLiveData("") //второй пароль для повторения чтоб не все так просто было
    var isPasswordsMatch: Boolean = false //true если пароли совпадают

    init {
        //подгружаем список ошибок валидации из репозитория пришедшее после ответа с сервера
        viewModelScope.launch {
            profileRepository.validationErrors.collect {
                listValidationErrors.postValue(it)
            }
        }
        viewModelScope.launch{
            profileRepository.profile.collect{
                Log.d(LOG,"Profile was changed: ViewModel: "+it?.toString())
                this@ProfileViewModel.profile.postValue(it)
            }
        }
    }

}