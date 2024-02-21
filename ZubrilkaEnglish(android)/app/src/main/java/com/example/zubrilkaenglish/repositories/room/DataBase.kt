package com.example.zubrilkaenglish.repositories.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.zubrilkaenglish.models.ProgressWord
import com.example.zubrilkaenglish.models.PropModel
import com.example.zubrilkaenglish.models.Voice
import com.example.zubrilkaenglish.models.Word
import com.example.zubrilkaenglish.utils.MyApplication

@Database(
    entities = [
    Word::class,
    PropModel::class,
    ProgressWord::class,
    Voice::class
                     ],
    version = 1
)
abstract class DataBase:RoomDatabase(){

    abstract fun getWordDAO(): WordDAO
    abstract fun getPropDAO(): PropDAO
    abstract fun getProgressDAO(): ProgressDAO
    abstract fun getVoiceDAO(): VoiceDAO

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