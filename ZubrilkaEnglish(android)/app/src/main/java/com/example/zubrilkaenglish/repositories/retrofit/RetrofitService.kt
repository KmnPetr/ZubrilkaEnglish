package com.example.zubrilkaenglish.repositories.retrofit

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.media.MediaPlayer
import android.widget.Toast
import com.example.zubrilkaenglish.models.Word
import com.example.zubrilkaenglish.utils.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileDescriptor
import java.io.FileOutputStream

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

    suspend fun getVoiceDataByName():ByteArray?{
        val name = "description.mp3"
            val response/*: Response<ResponseBody>*/ = RetrofitInstance.voiceApi.getVoiceByName(name)
            val bytes: ByteArray? = response.body()?.bytes()

            return bytes
    }

}
























