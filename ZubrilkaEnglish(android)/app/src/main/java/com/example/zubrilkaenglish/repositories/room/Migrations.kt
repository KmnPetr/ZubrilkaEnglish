package com.example.zubrilkaenglish.repositories.room

import android.util.Log
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.zubrilkaenglish.utils.LOG

val MIGRATION_1_2 = object : Migration(1,2){
    override fun migrate(database: SupportSQLiteDatabase) {
        migration1to2(database)
    }
}
//надо положить некоторые данные в бд не касающиеся изменения схемы
//функция должна вызваться в двух местах как в миграциях так и при создании бд с нуля в случае которого миграции не выполняются
fun migration1to2(database: SupportSQLiteDatabase){
    Log.d(LOG, "Migrating from version 1 to 2")
    database.execSQL("INSERT INTO prop_table (key, value) VALUES ('${PropKey.IS_FIRST_ENTRY.key}', 'true')")
}