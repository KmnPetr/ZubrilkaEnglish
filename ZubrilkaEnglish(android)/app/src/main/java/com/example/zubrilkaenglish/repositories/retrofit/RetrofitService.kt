package com.example.zubrilkaenglish.repositories.retrofit

import android.widget.Toast
import com.example.zubrilkaenglish.models.Profile
import com.example.zubrilkaenglish.models.Voice
import com.example.zubrilkaenglish.models.Word
import com.example.zubrilkaenglish.utils.MyApplication
import okhttp3.ResponseBody
import retrofit2.Response

class RetrofitService {
    suspend fun getAllWords(): List<Word>? {
        try{
            return RetrofitInstance.wordApi.getAllWords().body()//TODO там приходит response его нужно обработать здесь
        }catch (e:Exception){
            e.printStackTrace()
            Toast.makeText(MyApplication.context,"ошибка подключения к серверу",Toast.LENGTH_LONG).show()
            return null
        }
    }

    suspend fun getDictionaryVersion(): String? {
        try{
            return RetrofitInstance.propApi.getDictionaryVersion().body()?.value
        }catch (e:Exception){
            e.printStackTrace()
            Toast.makeText(MyApplication.context,"ошибка подключения к серверу",Toast.LENGTH_LONG).show()
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
        }catch (e: Exception){
            e.printStackTrace()
//            Toast.makeText(MyApplication.context,"ошибка подключения к серверу",Toast.LENGTH_LONG).show() //TODO ???
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
}
