package com.zubrilka.zubrilkaenglish.repositories.retrofit

import android.util.Log
import com.zubrilka.zubrilkaenglish.events.NfEvEnum
import com.zubrilka.zubrilkaenglish.events.NotificationEvent
import com.zubrilka.zubrilkaenglish.models.Profile
import com.zubrilka.zubrilkaenglish.models.PropModel
import com.zubrilka.zubrilkaenglish.models.StatisticsDTO
import com.zubrilka.zubrilkaenglish.models.Voice
import com.zubrilka.zubrilkaenglish.models.Word
import com.zubrilka.zubrilkaenglish.repositories.ProfileRepository
import com.zubrilka.zubrilkaenglish.utils.LOG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.greenrobot.eventbus.EventBus
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RetrofitService {
    /**
     * выдаст все методы profileApi
     */
    fun getProfileApi() = RetrofitInstance.profileApi
    //выдаст все методы statisticsApi
    fun getStatisticsApi() = RetrofitInstance.statisticsApi
    /**
     * запросит у сервера смену пароля
     * если это временный аккаунт сервер сам разберется проверять ли старый пароль
     */
    suspend fun requestChangePassword(newPassword: String, oldPassword: String): Response<Profile?>? {
        try{
            val response: Response<Profile?> = authRequest { jwtToken ->
                getProfileApi().changePassword(jwtToken, mapOf("newPassword" to newPassword,"oldPassword" to oldPassword))
            }
            return response
        }catch (e: SocketTimeoutException){
            GlobalScope.launch(Dispatchers.Main) { EventBus.getDefault().post(NotificationEvent("Не удалось изменить пароль",NfEvEnum.CONNECTION_LOST)) }
            return null
        } catch (e: UnknownHostException) {
            GlobalScope.launch(Dispatchers.Main) { EventBus.getDefault().post(NotificationEvent("Не удалось изменить пароль",NfEvEnum.CONNECTION_LOST)) }
            return null
        } catch (e:Exception){
            e.printStackTrace()
            return null
        }
    }
    suspend fun getAllWords(): List<Word>? {
        try{
            return RetrofitInstance.wordApi.getAllWords().body()//TODO там приходит response его нужно обработать здесь
        }catch (e: SocketTimeoutException){
            GlobalScope.launch(Dispatchers.Main) { EventBus.getDefault().post(NotificationEvent("Не удалось обновить словарь",NfEvEnum.CONNECTION_LOST)) }
            return null
        } catch (e: UnknownHostException) {
            GlobalScope.launch(Dispatchers.Main) { EventBus.getDefault().post(NotificationEvent("Не удалось обновить словарь",NfEvEnum.CONNECTION_LOST)) }
            return null
        } catch (e:Exception){
            e.printStackTrace()
            return null
        }
    }

    suspend fun getDictionaryVersion(): String? {
        try{
            return RetrofitInstance.propApi.getDictionaryVersion().body()?.value
        }catch (e: SocketTimeoutException){
            GlobalScope.launch(Dispatchers.Main) { EventBus.getDefault().post(NotificationEvent("Не удалось обновить словарь",NfEvEnum.CONNECTION_LOST)) }
            return null
        } catch (e: UnknownHostException) {
            GlobalScope.launch(Dispatchers.Main) { EventBus.getDefault().post(NotificationEvent("Не удалось обновить словарь",NfEvEnum.CONNECTION_LOST)) }
            return null
        } catch (e:Exception){
            e.printStackTrace()
            return null
        }
    }

    suspend fun getVoiceDataByName(voiceName: String): Voice? {

        try {
            val response: Response<ResponseBody> = RetrofitInstance.voiceApi.getVoiceByName(voiceName)
            if (response.isSuccessful){
                val filename: String? = response.headers()["filename"]
                val byteArray = response.body()?.bytes()
                if (filename != null && byteArray != null){
                    return Voice(filename,byteArray)
                }
            }
        }catch (e: SocketTimeoutException){
            GlobalScope.launch(Dispatchers.Main) { EventBus.getDefault().post(NotificationEvent("Не удалось загрузить аудиофайл",NfEvEnum.CONNECTION_LOST)) }
            return null
        } catch (e: UnknownHostException) {
            GlobalScope.launch(Dispatchers.Main) { EventBus.getDefault().post(NotificationEvent("Не удалось загрузить аудиофайл",NfEvEnum.CONNECTION_LOST)) }
            return null
        } catch (e: Exception){
            e.printStackTrace()
        }
        //на крайняк выбросит пустое значение
        return null
    }


    /**
     * отправит поинты на сервер заработанные в офлайн режимах тренировки
     * метод требует авторизации
     * создания новой учетной записи в случае отсутствия или обновления access токена
     */
    fun sendOfflinePoints(offlinePoints: Int) {
        GlobalScope.launch {
            val profile:Profile? = ProfileRepository.instance.profile.value
            try {
                if (profile != null){
                    val response = ProfileRepository.instance.authRequest { jwtToken: String -> getStatisticsApi().sendOfflinePoints(jwtToken,offlinePoints) }
                } else {
                    val accessToken:String? = ProfileRepository.instance.getTemporaryProfile()?.accessToken
                    getStatisticsApi().sendOfflinePoints("Bearer $accessToken",offlinePoints)
                }
            }catch (e: SocketTimeoutException){
                GlobalScope.launch(Dispatchers.Main) { EventBus.getDefault().post(NotificationEvent("Очки за обучение не отправлены",NfEvEnum.CONNECTION_LOST)) }
            } catch (e: UnknownHostException) {
                GlobalScope.launch(Dispatchers.Main) { EventBus.getDefault().post(NotificationEvent("Очки за обучение не отправлены",NfEvEnum.CONNECTION_LOST)) }
            }
        }
    }

    /**
     * запросит список статистики рейтинга первых 1500 пользователей набравших найбольшее количество очков
     */
    suspend fun getStatFirst1500(ownId: Long?): List<StatisticsDTO>? {
        try {
            return getStatisticsApi().getStatFirst1500(ownId).body()
        }catch (e: SocketTimeoutException){
            GlobalScope.launch(Dispatchers.Main) { EventBus.getDefault().post(NotificationEvent("Не удалось получить таблицу рейтинга", NfEvEnum.CONNECTION_LOST)) }
            return null
        } catch (e: UnknownHostException) {
            GlobalScope.launch(Dispatchers.Main) { EventBus.getDefault().post(NotificationEvent("Не удалось получить таблицу рейтинга", NfEvEnum.CONNECTION_LOST)) }
            return null
        }
    }

    /**
     * запросит у ссервера временный профиль
     */
    suspend fun requestTemporaryProfile(): Profile? {
        try {
            return getProfileApi().requestTemporaryProfile().body()
        }catch (e: SocketTimeoutException){
            GlobalScope.launch(Dispatchers.Main) { EventBus.getDefault().post(NotificationEvent("", NfEvEnum.CONNECTION_LOST)) }
            return null
        } catch (e: UnknownHostException) {
            GlobalScope.launch(Dispatchers.Main) { EventBus.getDefault().post(NotificationEvent("", NfEvEnum.CONNECTION_LOST)) }
            return null
        }
    }

    /**
     * запросит у сервера список слов для первоначальной установки их в качестве учебных
     * вызывается при первом запуске приложения
     */
    suspend fun getInitialTrainingList(): List<Word>? {
        try{
            return RetrofitInstance.wordApi.getInitialTrainingList().body()
        }catch (e: SocketTimeoutException){
            GlobalScope.launch(Dispatchers.Main) { EventBus.getDefault().post(NotificationEvent("",NfEvEnum.CONNECTION_LOST)) }
            return null
        } catch (e: UnknownHostException) {
            GlobalScope.launch(Dispatchers.Main) { EventBus.getDefault().post(NotificationEvent("",NfEvEnum.CONNECTION_LOST)) }
            return null
        } catch (e:Exception){
            e.printStackTrace()
            return null
        }
    }


    /**
     * выполнит вызов к серверу вместе с обработкой ситуации когда истечет срок действия accesToken, refreshToken
     * в параметры принимается лямбда, в которую вбивается запрос к серверу, который требует токен-менеджмента
     */
    suspend fun <T> authRequest(request: suspend (jwtToken: String) -> Response<T?>): Response<T?> {
        val response: Response<T?> = request("Bearer " + ProfileRepository.instance.profile.value?.accessToken.toString())
        if (response.isSuccessful) {//получил 200 с первого раза
            return response
        } else if (response.code() == 401) {//accessToken протух
            val refreshTokenResponce: Response<PropModel?> = getProfileApi()
                .refreshAccessToken("Bearer " + ProfileRepository.instance.profile.value?.refreshToken.toString())
            if (refreshTokenResponce.isSuccessful) {//получил refreshToken
                val newAccessToken = refreshTokenResponce.body()?.value
                Log.d(LOG, "Получен newAccessToken: $newAccessToken")

                val profile: Profile? = ProfileRepository.instance.profile.value
                if (profile !== null) {
                    profile.accessToken = newAccessToken
                    ProfileRepository.instance.updateProfile(profile)//сохранили в БД профиль с новым accessToken
                }

                return request("Bearer " + newAccessToken)//повторили пользовательский запрос
            } else if (refreshTokenResponce.code() == 401) {//refreshToken протух
                Log.d(LOG, "Походу refreshToken протух или еще чтото")
                ProfileRepository.instance.logOut()
                return Response.error(401, okhttp3.ResponseBody.create(null, "Unauthorized"))
            }
            return Response.error(refreshTokenResponce.code(), ResponseBody.create(null, refreshTokenResponce.message()))
        } else {
            Log.d(LOG, "Something else: ${response.message()}")
            return response
        }
    }
}
