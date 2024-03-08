package com.example.ze_adminandroid.utils

import android.app.Application

class MyApplication: Application() {
    companion object{
        lateinit var context:MyApplication
    }
    override fun onCreate() {
        super.onCreate()

        context=this
    }
}