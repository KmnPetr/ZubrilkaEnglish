package com.example.zubrilkaenglish.repositories

import android.annotation.SuppressLint
import android.util.Log
import com.example.zubrilkaenglish.models.Profile
import com.example.zubrilkaenglish.models.PropModel
import com.example.zubrilkaenglish.repositories.retrofit.RetrofitService
import com.example.zubrilkaenglish.repositories.room.RoomService
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
    val roomService = RoomService()
    val validationErrors: MutableStateFlow<List<String>?> = MutableStateFlow(null)
    val profile: MutableStateFlow<Profile?> = MutableStateFlow(null)

    init {
        GlobalScope.launch {
            roomService.getPropDAO().getProfile().collect{
                Log.d(LOG,"Profile was changed: ProfileRepository: "+it?.value)
                if (it != null) {
                    profile.value = Profile.toProfileObject(it.value)
                } else profile.value = null
            }
        }
    }

    fun loginRequest(username: String, password: String) {
        validationErrors.value = null
        GlobalScope.launch {
            try {
                val response: Response<Profile?> = retrofitService.getProfileApi().loginRequest(username,password)

                if (response.isSuccessful) {
                    // Обработка успешного ответа
                    onReceiveProfile(response.body())
                } else {
                    onReceiveErrors(response.errorBody()?.string())
                }
            } catch (e: Exception){
                Log.d(LOG,"Ops...  "+ e.message)
                e.printStackTrace()
            }
        }
    }


    /**
     * отправит запрос на регистрацию нового пользователя
     */
    @SuppressLint("SuspiciousIndentation")
    fun registrationRequest(newProfile: Profile){
        validationErrors.value = null
        GlobalScope.launch {
            try {
                val response: Response<Profile?> = retrofitService.getProfileApi().registration(newProfile)

                if (response.isSuccessful) {
                    // Обработка успешного ответа
                    onReceiveProfile(response.body())
                } else {
                    onReceiveErrors(response.errorBody()?.string())
                }
            } catch (e: Exception){
                Log.d(LOG,"Ops...  "+ e.message)
                e.printStackTrace()
            }
        }
    }

    //при получении ошибок с сервера
    private fun onReceiveErrors(jsonStringErrors: String?) {
        // Обработка ошибки
        val jsonArrayErrors = JSONArray(jsonStringErrors)
        val list = mutableListOf<String>()

        for (i in 0 until jsonArrayErrors.length()) {
            val item = jsonArrayErrors.getString(i)
            list.add(item)
        }

        list.forEach { Log.d(LOG,it) }

        validationErrors.value = ErrorsHandler().translate(list)

    }

    //при получении положительного ответа с профилем в теле
    private suspend fun onReceiveProfile(profile: Profile?) {
        if (profile != null) {
            roomService.getPropDAO().insertNewProp(PropModel("profile",profile.toJson()))
        }
        Log.d(LOG,"Response is successsful. Profile: "+ profile.toString())
    }
    /**
     * добавит ошибку что пароли при вводе не совпадают
     * ну раз уж текст ошибок здесь определяется
     */
    fun passwordMismatchError() {
        validationErrors.value = listOf("Пароли не совпадают.")
    }

    /**
     * разлогинит пользователя
     */
    fun logOut() {
        GlobalScope.launch{
            roomService.getPropDAO().deletePropByKey("profile")
        }
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
                    "Invalid password." -> "Неправильный пароль."
                    "User not found." -> "Профиль с таким email не существует."
                    else -> "Unknown error."
                })
            }
            return newList
        }
    }
}