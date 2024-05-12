package com.example.zubrilkaenglish.repositories

import android.annotation.SuppressLint
import android.util.Log
import com.example.zubrilkaenglish.models.Profile
import com.example.zubrilkaenglish.repositories.retrofit.RetrofitService
import com.example.zubrilkaenglish.utils.LOG
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import retrofit2.Response

/**
 * занимается аутентификацией пользователя
 */
class ProfileRepository private constructor(){
    companion object{
        val instance: ProfileRepository by lazy { ProfileRepository() }
    }
    private val retrofitService = RetrofitService()
    val validationErrors: MutableStateFlow<List<String>?> = MutableStateFlow(null)

    @SuppressLint("SuspiciousIndentation")
    fun registrationRequest(newProfile: Profile){
        validationErrors.value = null
        GlobalScope.launch {
            try {
                val response: Response<Profile?> = retrofitService.getProfileApi().registration(newProfile)

                if (response.isSuccessful) {
                    // Обработка успешного ответа
                    val profile: Profile? = response.body()
                    Log.d(LOG,"Response is successsful. Profile: "+ response.body().toString())
                } else {
                    val jsonString: String? = response.errorBody()?.string()
                    // Обработка ошибки
                    Log.d(LOG, jsonString.toString())
                    val jsonArray = JSONArray(jsonString)
                    val list = mutableListOf<String>()

                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getString(i)
                        list.add(item)
                    }

                    list.forEach { Log.d(LOG,it) }

                    validationErrors.value = ErrorsHandler().translate(list)
                }
            } catch (e: Exception){
                Log.d(LOG,"Ops...")
            }
        }
    }

    /**
     * добавит ошибку что пароли при вводе не совпадают
     * ну раз уж текст ошибок здесь определяется
     */
    fun passwordMismatchError() {
        validationErrors.value = listOf("Пароли не совпадают.")
    }

    /**
     * класс переведет строки с английского  пришедшие с сервера на русский привычный пользователю
     */
    class ErrorsHandler{
        fun translate(listErrorsEnglish: MutableList<String>): List<String> {
            val newList = mutableListOf<String>()
            listErrorsEnglish.forEach {
                newList.add(when(it) {
                    "Email is blank." -> "Поле \"Email\" не должно быть пустым."
                    "Email is invalid." -> "Невалидный email."
                    "RequestPassword is blank." -> "Поле \"Пароль\" не должно быть пустым."
                    "Name is blank." -> "Поле \"Имя\" не должно быть пустым."
                    "The password is shorter than 7 characters." -> "Пароль не может быть короче 7 символов."
                    "The password is too long." -> "The password is too big:)"
                    "The name is too long." -> "The name is too long."
                    "The email is too long." -> "The email is too long."
                    "This email is already in use." -> "Профиль с таким email уже существует."
                    else -> "Unknown error."
                })
            }
            return newList
        }
    }
}