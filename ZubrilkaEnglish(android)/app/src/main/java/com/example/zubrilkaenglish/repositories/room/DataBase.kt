package com.example.zubrilkaenglish.repositories.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.zubrilkaenglish.models.PropModel
import com.example.zubrilkaenglish.models.Word
import com.example.zubrilkaenglish.utils.MyApplication

@Database(entities = [Word::class,PropModel::class], version = 1)
abstract class DataBase:RoomDatabase(){

    abstract fun getWordDAO(): WordDAO
    abstract fun getPropDAO(): PropDAO

    companion object{

        private var database: DataBase?=null

        @Synchronized
        fun getInstanseDB():DataBase{
            return if(database==null){
                database= Room.databaseBuilder(
                    MyApplication.context,
                    DataBase::class.java,
                    "DataBase"
                ).build()
                return database as DataBase
            }else{
                return database as DataBase
            }
        }
    }
}