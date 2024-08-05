package com.example.zubrilkaenglish.repositories.retrofit

import com.example.zubrilkaenglish.events.NfEvEnum
import com.example.zubrilkaenglish.events.NotificationEvent
import com.example.zubrilkaenglish.models.Profile
import com.example.zubrilkaenglish.models.StatisticsDTO
import com.example.zubrilkaenglish.models.Voice
import com.example.zubrilkaenglish.models.Word
import com.example.zubrilkaenglish.repositories.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.greenrobot.eventbus.EventBus
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RetrofitService {
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
     * выдаст все методы profileApi
     */
    fun getProfileApi() = RetrofitInstance.profileApi
    //выдаст все методы statisticsApi
    fun getStatisticsApi() = RetrofitInstance.statisticsApi

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

}
