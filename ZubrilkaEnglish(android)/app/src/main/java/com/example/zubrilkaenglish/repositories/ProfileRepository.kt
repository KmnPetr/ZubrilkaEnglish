package com.example.zubrilkaenglish.repositories

import android.annotation.SuppressLint
import android.util.Log
import com.example.zubrilkaenglish.events.NfEvEnum
import com.example.zubrilkaenglish.events.NotificationEvent
import com.example.zubrilkaenglish.models.Profile
import com.example.zubrilkaenglish.models.PropModel
import com.example.zubrilkaenglish.repositories.retrofit.RetrofitService
import com.example.zubrilkaenglish.repositories.room.RoomService
import com.example.zubrilkaenglish.utils.LOG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.json.JSONArray
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * занимается аутентификацией пользователя
 */
class ProfileRepository private constructor() {
    companion object {
        val instance: ProfileRepository by lazy { ProfileRepository() }
    }

    private val retrofitService = RetrofitService()
    val roomService = RoomService()
    val validationErrors: MutableStateFlow<List<String>?> = MutableStateFlow(null)
    val profile: MutableStateFlow<Profile?> = MutableStateFlow(null)

    init {
        GlobalScope.launch {
            roomService.getPropDAO().getProfile().collect {
                Log.d(LOG, "Profile was changed: ProfileRepository: " + it?.value)
                if (it != null) {
                    profile.value = Profile.toProfileObject(it.value)
                } else profile.value = null
            }
        }
    }

    /**
     * запросит у ссервера временный профиль
     * сохранит его в БД
     */
    suspend fun getTemporaryProfile(): Profile? {
        val profile: Profile? = retrofitService.requestTemporaryProfile()
        if (profile != null) { updateProfile(profile) }
        this.profile.value = profile
        return profile
    }

    /**
     * выполнит вызов к серверу вместе с обработкой ситуации когда истечет срок действия accesToken, refreshToken
     * в параметры принимается лямбда, в которую вбивается запрос к серверу, который требует токен-менеджмента
     */
    suspend fun <T> authRequest(request: suspend (jwtToken: String) -> Response<T?>): Response<T?> {
        val response: Response<T?> = request("Bearer " + profile.value?.accessToken.toString())
        if (response.isSuccessful) {//получил 200 с первого раза
            return response
        } else if (response.code() == 401) {//accessToken протух
            val refreshTokenResponce: Response<PropModel?> = retrofitService.getProfileApi()
                .refreshAccessToken("Bearer " + profile.value?.refreshToken.toString())
            if (refreshTokenResponce.isSuccessful) {//получил refreshToken
                val newAccessToken = refreshTokenResponce.body()?.value
                Log.d(LOG, "Получен newAccessToken: $newAccessToken")

                val profile: Profile? = profile.value
                if (profile !== null) {
                    profile.accessToken = newAccessToken
                    updateProfile(profile)//сохранили в БД профиль с новым accessToken
                }

                return request("Bearer " + newAccessToken)//повторили пользовательский запрос
            } else if (refreshTokenResponce.code() == 401) {//refreshToken протух
                Log.d(LOG, "Походу refreshToken протух или еще чтото")
                logOut()
                return Response.error(401, okhttp3.ResponseBody.create(null, "Unauthorized"))
            }
            return Response.error(refreshTokenResponce.code(), okhttp3.ResponseBody.create(null, refreshTokenResponce.message()))
        } else {
            Log.d(LOG, "Something else: ${response.message()}")
            return response
        }
    }
    /**
     * отправит запрос на поля профиля (имя или email)
     */
    fun changeProfileField(typeField:String,newValueField: String) {
        validationErrors.value = null
        GlobalScope.launch {

            try {
                val profile: Profile? = profile.value
                if (profile!=null){
                    val response: Response<Profile?> = authRequest { jwtToken ->
                        retrofitService.getProfileApi().changeName(
                            profile.id,
                            jwtToken,
                            PropModel(typeField,newValueField))
                    }

                    if (response.isSuccessful) {
                        // Обработка успешного ответа
                        onReceiveProfile(response.body())
                    } else {
                        onReceiveErrors(response.errorBody()?.string())
                    }
                }

            }catch (e: SocketTimeoutException){
                GlobalScope.launch(Dispatchers.Main) { EventBus.getDefault().post(NotificationEvent("", NfEvEnum.CONNECTION_LOST)) }
            } catch (e: UnknownHostException) {
                GlobalScope.launch(Dispatchers.Main) { EventBus.getDefault().post(NotificationEvent("", NfEvEnum.CONNECTION_LOST)) }
            } catch (e: Exception){
                Log.d(LOG,"Ops...  "+ e.message)
                e.printStackTrace()
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
            } catch (e: SocketTimeoutException){
                GlobalScope.launch(Dispatchers.Main) { EventBus.getDefault().post(NotificationEvent("", NfEvEnum.CONNECTION_LOST)) }
            } catch (e: UnknownHostException) {
                GlobalScope.launch(Dispatchers.Main) { EventBus.getDefault().post(NotificationEvent("", NfEvEnum.CONNECTION_LOST)) }
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
            } catch (e: SocketTimeoutException){
                GlobalScope.launch(Dispatchers.Main) { EventBus.getDefault().post(NotificationEvent("", NfEvEnum.CONNECTION_LOST)) }
            } catch (e: UnknownHostException) {
                GlobalScope.launch(Dispatchers.Main) { EventBus.getDefault().post(NotificationEvent("", NfEvEnum.CONNECTION_LOST)) }
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
            updateProfile(profile)
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
    private suspend fun updateProfile(profile: Profile){
        roomService.getPropDAO().insertNewProp(PropModel("profile",profile.toJson()))
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
                    "Invalid login or password." -> "Неправильный логин или пароль."
                    "Field value is blank." -> "Поле не должно быть пустым."
                    "This Email is already used."  -> "Email уже занят."
                    else -> "Unknown error."
                })
            }
            return newList
        }
    }
}