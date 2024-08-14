package com.zubrilka.zubrilkaenglish.repositories.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zubrilka.zubrilkaenglish.models.Converters
import com.zubrilka.zubrilkaenglish.models.ProgressWord
import com.zubrilka.zubrilkaenglish.models.PropModel
import com.zubrilka.zubrilkaenglish.models.Voice
import com.zubrilka.zubrilkaenglish.models.Word
import com.zubrilka.zubrilkaenglish.models.Memo
import com.zubrilka.zubrilkaenglish.utils.MyApplication

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