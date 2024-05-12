package com.example.zubrilkaenglish.screens.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zubrilkaenglish.repositories.ProfileRepository
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val profileRepository = ProfileRepository.instance

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
    }

}