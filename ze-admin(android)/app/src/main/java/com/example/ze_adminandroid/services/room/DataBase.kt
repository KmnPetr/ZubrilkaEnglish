package com.example.ze_adminandroid.services.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ze_adminandroid.models.Voice
import com.example.ze_adminandroid.models.Word
import com.example.ze_adminandroid.utils.MyApplication


@Database(
    entities = [
        Word::class,
        Voice::class
    ],
    version = 1
)
abstract class DataBase: RoomDatabase(){

    abstract fun getEditedWordDAO(): EditedWordDAO
    abstract fun getCreatedVoiceDAO(): CreatedVoiceDAO

    companion object{

        private var database: DataBase?=null

        @Synchronized
        fun getInstanceDB():DataBase{
            return if(database==null){
                database= Room.databaseBuilder(
                    MyApplication.context,
                    DataBase::class.java,
                    "DataBase"
                ).build()
                database as DataBase
            }else{
                database as DataBase
            }
        }
    }
}