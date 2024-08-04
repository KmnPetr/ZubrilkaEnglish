package com.example.zubrilkaenglish.repositories.room

import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.zubrilkaenglish.models.Converters
import com.example.zubrilkaenglish.models.ProgressWord
import com.example.zubrilkaenglish.models.PropModel
import com.example.zubrilkaenglish.models.Voice
import com.example.zubrilkaenglish.models.Word
import com.example.zubrilkaenglish.models.Memo
import com.example.zubrilkaenglish.utils.LOG
import com.example.zubrilkaenglish.utils.MyApplication

@Database(
    entities = [
    Word::class,
    PropModel::class,
    ProgressWord::class,
    Voice::class,
    Memo::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class DataBase:RoomDatabase(){

    abstract fun getWordDAO(): WordDAO
    abstract fun getPropDAO(): PropDAO
    abstract fun getProgressDAO(): ProgressDAO
    abstract fun getVoiceDAO(): VoiceDAO
    abstract fun getMemoDAO(): MemoDAO

    companion object{

        private val database: DataBase by lazy { Room.databaseBuilder(
            MyApplication.context,
            DataBase::class.java,
            "DataBase"
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    migration1to2(db) //добавили некоторые данные в проперти таблицу без изменения схемы таблиц
                }
            })
            .addMigrations(MIGRATION_1_2)
            .build()
        }

        @Synchronized
        fun getInstanceDB():DataBase{
            return database
        }
    }

}