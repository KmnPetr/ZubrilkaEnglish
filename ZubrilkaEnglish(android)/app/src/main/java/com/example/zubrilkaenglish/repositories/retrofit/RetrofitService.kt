package com.example.zubrilkaenglish.repositories.retrofit

import android.widget.Toast
import com.example.zubrilkaenglish.models.Word
import com.example.zubrilkaenglish.utils.MyApplication

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

    suspend fun getUpdateAt(): String? {
        try{
            return RetrofitInstance.propApi.getUpdateAt().body()?.value
        }catch (e:Exception){
            e.printStackTrace()
            Toast.makeText(MyApplication.context,"ошибка подключения к серверу",Toast.LENGTH_LONG).show()
            return null
        }
    }
}