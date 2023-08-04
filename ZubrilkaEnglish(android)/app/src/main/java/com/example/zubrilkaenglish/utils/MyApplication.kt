package com.example.zubrilkaenglish.utils

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class MyApplication:Application() {

    companion object{
        lateinit var context:MyApplication
    }

    override fun onCreate() {
        super.onCreate()

        context=this

        //для работы ZonedDateTime на версии android ниже 8(там старая библиотечка, но есть и новая)
        AndroidThreeTen.init(this)
    }

}